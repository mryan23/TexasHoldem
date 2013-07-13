package com.example.pokergame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPListener extends Thread {
	ServerSocket sock;

	ArrayList<InetAddress> player_addr;
	Object lock;
	ArrayList<Integer> isSuccessful; 
	boolean loop = true;
	boolean isDealer = true;

	public TCPListener(ServerSocket socket, ArrayList<Integer> isSuccessful) {
		sock = socket;
		this.isSuccessful = isSuccessful;
		isDealer = false;
	}

	public TCPListener(ServerSocket socket, ArrayList<InetAddress> player_addr,
			Object lock) {
		sock = socket;
		this.player_addr = player_addr;
		this.lock = lock;
	}

	public void run() {
		while (loop)
			tcpStuff(sock);
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if (isDealer) {
				InetAddress ip = InetAddress.getByName(message);
				synchronized (lock) {
					if (!this.player_addr.contains(ip)) {
						System.out.println(message);
						this.player_addr.add(ip);
					}
				}
			}
			String clientSentence = inFromClient.readLine();
			System.out.println(clientSentence);
			if (clientSentence.contains("STARTGAME")) {
				isSuccessful.add(1);
				System.out.println("Value: " + isSuccessful.get(0));
				loop = false;
			}
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			outToClient.writeBytes(clientSentence + '\n');
			// System.out.println(clientSentence);
			// message = clientSentence;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}