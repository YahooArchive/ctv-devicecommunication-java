/*******************************************************************************
 * Copyright (c) 2012, Yahoo! Inc.
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 *  * Neither the name of Yahoo! Inc. nor the names of its
 *    contributors may be used to endorse or promote products
 *    derived from this software without specific prior
 *    written permission of Yahoo! Inc.
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

/***
 * This example demonstrates how to communicate with a specific TV app. It allows an IM-type message passing. Type in
 * the console and your message will be printed on the TV screen. Type something on the TV and it will show up in the
 * console.
 */

import com.yahoo.connectedtv.ycommand.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppChatExample {
	public static String HOST = "localhost";
	public static int PORT = 8099;
	public static String APP_NAME = "AppChatExample";

	/*** !!!!
	 * WARNING: These are demo keys and will only work on the simulator (ADK). For more informations about generating
	 * production keys, see http://developer.yahoo.com/connectedtv
	 *** !!!!
	 */
	public final static String APP_ID = "0xeTgF3c";
	public final static String CONSUMER_KEY = "dj0yJmk9T1Y0MmVIWWEzWVc3JmQ9WVdrOU1IaGxWR2RHTTJNbWNHbzlNVEUzTkRFM09ERTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0yNA--";
	public final static String SECRET = "1b8f0feb4d8d468676293caa769e19958bf36843";

	public static String WIDGET_ID = "com.yahoo.connectedtv.examples.appchat";

	public Connection conn;

	private CommandRouter router;

	public AppChatExample(String host, int port) {
		// Set up our router object, this is responsible for routing incoming
		// and outgoing messages
		router = new CommandRouter();
		try {
			// setup our socket connection to the tv, but don't connect yet
			conn = new Connection(InetAddress.getByName(host), port, router);

			// Tell out router which network connection to use
			router.setConnection(conn);

			// setup a handler for incoming GrantedSessionCommand message
			// objects
			router.registerCommandSubscriber(new CommandSubscriber(), GrantedSessionCommand.class);

			// Establish a connection
			conn.establish();
			// Since this is the first time we are connecting, we must
			// create a new session
			router.publishCommand(new CreateSessionCommand(APP_ID, CONSUMER_KEY, SECRET, APP_NAME));

			String message = this.getUserInput("Code: ");

			router.publishCommand(new AuthSessionCommand(message, conn.getPeerCertificate()));
		} catch (UnknownHostException e) {
			this.exitWithError("Error resolving " + host);
		} catch (IOException e) {
			this.exitWithError("Problem writing to the network connection");
		} catch (InterruptedException e) {
			this.exitWithError("Problem writing to the network connection");
		}
	}

	private String getUserInput(String prompt) {
		System.out.print(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return br.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class CommandSubscriber implements ICommandSubscriber {
		public void onCommandReceived(AbstractCommand command) {
			// Filter out the messages we care about
			if (command instanceof GrantedSessionCommand) {
				// Print our our unique key, this will be used for
				// subsequent connections
				Utilities.log("Your instanceId is " + ((GrantedSessionCommand) command).getInstanceId());

				try {
					router.registerCommandSubscriber(this, WidgetCommand.class, WIDGET_ID);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				AppChatExample.this.startUserInput();

				//Launch widget
				try {
					AppChatExample.this.router.publishCommand(new WidgetLaunchMethodCommand("callid-launch-" + WIDGET_ID, WIDGET_ID));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (command instanceof WidgetCommand) {
				// handle incoming message from a widget
				try {
					JSONObject payload = new JSONObject(command.getPayload());
					String username = payload.getString("username");
					String message = payload.getString("message");
					Utilities.log(username + ": " + message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else { // print out the others for educational purposes
				Utilities.log("Received: " + command.toString());
			}
		}

		public void onConnectionLost(Connection conn) {
			Utilities.log("Connection Lost!");
			// AppChatExample.this.closeConnectionAndExit();

			synchronized (AppChatExample.this.conn) {
				AppChatExample.this.conn.notifyAll();
			}
		}
	}

	private void startUserInput() {
		(new Thread() {
			public void run() {
				try {
					while (true) {
						String message = AppChatExample.this.getUserInput("Message: ");

						if (message == null || message.equals("")) {
							continue;
						} else if (message.equals("q") || message.equals("quit")) {
							break;
						}

						JSONObject msgObj = new JSONObject();
						msgObj.put("message", message);
						msgObj.put("username", "You");
						AppChatExample.this.router.publishCommand(new WidgetCommand(WIDGET_ID, msgObj));
					}
					AppChatExample.this.closeConnectionAndExit();

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void closeConnectionAndExit() {
		this.conn.close();

		// Notify the main thread that everything we wanted to
		// do is done.
		synchronized (this.conn) {
			this.conn.notifyAll();
		}
	}

	private void exitWithError(String message) {
		Utilities.log(message);
		System.exit(-1);
	}

	public static void main(String[] args) {
		AppChatExample chatExample = new AppChatExample(HOST, PORT);

		// Let's wait until everything is done. This thread will get
		// notified once we are ready to clean up
		synchronized (chatExample.conn) {
			try {
				chatExample.conn.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.exit(1);
	}
}
