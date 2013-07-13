package com.example.pokergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DealerCreateGameActivity extends Activity {

	Object lock = new Object();
	ArrayList<InetAddress> player_addr = new ArrayList<InetAddress>();
	TextView ipAddressEditText, connectedPlayersTextView;
	TCPListener listener;
	Context context = this;
	ServerSocket sock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealer_create_game);
		ipAddressEditText=(TextView)findViewById(R.id.ipAddressText);
		ipAddressEditText.setText(Utils.getIPAddress(true));
		connectedPlayersTextView=(TextView)findViewById(R.id.connectedUsersTextView);
		connectedPlayersTextView.setText("0");
		
		Timer autoUpdate = new Timer();
		  autoUpdate.schedule(new TimerTask() {
		   @Override
		   public void run() {
		    runOnUiThread(new Runnable() {
		     public void run() {
		      updateConnectedPlayers();
		     }
		    });
		   }
		  }, 0, 500);
		
		try {
			sock = new ServerSocket(6789);
			listener = new TCPListener( sock, player_addr, lock);
			listener.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
		Button startGameButton = (Button)findViewById(R.id.startGameButton);
		startGameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				for(InetAddress addr: player_addr){
					SendTcpMessage stm = new SendTcpMessage(addr, "STARTGAME");
					stm.start();
				}
				
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listener.interrupt();
				
				
				String[] ipAddrs = new String[player_addr.size()];
				for(int i = 0; i < ipAddrs.length; i++){
					ipAddrs[i]=player_addr.get(i).toString();
				}
				Bundle bundle = new Bundle();
				bundle.putStringArray("ipAddresses", ipAddrs);
				Intent i = new Intent(context, DealerMainActivity.class);
				i.putExtras(bundle); 
				startActivity(i);
				
				
			}
			
		});
		
	}
	
	private void updateConnectedPlayers(){
		connectedPlayersTextView.setText(player_addr.size()+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dealer_create_game, menu);
		return true;
	}
	public String getLocalIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		String ipString = String.format("%d.%d.%d.%d", (ip & 0xff),
				(ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		return ipString;
	}

}
