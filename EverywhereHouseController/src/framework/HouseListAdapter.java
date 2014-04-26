package framework;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import loadUrlImageFramework.ImageLoader;
import com.kbeanie.imagechooser.api.ChosenImage;
import ehc.net.HousesMenu;
import ehc.net.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HouseListAdapter extends BaseAdapter
{
	private String _name;
	private Context _context;
	private ArrayList<String> _objectList;
	private int _convertView;
	private ImageButton _image;
	private String _path;
	private ArrayList<String> _urls = new ArrayList<String>();
	private ArrayList<String> _access = new ArrayList<String>();
	public static ImageLoader _imgLoader;
	public static Post _post;
	JSONObject _data = new JSONObject();
	public static String _currentHouse;
	private ImageView _imageDialog;
	
	public HouseListAdapter(Context context, ArrayList<String> ObjectList,ArrayList<String> urls,ArrayList<String> access, int convertView)
	{
		_context = context;
		_objectList = ObjectList;
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
		_image.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				// custom dialog
				_currentHouse = _objectList.get(position);
				HousesMenu._selectMode = false;
				final Dialog dialog = new Dialog(_context);
				dialog.setTitle("Image");
				dialog.setContentView(R.layout.image_dialog);
				
				_imageDialog = (ImageView) dialog.findViewById(R.id.userImageDialog);
				_imgLoader.DisplayImage(_urls.get(position), R.drawable.base_picture, _imageDialog, 1);
				
				ImageButton _takePicture = (ImageButton)dialog.findViewById(R.id.takePicture);
				_takePicture.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						HousesMenu.takePicture();
					}
				});
				
				ImageButton _choosePicture = (ImageButton)dialog.findViewById(R.id.choosePicture);
				_choosePicture.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						HousesMenu.chooseImage();
					}
				});
				
				dialog.onBackPressed();
				dialog.setOnCancelListener(new OnCancelListener() 
				{
					
					@Override
					public void onCancel(DialogInterface dialog) 
					{
						// TODO Auto-generated method stub
						
						if(HousesMenu._selectMode)
						{
						
							new AlertDialog.Builder(_context)
					        .setTitle("Image")
					        .setMessage("Save changes?")
					        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int which) 
					            { 
					                // do nothing
					            }
					         })
					         .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int which) 
					            { 
//							    	_check.setVisibility(View.GONE);
							    	HousesMenu._check.setChecked(true);
							    	HousesMenu._currentHouse = _currentHouse;
					            }
					         })
					        .setIcon(android.R.drawable.ic_dialog_alert)
					         .show();
						}
					}
				});
				
				dialog.show();
			}
		});
		
		if(!_urls.get(position).equals("null"))
		{
			_path = _urls.get(position);
			_imgLoader.DisplayImage(_path, R.drawable.base_picture, _image, 0);
			
	    } 
		else _image.setImageResource(R.drawable.base_picture);
		
		return _view;
	}
	
	/**
	 * 
	 */
	public void setChosenImage(ChosenImage image)
	{
		_imageDialog.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
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
