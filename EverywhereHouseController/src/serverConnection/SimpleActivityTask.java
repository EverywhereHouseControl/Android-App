package serverConnection;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SimpleActivityTask extends AsyncTask<String, String, String> 
{

	private String _currentRoom;
	private String _file;
	private String _servicename;
	private String _action;
	private String _message;
	private String _data;
	private String _house;
	private String _start;
	private String _eventName;
	private Context _context;
	private final String _ip = Post._ip;

	private ArrayList<String> _parametros = new ArrayList<String>();

	/** The working. */
	private static volatile boolean _working = false;

	/**
	 * Execute task.
	 * 
	 * @param task
	 *            the task
	 */
	public static void executeTask(SimpleActivityTask task) 
	{
		if (!_working) 
		{
			_working = true;
			task.execute();
		}
	}

	/**
	 * Instantiates a new simple activity task.
	 * 
	 * @param activity
	 *            the activity
	 */
	public SimpleActivityTask(Context _c) 
	{
		this._context = _c;
	}

	/**
	 * Message "Loading"
	 */
	protected void onPreExecute() 
	{
		super.onPreExecute();
		/*
		 * pDialog = new ProgressDialog(context);
		 * pDialog.setMessage("Loading. Please wait...");
		 * pDialog.setIndeterminate(false); pDialog.setCancelable(false);
		 * pDialog.show();
		 */
	}

	@Override
	protected String doInBackground(String... _params) 
	{
		// TODO Auto-generated method stub
		int _internalError = 0;

		try 
		{
			Log.e("servicename", _servicename.toString());
			JSONObject obj = new JSONObject(_file);

			_parametros.add("command");
			_parametros.add("doaction");
			_parametros.add("username");
			_parametros.add(obj.getString("USERNAME"));
			_parametros.add("housename");
			_parametros.add(_house);
			_parametros.add("roomname");
			_parametros.add(_currentRoom);
			_parametros.add("servicename");
			_parametros.add(_servicename);
			_parametros.add("actionname");
			_parametros.add(_action);
			_parametros.add("data");
			_parametros.add(_data);

			Log.d("PARAMETROS", _parametros.toString());
			errorControl(_parametros, _internalError);

		} 
		catch (Exception _e1) 
		{
			// TODO Auto-generated catch block
			_e1.printStackTrace();
		}

		return null;
	}

	/**
		 * 
		 */
	protected void onPostExecute(String file_url) 
	{
		Toast.makeText(_context, _message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Error control for modify user.
	 */
	private void errorControl(ArrayList<String> parametros, int _internalError) 
	{

		switch (_internalError) 
		{
			case 0: 
			{
				JSONObject data = Post.getServerData(parametros,_ip);
				try 
				{
					JSONObject json_data = data.getJSONObject("error");
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
	
				}
				catch (JSONException _e) 
				{
					// TODO Auto-generated catch block
					_e.printStackTrace();
				}
				break;
			}
			default: 
			{
	
			}
		}
	}

	private void parser() 
	{
		
		_file = JSON.loadUserInformation(_context);
		String _file2 = JSON.loadUserEnvironment(_context);
		try
		{
			JSONObject _obj = new JSONObject(_file2);
			JSONObject infoCasa = _obj.getJSONArray("houses").getJSONObject(0);
			_house = infoCasa.getString("name");
		}
		catch(Exception _ex)
		{
			_ex.printStackTrace();
		}
			
	}

	public void sendAction(String _r, String _s, String _a, String _d) 
	{
		_servicename = _s;
		_currentRoom = _r;
		_action = _a;
		_data = _d;
		parser();	
		execute();
	}

	public void sendEvent(String house, String room, String service,
			String actionName, String data, String start, String name) 
	{
		_house=house;
		_currentRoom = room;
		_servicename = service;
		_action = actionName;
		_data = data;
		_start = start;
		_eventName = name;
	}

}



