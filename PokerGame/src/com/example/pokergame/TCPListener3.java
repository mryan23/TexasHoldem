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
					dealer.addToBet(dealer.getTurn(), m.proposedBet);
					if (dealer.prevBet == dealer.getBet(dealer.getTurn())) {
						dealer.sameCount++;
					} else {
						dealer.prevBet = dealer.getBet(dealer.getTurn());
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
							dealer.prevBet = 0;
							dealer.dealCards();
							dealer.increaseTurn();
							newMessage.msgType = MessageType.TURN;
							newMessage.currentBet = 0;
							newMessage.message = "What is your wager?";
						}
					} else {
						newMessage.msgType = MessageType.TURN;
						dealer.increaseTurn();
						newMessage.currentBet = dealer.prevBet
								- dealer.getBet(dealer.getTurn());
						if (dealer.prevBet == dealer.getBet(dealer.getTurn())) {
							newMessage.message = "What is your wager?";
						} else {
							if (dealer.getBet(dealer.getTurn()) == 0)
								newMessage.message = (dealer.prevBet - dealer
										.getBet(dealer.getTurn())) + " to call";
							else {
								int index;
								if(dealer.getTurn()==0)
									index=dealer.getPlayers().size()-1;
								else
									index=dealer.getTurn()-1;
								int val = dealer.getBet(index);
								String str="";
								while(index!= dealer.getTurn()){
									int nindex;
									if(index==0)
										nindex=dealer.getPlayers().size()-1;
									else
										nindex=index-1;
									if(dealer.getBet(nindex)<val){
										str=dealer.playerNames.get(index)+" ";
									}
									index=nindex;
								}
								newMessage.message = str+ "raised to "
										+ dealer.prevBet
										+ "\n"
										+ (dealer.prevBet - dealer
												.getBet(dealer.getTurn()))
										+ " to call";
							}
						}

					}
					break;
				case TURN:
					break;
				case FOLD:
					dealer.foldCurrentPlayer();
					if (dealer.getPlayers().size() == 1) {
						dealer.winnerText = "Winner";
						newMessage.msgType = MessageType.RESULT;
						newMessage.money = dealer.getPot();
						newMessage.result = Result.WIN;
						newMessage.message = "You Won!";
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

							}
						} else {
							newMessage.currentBet = dealer.prevBet
									- dealer.getBet(dealer.getTurn());
							newMessage.msgType = MessageType.TURN;

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
				stm = new SendTcpMessage2(dealer.getPlayers().get(
						dealer.getTurn()), newMessage);
				stm.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}