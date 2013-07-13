package com.example.pokergame;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.poker.Card;

public class PlayerHandActivity extends Activity {

	ArrayList<Card> hand = new ArrayList<Card>();
	TextView debugCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_hand);
		debugCard = (TextView) findViewById(R.id.DebugCardTextView);

		try {
			TCPListener2 tl = new TCPListener2(new ServerSocket(6789), hand);
			tl.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Timer autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						synchronized (hand) {
							String str = "";
							for(Card c:hand)
							{
								str+=c.getValue()+" "+c.getSuit()+"\n";
							}
							debugCard.setText(str);
						}
					}
				});
			}
		}, 0, 100);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_hand, menu);
		return true;
	}

}
