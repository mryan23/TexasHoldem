package com.example.pokergame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.poker.Player;

public class TCPListener2 extends Thread {
	ServerSocket sock;

	ArrayList<InetAddress> player_addr;
	Object lock;
	ArrayList<Integer> isSuccessful;
	boolean loop = true;
	boolean isDealer = true;
	Player p;

	public TCPListener2(ServerSocket socket, Player p) {
		sock = socket;
		this.p = p;
		isDealer = false;
	}

	public TCPListener2(ServerSocket socket,
			ArrayList<InetAddress> player_addr, Object lock) {
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

	public void setPlayer(Player player) {
		p = player;
	}

	public void tcpStuff(ServerSocket welcomeSocket) {
		try {
			Socket connectionSocket = welcomeSocket.accept();
			InputStream is = connectionSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			/*
			 * BufferedReader inFromClient = new BufferedReader( new
			 * InputStreamReader(is));
			 */
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
			Message m = (Message) ois.readObject();
			
			// System.out.println(m.card.getValue());
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			outToClient.writeBytes("ACK" + "\n");

			synchronized (p) {
				p.updated = true;
				if(m.message!=null)
					p.setUpdateText(m.message);
				switch (m.msgType) {
				case CARD:
					if (!p.getHand().hand.contains(m.card)) {
						p.addCard(m.card);
					}
					break;
				case TURN:
					System.out.println("MINE!!!");
					p.turn = true;
					p.minBet = m.currentBet;
					break;
				case RESULT:
					System.out.println("RESULT RECIEVED");
					System.out.println(m.money);
					p.addMoney(m.money); 
					p.setStatus(Player.GAME_OVER);

					break;
				default:
					break;
				}
			}

			// String clientSentence = inFromClient.readLine();
			// System.out.println(clientSentence);
			/*
			 * if (clientSentence.contains("STARTGAME")) { isSuccessful.add(1);
			 * System.out.println("Value: " + isSuccessful.get(0)); loop =
			 * false; }
			 * 
			 * outToClient.writeBytes(clientSentence + '\n'); //
			 * System.out.println(clientSentence); // message = clientSentence;
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}