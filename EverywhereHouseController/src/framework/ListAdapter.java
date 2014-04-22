package framework;

import java.util.ArrayList;

import ehc.net.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter
{

	private Context _context;
	private ArrayList<String> _list;
	private int _itemView;
	
	
	public ListAdapter(Context context, ArrayList<String> list, int contentView)
	{
		_context = context;
		_list = new ArrayList<String>();
		_list = list;
		_itemView = contentView;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _list.size();
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
		View _view = convertView;
		if(_view == null)
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(_itemView,null);	
		}
		
		TextView _textView = (TextView) _view.findViewById(R.id.RoomGroupName);
		_textView.setTypeface(null, Typeface.BOLD);
		_textView.setText(_list.get(position));
		
		ImageView _imageView = (ImageView) _view.findViewById(R.id.HouseImageList);
		if(_textView.getText().toString().contains("cocina"))
		{
			_imageView.setBackgroundResource(R.drawable.cooker);
		}
		else if(_textView.getText().toString().contains("terraza"))
		{
			_imageView.setBackgroundResource(R.drawable.terrace);
		}
		else
		{
			_imageView.setBackgroundResource(R.drawable.interrogation);
		}
		
		
		return _view;
	}

}
