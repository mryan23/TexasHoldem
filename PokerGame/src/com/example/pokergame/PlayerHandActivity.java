package com.example.pokergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.poker.Card;
import com.example.poker.OutOfMoneyException;
import com.example.poker.Player;

public class PlayerHandActivity extends Activity {

	Player p = new Player(1000);
	TextView debugCard;
	Button foldButton;
	Button betButton;
	InetAddress ipAddress;
	NumberPicker np;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_hand);
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		String ip = bundle.getString("ipAddress");
		try {
			ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		debugCard = (TextView) findViewById(R.id.DebugCardTextView);
		foldButton = (Button) findViewById(R.id.Fold);
		betButton = (Button) findViewById(R.id.Bet);
		np = (NumberPicker) findViewById(R.id.betSelector);

		try {
			TCPListener2 tl = new TCPListener2(new ServerSocket(6789), p);
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
						synchronized (p) {
							String str = "";
							for (Card c : p.getHand().hand) {
								str += c.getValue() + " " + c.getSuit() + "\n";
							}
							debugCard.setText(str);
							if(foldButton.isEnabled()!= p.turn)
								foldButton.setEnabled(p.turn);
							if(betButton.isEnabled()!= p.turn)
								betButton.setEnabled(p.turn);					

						}
					}
				});
			}
		}, 0, 100);

	}

	private void setListeners() {
		foldButton.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.msgType = MessageType.FOLD;
				String str = ipAddress.toString();
				try {
					SendTcpMessage2 stm = new SendTcpMessage2(InetAddress
							.getByName(str), msg);
					stm.start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		betButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.msgType = MessageType.BET;
				msg.proposedBet = np.getValue();
				try {
					p.bet(np.getValue());
				} catch (OutOfMoneyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String str = ipAddress.toString();
				
				try {
					SendTcpMessage2 stm = new SendTcpMessage2(InetAddress
							.getByName(str), msg);
					stm.start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_hand, menu);
		return true;
	}

}
