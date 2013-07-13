package com.example.pokergame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener extends Thread {
	ServerSocket sock;

	public TCPListener(ServerSocket socket ) {
		sock = socket;
	}

	public void run() {
		while (true)
			tcpStuff(sock);
	}

	public void tcpStuff(ServerSocket welcomeSocket) {
		try {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			InetAddress add = connectionSocket.getInetAddress();
			byte[] bytes = add.getAddress();
			String message = add.toString();
			if (bytes.length == 4) {
				message = bytes[0] + "." + bytes[1] + "." + bytes[2] + "."
						+ bytes[3];
			}
			System.out.println(message);
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			String clientSentence = inFromClient.readLine();
			outToClient.writeBytes(clientSentence + '\n');
			//System.out.println(clientSentence);
			// message = clientSentence;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}