package framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import loadUrlImageFramework.FileCache;
import loadUrlImageFramework.ImageLoader;
import loadUrlImageFramework.Utils;

import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import ehc.net.HousesMenu;
import ehc.net.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

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
	private ImageView _image;
	private String _path;
	private File _f;
	private ArrayList<String> _urls = new ArrayList<String>();
	private ImageLoader _imgLoader;
	
	public HouseListAdapter(String houseName, Context context, ArrayList<String> ObjectList,ArrayList<String> urls, int convertView)
	{
		_name = houseName;
		_context = context;
		_objectList = ObjectList;
		_check = false;
		_currentOption = 0;
		_stateChosenImage = false;
		_path = null; 
		_convertView = convertView;
		_urls = urls;
		_imgLoader = new ImageLoader(_context);
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
	public View getView(int position, View convertView, ViewGroup parent) 
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
				// TODO Auto-generated method stub
				HousesMenu.createdMainMenuIntent(_button.getText().toString());
			}
		});
		
		_image = (ImageView) _view.findViewById(R.id.HouseImageList);
		
//		boolean haveImage = false;
		
		if(!_urls.get(position).equals("null"))
		{
//			haveImage = true;
			_path = _urls.get(position);
			
			// ImageLoader class instance
	        
	         
	        // whenever you want to load an image from url
	        // call DisplayImage function
	        // url - image url to load
	        // loader - loader image, will be displayed before getting image
	        // image - ImageView
	        _imgLoader.DisplayImage(_path, R.drawable.base_picture, _image);
//		}
//		
//		if(haveImage)
//		{
//			FileCache fileCache=new FileCache(_context);
//			_f=fileCache.getFile(_path);
//		    _image.setImageBitmap(decodeFile());
//		    notifyDataSetChanged();
	       
//		}		
//		else if((_currentOption!=0 && _stateChosenImage))
//		{
//			_f = new  File(_path);
//			_image.setImageBitmap(decodeFile());
//			_currentOption = 0;
//			_stateChosenImage = false;
//			notifyDataSetChanged();
	    } 
	
		else _image.setImageResource(R.drawable.base_picture);
		

		CheckBox _check = (CheckBox) _view.findViewById(R.id.HouseCheckBox);
		if(this._check)
		{
			_check.setChecked(false);
			_check.setVisibility(View.VISIBLE);
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
							HousesMenu.takePicture();
							break;
						case 2:
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
	
	
	/**
	 * 
	 * @return
	 */
	private Bitmap decodeFile()
	{
		try 
        {
            URL _imageUrl = new URL(_path);
            HttpURLConnection _conn = (HttpURLConnection)_imageUrl.openConnection();
            _conn.setConnectTimeout(30000);
            _conn.setReadTimeout(30000);
            _conn.setInstanceFollowRedirects(true);
            InputStream _is=_conn.getInputStream();
            OutputStream _os = new FileOutputStream(_f);
            Utils.CopyStream(_is, _os);
            _os.close();
            
            // Assume documentId points to an image file. Build a thumbnail no
	        // larger than twice the sizeHint
            BitmapFactory.Options _options = new BitmapFactory.Options();
	        _options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(_f.getAbsolutePath(), _options);
	        final int _targetHeight = _image.getHeight(); //175
	        final int _targetWidth = _image.getWidth();	//175
	        final int _height = _options.outHeight;
	        final int _width = _options.outWidth;
	        _options.inSampleSize = 1;
	        if (_height > _targetHeight || _width > _targetWidth) 
	        {
	            final int _halfHeight = _height / 2;
	            final int _halfWidth = _width / 2;
	            // Calculate the largest inSampleSize value that is a power of 2 and
	            // keeps both
	            // height and width larger than the requested height and width.
	            while ((_halfHeight / _options.inSampleSize) > _targetHeight
	                    || (_halfWidth / _options.inSampleSize) > _targetWidth) 
	            {
	                _options.inSampleSize *= 2;
	            }
	        }
	        _options.inJustDecodeBounds = false;
	      //options.inDensity = (int) _context.getResources().getDisplayMetrics().density;
	        return BitmapFactory.decodeFile(_f.getAbsolutePath(), _options);
        }
		catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
		return null;
	}
}
