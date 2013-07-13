package com.example.pokergame;

import java.io.IOException;
import java.net.ServerSocket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PlayerHandActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_hand);
		
		try {
			TCPListener2 tl = new TCPListener2(new ServerSocket(6789), null);
			tl.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_hand, menu);
		return true;
	}

}

