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
 * This example demonstrates how to build in video playing handling. It pairs up with the "App<->Device Video List"
 * example that can be found in the simulator store. Once connected, it fetches a list of videos from the app and lists
 * them out. You can enter in any URL and it will play the video in the simulator.
 */

import com.yahoo.connectedtv.ycommand.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class VideoListExample {
	public static String HOST = "localhost";
	public static int PORT = 8099;
	public static String APP_NAME = "SimpleExample";

	/*** !!!!
	 * WARNING: These are demo keys and will only work on the simulator (ADK). For more informations about generating
	 * production keys, see http://developer.yahoo.com/connectedtv
	 *** !!!!
	 */
	public final static String APP_ID = "0xeTgF3c";
	public final static String CONSUMER_KEY = "dj0yJmk9T1Y0MmVIWWEzWVc3JmQ9WVdrOU1IaGxWR2RHTTJNbWNHbzlNVEUzTkRFM09ERTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0yNA--";
	public final static String SECRET = "1b8f0feb4d8d468676293caa769e19958bf36843";

	public static String WIDGET_ID = "com.yahoo.connectedtv.examples.dcvideolist";
	public static String METHOD_NAME = "fetchVideos";
	public static String CALL_ID = "ireallyshouldbeunique";

	public Connection conn;

	private CommandRouter router;

	private CommandSubscriber commandHandler;

	public VideoListExample(String host, int port) {
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
			this.commandHandler = new CommandSubscriber();
			router.registerCommandSubscriber(this.commandHandler, GrantedSessionCommand.class);

			// Establish a connection
			conn.establish();
			// Since this is the first time we are connecting, we must
			// create a new session
			router.publishCommand(new CreateSessionCommand(APP_ID, CONSUMER_KEY, SECRET, APP_NAME));

			// type in the 4 digit code on the screen
			String message = this.getUserInput("Code: ");

			AuthSessionCommand asc = new AuthSessionCommand(message, conn.getPeerCertificate());

			router.publishCommand(asc);
		} catch (UnknownHostException e) {
			this.exitWithError("Error resolving " + HOST);
		} catch (IOException e) {
			this.exitWithError("Problem writing to the network connection");
		} catch (InterruptedException e) {
			this.exitWithError("Problem writing to the network connection");
		}
	}

	private String getUserInput(String prompt) {
		System.out.print(prompt);
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
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
				// Print our our unique key, this will be used for subsequent connections
				Utilities.log("Your instanceId is " + ((GrantedSessionCommand) command).getInstanceId());

				//register for commands from the widget
				try {
					router.registerCommandSubscriber(VideoListExample.this.commandHandler, WidgetCommand.class, WIDGET_ID);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				VideoListExample.this.startUserInput();
			} else if (command instanceof WidgetCommand) {
				// handle incoming message from a widget
				VideoListExample.this.handleWidgetMessage((WidgetCommand) command);
			} else if (command instanceof WidgetMethodCommand) {
				try {
					JSONArray videos = new JSONArray(command.getPayload());
					for (int i = 0; i < videos.length(); i++) {
						JSONObject video = videos.getJSONObject(i);
						System.out.println(i + ". Video: " + video.getString("title"));
						System.out.println("\tdescription: " + video.getString("description"));
						System.out.println("\tthumbnail: " + video.getString("thumbnail"));
						System.out.println("\turl: " + video.getString("url"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public void onConnectionLost(Connection conn) {
			Utilities.log("Connection Lost!");

			synchronized (VideoListExample.this.conn) {
				VideoListExample.this.conn.notifyAll();
			}
		}
	}

	private void startUserInput() {
		router.registerRMCCommandSubscriber(WIDGET_ID, METHOD_NAME, CALL_ID, this.commandHandler);

		try {
			this.router.publishCommand(new WidgetMethodCommand(AbstractCommand.METHOD_CALL, METHOD_NAME, CALL_ID, WIDGET_ID, null));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		(new Thread() {
			public void run() {
				try {
					while (true) {
						String url = VideoListExample.this.getUserInput("URL to play (q to quit): ");

						if (url == null || url.equals("")) {
							continue;
						} else if (url.equals("q") || url.equals("quit")) {
							break;
						}

						JSONObject msgObj = new JSONObject();
						msgObj.put("url", url);
						msgObj.put("method", "launchvideo");
						VideoListExample.this.router.publishCommand(new WidgetLaunchMethodCommand("callid-" + url, WIDGET_ID, msgObj.toString()));
					}
					VideoListExample.this.closeConnectionAndExit();

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void handleWidgetMessage(WidgetCommand command) {
		try {
			JSONObject payload = new JSONObject(command.getPayload());
			String username = payload.getString("username");
			String message = payload.getString("message");
			Utilities.log(username + ": " + message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		VideoListExample chatExample = new VideoListExample(HOST, PORT);

		// Let's wait until everything is done. This thread will get
		// notified once we are ready to clean up
		try {
			synchronized (chatExample.conn) {
				chatExample.conn.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(1);
	}
}
