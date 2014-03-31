package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class JSON {
	private static JSON _instance;
	public ArrayList<String> _rooms;
	public ArrayList<String> _items;

	private static final String _TAG_NAME = "name";
//	private static final String _TAG_USER = "User";
	private static final String _TAG_ROOMS = "Rooms";
	private static final String _TAG_ITEMS = "items";
	private String _file = null;

	@SuppressWarnings("unused")
	private JSON() {}

	public static synchronized JSON getInstance(Context c) 
	{
		_instance = new JSON(c);
		return _instance;
	}

	public JSON(Context c) 
	{
		// try
		// {
		// InputStream is = c.getAssets().open("config.json");
		// int size = is.available();
		// byte[] buffer = new byte[size];
		// is.read(buffer);
		// is.close();
		// this.file = new String(buffer, "UTF-8");
		// try {
		// loadJSON();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		//
		// }
		// catch (IOException ex)
		// {
		// ex.printStackTrace();
		// }
		try 
		{
			InputStream _is = c.openFileInput("configuration.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._file = new String(buffer, "UTF-8");
			try 
			{
				loadJSON();
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}

	private void loadJSON() throws JSONException 
	{
		Log.d("JSON ", _file.toString());

		JSONObject _obj = new JSONObject(_file);
		try 
		{
			JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
			for (int i = 0; i <= _habitaciones.length(); i++) 
			{
				JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);

				System.out.println(_habitacion.getString(_TAG_NAME));

				Log.e("COLIN_TAG", _habitacion.getString(_TAG_NAME));
				JSONArray items = _habitacion.getJSONArray(_TAG_ITEMS);

				for (int j = 0; j <= items.length(); j++) 
				{
					Log.e("COLIN_TAG", items.getString(j));
					System.out.println(items.getString(j));
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	public ArrayList<String> getRooms() throws JSONException 
	{
		JSONObject _obj = new JSONObject(this._file);
		ArrayList<String> _rooms = new ArrayList<String>();

		try 
		{
			JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
			for (int i = 1; i <= _habitaciones.length(); i++) 
			{
				JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);
				_rooms.add(_habitacion.getString(_TAG_NAME));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return _rooms;
	}

	public ArrayList<String> getAllItems() throws JSONException 
	{

		JSONObject _obj = new JSONObject(this._file);
		ArrayList<String> _rooms = new ArrayList<String>();

		try 
		{
			JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
			for (int i = 1; i <= _habitaciones.length(); i++) 
			{
				JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);

				System.out.println(_habitacion.getString(_TAG_NAME));

				Log.e("COLIN_TAG", _habitacion.getString(_TAG_NAME));
				JSONArray _items = _habitacion.getJSONArray(_TAG_ITEMS);

				for (int j = 0; j < _items.length(); j++) 
				{
					_rooms.add(_items.getString(j));
					Log.e("COLIN_TAG", _items.getString(j));
					System.out.println(_items.getString(j));
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return _rooms;
	}

	@SuppressLint("DefaultLocale")
	public ArrayList<String> getItems(String roomName) throws JSONException 
	{

		JSONObject _obj = new JSONObject(this._file);
		ArrayList<String> _rooms = new ArrayList<String>();

		try 
		{
			JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
			for (int i = 1; i <= _habitaciones.length(); i++) 
			{
				JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);
				JSONArray _items = _habitacion.getJSONArray(_TAG_ITEMS);
				Log.e("getItems", "items " + _items);
				if (roomName.equals(_habitacion.getString(_TAG_NAME))) 
				{
					for (int j = 0; j < _items.length(); j++) 
					{
						_rooms.add(_items.getString(j).toUpperCase());
					}
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return _rooms;
	}
}