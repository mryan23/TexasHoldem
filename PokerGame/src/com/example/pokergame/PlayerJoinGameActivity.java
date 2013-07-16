package com.example.pokergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlayerJoinGameActivity extends Activity {

	EditText nameField;
	Button joinButton;
	ArrayList<Integer> isSuccessful = new ArrayList<Integer>();
	Context context = this;
	ArrayList<InetAddress>addresses=new ArrayList<InetAddress>();

	InetAddress broadcast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_join_game);
		nameField = (EditText) findViewById(R.id.nameEditText);
		joinButton = (Button) findViewById(R.id.joinGameButton);

		TCPListener listener;

		

		try {
			listener = new TCPListener(new ServerSocket(6789), isSuccessful,addresses);
			listener.sendResponse(false);
			listener.start();

			NetworkInterface ni;
			ni = NetworkInterface.getByInetAddress(InetAddress.getByName(Utils
					.getIPAddress(true)));
			InterfaceAddress ia = ni.getInterfaceAddresses().get(1);
			broadcast= InetAddress.getByName("255.255.255.255");
			//dummy += " ";

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setListeners(); // send ip/joining msg

		final Timer autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (isSuccessful.contains(1)) {
							System.out.println("Value in Activity: "
									+ isSuccessful.get(0));
							Intent i = new Intent(context,
									PlayerHandActivity.class);
							Bundle bundle = new Bundle();
							//System.out.println(ipAddressField.toString());
							if(addresses.size()==0)
								return;
							bundle.putString("ipAddress", addresses.get(0).toString().substring(1));
							i.putExtras(bundle);
							autoUpdate.cancel();
							startActivity(i);
						}
					}
				});
			}
		}, 0, 100);

	}

	private void setListeners() {
		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Thread udpThread;
				udpThread= new UDPBroadcaster(broadcast,nameField.getText().toString());
				udpThread.start();

				/*String str = ipAddressField.getText().toString();
				try {
					SendTcpMessage stm = new SendTcpMessage(InetAddress
							.getByName(str), "JOINING");
					stm.start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

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
