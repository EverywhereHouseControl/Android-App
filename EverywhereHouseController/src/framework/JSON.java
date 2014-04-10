package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ehc.net.Event;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class JSON {
	private static JSON _instance;
	public ArrayList<String> _rooms;
	public ArrayList<String> _items;
	public HashMap<String, Event> _events;
	public HashMap<String, JSONArray> _roomsHouses = new HashMap<String, JSONArray>();

	private static final String _TAG_NAME = "name";
	// private static final String _TAG_USER = "User";
	private static final String _TAG_HOUSES = "houses";
	private static final String _TAG_ROOMS = "rooms";
	private static final String _TAG_SERIVICES = "services";
	private String _file = null;
	private String _eventFile = null;

	@SuppressWarnings("unused")
	private JSON() {
	}

	public static synchronized JSON getInstance(Context c) {
		_instance = new JSON(c);
		return _instance;
	}

	public JSON(Context c) {
		loadUserEnvironment(c);
		loadUserEvents(c);
	}

	private void loadUserEnvironment(Context c) {
		try {
			InputStream _is = c.openFileInput("configuration.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._file = new String(buffer, "UTF-8");
			try {
				loadJSON();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadUserEvents(Context c) {
		try {
			_events = new HashMap<String, Event>();
			InputStream _is = c.getAssets().open("event.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._eventFile = new String(buffer, "UTF-8");
			try {
				loadJSONEvent();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadJSONEvent() throws JSONException {
		Log.d("JSON ", _eventFile.toString());

		JSONObject obj = new JSONObject(_eventFile);
		try {
			for (int i = 0; i <= obj.length(); i++) {
				JSONObject event = obj.getJSONObject("Event" + i);
				Event ev = new Event((String) event.get("Name"), (int) event.getInt("item"), (String) event.get("Created"), (String) event.get("Year"),
						(String) event.get("Month"), (String) event.get("Day"), (String) event.get("Hour"), (String) event.get("Minute"));
				_events.put("Event" + i, ev);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Event> getEvents() {
		return _events;
	}
	
	/**
	 * 
	 * @throws JSONException
	 */
	private void loadJSON() throws JSONException 
	{
		Log.d("JSON ", _file.toString());

		JSONObject _obj = new JSONObject(_file);
		try 
		{
			JSONArray _houses = _obj.getJSONArray(_TAG_HOUSES);
			for (int i = 0; i < _houses.length(); i++) 
			{				
				JSONObject _house = _houses.getJSONObject(i);
				Log.d("HOUSE", _house.toString());
				
				JSONArray _rooms = _house.getJSONArray(_TAG_ROOMS);
				Log.d("ROOMS", _rooms.toString());
				
				JSONArray _roomsList = new JSONArray();
				
				for (int j=0; j< _rooms.length(); j++)
				{
					JSONObject _room = _rooms.getJSONObject(j);
					Log.d("ROOM", _room.toString());
					_roomsList.put(_room);
					
					
					JSONArray _services = _room.getJSONArray(_TAG_SERIVICES);
					Log.d("SERVICES", _services.toString());
					
				}
				_roomsHouses.put(_house.getString(_TAG_NAME), _roomsList);
				Log.d("Table houses: ", _house.getString(_TAG_NAME) +" "+_roomsHouses.get(_house.getString(_TAG_NAME)));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getHouses() throws JSONException 
	{
		ArrayList<String> _housesList = new ArrayList<String>();
		Set<String> s = _roomsHouses.keySet();
		Iterator<String> it = s.iterator();
		
		while(it.hasNext())
		{
			_housesList.add(it.next());
		}
		return _housesList;
	}
	
	/**
	 * 
	 * @param house
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getRooms(String house) throws JSONException 
	{
		ArrayList<String> _roomsList = new ArrayList<String>();
		try 
		{
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get(house);
			
			Log.d("NUM ROOMS: ",Integer.toString(_rooms.length()));
			
			for (int i = 0; i < _rooms.length(); i++) 
			{
				JSONObject _room = _rooms.getJSONObject(i);
				_roomsList.add(_room.getString(_TAG_NAME));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return _roomsList;
	}	

//	public ArrayList<String> getAllItems() throws JSONException {
//
//		JSONObject _obj = new JSONObject(this._file);
//		ArrayList<String> _rooms = new ArrayList<String>();
//
//		try {
//			JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
//			for (int i = 1; i <= _habitaciones.length(); i++) {
//				JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);
//
//				System.out.println(_habitacion.getString(_TAG_NAME));
//
//				Log.e("COLIN_TAG", _habitacion.getString(_TAG_NAME));
//				JSONArray _items = _habitacion.getJSONArray(_TAG_ITEMS);
//
//				for (int j = 0; j < _items.length(); j++) {
//					_rooms.add(_items.getString(j));
//					Log.e("COLIN_TAG", _items.getString(j));
//					System.out.println(_items.getString(j));
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return _rooms;
//	}

	@SuppressLint("DefaultLocale")
	public ArrayList<String> getItems(String roomName, String house) throws JSONException 
	{		
		ArrayList<String> _itemList = new ArrayList<String>();
		
		try 
		{
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get(house);
			
			for (int i = 0; i < _rooms.length(); i++) 
			{
				JSONObject _room = _rooms.getJSONObject(i);
				
				if(_room.get(_TAG_NAME).equals(roomName))
				{
					JSONArray _services = _room.getJSONArray(_TAG_SERIVICES);
					for(int j=0; j< _services.length(); j++)
					{
						JSONObject _item = _services.getJSONObject(j);
						_itemList.add(_item.getString(_TAG_NAME));
					}
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return _itemList;
	}
}