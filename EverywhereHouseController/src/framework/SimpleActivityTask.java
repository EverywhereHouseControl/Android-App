package framework;

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
import android.util.Log;
import android.widget.Toast;

public abstract class SimpleActivityTask extends AsyncTask<String, String, String> {

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

	private ArrayList<String> parametros = new ArrayList<String>();


	/** The working. */
	private static volatile boolean working = false;
	
	
	/**
	 * Execute task.
	 * 
	 * @param task
	 *            the task
	 */
	public static void executeTask(SimpleActivityTask task) {
		if (!working) {
			working = true;
			task.execute();
		}
	}
	

	
	/**
	 * Instantiates a new simple activity task.
	 * 
	 * @param activity
	 *            the activity
	 */
	protected SimpleActivityTask(Context c) {
		this.context = c;
	}
	
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

			Log.d("PARAMETROS", parametros.toString());
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

	public void sendAction(Context c, String a, String d) {
		context = c;
		action=a;
		_data=d;
		parser();
		_post = new Post();		
		execute();
		
	}
}
