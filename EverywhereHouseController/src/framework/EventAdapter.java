package framework;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ehc.net.R;

public class EventAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Event> event;
	private String _file;
	private Post _post;
	private String eventName;
	private String userName;

	public EventAdapter(Context c, ArrayList<Event> e) {
		event = e;
		context = c;
	}

	@Override
	public int getCount() {
		return event.size();
	}

	@Override
	public Object getItem(int position) {
		return event.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater _infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = _infalInflater.inflate(R.layout.event_fragment, null);
		}

		TextView nameText = (TextView) convertView.findViewById(R.id.textView2);
		nameText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView itemText = (TextView) convertView.findViewById(R.id.forgotPassword);
		itemText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView createText = (TextView) convertView.findViewById(R.id.textView3);
		createText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView hourText = (TextView) convertView.findViewById(R.id.textView4);
		hourText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		ImageView deleteButton = (ImageView) convertView.findViewById(R.id.removeEvent);
		
		
		TextView name = (TextView) convertView.findViewById(R.id.fragment_name);
		name.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));

		TextView item = (TextView) convertView.findViewById(R.id.fragment_item);
		item.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView creator = (TextView) convertView
				.findViewById(R.id.fragment_creator);
		creator.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView hour = (TextView) convertView.findViewById(R.id.fragment_hour);
		hour.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		deleteButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					userName = event.get(position).getCreator();
					eventName = event.get(position).getName();
					event.remove(position);
					notifyDataSetChanged();

					_post = new Post();
					sendEventConnection _connection = new sendEventConnection(context);
			    	_connection.execute();	
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		name.setText(event.get(position).getName());
		item.setText(Integer.toString(event.get(position).getItem()));
		creator.setText(event.get(position).getCreator());
		hour.setText(event.get(position).getHour() + ":"
				+ event.get(position).getMinute());

		
		
		return convertView;
	}

	public void clear() {
		event.clear();
	}
	
	// Background process
    private class sendEventConnection extends AsyncTask<String, String, String>
    {    	
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	private Context context = null;
    	
    	public sendEventConnection(Context c) {
    		context=c;
		}
    	
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(context);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{		      
				//Query
				ArrayList<String> _parametros = new ArrayList<String>();

				Log.d("test",userName);
				_parametros.add("command");
				_parametros.add("deleteprogramaction");
				_parametros.add("username");
				_parametros.add(userName.toUpperCase());
				Log.d("test",userName);
				_parametros.add("programname");
				Log.d("test",eventName);
				_parametros.add(eventName.toUpperCase());
				_parametros.add("actionname");
				_parametros.add("SEND");
				
				Log.d("PARAMETROS",_parametros.toString());
				
				JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");//"");
				Log.d("DATA",_data.toString());
				
				try 
				{
					JSONObject _json_data = _data.getJSONObject("error");
					switch(_json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = _json_data.getString("ENGLISH");					
							break;
						}
						default:
						{
							_internalError = _json_data.getInt("ERROR");
							_message = _json_data.getString("ENGLISH");
							break;
						}
					}
				
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
			 }
			catch (Exception _e) 
			 {
			 	_e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog
            _pDialog.dismiss();
            if(_internalError!=0)Toast.makeText(context, _message, Toast.LENGTH_SHORT).show();
		}
    }
    

}