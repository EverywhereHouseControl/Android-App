package framework;

import java.util.ArrayList;

import loadUrlImageFramework.ImageLoader;
import com.kbeanie.imagechooser.api.ChosenImage;
import ehc.net.HousesMenu;
import ehc.net.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HouseListAdapter extends BaseAdapter
{

	private String _name;
	private Context _context;
	private ArrayList<String> _objectList;
	private ViewGroup _parent;
	private boolean _check;
	private int _convertView;
	private boolean _stateChosenImage;
	private int _currentOption;
	private ChosenImage _currentImage;
	private ImageButton _image;
	private String _path;
	private ArrayList<String> _urls = new ArrayList<String>();
	private ArrayList<String> _access = new ArrayList<String>();
	private ImageLoader _imgLoader;
	
	public HouseListAdapter(Context context, ArrayList<String> ObjectList,ArrayList<String> urls,ArrayList<String> access, int convertView)
	{
		_context = context;
		_objectList = ObjectList;
		_check = false;
		_currentOption = 0;
		_stateChosenImage = false;
		_path = null; 
		_convertView = convertView;
		_urls = urls;
		_imgLoader = new ImageLoader(_context);
		_access = access;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _objectList.size();
	}

	@Override
	public Object getItem(int position) 
	{		
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
		
		_parent = parent;
		View _view = convertView;
		if(_view == null)
		{
			LayoutInflater _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(_convertView,null);	
		}
		
		final Button _button = (Button) _view.findViewById(R.id.HouseButton);
		_button.setText(_objectList.get(position));
		_button.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				 //TODO Auto-generated method stub
				if(_access.get(position).equals("3"))
				{
					Toast.makeText(_context, "Required access", Toast.LENGTH_SHORT).show();
				}
				else 
					HousesMenu.createdMainMenuIntent(_button.getText().toString());
			}
		});
		
		_image = (ImageButton) _view.findViewById(R.id.HouseImageList);
		
		if(!_urls.get(position).equals("null"))
		{
			_path = _urls.get(position);
			_imgLoader.DisplayImage(_path, R.drawable.base_picture, _image);
			
	    } 
		else _image.setImageResource(R.drawable.base_picture);
		
		_image.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(_access.get(position).equals("3"))
				{
					Toast.makeText(_context, "Required access", Toast.LENGTH_SHORT).show();
				}
				else 
					HousesMenu.createdMainMenuIntent(_button.getText().toString());
			}
		});
		

		CheckBox _check = (CheckBox) _view.findViewById(R.id.HouseCheckBox);
		if(this._check)
		{
			_check.setChecked(false);
			_check.setVisibility(View.VISIBLE);
			
			if(_access.get(position).equals("3"))
			{
				_check.setVisibility(View.GONE);
			}
		}
		else _check.setVisibility(View.GONE);
		
		_check.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{	
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				// TODO Auto-generated method stub
				if(isChecked)
				{
					switch( _currentOption )
					{
						case 1:
							HousesMenu._currentHouse = _button.getText().toString();
							HousesMenu.takePicture();
							break;
						case 2:
							HousesMenu._currentHouse = _button.getText().toString();
							HousesMenu.chooseImage();
							break;
						default:
							break;
					}
				}
			}
		});
		
		return _view;
	}
	
	/**
	 * 
	 */
	public void setCheckON()
	{
		_check = true;
		notifyDataSetChanged();
	}

	/**
	 * 
	 */
	public void setCheckOFF()
	{
		_check = false;
		notifyDataSetChanged();
	}
	
	/**
	 * 
	 */
	public void setChosenOption(int option)
	{
		_currentOption = option;
	}
	
	/**
	 * 
	 */
	public void setChosenImage(ChosenImage image)
	{
		_currentImage = image;
	}
	
	/**
	 * 
	 * @param state
	 */
	public void setStateChosenImage(boolean state)
	{
		_stateChosenImage = state;
	}
	
	/**
	 * 
	 * @param path
	 */
	public void setPathImage(String path)
	{
		_path = path;
	}
}
