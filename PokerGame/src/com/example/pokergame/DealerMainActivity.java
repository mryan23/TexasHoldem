package com.example.pokergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poker.Card;
import com.example.poker.Dealer;
import com.example.poker.Deck;
import com.example.poker.Hand;
import com.example.poker.TooManyCardsException;

public class DealerMainActivity extends Activity {

	Deck deck = new Deck();
	Dealer dealer = new Dealer(deck);
	TextView potTextView, debugCardTextView, winnerTextView;
	ArrayList<InetAddress> player_addr = new ArrayList<InetAddress>();
	ImageView[] imageViews = new ImageView[5];
	boolean[] drawn = {false,false,false,false,false};
	volatile Object lock=new Object();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealer_main);

		winnerTextView=(TextView)findViewById(R.id.winnerTextView);
		potTextView = (TextView) findViewById(R.id.PotTextView);
		potTextView.setText("$0");
		//debugCardTextView = (TextView)findViewById(R.id.debugCardTextView);
		
		imageViews[0]=(ImageView)findViewById(R.id.imageView1);
		imageViews[1]=(ImageView)findViewById(R.id.imageView2);
		imageViews[2]=(ImageView)findViewById(R.id.imageView3);
		imageViews[3]=(ImageView)findViewById(R.id.imageView4);
		imageViews[4]=(ImageView)findViewById(R.id.imageView5);

		try {
			TCPListener3 listener = new TCPListener3(new ServerSocket(6789),
					dealer, lock);
			listener.start();
		} catch (IOException e1) { 
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Intent i = this.getIntent();
		Bundle bundle = i.getExtras();
		System.out.println(bundle == null);
		String[] ipAddrs = bundle.getStringArray("ipAddresses");
		deck.shuffle();
		Timer autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateScreen();
					}
				});
			}
		}, 0, 100);

		try {

			if (ipAddrs != null) {
				for (int index = 0; index < ipAddrs.length; index++) {
					System.out.println(ipAddrs[index]);
					player_addr.add(InetAddress.getByName(ipAddrs[index]
							.substring(1)));

				}
				ArrayList<InetAddress> dummy = new ArrayList<InetAddress>();
				for (int index = 0; index < player_addr.size(); index++)
					dummy.add(player_addr.get(index));
				synchronized (dealer) {
					dealer.setPlayers(dummy);
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("OIWEROIJWER");
			e.printStackTrace();
		}

		setListeners();

	}

	private void updateScreen() {
		if(lock==null)
		{
			System.out.println("LOCK IS NULL");
			/*deck = new Deck();
			deck.shuffle();
			dealer=new Dealer(deck);
			for(ImageView iv: imageViews){
				iv.setImageResource(R.drawable.b2fv);
			}*/
		}
		winnerTextView.setText(dealer.winnerText);
		potTextView.setText("$" + dealer.getPot());
		/*String str = "";
		for(Card c: dealer.getCards()){
			str+=c.getValue()+" "+c.getSuit()+"\n";
		}
		debugCardTextView.setText(str);*/
		for(int i = 0; i < dealer.getCards().size(); i++){
			Card c = dealer.getCards().get(i);
			imageViews[i].setImageResource((getResources().getIdentifier("img"+c.getValue()+"_"+c.getSuit(), "drawable", "com.example.pokergame")));
			
			//imageViews[i].setImageBitmap(BitmapFactory.decodeFile("res/drawable-hdpi/img"+c.getValue()+"_"+c.getSuit()+".png"));	
			
		}
	}

	public void setListeners() {
		Button test = (Button) findViewById(R.id.sendTestCard);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				synchronized (dealer) {
					ArrayList<Hand> hands = new ArrayList<Hand>();
					for(int j = 0; j < dealer.getPlayers().size(); j++){
						hands.add(new Hand());
					}
					for (int i = 0; i < 2; i++) {
						//for (InetAddress ip : dealer.getPlayers()) {
						for(int j = 0; j < dealer.getPlayers().size(); j++){
							InetAddress ip = dealer.getPlayers().get(j);
							Message message = new Message();
							message.msgType = MessageType.CARD;
							Card top = deck.getTop();
							try {
								hands.get(j).addCard(top);
							} catch (TooManyCardsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							message.card = top;
							System.out.println(message.card);
							SendTcpMessage2 stm = new SendTcpMessage2(ip,
									message);
							stm.start();
						}
					}
					dealer.hands = hands;
					int turn = dealer.getTurn();
					Message message = new Message();
					message.msgType = MessageType.TURN; 
					message.currentBet = 0;
					SendTcpMessage2 stm = new SendTcpMessage2(dealer
							.getPlayers().get(turn), message);
					stm.start();
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dealer_main, menu);
		return true;
	}

}
