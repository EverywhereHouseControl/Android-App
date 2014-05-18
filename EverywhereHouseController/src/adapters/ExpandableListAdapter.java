package adapters;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.SimpleActivityTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ehc.net.R;
import environment.RemoteController;


public class ExpandableListAdapter extends BaseExpandableListAdapter 
{
	//------------Variables-----------------------
	private Context _context;
	private Map<String, List<String>> _laptopCollections;
	private List<String> _laptops;
	private String _currentRoom;
	private String _servicename;
	private String _action;
	private String _data;
	private String _house;
	//-----------------------------------------
	
	public ExpandableListAdapter( String house,String room, Context context,
			List<String> laptops, Map<String, List<String>> laptopCollections ) 
	{
		_context = context;
		_laptops = laptops;
		_laptopCollections = laptopCollections;
		_currentRoom = room;
		_house = house;		
	}
	
	@Override
	public Object getChild( int groupPosition, int childPosition ) 
	{
		return _laptopCollections.get( _laptops.get( groupPosition ) ).get( childPosition );
	}
	
	@Override
	public long getChildId( int groupPosition, int childPosition ) 
	{
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		LayoutInflater _inflater = ( LayoutInflater ) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		String _itemName = ( String ) getChild( groupPosition, childPosition );
		
		String _laptopName = ( String ) getGroup( groupPosition );			
		JSONObject _list = new JSONObject();
		_list = JSON.getServices( _house,_currentRoom,_laptopName );
		
		
		if ( _itemName.equals( "controller" ) ) 
		{
			int _itemType = getChildXML( _itemName );
			convertView = _inflater.inflate( _itemType, parent, false );
			setListeners( convertView, _itemType, groupPosition,childPosition );
			_servicename = ( String ) getGroup( groupPosition );
			
			TextView _tv = ( TextView )convertView.findViewById( R.id.state );
			try 
			{
				_tv.setText( "State: " + _list.getString( "state" ) );
			}
			catch ( Exception e ) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if( _itemName.equals( "real" ) )
		{
			int _itemType = getChildXML( _itemName );
			convertView = _inflater.inflate( _itemType, parent, false );
			
			TextView _tv = ( TextView )convertView.findViewById( R.id.real_value );
			try
			{
				switch( _list.getInt( "interface" ) )
				{
					case 6:
					{
						_tv.setText( _list.getString( "state" ) );
						break;
					}
					default:
					{
						_tv.setText( _list.getString( "state" )+ " ºC" );			
					}
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}	
		}
		else if( _itemName.equals( "integer" ) )
		{
			
//			String _laptopName = (String) getGroup(groupPosition);
		
			//BLINDS
			int _childsNum = getChildrenCount( groupPosition );
			int _itemType = getChildXML( _itemName );
			convertView = _inflater.inflate( _itemType, parent, false );
			
			if( childPosition < _childsNum-1 )
			{
				Button _b = ( Button )convertView.findViewById( R.id.integer_value );
				_b.setText( "Up" );			
											
				TextView _tv = ( TextView )convertView.findViewById( R.id.childname );
				try 
				{
					_tv.setText( "State: " + _list.getString( "state" ) );
				}
				catch ( Exception e ) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Button _b = ( Button )convertView.findViewById( R.id.integer_value );
				_b.setText( "Down" );
			}
			
			setListeners( convertView, _itemType, groupPosition,childPosition );
			_servicename = ( String ) getGroup( groupPosition );

		}
		else if ( _itemName.equals( "boolean" ) ) 
		{
			int _itemType = getChildXML( _itemName );
			convertView = _inflater.inflate( _itemType, parent, false );
					
			try
			{
				switch( _list.getInt( "interface" ) )
				{
					case 3:
					{
						Button _b = ( Button )convertView.findViewById( R.id.boolean_value );
						_b.setText( "Open" );	
						break;
					}
					case 8:
					{
						Button _b = ( Button )convertView.findViewById( R.id.boolean_value );
						
						if( _list.getString( "state" ).equals( "OFF" ) )_b.setText( "Open" );
						else _b.setText( "Close" );
						
						TextView _tv = ( TextView )convertView.findViewById( R.id.childname );
						_tv.setText( "State: " + _list.getString( "state" ) );		
						break;
					}
					default:
					{
						Button _b = ( Button )convertView.findViewById( R.id.boolean_value );
						
						if( _list.getString( "state" ).equals( "OFF" ) )_b.setText( "On" );
						else _b.setText( "Off" );
						
						TextView _tv = ( TextView )convertView.findViewById( R.id.childname );
						_tv.setText( "State: " + _list.getString( "state" ) );			
					}
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}	
			
			setListeners( convertView, _itemType, groupPosition,childPosition );
			_servicename = ( String ) getGroup( groupPosition );
		}
		return convertView;
	}

	private void setListeners( final View convertView, int itemType, final int groupPosition, final int childPosition ) 
	{
		final TextView _tv = ( TextView ) convertView.findViewById( R.id.childname );
		
		if ( itemType == R.layout.float_item ) 
		{
			SeekBar _sb = ( SeekBar ) convertView.findViewById( R.id.float_value );
			_sb.setOnSeekBarChangeListener( new OnSeekBarChangeListener() 
			{
				@Override
				public void onStopTrackingTouch( SeekBar seekBar ) 
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch( SeekBar seekBar ) 
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ) 
				{
					// TODO Auto-generated method stub
					_tv.setText( progress );
				}
			});
		}	
		else if ( itemType == R.layout.boolean_item ) 
		{
			final Button _button = ( Button ) convertView.findViewById( R.id.boolean_value );
			_button.setOnClickListener( new View.OnClickListener() 
			{
				
				@Override
				public void onClick( View v ) 
				{
					// TODO Auto-generated method stub
					_servicename = ( String ) getGroup( groupPosition );
					// TODO Auto-generated method stub
					String _laptopName = ( String ) getGroup( groupPosition );			
					JSONObject _list = new JSONObject();
					_list = JSON.getServices( _house,_currentRoom,_laptopName );
					
					try
					{
						switch( _list.getInt( "interface" ) )
						{
							case 3:
							{
								_action = "SEND";	_data = "OPEN";					
								break;
							}
							case 8:
							{
								if ( _list.getString( "state" ).equals( "OFF" ) ) 
								{
									_action = "SEND";	_data = "OPEN";
								} 
								else 
								{
									_action = "SEND";	_data = "CLOSE";
								}		
								break;
							}
							default:
							{
								if ( _list.getString( "state" ).equals( "OFF" ) ) 
								{
									_action = "SEND";	_data = "ON";
								} 
								else 
								{
									_action = "SEND";	_data = "OFF";
								}		
							}
						}
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}				
					new SimpleActivityTask( _context ).sendAction( _currentRoom,_servicename,_action,_data );
					
					lastPressed(false);
					
				}
			});
		}
		else if ( itemType == R.layout.controller_item )
		{
			Button _remote_control = ( Button ) convertView.findViewById( R.id.launch_value );
			_remote_control.setOnClickListener( new View.OnClickListener() 
			{
				@Override
				public void onClick( View v ) 
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent( _context, RemoteController.class );
					intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
					intent.putExtra( "Room",_currentRoom );
					intent.putExtra( "Service",( String ) getGroup( groupPosition ) );
					_context.startActivity( intent );
					
					lastPressed(true);
				}
			});
		}
		else if ( itemType == R.layout.integer_item )
		{
			Button _remote_control = ( Button ) convertView.findViewById( R.id.integer_value );
			_remote_control.setOnClickListener( new View.OnClickListener() 
			{
				@Override
				public void onClick( View v ) 
				{
					String _laptopName = ( String ) getGroup( groupPosition );
					_servicename = ( String ) getGroup( groupPosition );
					
					if( _laptopName.equals( "BLINDS" ) )
					{
						int _childsNum = getChildrenCount( groupPosition );
						if( childPosition < _childsNum-1 )
						{
							_action = "SEND";	_data = "UP";
							new SimpleActivityTask( _context ).sendAction( _currentRoom,_servicename,_action,_data );
						}
						else
						{
							_action = "SEND";	_data = "DOWN";
							new SimpleActivityTask( _context ).sendAction( _currentRoom,_servicename,_action,_data );
						}
					}
					
					lastPressed( false );					
				}
			});
		}
		
	}

	private int getChildXML( String itemType ) 
	{
		if ( itemType.equals( "float" ) ) 
		{
			return R.layout.float_item;
		} 
		else if ( itemType.equals( "boolean" ) ) 
		{
			return R.layout.boolean_item;
		}
		else if ( itemType.equals( "integer" ) )
		{
			return R.layout.integer_item;
		}
		else if ( itemType.equals( "controller" ) )
		{
			return R.layout.controller_item;
		}
		else if ( itemType.equals( "real" ) )
		{
			return R.layout.real_item;
		}
		else
			return R.layout.boolean_item;
	}
	
	/**
	 * 
	 * @param otherActivity
	 */
	private void lastPressed(boolean otherActivity)
	{
		SharedPreferences _pref = _context.getSharedPreferences( "LOG",Context.MODE_PRIVATE );
		Editor _editor=_pref.edit();
		_editor.putString( "HOUSE",_house );
		_editor.putString( "ROOM", _currentRoom );	
		if( otherActivity )_editor.putString( "NEWACTIVITY", "TRUE" );
		else _editor.putString( "NEWACTIVITY", "FALSE" );
		_editor.commit();
	}
		
	@Override
	public int getChildrenCount( int groupPosition ) 
	{
		return _laptopCollections.get( _laptops.get( groupPosition  )).size();
	}
	
	@Override
	public Object getGroup( int groupPosition ) 
	{
		return _laptops.get( groupPosition );
	}
	
	@Override
	public int getGroupCount() 
	{
		return _laptops.size();
	}
	
	@Override
	public long getGroupId( int groupPosition ) 
	{
		return groupPosition;
	}
	
	@Override
	public View getGroupView( int groupPosition, boolean isExpanded,View convertView, ViewGroup parent ) 
	{
		String _laptopName = ( String ) getGroup( groupPosition );
		if ( convertView == null ) 
		{
			LayoutInflater _infalInflater = ( LayoutInflater ) _context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			convertView = _infalInflater.inflate( R.layout.group_item, null );
		}
		TextView item = ( TextView ) convertView.findViewById( R.id.RoomGroupName );
		item.setTypeface( null, Typeface.BOLD );
		item.setText( _laptopName );
		
		JSONObject _list = new JSONObject();
		_list = JSON.getServices( _house,_currentRoom,_laptopName );
		
		try
		{
			switch( _list.getInt( "interface" ) )
			{
				case 1:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.tv );
					break;
				}
				case 2:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.lamp );
					break;
				}
				case 3:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.intercom );
					break;
				}
				case 4:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.plug );
					break;
				}
				case 5:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.air_conditioning );
					break;
				}
				case 6:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.light_sensor );
					break;
				}
				case 7:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.blinds );
					break;
				}
				case 8:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.door );
					break;
				}
				case 9:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.motion );
					break;
				}
				case 10:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.rain_sensor );
					break;
				}
				case 11:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.temperature );
					break;
				}
				default:
				{
					ImageView _image = ( ImageView ) convertView.findViewById( R.id.HouseImageList );
					_image.setBackgroundResource( R.drawable.interrogation );
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() 
	{
		return true;
	}

	@Override
	public boolean isChildSelectable( int groupPosition, int childPosition ) 
	{
		return true;
	}
	
}