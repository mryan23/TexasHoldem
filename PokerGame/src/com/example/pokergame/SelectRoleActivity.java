package com.example.pokergame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectRoleActivity extends Activity {

	Button playerButton;
	Button dealerButton;
	Context context;
	//written from aide!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        playerButton = (Button)findViewById(R.id.playerRoleButton);
        context = this;
        playerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, PlayerJoinGameActivity.class);
				startActivity(i);
				
			}
        	
        });
        dealerButton=(Button)findViewById(R.id.dealerRoleButton);
        dealerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, DealerCreateGameActivity.class);
				startActivity(i);
				
			}
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_role, menu);
        return true;
    }
    
}
