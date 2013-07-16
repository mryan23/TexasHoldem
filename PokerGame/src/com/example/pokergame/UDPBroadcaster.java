package com.example.pokergame;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class UDPBroadcaster extends Thread {

	DatagramSocket clientSocket;
	InetAddress broadcast;
	String message;
	public UDPBroadcaster(InetAddress broadcast, String message) {
		this.broadcast = broadcast;
		this.message = message;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// while(true&&!this.isInterrupted()){
		try {
			byte[] sendData = new byte[1024];
			sendData = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, broadcast, 9876);
			clientSocket.send(sendPacket);
			System.out.println("BROADCAST SENT");
			//wait(100);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}
}
