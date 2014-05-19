package adapters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ehc.net.R;

public class ListAdapter extends BaseAdapter
{
	//------------Variables-----------------------
	private Context _context;
	private ArrayList<String> _list;
	private int _itemView;
	private String _house;
	//-------------------------------------------
	
	public ListAdapter( Context context, ArrayList<String> list, int contentView, String house )
	{
		_context = context;
		_list = new ArrayList<String>();
		_list = list;
		_itemView = contentView;
		_house = house;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _list.size();
	}

	@Override
	public Object getItem( int position ) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId( int position ) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) 
	{
		// TODO Auto-generated method stub
		View _view = convertView;
		if( _view == null )
		{
			LayoutInflater _inflater = ( LayoutInflater ) _context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			_view = _inflater.inflate( _itemView,null );	
		}
		
		TextView _textView = ( TextView ) _view.findViewById( R.id.RoomGroupName );
		_textView.setTypeface( null, Typeface.BOLD );
		_textView.setText( _list.get( position ).toUpperCase() );
		
		ImageView _imageView = ( ImageView ) _view.findViewById( R.id.HouseImageList );
		
		
		 JSON.getInstance( _context );
		 JSONArray _rooms = JSON._roomsHouses.get( _house );
		 JSONObject _room;
		 
		 try 
		 {
			_room = _rooms.getJSONObject( position );
			
			switch( _room.getInt( "interface" ) )
			{
				case 0:
				{
					_imageView.setBackgroundResource( R.drawable.cooker );
					break;
				}
				case 1:
				{
					_imageView.setBackgroundResource( R.drawable.sofa );
					break;
				}
				case 2:
				{
					_imageView.setBackgroundResource( R.drawable.bed );
					break;
				}
				case 3:
				{
					_imageView.setBackgroundResource( R.drawable.bathroom );
					break;
				}
				case 4:
				{
					_imageView.setBackgroundResource( R.drawable.watering_can );
					break;
				}
				case 5:
				{
					_imageView.setBackgroundResource( R.drawable.garage );
					break;
				}
				case 6:
				{
					_imageView.setBackgroundResource( R.drawable.terrace );
					break;
				}
				default:
				{
					_imageView.setBackgroundResource( R.drawable.interrogation );
				}
			}
			
		} 
		 catch ( JSONException e ) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return _view;
	}

}
