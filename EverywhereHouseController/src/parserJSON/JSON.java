package parserJSON;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roomorama.caldroid.CalendarHelper;

import framework.Event;
import framework.SpinnerEventContainer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

public class JSON {
	private static JSON _instance;
	public ArrayList<String> _houses = new ArrayList<String>();
	public ArrayList<String> _rooms = new ArrayList<String>();
	public ArrayList<String> _items = new ArrayList<String>();
	public ArrayList<String> _access = new ArrayList<String>();
	public HashMap<String, Pair<String, String>> _places = new HashMap<String, Pair<String, String>>();
	public HashMap<String, String> _urls = new HashMap<String, String>();
	public HashMap<String, Event> _events;
	public HashMap<String, JSONArray> _roomsHouses = new HashMap<String, JSONArray>();

	private static final String _TAG_NAME = "name";
	// private static final String _TAG_USER = "User";
	private static final String _TAG_HOUSES = "houses";
	private static final String _TAG_ROOMS = "rooms";
	private static final String _TAG_SERVICES = "services";
	private static final String _TAG_ACTIONS = "actions";
	private String _fileConfig = null;
	private String _fileInfo = null;
	private String _eventFile = null;
	private String _urlImage = null;

	@SuppressWarnings("unused")
	public JSON() {
	}

	public static synchronized JSON getInstance(Context c) {
		_instance = new JSON(c);
		return _instance;
	}

