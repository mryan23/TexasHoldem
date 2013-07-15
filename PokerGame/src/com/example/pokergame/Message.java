package com.example.pokergame;

import java.io.Serializable;

import com.example.poker.Card;


public class Message implements Serializable{
	public MessageType msgType; 
	public int currentBet;
	public int proposedBet;
	public Card card;
	public int money;
	public String message;
	
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