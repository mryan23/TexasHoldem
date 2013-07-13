package com.example.pokergame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SendTcpMessage extends Thread {
	InetAddress ipAddress;
	String message;

	public SendTcpMessage(InetAddress ip, String message) {
		ipAddress = ip;
		this.message = message;
	}

	@Override
	public void run() {
		try {
			// BufferedReader inFromUser = new BufferedReader(
			// new InputStreamReader(System.in));
			Socket clientSocket = null;
			clientSocket = new Socket(ipAddress, 6789);
			// Socket clientSocket = new Socket("10.0.1.102", 6789);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			// sentence = inFromUser.readLine();
			System.out.println("SENDING TO DEALER");
			outToServer.writeBytes(message + '\n');
			String modifiedSentence = inFromServer.readLine();
			//System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
