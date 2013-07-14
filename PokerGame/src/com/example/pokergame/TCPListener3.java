package com.example.pokergame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.poker.Dealer;

public class TCPListener3 extends Thread {
	ServerSocket sock;

	ArrayList<InetAddress> player_addr;
	Object lock;
	ArrayList<Integer> isSuccessful;
	boolean loop = true;
	boolean isDealer = true;
	Dealer dealer;

	public TCPListener3(ServerSocket socket, Dealer deal, Object lock) {
		sock = socket;
		isDealer = true;
		dealer = deal;
		this.lock = lock;
	}

	public TCPListener3(ServerSocket socket, ArrayList<Integer> isSuccessful) {
		sock = socket;
		this.isSuccessful = isSuccessful;
		isDealer = false;
	}

	public TCPListener3(ServerSocket socket,
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

			Message m = (Message) ois.readObject();
			// System.out.println(m.card.getValue());
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			outToClient.writeBytes("ACK" + "\n");

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
			Message newMessage = new Message();
			SendTcpMessage2 stm;
			System.out.println("RECIEVED: " + m.msgType);
			synchronized (dealer) {
				switch (m.msgType) {
				case BET:
					dealer.addToPot(m.proposedBet);
					if (dealer.prevBet == m.proposedBet) {
						dealer.sameCount++;
					} else {
						dealer.prevBet = m.proposedBet;
						dealer.sameCount = 1;
					}
					if (dealer.sameCount == dealer.getPlayers().size()) {
						// DEAL CARDS
						if (dealer.getCards().size() == 5) {
							dealer.dealCards();

							synchronized (lock) {
								lock = null;
							}
						} else {
							dealer.sameCount = 0;
							dealer.dealCards();
							dealer.increaseTurn();
							newMessage.msgType = MessageType.TURN;
							newMessage.currentBet = 0;
							stm = new SendTcpMessage2(dealer.getPlayers().get(
									dealer.getTurn()), newMessage);
							stm.start();
						}
					} else {
						dealer.increaseTurn();
						newMessage.msgType = MessageType.TURN;
						newMessage.currentBet = dealer.prevBet;
						stm = new SendTcpMessage2(dealer.getPlayers().get(
								dealer.getTurn()), newMessage);
						stm.start();
					}
					break;
				case TURN:
					break;
				case FOLD:
					dealer.foldCurrentPlayer();
					if (dealer.getPlayers().size() == 1) {
						newMessage.msgType = MessageType.RESULT;
						newMessage.result = Result.WIN;
						newMessage.money = dealer.getPot();
					} else {
						if (dealer.sameCount == dealer.getPlayers().size()) {
							if (dealer.getCards().size() == 5) {
								dealer.dealCards();
								synchronized (lock) {
									lock = null;
								}

							} else {
								dealer.sameCount = 0;
								dealer.dealCards();
								newMessage.currentBet = 0;
								newMessage.msgType = MessageType.TURN;
								stm = new SendTcpMessage2(dealer.getPlayers()
										.get(dealer.getTurn()), newMessage);
								stm.start();
							}
						} else {
							newMessage.currentBet = dealer.prevBet;
							newMessage.msgType = MessageType.TURN;

							stm = new SendTcpMessage2(dealer.getPlayers().get(
									dealer.getTurn()), newMessage);
							stm.start();
						}
					}
					break;
				case CARD:
					break;
				case RESULT:
					break;
				case START:
					break;
				case JOIN:
					break;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}