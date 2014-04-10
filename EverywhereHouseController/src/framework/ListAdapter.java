package framework;

import java.util.ArrayList;

import ehc.net.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter
{

	private String _houseName;
	private Context _context;
	private ArrayList<String> _roomsList;
	
	public ListAdapter(String houseName, Context context, ArrayList<String> rooms)
	{
		this._houseName = houseName;
		this._context = context;
		this._roomsList = rooms;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _roomsList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		
		if (convertView == null) 
		{
			LayoutInflater _infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = _infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.groupname);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(_roomsList.get(position));
		
		return convertView;
	}

}
