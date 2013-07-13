package com.example.pokergame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PlayerJoinGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_join_game);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_join_game, menu);
		return true;
	}

}
