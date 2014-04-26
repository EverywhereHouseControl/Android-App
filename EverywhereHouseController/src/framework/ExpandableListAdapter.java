package framework;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import ehc.net.R;
import environment.RemoteController;


public class ExpandableListAdapter extends BaseExpandableListAdapter 
{

	private Context _context;
	private Map<String, List<String>> _laptopCollections;
	private List<String> _laptops;
	private String _currentRoom;
	private String _servicename;
	private String _action;
	private String _data;

	
	@SuppressLint("DefaultLocale")
	public ExpandableListAdapter(String room, Context context,
			List<String> laptops, Map<String, List<String>> laptopCollections) 
	{
		this._context = context;
		this._laptops = laptops;
		this._laptopCollections = laptopCollections;
		this._currentRoom = room.toUpperCase();
		
		
	}

	public Object getChild(int groupPosition, int childPosition) 
	{
		return _laptopCollections.get(_laptops.get(groupPosition)).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	@SuppressLint("DefaultLocale")
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String _itemName = (String) getChild(groupPosition, childPosition);
		
		if (_itemName.equals("controller")) 
		{
			int _itemType = getChildXML(_itemName);
			convertView = _inflater.inflate(_itemType, parent, false);
			setListeners(convertView, _itemType, groupPosition,childPosition);//setListeners(convertView, _itemType);
			_servicename = (String) getGroup(groupPosition);
//			_servicename = _servicename.toUpperCase();
			
//			Intent intent = new Intent(_context, RemoteController.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra("Room",_currentRoom);
//			intent.putExtra("Service",(String) getGroup(groupPosition));
//			_context.startActivity(intent);
//			convertView = _inflater.inflate(R.layout.empty_item, parent, false);
//			return convertView;
		}
		else if(_itemName.equals("integer") )
		{
			
			String _laptopName = (String) getGroup(groupPosition);
			
			if(_laptopName.equals("BLINDS"))
			{
				int _childsNum = getChildrenCount(groupPosition);
				int _itemType = getChildXML(_itemName);
				convertView = _inflater.inflate(_itemType, parent, false);
				if(childPosition<_childsNum-1)
				{
					Button _b = (Button)convertView.findViewById(R.id.integer_value);
					_b.setText("Up");
				}
				else
				{
					Button _b = (Button)convertView.findViewById(R.id.integer_value);
					_b.setText("Down");
				}
				setListeners(convertView, _itemType, groupPosition,childPosition);//setListeners(convertView, _itemType);
				_servicename = (String) getGroup(groupPosition);
//				_servicename = _servicename.toUpperCase();
			}			
		}
		else if (_itemName.equals("boolean")) 
		{
			int _itemType = getChildXML(_itemName);
			convertView = _inflater.inflate(_itemType, parent, false);
			// -------------------------------------
			setListeners(convertView, _itemType, groupPosition,childPosition);//setListeners(convertView, _itemType);
			_servicename = (String) getGroup(groupPosition);
//			_servicename = _servicename.toUpperCase();
		}
		return convertView;
	}

	private void setListeners(final View convertView, int itemType, final int groupPosition, final int childPosition) 
	{
		final TextView _tv = (TextView) convertView.findViewById(R.id.childname);
		if (itemType == R.layout.float_item) 
		{
			SeekBar _sb = (SeekBar) convertView.findViewById(R.id.float_value);
			_sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
			{
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) 
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) 
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
				{
					// TODO Auto-generated method stub
					_tv.setText(progress);
				}
			});
		}	
		else if (itemType == R.layout.boolean_item) 
		{
			final ToggleButton _toggleButton = (ToggleButton) convertView.findViewById(R.id.boolean_value);
			_toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
				{
					_servicename = (String) getGroup(groupPosition);
					// TODO Auto-generated method stub
					if (isChecked) 
					{
						_action = "SEND";	_data = "ON";
					} 
					else 
					{
						_action = "SEND";	_data = "OFF";
					}
					new SimpleActivityTask(_context).sendAction(_currentRoom,_servicename,_action,_data);
				}
			});
		}
		else if (itemType == R.layout.controller_item)
		{
			Button _remote_control = (Button) convertView.findViewById(R.id.launch_value);
			_remote_control.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(_context, RemoteController.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("Room",_currentRoom);
					intent.putExtra("Service",(String) getGroup(groupPosition));
					_context.startActivity(intent);
				}
			});
		}
		else if (itemType == R.layout.integer_item)
		{
			Button _remote_control = (Button) convertView.findViewById(R.id.integer_value);
			_remote_control.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
//					Intent intent = new Intent(_context, RemoteController.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.putExtra("Room",_currentRoom);
//					intent.putExtra("Service",(String) getGroup(groupPosition));
//					_context.startActivity(intent);
					String _laptopName = (String) getGroup(groupPosition);
					_servicename = (String) getGroup(groupPosition);
					
					if(_laptopName.equals("BLINDS"))
					{
						int _childsNum = getChildrenCount(groupPosition);
						if(childPosition<_childsNum-1)
						{
							_action = "SEND";	_data = "UP";
							new SimpleActivityTask(_context).sendAction(_currentRoom,_servicename,_action,_data);
						}
						else
						{
							_action = "SEND";	_data = "DOWN";
							new SimpleActivityTask(_context).sendAction(_currentRoom,_servicename,_action,_data);
						}
					}
				}
			});
		}
		
	}

	private int getChildXML(String itemType) 
	{
		if (itemType.equals("float")) 
		{
			return R.layout.float_item;
		} 
		else if (itemType.equals("boolean")) 
		{
			return R.layout.boolean_item;
		}
		else if (itemType.equals("integer"))
		{
			return R.layout.integer_item;
		}
		else if (itemType.equals("controller"))
		{
			return R.layout.controller_item;
		}
		else
			return R.layout.boolean_item;

	}

	public int getChildrenCount(int groupPosition) 
	{
		return _laptopCollections.get(_laptops.get(groupPosition)).size();
	}

	public Object getGroup(int groupPosition) 
	{
		return _laptops.get(groupPosition);
	}

	public int getGroupCount() 
	{
		return _laptops.size();
	}

	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) 
	{
		String _laptopName = (String) getGroup(groupPosition);
		if (convertView == null) 
		{
			LayoutInflater _infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = _infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.RoomGroupName);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(_laptopName);
		///////////////////////////////////////
		if(_laptopName.equals("LIGHTS"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.lamp);
		}
		else if(_laptopName.equals("TV"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.tv);
		}
		else if(_laptopName.equals("DVD"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.dvd);
		}
		else if(_laptopName.equals("STEREO"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.stereo);
		}
		else if(_laptopName.equals("AIRCONDITIONING"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.air_conditioning);
		}
		else if(_laptopName.equals("DOOR"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.door);
		}
		else if(_laptopName.equals("HEATING"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.heating);
		}
		else if(_laptopName.equals("BLINDS"))
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.blinds);
		}
		else
		{
			ImageView _image = (ImageView) convertView.findViewById(R.id.HouseImageList);
			_image.setBackgroundResource(R.drawable.interrogation);
		}
		/////////////////////////////////////////
		
		return convertView;
	}

	public boolean hasStableIds() 
	{
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
	
}