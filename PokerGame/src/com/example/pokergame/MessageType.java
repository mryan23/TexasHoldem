package com.example.pokergame;

public enum MessageType {
	JOIN, START, 				/* INIT */
	CARD, RESULT, TURN,			/* DEALER */
	BET, FOLD;			/* PLAYER */
}
