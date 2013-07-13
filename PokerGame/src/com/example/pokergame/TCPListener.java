package com.example.pokergame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPListener extends Thread {
	ServerSocket sock;

	ArrayList<InetAddress> player_addr;
	Object lock;

	public TCPListener(ServerSocket socket, ArrayList<InetAddress> player_addr,
			Object lock) {
		sock = socket;
		this.player_addr = player_addr;
		this.lock = lock;
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
				message = (bytes[0] & 0xff) + "." + (bytes[1] & 0xff) + "."
						+ (bytes[2] & 0xff) + "." + (bytes[3] & 0xff);
			}

			InetAddress ip = InetAddress.getByName(message);
			synchronized (lock) {
				if (!this.player_addr.contains(ip)) {
					System.out.println(message);
					this.player_addr.add(ip);
				}
			}
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			String clientSentence = inFromClient.readLine();
			outToClient.writeBytes(clientSentence + '\n');
			// System.out.println(clientSentence);
			// message = clientSentence;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}