	public JSON(Context c) {
		loadUserInformation(c);
		loadUserEnvironment(c);
		try {
			loadJSONEvent();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadUserEnvironment(Context c) {
		try {
			InputStream _is = c.openFileInput("configuration.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._fileConfig = new String(buffer, "UTF-8");
			try {
				loadJSONconfig();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadUserInformation(Context c) {
		try {
			InputStream _is = c.openFileInput("profileInformation.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._fileInfo = new String(buffer, "UTF-8");
			try {
				loadJSONinfo();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadJSONEvent() throws JSONException {
		_events = new HashMap<String, Event>();
		JSONObject obj = new JSONObject(_fileInfo);
		for (int i = 0; i <= obj.getJSONObject("JSON").getJSONArray("houses")
				.length(); i++) {
			JSONObject house = obj.getJSONObject("JSON").getJSONArray("houses")
					.getJSONObject(i).getJSONObject("events");

			for (int j = 0; j <= house.length(); j++) {
				JSONObject event = house.getJSONObject("Event" + j);
				String dateFormat = event.get("Year") + "-"
						+ event.get("Month") + "-" + event.get("Day");
				Date date = null;

				try {
					date = CalendarHelper.getDateFromString(dateFormat,
							"yyyy-MM-dd");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Event ev = new Event((String) event.get("Name"),
						(int) event.getInt("item"),
						(String) event.get("Created"), date,
						(String) event.get("Hour"),
						(String) event.get("Minute"));
				_events.put("Event" + j, ev);
			}

		}
	}

	public HashMap<String, Event> getEvents() {
		return _events;
	}

	/**
	 * 
	 * @throws JSONException
	 */
	private void loadJSONconfig() throws JSONException {
		Log.d("JSON ", _fileConfig.toString());

		JSONObject _obj = new JSONObject(_fileConfig);
		try {
			this._houses = new ArrayList<String>();

			JSONArray _houses = _obj.getJSONArray(_TAG_HOUSES);

			for (int i = 0; i < _houses.length(); i++) {

				JSONObject _house = _houses.getJSONObject(i);

				this._houses.add(_house.getString("name"));

				_access.add(_house.getString("access"));

				_places.put(_house.getString("name"), new Pair<String, String>(
						_house.getString("city"), _house.getString("country")));

				try {
					String _url = _house.getString("image");
					Log.d("URL", _url);
					if (_url.equals("null"))// == null)
					{
						Log.d("URL", _url);
						_url = "null";
					}
					_urls.put(_house.getString("name"), _url);
				} catch (Exception e) {
					Log.d("ERROR", e.toString());
				}

				Log.d("HOUSE", _house.toString());

				try {
					JSONArray _rooms = _house.getJSONArray(_TAG_ROOMS);
					Log.d("ROOMS", _rooms.toString());

					JSONArray _roomsList = new JSONArray();

					for (int j = 0; j < _rooms.length(); j++) {
						JSONObject _room = _rooms.getJSONObject(j);
						Log.d("ROOM", _room.toString());
						_roomsList.put(_room);
						try {
							JSONArray _services = _room
									.getJSONArray(_TAG_SERVICES);
							Log.d("SERVICES", _services.toString());
						} catch (Exception e) {
							Log.d("ERROR", e.toString());
						}

					}
					_roomsHouses.put(_house.getString(_TAG_NAME), _roomsList);
					Log.d("Table houses: ", _house.getString(_TAG_NAME) + " "
							+ _roomsHouses.get(_house.getString(_TAG_NAME)));
				} catch (Exception e) {
					Log.d("ERROR", e.toString());
					_roomsHouses.put(_house.getString(_TAG_NAME),
							new JSONArray());
					Log.d("Table houses: ", _house.getString(_TAG_NAME) + " "
							+ _roomsHouses.get(_house.getString(_TAG_NAME)));
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadJSONinfo() throws JSONException {
		Log.d("JSON ", _fileInfo.toString());

		JSONObject _obj = new JSONObject(_fileInfo);

		_urlImage = _obj.getString("URL");
		Log.d("URL image", _urlImage);

	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getHousesName() throws JSONException {
		return _houses;
	}

	public String getURLUserImage() {
		return _eventFile;

	}

	/**
	 * 
	 * @param house
	 * @return
	 */
	public String getUrlImage(String house) {
		return _urls.get(house);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getHousesAccess() {
		return _access;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getUrlsImage() {
		ArrayList<String> _urls = new ArrayList<String>();
		for (int i = 0; i < _houses.size(); i++) {
			_urls.add(this._urls.get(_houses.get(i)));
		}
		return _urls;
	}

	/**
	 * 
	 * @param house
	 * @return
	 */
	public Pair<String, String> getPlace(String house) {
		return _places.get(house);
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 */
	/*
	 * public ArrayList<String> getHouses() throws JSONException {
	 * ArrayList<String> _housesList = new ArrayList<String>(); Set<String> s =
	 * _roomsHouses.keySet(); Iterator<String> it = s.iterator();
	 * 
	 * while(it.hasNext()) { _housesList.add(it.next()); } return _housesList; }
	 */

	/**
	 * 
	 * @param house
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getRooms(String house) throws JSONException {
		ArrayList<String> _roomsList = new ArrayList<String>();
		try {
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get(house);

			Log.d("NUM ROOMS: ", Integer.toString(_rooms.length()));

			for (int i = 0; i < _rooms.length(); i++) {
				JSONObject _room = _rooms.getJSONObject(i);
				_roomsList.add(_room.getString(_TAG_NAME));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return _roomsList;
	}

	// public ArrayList<String> getAllItems() throws JSONException {
	//
	// JSONObject _obj = new JSONObject(this._file);
	// ArrayList<String> _rooms = new ArrayList<String>();
	//
	// try {
	// JSONObject _habitaciones = _obj.getJSONObject(_TAG_ROOMS);
	// for (int i = 1; i <= _habitaciones.length(); i++) {
	// JSONObject _habitacion = _habitaciones.getJSONObject("R" + i);
	//
	// System.out.println(_habitacion.getString(_TAG_NAME));
	//
	// Log.e("COLIN_TAG", _habitacion.getString(_TAG_NAME));
	// JSONArray _items = _habitacion.getJSONArray(_TAG_ITEMS);
	//
	// for (int j = 0; j < _items.length(); j++) {
	// _rooms.add(_items.getString(j));
	// Log.e("COLIN_TAG", _items.getString(j));
	// System.out.println(_items.getString(j));
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return _rooms;
	// }

	@SuppressLint("DefaultLocale")
	public ArrayList<String> getItems(String roomName, String house)
			throws JSONException {
		ArrayList<String> _itemList = new ArrayList<String>();

		try {
			JSONArray _rooms = new JSONArray();
			Log.d("ROOMS", _roomsHouses.toString());
			_rooms = _roomsHouses.get(house);
			Log.d("ROOMS", _rooms.toString());

			for (int i = 0; i < _rooms.length(); i++) {
				JSONObject _room = _rooms.getJSONObject(i);
				Log.d("ROOM$", _room.toString());
				if (_room.get(_TAG_NAME).equals(roomName)) {
					try {
						JSONArray _services = _room.getJSONArray(_TAG_SERVICES);
						Log.d("SERVICES", _services.toString());
						for (int j = 0; j < _services.length(); j++) {
							JSONObject _item = _services.getJSONObject(j);
							Log.d("ITEM", _item.toString());
							_itemList.add(_item.getString(_TAG_NAME));
						}
					} catch (Exception e) {
						Log.d("ERROR", e.toString());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return _itemList;
	}

	/*
	 * Returns service's name + room's name
	 */
	// At the moment, testing with bertoldo+mansion
	public SpinnerEventContainer getItemsWithLocation() throws JSONException {
		SpinnerEventContainer info = new SpinnerEventContainer();
		for (int k = 0; k < _houses.size(); k++) {
			String house = _houses.get(k);
			try {
				JSONArray _rooms = new JSONArray();
				_rooms = _roomsHouses.get(house);

				for (int i = 0; i < _rooms.length(); i++) {
					JSONObject _room = _rooms.getJSONObject(i);
					String room = _room.getString(_TAG_NAME);

					JSONArray _services = _room.getJSONArray(_TAG_SERVICES);
					Log.d("SERVICES", _services.toString());
					for (int j = 0; j < _services.length(); j++) {
						String service = _services.getJSONObject(j).getString(
								_TAG_NAME);
						info.add(room, service, house);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return info;
	}

	public ArrayList<String> getServices(String house, String room, String item) {
		ArrayList<String> serviceList = new ArrayList<String>();
		serviceList.add("Select an action: ");

		try {
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get(house);

			for (int i = 0; i < _rooms.length(); i++) {
				JSONObject _room = _rooms.getJSONObject(i);
				Log.d("ROOM$", _room.toString());
				if (_room.get(_TAG_NAME).equals(room)) {
					try {
						JSONArray _services = _room.getJSONArray(_TAG_SERVICES);
						Log.d("SERVICES", _services.toString());
						for (int j = 0; j < _services.length(); j++) {
							JSONObject _item = _services.getJSONObject(j);
							if (_item.get(_TAG_NAME).equals(item)) {
								JSONArray actions = _item
										.getJSONArray(_TAG_ACTIONS);
								for (int k = 0; k < actions.length(); k++)
									serviceList.add(actions.get(k).toString());
							}
						}
					} catch (Exception e) {
						Log.d("ERROR", e.toString());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return serviceList;

	}
}