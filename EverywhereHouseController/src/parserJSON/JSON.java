package parserJSON;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.roomorama.caldroid.CalendarHelper;

import framework.Event;
import framework.SpinnerEventContainer;

public class JSON 
{
	//------------Variables-----------------------
	private static JSON _instance;
	public static ArrayList<String> _houses = new ArrayList<String>();
	public static ArrayList<String> _rooms = new ArrayList<String>();
	public static ArrayList<String> _items = new ArrayList<String>();
	public static ArrayList<String> _access = new ArrayList<String>();
	public static HashMap<String, Pair<String, String>> _places = new HashMap<String, Pair<String, String>>();
	public static HashMap<String, String> _urls = new HashMap<String, String>();
	public static HashMap<String, Event> _events;
	public static HashMap<String, JSONArray> _roomsHouses = new HashMap<String, JSONArray>();

	private static final String _TAG_NAME = "name";
	// private static final String _TAG_USER = "User";
	private static final String _TAG_HOUSES = "houses";
	private static final String _TAG_ROOMS = "rooms";
	private static final String _TAG_SERVICES = "services";
//	private static final String _TAG_ACTIONS = "actions";
	private static String _fileConfig = null;
	private static String _fileInfo = null;
	private String _eventFile = null;
//	private static String _urlImage = null;
	//----------------------------------------------

	public JSON() {}

	public static synchronized JSON getInstance( Context c ) 
	{
		_instance = new JSON( c );
		return _instance;
	}

