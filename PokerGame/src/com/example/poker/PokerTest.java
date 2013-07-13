package com.example.poker;

public class PokerTest {

	public static void main(String[] args) {
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		Hand hand3 = new Hand();
		
		try {
			hand1.addCard(new Card(2, 0));
			hand1.addCard(new Card(5, 1));
			hand1.addCard(new Card(3, 0));
			hand1.addCard(new Card(3, 1));
			hand1.addCard(new Card(4, 0));
			hand1.addCard(new Card(5,0));
			hand1.addCard(new Card(6,0));
		} catch (TooManyCardsException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		try {
			hand2.addCard(new Card(3, 2));
			hand2.addCard(new Card(3, 3));
			hand2.addCard(new Card(3, 0));
			hand2.addCard(new Card(3, 1));
			hand2.addCard(new Card(4, 0));
			hand2.addCard(new Card(5,0));
			hand2.addCard(new Card(6,0));
			
			hand3.addCard(new Card(7,0));
			hand3.addCard(new Card(8,0));
			hand3.addCard(new Card(3, 0));
			hand3.addCard(new Card(3, 1));
			hand3.addCard(new Card(4, 0));
			hand3.addCard(new Card(5,0));
			hand3.addCard(new Card(6,0));
		} catch (TooManyCardsException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		try {
			HandRank hand1Rank = new HandRank(hand1);
			System.out.println(hand1Rank.getRankName());
			HandRank hand2Rank = new HandRank(hand2);
			System.out.println(hand2Rank.getRankName());
			HandRank hand3Rank = new HandRank(hand3);
			System.out.println(hand3Rank.getRankName());
		} catch (TooFewCardsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//int result = HandRank.compareHands(hand1, hand2, 5);
			Hand[] hands = {hand1, hand2, hand3};
			int result = HandRank.compareHands(hands);
			System.out.println("Result is: " + result);
			
		} catch (TooFewCardsException e) {
			e.printStackTrace();
		} catch (TooManyCardsException e) {
			e.printStackTrace();
		}
		
		/*
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		Deck deck = new Deck();
		
		deck.shuffle();
		
		// Build the first hand
		for(int i = 0; i < Hand.MAX_SIZE; i++) {
			try {
				Card card = deck.getTop();
				hand1.addCard(card);
				System.out.println("HAND 1 - Card: value - " + card.getValue() + " ; suit - " + card.getSuit());
			} catch (TooManyCardsException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		System.out.println("");
		
		// Build the second hand
		for(int i = 0; i < Hand.MAX_SIZE; i++) {
			try {
				Card card = deck.getTop();
				hand2.addCard(card);
				System.out.println("HAND 2 - Card: value - " + card.getValue() + " ; suit - " + card.getSuit());
			} catch (TooManyCardsException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		
		System.out.println("");
		
		try {
			HandRank rank1 = new HandRank(hand1);
			HandRank rank2 = new HandRank(hand2);
			
			int result = HandRank.compareRanks(rank1, rank2);
			
			if(result == 0) {
				System.out.println("HAND 1 is the Winner! With a " + rank1.getRankName());
			} else if(result == 1) {
				System.out.println("HAND 2 is the Winner! With a " + rank2.getRankName());
			} else if(result == -1) {
				System.out.println("It is a tie! With " + rank1.getRankName());
			}
			
		} catch (TooFewCardsException e) {
			throw new RuntimeException(e.getMessage());
		}
		*/
	}
	
}
