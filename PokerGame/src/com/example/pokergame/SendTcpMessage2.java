package com.example.pokergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SendTcpMessage2 extends Thread {
	InetAddress ipAddress;
	Message message;

	public SendTcpMessage2(InetAddress ip, Message message) {
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
			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			// sentence = inFromUser.readLine();
			outToServer.writeObject(message); 
			outToServer.flush();
			//outToServer.writeBytes(message + '\n');
			System.out.println("SENDING TO DEALER"+message.card);
			String modifiedSentence = inFromServer.readLine(); 
			System.out.println("RECIEVING "+modifiedSentence);
			//System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
