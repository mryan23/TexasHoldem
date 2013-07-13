package com.example.pokergame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.poker.Card;

public class DealerMainActivity extends Activity {

	ArrayList<InetAddress> player_addr = new ArrayList<InetAddress>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dealer_main);
		
		Intent i = this.getIntent(); 
		Bundle bundle = i.getExtras();
		System.out.println(bundle==null);
		String[] ipAddrs = bundle.getStringArray("ipAddresses");
		
		try {
			
			if (ipAddrs != null) {
				for (int index = 0; index < ipAddrs.length; index++) {
					System.out.println(ipAddrs[index]);
					player_addr.add(InetAddress.getByName(ipAddrs[index].substring(1)));

				}
			}
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("OIWEROIJWER");
			e.printStackTrace();
		}
		
		Button test = (Button)findViewById(R.id.sendTestCard);
		test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(InetAddress ip: player_addr){
					Message message = new Message();
					message.card=new Card(14,Card.SPADE);
					SendTcpMessage2 stm = new SendTcpMessage2(ip, message);
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
