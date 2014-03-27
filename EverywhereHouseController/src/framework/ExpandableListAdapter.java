package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ehc.net.R;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
    //----------------------------
    private String	currentRoom;
    private ProgressDialog pDialog;
    private String file;
    private String servicename;
    private String action;
	private Post _post;
	private String _message;

 
    public ExpandableListAdapter(String room,Context context, List<String> laptops,
    		Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.laptops = laptops;
        this.laptopCollections = laptopCollections;
        this.currentRoom = room.toUpperCase();
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

    	String itemName = null;
        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            itemName = (String) getChild(groupPosition,childPosition);
            int itemType = getChildXML(itemName);
            convertView = inflater.inflate(itemType, null);
            //-------------------------------------
            setListeners(convertView,itemType);
            servicename = (String) getGroup(groupPosition);
        	servicename = servicename.toUpperCase();
        	Log.d("servicename", servicename.toString());
            //--------------------------------------
        }
       
        return convertView;
    }
    
    private void setListeners(final View convertView, int itemType){
    	final TextView tv = (TextView) convertView.findViewById(R.id.childname);
    	if (itemType == R.layout.float_item){
    		
    		SeekBar sb = (SeekBar) convertView.findViewById(R.id.float_value);
        	sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					tv.setText(progress);
				}
			});
    	}
    	//-------------------------------------------------------------------------
    	if (itemType == R.layout.boolean_item)
    	{
    		final ToggleButton checkbox = (ToggleButton) convertView.findViewById(R.id.boolean_value);
    		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
    		{	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					// TODO Auto-generated method stub
					if(isChecked)
					{
						action = "ENCENDER";
					}
					else
					{
						action = "APAGAR";
					}
					parser();
					_post = new Post();						
					doActionConnection connection = new doActionConnection();
			    	connection.execute();
				}
			});
    	}
    	//---------------------------------------------------------------------------------	
    }
    
    private int getChildXML(String itemType){
    	 if (itemType.equals("float")){
    		 return R.layout.float_item;
    	 } else if (itemType.equals("boolean")){
        	 return R.layout.boolean_item;
    	 }
    	return 0;
    }
 
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }
 
    public int getGroupCount() {
        return laptops.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.groupname);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    //---------------------------------------------------------------------------
    /**
	 * 
	 */
	private void parser()
	{
		try 
		{
			InputStream is = context.openFileInput("profileInformation.json");
			int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        this.file = new String(buffer, "UTF-8");
		} 
    	catch (IOException ex) 
    	{
    		ex.printStackTrace();
    	}
		catch (Exception ex) 
    	{
    		ex.printStackTrace();
    	}
	}
	
    private class doActionConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> parametros = new ArrayList<String>();
	    
		/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{
            super.onPreExecute();
            /*pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }    	
    	    	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			int _internalError = 0;

			try 
			{
				Log.e("servicename", servicename.toString());
				JSONObject obj = new JSONObject(file);
			
				parametros.add("command");
				parametros.add("doaction");
				parametros.add("username");
				parametros.add(obj.getString("USERNAME"));
				parametros.add("housename");
				parametros.add("casaBertoldo");
				parametros.add("roomname");
				parametros.add(currentRoom);
				parametros.add("servicename");
				//parametros.add(servicename);
				parametros.add("LIGTHS");
				parametros.add("actionname");
				parametros.add(action);
				parametros.add("data");
				parametros.add("UNO");
			 			
				errorControl(parametros,_internalError);	
			
			} 
			catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			return null;
		}
		
		/**
		 * 
		 */
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
			//pDialog.dismiss();
            Toast.makeText(context, _message, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Error control for modify user.
	 */
	private void errorControl(ArrayList<String> parametros, int _internalError)
	{
		
		switch(_internalError)
		{
			case 0:
			{
				JSONArray data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php");
				try 
				{
					JSONObject json_data = data.getJSONObject(0);
					Log.d("ERROR",json_data.toString());
					
					switch(json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = json_data.getString("ENGLISH");					
							break;
						}
						default:
						{
							_message = json_data.getString("ENGLISH");
							break;
						}
					}
				
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;
			}
			default:
			{
				
			}
		}		
	}
    
    
    
}