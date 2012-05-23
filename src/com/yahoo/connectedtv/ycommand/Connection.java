/*******************************************************************************
 * Copyright (c) 2011, Yahoo! Inc.
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *
 * * Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * * Neither the name of Yahoo! Inc. nor the names of its
 *   contributors may be used to endorse or promote products
 *   derived from this software without specific prior
 *   written permission of Yahoo! Inc.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS 
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.yahoo.connectedtv.ycommand;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Wraps a SSL TCP Connection
 *
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class Connection {
	/**
	 * Field PROTOCOL_VERSION. (value is 1)
	 */
	public static final int PROTOCOL_VERSION = 1;
	/**
	 * Field DEFAULT_PORT. (value is 8099)
	 */
	public static final int DEFAULT_PORT = 8099;

	/**
	 * Field WRITER_THREAD_POLL_INTERVAL. seconds
	 */
	private static final long WRITER_THREAD_POLL_INTERVAL = 1;

	public static final String DNSSD_SERVICE_TYPE = "_yctvwidgets._tcp.local.";

	/**
	 * Field addr.
	 */
	protected InetSocketAddress addr;
	/**
	 * Field socket.
	 */
	protected Socket socket;
	/**
	 * Field reader.
	 */
	protected InputStreamReader reader;
	/**
	 * Field writer.
	 */
	protected OutputStreamWriter writer;

	/**
	 * Field receiverThread.
	 */
	protected Thread receiverThread;

	/**
	 * Field writerThread.
	 */
	protected Thread writerThread;

	protected BlockingQueue<String> outgoingCommands;
	/**
	 * Field handler.
	 */
	protected IConnectionHandler handler;

	/**
	 * Field peerCertificate.
	 */
	protected String peerCertificate;

	/**
	 * Field isConnected.
	 */
	protected Boolean isConnected;

	/**
	 * Field isClosing. We are using this to distinguish between a close() call
	 * and a legit error coming from the socket
	 */
	protected Boolean isClosing;
	private static final int DEFAULT_CONNECT_TIMEOUT = 30000;

	/**
	 * @param addr    hostname or ip of tv
	 * @param port    port to connect to (default is DEFAULT_PORT (8099))
	 * @param handler Event handler, most likely a CommandRouter
	 * @throws UnknownHostException
	 */
	public Connection(String addr, int port, IConnectionHandler handler) throws UnknownHostException {
		this(new InetSocketAddress(addr, port), handler);
	}

	/**
	 * @param addr    InetAddress object for the tv
	 * @param port    port to connect to (default is DEFAULT_PORT (8099))
	 * @param handler Event handler, most likely a CommandRouter
	 * @throws UnknownHostException
	 */
	public Connection(InetAddress addr, int port, IConnectionHandler handler) throws UnknownHostException {
		this(new InetSocketAddress(addr, port), handler);
	}

	/**
	 * @param addr    contains both IP/hostname and port of the tv
	 * @param handler Event handler, most likely a CommandRouter
	 * @throws UnknownHostException
	 */
	public Connection(InetSocketAddress addr, IConnectionHandler handler) throws UnknownHostException {
		this.addr = addr;
		if (this.addr == null || this.addr.isUnresolved() || this.addr.getAddress() == null) {
			throw new UnknownHostException("Cannot resolve " + this.addr);
		}
		this.handler = handler;
		this.isConnected = false;
		this.isClosing = true;
		this.outgoingCommands = new LinkedBlockingQueue<String>();
	}

	/**
	 * Method createReceiverThread.
	 *
	 * @param reader InputStreamReader
	 * @return Thread
	 */
	protected Thread createReceiverThread(InputStreamReader reader) {
		/**
		 */
		class ReaderThread implements Runnable {
			/**
			 * Field reader.
			 */
			InputStreamReader reader;
			/**
			 * Field conn.
			 */
			Connection conn;

			/**
			 * Constructor for ReaderThread.
			 *
			 * @param reader
			 *            InputStreamReader
			 * @param conn
			 *            Connection
			 */
			public ReaderThread(InputStreamReader reader, Connection conn) {
				this.reader = reader;
				this.conn = conn;
			}

			/**
			 * Method run.
			 *
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				Utilities.log("Starting reader thread");
				while (this.conn.isActive()) {
					try {
						String command = readUntilEndTag(this.reader);
						Utilities.log("RECEIVED:" + command);

						this.conn.handler.onDataAvailable(command);
					} catch (IOException e) {
						Utilities.log("Connection LOST!");
						e.printStackTrace();
						break;
					} catch (CommandParseException e) {
						Utilities.log("Ignoring Bad Command, reason:" + e.reason);
						e.printStackTrace();
					}
				}
				this.conn.isConnected = false;
				Utilities.log("Reader Thread closed");
				if (!Connection.this.isClosing) {
					this.conn.close();
				}
			}
		}
		ReaderThread rt = new ReaderThread(reader, this);
		Thread thread = new Thread(null, rt, "SSLSocketReaderThread");
		thread.start();
		return thread;
	}

	/**
	 * Method createWriterThread.
	 *
	 * @param writer OutputStreamWriter
	 * @return Thread
	 */
	protected Thread createWriterThread(OutputStreamWriter writer) {
		/**
		 */
		class WriterThread implements Runnable {
			OutputStreamWriter writer;
			Connection conn;

			public WriterThread(OutputStreamWriter writer, Connection conn) {
				this.writer = writer;
				this.conn = conn;
			}

			public void run() {
				Utilities.log("Starting writer thread");
				while (true) {
					try {
						String command = Connection.this.outgoingCommands.poll(WRITER_THREAD_POLL_INTERVAL, TimeUnit.SECONDS);

						if (Connection.this.isClosing || this.conn == null || !this.conn.isActive()) {
							break;
						}

						if (command != null) {
							try {
								Utilities.log("WRITING:" + command);
								this.writer.write(command);
								this.writer.flush();
							} catch (IOException e) {
								e.printStackTrace();
								break;
							}
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				}
				Utilities.log("Writer Thread closed");
			}
		}
		WriterThread rt = new WriterThread(writer, this);
		Thread thread = new Thread(null, rt, "SSLSocketWriterThread");
		thread.start();
		return thread;
	}

	/**
	 * Method readChar.
	 *
	 * @param reader InputStreamReader
	 * @return char
	 * @throws IOException
	 */
	protected char readChar(InputStreamReader reader) throws IOException {
		int data = reader.read();
		//Utilities.log("READ:" + data + ":" + (char) data);

		if (data == -1) {
			throw new IOException("Socket Disconnected, found EOF");
		}

		return (char) data;

	}

	/**
	 * Method readUntilEndTag.
	 *
	 * @param reader InputStreamReader
	 * @return String
	 * @throws IOException
	 */
	protected String readUntilEndTag(InputStreamReader reader) throws IOException {
		StringBuffer sb = new StringBuffer();
		while (true) {
			char level1Char = readChar(reader);
			if (level1Char == '|') {
				char level2Char = readChar(reader);
				if (level2Char == 'E') {
					char level3Char = readChar(reader);
					if (level3Char == 'N') {
						char level4Char = readChar(reader);
						if (level4Char == 'D') {
							break;
						} else {
							sb.append(level1Char);
							sb.append(level2Char);
							sb.append(level3Char);
							sb.append(level4Char);
						}
					} else {
						sb.append(level1Char);
						sb.append(level2Char);
						sb.append(level3Char);
					}
				} else {
					sb.append(level1Char);
					sb.append(level2Char);
				}
			} else {
				sb.append(level1Char);
			}
		}

		// Utilities.log("Message size:" + sb.length());

		return sb.toString();
	}

	/**
	 * Method createInsecureSSLConnection.
	 *
	 * @param addr InetSocketAddress
	 * @return Socket
	 * @throws IOException
	 */
	protected Socket createInsecureSSLConnection(InetSocketAddress addr) throws IOException {
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");

			sc.init(null, new TrustManager[]{new EasyX509TrustManager(null)}, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		Socket socketConn = new Socket();
		socketConn.connect(addr, DEFAULT_CONNECT_TIMEOUT);

		return sc.getSocketFactory().createSocket(socketConn, addr.getHostName(), addr.getPort(), true);
	}

	// One of the most irritating bugs yet, if you know of a better way, I would
	// LOVE to hear it

	/**
	 * Method fixCertificate.
	 *
	 * @param cert byte[]
	 * @return String
	 */
	protected String fixCertificate(byte[] cert) {
		String newCert = new String(Base64.encodeBytes(cert));

		// first strip out all the new lines
		newCert = newCert.replaceAll("\n", "");

		// now lets insert the newlines at the correct spot
		StringBuffer sb = new StringBuffer();
		sb.append("-----BEGIN CERTIFICATE-----");
		for (int i = 0; i < newCert.length(); i++) {
			if (i % 64 == 0) {
				sb.append('\n');
			}
			sb.append(newCert.charAt(i));
		}
		sb.append("\n-----END CERTIFICATE-----");
		return sb.toString();
	}

	/**
	 * Try to establish a connection to remote server.
	 *
	 * @throws InterruptedException
	 */
	public synchronized void establish() throws IOException, InterruptedException {
		if (this.isActive()) {
			Utilities.log("closing existing connection");
			this.close(false); // we are re-connecting
		}
		Utilities.log("Connecting to " + this.addr.getAddress() + ":" + this.addr.getPort());
		this.socket = createInsecureSSLConnection(this.addr);
		if (this.socket != null) {
			this.isConnected = true;
			this.isClosing = false;

			Charset charset = Charset.forName("UTF-8");

			this.reader = new InputStreamReader(socket.getInputStream(), charset);
			this.writer = new OutputStreamWriter(this.socket.getOutputStream(), charset);
			this.receiverThread = this.createReceiverThread(this.reader);
			this.writerThread = this.createWriterThread(this.writer);

			Utilities.log("Connection established to " + this.addr.getAddress() + ":" + this.addr.getPort());

			this.write(String.format("SESSION|CONFIG|%d|END", PROTOCOL_VERSION));

			Certificate[] servercerts = ((SSLSocket) this.socket).getSession().getPeerCertificates();
			if (servercerts.length > 0) {
				Utilities.log("Fixing certificates");
				try {
					this.peerCertificate = this.fixCertificate(servercerts[0].getEncoded());
				} catch (CertificateEncodingException e) {
					e.printStackTrace();
					this.close();
					throw new IOException("peer certificate encoding problem");
				}
				Utilities.log("Certificates fixed and saved.");
			} else {
				Utilities.log(Level.SEVERE, "unable to get peer certificate");
				this.close();
				throw new IOException("unable to get peer certificate");
			}

			this.isConnected = this.socket.isConnected();

		}
	}

	/**
	 * Write a command to the socket connection
	 *
	 * @param command Command object to send
	 * @throws InterruptedException
	 */
	public void write(AbstractCommand command) throws InterruptedException {
		this.write(command.toCommandString());
	}

	/**
	 * Write a string to the socket connection
	 *
	 * @param command Command string to send
	 * @throws InterruptedException
	 */
	public void write(String command) throws InterruptedException {
		this.outgoingCommands.put(command);
	}

	/**
	 * Close connection and notify all handlers
	 */
	public void close() {
		this.close(true);
	}

	/**
	 * Close connection
	 *
	 * @param notifyHandler Notify all connection listeners
	 */
	public synchronized void close(boolean notifyHandler) {
		Utilities.log("Closing Connection to " + this.addr);
		this.isConnected = false;
		this.isClosing = true;
		try {
			if (this.socket != null) {
				Utilities.log("Closing SOCKET");
				this.socket.close();
			}
			if (this.reader != null) {
				Utilities.log("Closing READER");
				this.reader.close();
			}
			if (this.receiverThread != null && this.receiverThread.isAlive()
					&& !this.receiverThread.equals(Thread.currentThread())) {
				Utilities.log("Closing READERTHREAD");
				this.receiverThread.join();
			}
			this.reader = null;
			this.socket = null;
			this.receiverThread = null;
			Utilities.log("Closing done.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Utilities.log("Error disconnecting socket");
			e.printStackTrace();
		}
		if (notifyHandler) {
			this.handler.onConnectionClosed(this);
		}
	}

	/**
	 * Method isActive.
	 *
	 * @return boolean
	 */
	public boolean isActive() {
		return this.socket != null && this.isConnected && this.socket.isConnected();
	}

	/**
	 * Method getAddr.
	 *
	 * @return InetSocketAddress
	 */
	public InetSocketAddress getAddr() {
		return addr;
	}

	/**
	 * Method getIP.
	 *
	 * @return InetAddress
	 */
	public InetAddress getIP() {
		return addr.getAddress();
	}

	/**
	 * Method getPort.
	 *
	 * @return int
	 */
	public int getPort() {
		return this.addr.getPort();
	}

	/**
	 * Method getPeerCertificate.
	 *
	 * @return String
	 */
	public String getPeerCertificate() {
		return this.peerCertificate;
	}
}
