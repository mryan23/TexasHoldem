package com.example.pokergame;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPListener extends Thread {

	DatagramSocket serverSocket;
	boolean tcpSent = false;
	ArrayList<InetAddress> player_addr;
	ArrayList<String> names;

	public UDPListener(ArrayList<InetAddress>players, ArrayList<String> names) {
		player_addr=players;
		this.names = names;
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
				String str ="";
				for(int i = 0; i < receiveData.length; i++){
					if(receiveData[i]!=0)
						str+=(char)receiveData[i];
					else
						break;
				}
				System.out.println(str);
				InetAddress IPAddress = receivePacket.getAddress();
				if(!player_addr.contains(IPAddress)){
					player_addr.add(IPAddress);
					names.add(str);
				}
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
