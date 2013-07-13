package com.example.pokergame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlayerJoinGameActivity extends Activity {

	EditText ipAddressField;
	Button joinButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_join_game);
		ipAddressField = (EditText)findViewById(R.id.ipAddressEditText);
		joinButton = (Button) findViewById(R.id.joinGameButton);
		setListeners();
	}
	
	private void setListeners(){
		joinButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String str = ipAddressField.getText().toString();
				try {
					SendTcpMessage stm = new SendTcpMessage(InetAddress.getByName(str),"TESTING123");
					stm.run();
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
		getMenuInflater().inflate(R.menu.player_join_game, menu);
		return true;
	}

}
