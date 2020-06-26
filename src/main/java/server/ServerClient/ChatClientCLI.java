package server.ServerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;



public class ChatClientCLI {

	private SimpleChatClient client;
	private SimpleChatServer server;
	private boolean isRunning;
	private static final String SHELL_STRING = "Enter message (or exit to quit)> ";
	private Thread loopThread;

	public ChatClientCLI(SimpleChatClient client) {
		this.client = client;
		this.isRunning = false;
	}

	public void loop() throws IOException {
		loopThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String message;
				while (client.isConnected()) {
					System.out.print(SHELL_STRING);
					try {
						message = reader.readLine();
						client.sendToServer(message);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});

		loopThread.start();
		this.isRunning = true;

	}

	public void displayMessage(Object message) {
		if (isRunning) {
			System.out.print("(Interrupted)\n");
		}
		System.out.println("Received message from server: " + message.toString());
		if (isRunning)
			System.out.print(SHELL_STRING);
	}

	public void closeConnection() {
		System.out.println("Connection closed.");
		System.exit(0);
	}
}