	public JSON( Context c ) 
	{
		loadUserInformation( c );
		loadUserEnvironment( c );
		try 
		{
			loadJSONEvent();
		}
		catch ( JSONException e ) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Saves from the server query the profile information in the file 'profile.json'.
	 * @param JSON
	 * @param context
	 */
	public static void saveProfileInfo( JSONObject JSON, Context context )
	{
		try 
		{
			FileOutputStream _outputStream = context.openFileOutput( "profileInformation.json", Context.MODE_PRIVATE );
			_outputStream.write(JSON.toString().getBytes() );
			_outputStream.close();	
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * * Saves from the server query the house configuration in the file 'configuration.json'.
	 * @param JSON
	 * @param context
	 */
	public static void saveConfig( Object JSON, Context context )
	{	
		try 
		{
			FileOutputStream _outputStream = context.openFileOutput("configuration.json", Context.MODE_PRIVATE);
			_outputStream.write(JSON.toString().getBytes());
			_outputStream.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static String loadUserEnvironment( Context c ) 
	{
		try 
		{
			InputStream _is = c.openFileInput( "configuration.json" );
			int _size = _is.available();
			byte[] buffer = new byte[ _size] ;
			_is.read( buffer );
			_is.close();
			_fileConfig = new String( buffer, "UTF-8" );
			try 
			{
				loadJSONconfig();
			} catch ( JSONException e ) 
			{
				e.printStackTrace();
			}
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
		return _fileConfig;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static String loadUserInformation( Context c ) 
	{
		try 
		{
			InputStream _is = c.openFileInput( "profileInformation.json" );
			int _size = _is.available();
			byte[] buffer = new byte[ _size ];
			_is.read( buffer );
			_is.close();
			_fileInfo = new String( buffer, "UTF-8" );
//			try 
//			{
//				loadJSONinfo();
//			} catch ( JSONException e ) 
//			{
//				e.printStackTrace();
//			}
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
		return _fileInfo;
	}

	/**
	 * 
	 * @throws JSONException
	 */
	private void loadJSONEvent() throws JSONException
	{
		_events = new HashMap<String, Event>();
		JSONObject obj = new JSONObject( _fileInfo );
		
		for (int i = 0; i <= obj.getJSONObject( "JSON" ).getJSONArray( "houses" ).length(); i++ ) 
		{
			JSONObject house = obj.getJSONObject( "JSON" ).getJSONArray( "houses" ).getJSONObject( i ).getJSONObject( "events" );

			for ( int j = 0; j <= house.length(); j++ ) 
			{ 
				JSONObject event = house.getJSONObject( "Event" + j );
				String dateFormat = event.get(" Year" ) + "-" + event.get( "Month" ) + "-" + event.get( "Day" );
				Date date = null;

				try 
				{
					date = CalendarHelper.getDateFromString( dateFormat,"yyyy-MM-dd" );
				} 
				catch ( ParseException e ) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Event _ev = new Event( (String ) event.get( "Name" ),
						(int) event.getInt( "item" ),
						(String) event.get( "Created" ), date,
						(String) event.get( "Hour" ),
						(String) event.get( "Minute" ));
				_events.put( "Event" + j, _ev );
			}

		}
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Event> getEvents() 
	{
		return _events;
	}

	/**
	 * 
	 * @throws JSONException
	 */
	private static void loadJSONconfig() throws JSONException 
	{
		JSONObject _obj = new JSONObject( _fileConfig );
		try 
		{
			_houses = new ArrayList<String>();

			JSONArray _housesArray = _obj.getJSONArray( _TAG_HOUSES );

			for ( int i = 0; i < _housesArray.length(); i++ ) 
			{

				JSONObject _house = _housesArray.getJSONObject( i );

				_houses.add( _house.getString( "name" ) );

				_access.add( _house.getString( "access" ) );

				_places.put( _house.getString("name"), new Pair<String, String>( _house.getString( "city" ), _house.getString( "country" ) ) );

				try 
				{
					String _url = _house.getString( "image" );
					
					if ( _url.equals("null") )
					{
						_url = "null";
					}
					_urls.put( _house.getString( "name" ), _url );
				} 
				catch (Exception e) 
				{
					Log.d("ERROR", e.toString());
				}

				try 
				{
					JSONArray _rooms = _house.getJSONArray( _TAG_ROOMS );

					JSONArray _roomsList = new JSONArray();

					for ( int j = 0; j < _rooms.length(); j++ ) 
					{
						JSONObject _room = _rooms.getJSONObject( j );
						_roomsList.put( _room );
						
						try 
						{
							@SuppressWarnings("unused")
							JSONArray _services = _room.getJSONArray( _TAG_SERVICES );
						} 
						catch ( Exception e ) 
						{
							e.printStackTrace();
						}

					}
					_roomsHouses.put( _house.getString( _TAG_NAME ), _roomsList );
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					_roomsHouses.put( _house.getString(_TAG_NAME), new JSONArray() );
					Log.d("Table houses: ", _house.getString(_TAG_NAME) + " "
							+ _roomsHouses.get(_house.getString(_TAG_NAME)));
				}

			}
		} 
		catch ( JSONException e ) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getHousesName() throws JSONException 
	{
		return _houses;
	}

	public String getURLUserImage() 
	{
		return _eventFile;

	}

	/**
	 * 
	 * @param house
	 * @return
	 */
	public String getUrlImage( String house )
	{
		return _urls.get(house);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getHousesAccess() 
	{
		return _access;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getUrlsImage() 
	{
		ArrayList<String> _urlsArray = new ArrayList<String>();
		for ( int i = 0; i < _houses.size(); i++ ) 
		{
			_urlsArray.add( _urls.get( _houses.get( i ) ) );
		}
		return _urlsArray;
	}

	/**
	 * 
	 * @param house
	 * @return
	 */
	public Pair<String, String> getPlace( String house ) 
	{
		return _places.get(house);
	}

	/**
	 * 
	 * @param house
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<String> getRooms(String house) throws JSONException 
	{
		ArrayList<String> _roomsList = new ArrayList<String>();
		try {
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get( house );

			for ( int i = 0; i < _rooms.length(); i++ ) 
			{
				JSONObject _room = _rooms.getJSONObject( i );
				_roomsList.add( _room.getString( _TAG_NAME ) );
			}
		} catch ( JSONException e ) 
		{
			e.printStackTrace();
		}
		return _roomsList;
	}

	/**
	 * 
	 * @param roomName
	 * @param house
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getItems(String roomName, String house)throws JSONException 
	{
		ArrayList<String> _itemList = new ArrayList<String>();

		try 
		{
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get(house);

			for ( int i = 0; i < _rooms.length(); i++ ) 
			{
				JSONObject _room = _rooms.getJSONObject( i );
				
				if ( _room.get( _TAG_NAME ).equals( roomName ) ) 
				{
					try 
					{
						JSONArray _services = _room.getJSONArray( _TAG_SERVICES );

						for ( int j = 0; j < _services.length(); j++ ) 
						{
							JSONObject _item = _services.getJSONObject( j );
							_itemList.add(_item.getString( _TAG_NAME ) );
						}
					} catch ( Exception e ) 
					{
						e.printStackTrace();
					}
				}
			}
		} 
		catch ( JSONException e ) 
		{
			e.printStackTrace();
		}
		return _itemList;
	}

	/*
	 * Returns service's name + room's name
	 */
	// At the moment, testing with bertoldo+mansion
	/**
	 * 
	 * @return
	 * @throws JSONException
	 */
	public SpinnerEventContainer getItemsWithLocation() throws JSONException 
	{
		SpinnerEventContainer info = new SpinnerEventContainer();
		for ( int k = 0; k < _houses.size(); k++ ) 
		{
			String house = _houses.get( k );
			try 
			{
				JSONArray _rooms = new JSONArray();
				_rooms = _roomsHouses.get( house );

				for ( int i = 0; i < _rooms.length(); i++ ) 
				{
					JSONObject _room = _rooms.getJSONObject( i );
					String room = _room.getString( _TAG_NAME );

					JSONArray _services = _room.getJSONArray( _TAG_SERVICES );
					
					for ( int j = 0; j < _services.length(); j++ ) 
					{
						String service = _services.getJSONObject( j ).getString( _TAG_NAME );
						info.add( room, service, house );
					}
				}
			} 
			catch ( JSONException e ) 
			{
				e.printStackTrace();
			}
		}
		return info;
	}

	/**
	 * 
	 * @param house
	 * @param room
	 * @param item
	 * @return
	 */
	public static JSONObject getServices( String house, String room, String item ) 
	{
		JSONObject _service = new JSONObject();
		try 
		{
			JSONArray _rooms = new JSONArray();
			_rooms = _roomsHouses.get( house );

			for ( int i = 0; i < _rooms.length(); i++ ) 
			{				
				JSONObject _room = _rooms.getJSONObject(i);
				
				if ( _room.get( _TAG_NAME ).equals( room ) ) 
				{
					try 
					{
						JSONArray _services = _room.getJSONArray( _TAG_SERVICES );
						
						for ( int j = 0; j < _services.length(); j++ ) 
						{
							JSONObject _item = _services.getJSONObject( j );
							
							if ( _item.get( _TAG_NAME ).equals( item ) ) 
							{
								return _item;
							}
						}
					} 
					catch ( Exception e ) 
					{
						e.printStackTrace();
					}
				}
			}
		} 
		catch ( JSONException e ) 
		{
			e.printStackTrace();
		}
		
		return _service;
	}
}