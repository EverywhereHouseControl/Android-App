package framework;

import java.util.ArrayList;

import loadUrlImageFramework.ImageLoader;

import org.json.JSONException;

import ehc.net.HousesMenu;
import ehc.net.LogIn;
import ehc.net.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SlidingMenuAdapter extends BaseAdapter
{
	private Context _context;
	private String _currentHouse;
	private JSON _JSONFile;
	private ArrayList<String> _houses = new ArrayList<String>();
	private ArrayList<String> _urls = new ArrayList<String>();
	private ArrayList<String> _access = new ArrayList<String>();
	private ArrayList<String> _optionList = new ArrayList<String>();
	private ImageLoader _imgLoader = HouseListAdapter._imgLoader;
	private int _count = 5;
	
	
	public SlidingMenuAdapter(Context context, String currentHouse)
	{
		_context = context;
		_currentHouse = currentHouse;
//		_imgLoader = new ImageLoader(_context);
		_optionList.add("Profile");
		_optionList.add("Log out");
		_optionList.add("Exit");
		
		//-----------------It Reads config.json-----------------
        _JSONFile = JSON.getInstance(_context);
		try 
		{
			_houses = _JSONFile.getHousesName();
			_urls = _JSONFile.getUrlsImage();	
			_access = _JSONFile.getHousesAccess();
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_count = _count + _houses.size();
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _count;
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		View _view = convertView;
		if(position==0)
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(R.layout.title_text_view,null);
			TextView _text = (TextView) _view.findViewById(R.id.HousesTextView);
			if(_houses.size()>1)_text.setText("HOUSES");
			else _text.setText("HOUSE");		
		}	
		else if( position < (_houses.size()+1))
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(R.layout.house_item,null);
			
			final Button _button = (Button) _view.findViewById(R.id.HouseButton);
			_button.setText(_houses.get(position-1));
			_button.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(_access.get(position-1).equals("3"))
					{
						Toast.makeText(_context, "Required access", Toast.LENGTH_SHORT).show();
					}
					else 
						HousesMenu.createdMainMenuIntent(_button.getText().toString());
					
				}
			});
			
			if(_button.getText().equals(_currentHouse))
			{
				_button.setClickable(false);
				_button.setBackgroundResource(R.drawable.abs__ab_bottom_solid_light_holo);
			}
			
			final ImageButton _image = (ImageButton) _view.findViewById(R.id.HouseImageList);
			
			if(!_urls.get(position-1).equals("null"))
			{
				 _imgLoader.DisplayImage(_urls.get(position-1), R.drawable.base_picture, _image, 0);
			}
			else _image.setImageResource(R.drawable.base_picture);
			
			_image.setOnClickListener(new View.OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(_access.get(position-1).equals("3"))
					{
						Toast.makeText(_context, "Required access", Toast.LENGTH_SHORT).show();
					}
					else 
						HousesMenu.createdMainMenuIntent(_button.getText().toString());
				}
			});
		}
		else if( position == (_houses.size()+1))
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(R.layout.title_text_view,null);
			
			TextView _text = (TextView) _view.findViewById(R.id.HousesTextView);
			_text.setText("OPTIONS");	
		}
		else
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(R.layout.option_view,null);
			final Button _button = (Button) _view.findViewById(R.id.OptionButton);
			_button.setText(_optionList.get(position-(_houses.size()+2)));
			_button.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					if(_button.getText().equals("Profile"))
					{
						Log.d("OPTION","PROFILE");
						Class<?> _clazz = null;
						try 
						{
							_clazz = Class.forName( "ehc.net.Profile");
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent _intent = new Intent( _context,_clazz );
						_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						_intent.putExtra("House",_currentHouse);
		    			_context.startActivity( _intent );
					}
					else if(_button.getText().equals("Log out"))
					{
						Log.d("OPTION","CHANGE");
						Intent _intent = new Intent(_context,LogIn.class);
						_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            	_context.startActivity(_intent); 
					}
					else
					{
						Log.d("OPTION","EXIT");
						Intent _intent = new Intent(Intent.ACTION_MAIN);
		            	_intent.addCategory(Intent.CATEGORY_HOME);
		            	_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            	_context.startActivity(_intent);  
					}        
				}
			});
		}	
		return _view;
	}
}
