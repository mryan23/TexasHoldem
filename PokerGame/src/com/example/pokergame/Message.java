package com.example.pokergame;

import com.example.poker.Card;


public class Message {
	MessageType msgType; 
	int currentBet;
	int proposedBet;
	Card card;
	int money;
	
	public MessageType getMsgType() {
		return msgType;
	}
	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}
	public int getCurrentBet() {
		return currentBet;
	}
	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}
	public int getProposedBet() {
		return proposedBet;
	}
	public void setProposedBet(int proposedBet) {
		this.proposedBet = proposedBet;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	Result result;
}