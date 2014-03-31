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
import android.widget.CompoundButton;
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
			Intent intent = new Intent(_context, RemoteController.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("Room",_currentRoom);
			intent.putExtra("Service",(String) getGroup(groupPosition));
			_context.startActivity(intent);
			convertView = _inflater.inflate(R.layout.empty_item, parent, false);
			return convertView;
		} 
		else 
		{
			int _itemType = getChildXML(_itemName);
			convertView = _inflater.inflate(_itemType, parent, false);
			// -------------------------------------
			setListeners(convertView, _itemType);
			_servicename = (String) getGroup(groupPosition);
			_servicename = _servicename.toUpperCase();
		}
		return convertView;
	}

	private void setListeners(final View convertView, int itemType) 
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
		
		
		if (itemType == R.layout.boolean_item) 
		{
			final ToggleButton _toggleButton = (ToggleButton) convertView.findViewById(R.id.boolean_value);
			_toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
				{
					// TODO Auto-generated method stub
					if (isChecked) 
					{
						_action = "ENVIAR";	_data = "1";

					} 
					else 
					{
						_action = "ENVIAR";	_data = "0";
					}
					new SimpleActivityTask(_context).sendAction(_currentRoom,_servicename,_action,_data);
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
		TextView item = (TextView) convertView.findViewById(R.id.groupname);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(_laptopName);
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