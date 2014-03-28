package environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import ehc.net.R;
import framework.Post;

public class RemoteController extends Activity {

	private String currentRoom;
	private ProgressDialog pDialog;
	private String file;
	private String servicename;
	private String action;
	private Post _post;
	private String _message;
	private String _data;
	private String _house;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_control_view);

		setListeners();
	}
	
	private void setListeners(){
		ImageView button_one = (ImageView) findViewById(R.id.one_remote);
		button_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("UNO","16748655");				
			}
		});
		
		ImageView button_two = (ImageView) findViewById(R.id.two_remote);
		button_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("DOS","16758855");				
			}
		});
		
		ImageView button_three = (ImageView) findViewById(R.id.three_remote);
		button_three.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("TRES","16775175");				
			}
		});
		
		ImageView button_four = (ImageView) findViewById(R.id.four_remote);
		button_four.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("CUATRO","16756815");				
			}
		});
		
		ImageView button_five = (ImageView) findViewById(R.id.five_remote);
		button_five.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("CINCO","16750695");				
			}
		});
		
		ImageView button_six = (ImageView) findViewById(R.id.six_remote);
		button_six.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("SEIS","16767015");				
			}
		});
		
		ImageView button_seven = (ImageView) findViewById(R.id.seven_remote);
		button_seven.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("SIETE","16746615");				
			}
		});
		
		ImageView button_eight = (ImageView) findViewById(R.id.eight_remote);
		button_eight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("OCHO","16754775");				
			}
		});
		
		ImageView button_nine = (ImageView) findViewById(R.id.nine_remote);
		button_nine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("NUEVE","16771095");				
			}
		});
		
		ImageView button_zero = (ImageView) findViewById(R.id.zero_remote);
		button_zero.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("CERO","16730295");				
			}
		});
		
		ImageView button_fav = (ImageView) findViewById(R.id.star_remote);
		button_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("FAV","16732845");				
			}
		});
		
		ImageView button_up = (ImageView) findViewById(R.id.up_arrow_remote);
		button_up.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("UP","16769055");				
			}
		});
		
		ImageView button_left = (ImageView) findViewById(R.id.left_arrow_remote);
		button_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("LEFT","16718055");				
			}
		});
		
		ImageView button_play = (ImageView) findViewById(R.id.play_remote);
		button_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("PLAY","16720605");				
			}
		});
		
		ImageView button_right = (ImageView) findViewById(R.id.rigth_arrow_remote);
		button_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("RIGHT","16773135");				
			}
		});
				
		ImageView button_mute = (ImageView) findViewById(R.id.mute_remote);
		button_mute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("MUTE","16745085");				
			}
		});
		
		ImageView button_setup = (ImageView) findViewById(R.id.config_remote);
		button_setup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("SETUP","16774605");				
			}
		});
		
		ImageView button_power = (ImageView) findViewById(R.id.on_off_remote);
		button_power.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				startConnection("POWER","16722135");				
			}
		});
		
		
		
	}
	
	

	private void startConnection(String act, String data){
		Toast.makeText(this, "Has pulsado " + act, Toast.LENGTH_SHORT).show();
//		action=act;
//		_data=data;
//		parser();
//		_post = new Post();
//		doActionConnection connection = new doActionConnection();
//		connection.execute();
	}
	
	private void parser() {
		String file2;
		try {
			InputStream is = context.openFileInput("profileInformation.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			this.file = new String(buffer, "UTF-8");
			is = context.openFileInput("configuration.json");
			size = is.available();
			buffer = new byte[size];
			is.read(buffer);
			is.close();
			file2 = new String(buffer, "UTF-8");
			JSONObject obj = new JSONObject(file2);
			_house = obj.getString("House");

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class doActionConnection extends AsyncTask<String, String, String> {
		private ArrayList<String> parametros = new ArrayList<String>();


		/**
		 * Message "Loading"
		 */
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * pDialog = new ProgressDialog(context);
			 * pDialog.setMessage("Loading. Please wait...");
			 * pDialog.setIndeterminate(false); pDialog.setCancelable(false);
			 * pDialog.show();
			 */
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			int _internalError = 0;

			try {
				Log.e("servicename", servicename.toString());
				JSONObject obj = new JSONObject(file);

				parametros.add("command");
				parametros.add("doaction");
				parametros.add("username");
				parametros.add(obj.getString("USERNAME"));
				parametros.add("housename");
				parametros.add(_house);
				parametros.add("roomname");
				parametros.add(currentRoom);
				parametros.add("servicename");
				parametros.add(servicename);
				parametros.add("actionname");
				parametros.add(action);
				parametros.add("data");
				parametros.add("_data");

				Log.d("PARAMETROS",parametros.toString());
				errorControl(parametros, _internalError);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return null;
		}

		/**
		 * 
		 */
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			// pDialog.dismiss();
			Toast.makeText(context, _message, Toast.LENGTH_SHORT).show();
		}
		
		/**
		 * Error control for modify user.
		 */
		private void errorControl(ArrayList<String> parametros, int _internalError) {
			
			switch (_internalError) {
			case 0: {
				JSONArray data = _post.getServerData(parametros,
						"http://5.231.69.226/EHControlConnect/index.php");
				try {
					JSONObject json_data = data.getJSONObject(0);
					Log.d("ERROR", json_data.toString());

					switch (json_data.getInt("ERROR")) {
					case 0: {
						_message = json_data.getString("ENGLISH");
						break;
					}
					default: {
						_message = json_data.getString("ENGLISH");
						break;
					}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			default: {

			}
			}
		}
	}

	

}