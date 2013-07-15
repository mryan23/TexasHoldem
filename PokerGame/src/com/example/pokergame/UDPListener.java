package com.example.pokergame;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPListener extends Thread {

	DatagramSocket serverSocket;
	boolean tcpSent = false;

	public UDPListener() {
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		byte[] receiveData = new byte[1024];
		while (true) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
				InetAddress IPAddress = receivePacket.getAddress();
				String str = "10.0.1.2";
				if (!tcpSent) {
					SendTcpMessage stm = new SendTcpMessage(
							IPAddress, "JOINING");
					stm.start();
					tcpSent = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
