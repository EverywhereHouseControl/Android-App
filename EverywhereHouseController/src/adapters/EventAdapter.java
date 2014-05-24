package adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;

import serverConnection.Post;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ehc.net.R;
import framework.Event;

public class EventAdapter extends BaseAdapter 
{
	//------------Variables-----------------------
	private Context _context;
	private ArrayList<Event> _event;
	private String _eventName;
	private String _userName;
	private String _currentHouse;
	
	//-----------------------------------

	public EventAdapter( Context c, ArrayList<Event> e, String currentHouse ) 
	{
		_event = e;
		_context = c;
		_currentHouse = currentHouse;
	}

	@Override
	public int getCount() 
	{
		return _event.size();
	}

	@Override
	public Object getItem( int position ) 
	{
		return _event.get( position );
	}

	@Override
	public long getItemId( int position ) 
	{
		return position;
	}

	@Override
	public View getView( final int position, View convertView, ViewGroup parent ) 
	{
		if ( convertView == null ) 
		{
			LayoutInflater _infalInflater = ( LayoutInflater ) _context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			convertView = _infalInflater.inflate( R.layout.event_fragment, null );
		}

		TextView _nameText = ( TextView ) convertView.findViewById( R.id.textView2 );
		_nameText.setTypeface(Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" ) );
		
		TextView _itemText = ( TextView ) convertView.findViewById(R.id.forgotPassword);
		_itemText.setTypeface(Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" ) );
		
		TextView _createText = (TextView) convertView.findViewById( R.id.textView3 );
		_createText.setTypeface(Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" ) );
		
		TextView _hourText = ( TextView ) convertView.findViewById( R.id.textView4 );
		_hourText.setTypeface(Typeface.createFromAsset(_context.getAssets(), "imagine_earth.ttf" ) );
		
		ImageView _deleteButton = ( ImageView ) convertView.findViewById( R.id.removeEvent );
		
		
		TextView _name = ( TextView ) convertView.findViewById( R.id.fragment_name );
		_name.setTypeface(Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" ) );

		TextView _item = ( TextView ) convertView.findViewById( R.id.fragment_item );
		_item.setTypeface(Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" ) );
		
		TextView _creator = ( TextView ) convertView.findViewById( R.id.fragment_creator );
		_creator.setTypeface(Typeface.createFromAsset( _context.getAssets(),	"imagine_earth.ttf" ) );
		
		TextView _hour = ( TextView ) convertView.findViewById( R.id.fragment_hour );
		_hour.setTypeface( Typeface.createFromAsset( _context.getAssets(), "imagine_earth.ttf" )) ;
		
		_deleteButton.setOnClickListener( new OnClickListener() 
		{
			
			@Override
			public void onClick( View v ) 
			{
				try 
				{
					_userName = _event.get( position ).getCreator();
					_eventName = _event.get( position ).getName();
					_event.remove( position );
					
					sendEventConnection _connection = new sendEventConnection(_context);
			    	_connection.execute();	
			    	
				} 
				catch (Throwable e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		_name.setText(_event.get( position ).getName() );
		_item.setText(Integer.toString( _event.get( position ).getItem() ) );
		_creator.setText( _event.get( position ).getCreator());
		_hour.setText(_event.get(position).getHour() + ":"
				+ _event.get( position ).getMinute());

		return convertView;
	}

	public void clear() 
	{
		_event.clear();
	}
	
	// Background process
    private class sendEventConnection extends AsyncTask<String, String, String>
    {    	
    	//------------Variables-----------------------
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	private Context _context = null;
    	//-----------------------------------
    	
    	public sendEventConnection( Context c ) 
    	{
    		_context=c;
		}
    	
    	@Override
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(_context);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground( String... arg0 ) 
		{
			try 
			{		      
				//Query
				SharedPreferences _pref = _context.getSharedPreferences( "LOG", Context.MODE_PRIVATE );								
				ArrayList<String> _parametros = new ArrayList<String>();

				_parametros.add( "command" );
				_parametros.add( "deleteprogramaction" );
				_parametros.add( "username" );
				_parametros.add( _userName.toUpperCase() );
				_parametros.add( "programname");
				_parametros.add( _eventName.toUpperCase() );
				_parametros.add( "actionname" );
				_parametros.add( "SEND" );
				
				
				JSONObject _data = Post.getServerData(_parametros,Post._ip);
				
				try 
				{
					JSONObject _json_data = _data.getJSONObject( "error" );
					switch( _json_data.getInt( "ERROR" ) )
					{
						case 0:
						{
							_message = _json_data.getString( "ENGLISH" );
							_parametros = new ArrayList<String>();
							_parametros.add( "command" );
							_parametros.add( "login2" );
							_parametros.add( "username" );
							_parametros.add( _userName.toUpperCase() );
							_parametros.add( "password" );
							_parametros.add( _pref.getString( "PASSWORD", "" ) );
							
							_data = Post.getServerData( _parametros,Post._ip);
							
							if ( _data != null && _data.length() > 0 ) 
							{				
								try 
								{
									_json_data = _data.getJSONObject( "result" );
								} catch (JSONException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//log(json_data.toString());
								
								try {
									if ( _json_data.getInt( "IDUSER" ) != 0 ) 
									{ 			        
										//Save the profile's information.
										JSON.saveProfileInfo( _json_data, _context );
										//Save the house's configuration
										JSON.saveConfig( _json_data.get( "JSON" ), _context );
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} 
							break;
						}
						default:
						{
							_internalError = _json_data.getInt( "ERROR" );
							_message = _json_data.getString( "ENGLISH" );
							break;
						}
					}
				
				} 
				catch ( JSONException e ) 
				{
					e.printStackTrace();
				}
				
			 }
			catch ( Exception e ) 
			 {
			 	e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute (String file_url) 
		{
            // dismiss the dialog		
            _pDialog.dismiss();            
            Class<?> _clazz = null;			
			try{
				_clazz = Class.forName( "ehc.net.CaldroidSampleActivity" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent _intent = new Intent( _context, _clazz );
			_intent.putExtra( "House", _currentHouse );
			_intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );			
			_context.startActivity( _intent );            
		}
    }
}