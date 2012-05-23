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

import com.yahoo.connectedtv.ycommand.CommandRouter;

/***
 * This Example is very simple. It establishes a connection to the TV, then sends a series of navigation commands
 * that makes the application dock do a little dance.
 */

import com.yahoo.connectedtv.ycommand.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class SimpleExample {
	public static String HOST = "localhost";
	public static int PORT = 8099;
	public static String APP_NAME = "SimpleExample";
	public final static String APP_ID = "0xeTgF3c";
	public final static String CONSUMER_KEY = "dj0yJmk9T1Y0MmVIWWEzWVc3JmQ9WVdrOU1IaGxWR2RHTTJNbWNHbzlNVEUzTkRFM09ERTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0yNA--";
	public final static String SECRET = "1b8f0feb4d8d468676293caa769e19958bf36843";

	public static CommandRouter router;
	public static Connection conn;

	public static void main(String[] args) {
		// Set up our router object, this is responsible for routing incoming
		// and outgoing messages
		router = new CommandRouter();
		try {
			// setup our socket connection to the tv, but don't connect yet
			conn = new Connection(HOST, PORT, router);

			// Tell out router which network connection to use
			router.setConnection(conn);

			// setup a handler for incoming GrantedSessionCommand message
			// objects
			try {
				router.registerCommandSubscriber(new ICommandSubscriber() {
					public void onCommandReceived(AbstractCommand command) {
						// Filter out the messages we care about
						if (command instanceof GrantedSessionCommand) {
							// Print our our unique key, this will be used for
							// subsequent connections
							Utilities.log("Your instanceId is " + ((GrantedSessionCommand) command).getInstanceId());


							try {
								//let's make the dock show up on the TV
								router.publishCommand(new NavigationInputCommand("press_yahoo"));

								Thread.sleep(2000); //sleep for 2 seconds so the animation to dock finishes

								// Lets do something cool, like tell the TV to navigate to the right. Then do a little dance
								// This is the same as pressing "right" on your remote
								router.publishCommand(new NavigationInputCommand("press_right"));

								Thread.sleep(1000);

								// slide to the left
								router.publishCommand(new NavigationInputCommand("press_left"));

								Thread.sleep(1000);

								//slide to the right
								router.publishCommand(new NavigationInputCommand("press_right"));

								Thread.sleep(1000);

								//take it back now, y'all
								router.publishCommand(new NavigationInputCommand("press_left"));

								//cha cha cha
								router.publishCommand(new NavigationInputCommand("press_up"));
								Thread.sleep(1000);
								router.publishCommand(new NavigationInputCommand("press_down"));
								Thread.sleep(1000);
								router.publishCommand(new NavigationInputCommand("press_up"));
								Thread.sleep(1000);
								router.publishCommand(new NavigationInputCommand("press_down"));
							} catch (InterruptedException e) {
								Utilities.log("Problem writing to the network connection");
								conn.close();
								System.exit(-1);
							}

							// Notify the main thread that everything we wanted to
							// do is done.
							synchronized (conn) {
								conn.notifyAll();
							}
						} else { // print out the others for educational purposes
							Utilities.log("Received: " + command.toString());
						}
					}

					public void onConnectionLost(Connection conn) {
						Utilities.log("Connection Lost!");
					}
				}, GrantedSessionCommand.class);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				// Establish a connection
				conn.establish();
				// Since this is the first time we are connecting, we must
				// create a new session
				router.publishCommand(new CreateSessionCommand(APP_ID, CONSUMER_KEY, SECRET, APP_NAME));

				String message = getUserInput("Code: ");
				router.publishCommand(new AuthSessionCommand(message, conn.getPeerCertificate()));

				// Let's wait until everything is done. This thread will get
				// notified once we are ready to clean up
				try {
					synchronized (conn) {
						conn.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException ioe) {
				Utilities.log("Error establishing connection to " + HOST + ":" + PORT);
			} catch (InterruptedException e) {
				Utilities.log("Error establishing connection to " + HOST + ":" + PORT);
			}

			// It is always good practice to clean up after yourself
			conn.close();

		} catch (UnknownHostException e) {
			Utilities.log("Error resolving " + HOST);
			System.exit(-1);
		} catch (IOException e) {
			Utilities.log("Problem writing to the network connection");
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(1);
	}

	private static String getUserInput(String prompt) {
		System.out.print(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return br.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
