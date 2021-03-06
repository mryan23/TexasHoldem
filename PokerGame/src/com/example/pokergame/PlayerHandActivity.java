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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.poker.Card;
import com.example.poker.OutOfMoneyException;
import com.example.poker.Player;

public class PlayerHandActivity extends Activity {

	Player p = new Player(1000);
	TextView turnText, moneyText, updateText;
	Button foldButton;
	Button betButton;
	InetAddress ipAddress;
	NumberPicker np;
	boolean hasRecievedCards = false;
	TCPListener2 tl;

	ImageView[] imageViews = new ImageView[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_hand);
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		String ip = bundle.getString("ipAddress");
		System.out.println(ip);
		try {
			ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		updateText = (TextView) findViewById(R.id.PlayerUpdateTextView);
		turnText = (TextView) findViewById(R.id.turnLabel);
		moneyText = (TextView) findViewById(R.id.money);
		foldButton = (Button) findViewById(R.id.Fold);
		betButton = (Button) findViewById(R.id.Bet);
		np = (NumberPicker) findViewById(R.id.betSelector);
		imageViews[0] = (ImageView) findViewById(R.id.playerImageView1);
		imageViews[1] = (ImageView) findViewById(R.id.playerImageView2);

		np.setMinValue(0);
		moneyText.setText("$" + p.getTotalMoney() + "");
		setListeners();

		try {
			tl = new TCPListener2(new ServerSocket(6789), p);
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
						// synchronized (p) {
						updateScreen();
						// }
					}
				});
			}
		}, 0, 100);

	}

	private void updateScreen() {
		// synchronized (p) {
		if (p.status == Player.FOLDED || p.status == Player.GAME_OVER) {
			clearScreen();
		}

		if (!hasRecievedCards && p.getHand().hand.size() > 0) {
			for (int i = 0; i < p.getHand().hand.size(); i++) {
				Card c = p.getHand().hand.get(i);
				imageViews[i].setImageResource((getResources().getIdentifier(
						"img" + c.getValue() + "_" + c.getSuit(), "drawable",
						"com.example.pokergame")));
			}
		}
		if (p.getHand().hand.size() == 2) {
			hasRecievedCards = true;
		}

		if (foldButton.isEnabled() != p.turn)
			foldButton.setEnabled(p.turn);
		if (betButton.isEnabled() != p.turn)
			betButton.setEnabled(p.turn);
		if (p.turn) {
			turnText.setText("Its your turn!");
		} else {
			turnText.setText(" ");
		}
		if (p.updated) {
			np.setMaxValue(p.getTotalMoney());
			if (p.minBet >= 0) {
				np.setMinValue(p.minBet);
				if(p.minBet>0)
					np.setValue(p.minBet);
				
			}
			p.updated = false;
		}
		moneyText.setText(p.getTotalMoney() + "");
		updateText.setText(p.getUpdateText());

		// }
	}

	private void clearScreen() {
		p = new Player(p.getTotalMoney());
		tl.setPlayer(p);
		hasRecievedCards = false;
		for (ImageView im : imageViews) {
			im.setImageResource(R.drawable.b2fv);
		}
	}

	private void setListeners() {
		foldButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.msgType = MessageType.FOLD;
				p.turn = false;
				SendTcpMessage2 stm = new SendTcpMessage2(ipAddress, msg);
				stm.start();
				p.status = Player.FOLDED;
				p.setUpdateText("");

			}

		});

		betButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.msgType = MessageType.BET;
				msg.proposedBet = np.getValue();
				p.turn = false;
				p.setUpdateText("");
				try {
					p.bet(np.getValue());
					moneyText.setText("$" + p.getTotalMoney() + "");
				} catch (OutOfMoneyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				SendTcpMessage2 stm = new SendTcpMessage2(ipAddress, msg);
				stm.start();

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
