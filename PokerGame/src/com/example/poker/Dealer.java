package com.example.poker;

import java.net.InetAddress;
import java.util.ArrayList;

import com.example.pokergame.Message;
import com.example.pokergame.MessageType;
import com.example.pokergame.Result;
import com.example.pokergame.SendTcpMessage2;

public class Dealer {

	private int pot;
	private ArrayList<Card> cards;
	private ArrayList<InetAddress> player_addr;
	public ArrayList<String> playerNames;
	private ArrayList<Integer> playerBets;
	public ArrayList<Hand> hands;
	private int turn;
	public int prevBet;
	public int sameCount;
	public Deck deck;
	public String winnerText;
	public int winner;
	
	public Dealer(Deck d) {
		reset(d);
	}
	public void reset(Deck d){
		deck = d;
		pot = 0;
		turn = 0;
		cards = new ArrayList<Card>();
		player_addr = new ArrayList<InetAddress>();
		playerBets = new ArrayList<Integer>();
		playerNames = new ArrayList<String>();
		prevBet = 0;
		sameCount = 0;
		winnerText="";
		winner=-1;
	}

	public int getPot() {
		return pot;
	}

	public void addToPot(int delta) {
		pot += delta;
	}

	public void addCard(Card c) {
		cards.add(c);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setPlayers(ArrayList<InetAddress> players, ArrayList<String> names) {
		player_addr = players;
		playerNames = names;
		for(InetAddress ip : player_addr)
			playerBets.add(0);
	}
	
	public int getBet(int i){
		if(i < playerBets.size() && i >= 0)
			return playerBets.get(i);
		return -1;
	}
	public void addToBet(int i, int val){
		if(i < playerBets.size() && i >= 0){
			Integer removed = playerBets.remove(i);
			playerBets.add(i, removed+val);
		}
	}
	public void clearBets(){
		playerBets=new ArrayList<Integer>();
		for(InetAddress ip:player_addr)
			playerBets.add(0);
	}

	public ArrayList<InetAddress> getPlayers() {
		return player_addr;
	}

	public int getTurn() {
		return turn;
	}

	public void increaseTurn() {
		turn = (turn + 1) % player_addr.size();
	}

	public void foldCurrentPlayer() {
		player_addr.remove(turn);
		playerBets.remove(turn);
		playerNames.remove(turn);
		if (turn >= player_addr.size())
			turn = 0;
	}

	public void dealCards() {
		clearBets();
		if (cards.size() == 0) {
			cards.add(deck.getTop());
			cards.add(deck.getTop());
			cards.add(deck.getTop());
		} else if (cards.size() < 5) {
			cards.add(deck.getTop());
		}
		else
		{
			Hand[] ha = new Hand[hands.size()];
			for(int i = 0; i < ha.length; i++){
				ha[i]=hands.get(i);
				for(int j = 0; j < getCards().size(); j++){
					try {
						ha[i].addCard(getCards().get(j));
					} catch (TooManyCardsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			try {
				int index = HandRank.compareHands(ha);
				winner=index;
				System.out.println("WINNER IS "+index+" WITH "+new HandRank(hands.get(index)).getRankName());
				winnerText=playerNames.get(index)+" wins with "+new HandRank(hands.get(index)).getRankName();
				for(int i = 0; i < player_addr.size(); i++){
					Message newMessage = new Message();
					newMessage.setMsgType(MessageType.RESULT);
					newMessage.setMoney(0);
					if(i == index)
					{
						newMessage.setResult(Result.WIN);
						newMessage.setMoney(getPot());
					}
					else
						newMessage.setResult(Result.LOSE);
					
					SendTcpMessage2 stm=new SendTcpMessage2(player_addr.get(i),newMessage);
					stm.start();
				}
				
			} catch (TooFewCardsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TooManyCardsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
