package gcmService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ehc.net.Main;
import ehc.net.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends IntentService
{
	public static NotificationManager mNotificationManager = null;
	private final String _ip = Post._ip;
	
	public GCMIntentService() 
	{
	        super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        
        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();
        Log.d("GCM",extras.toString());
        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
            	mostrarNotification(Calendar.getInstance().getTimeInMillis(),extras.getString("mensaje"));
            }
        }
        
        if(!isApplicationBroughtToBackground())
        {
        	Log.d("NO BACKGROUND", "LOGIN HECHO");
        	
        	SharedPreferences _pref = getSharedPreferences("LOG",Context.MODE_PRIVATE);
        	
        	ArrayList<String> _parametros = new ArrayList<String>();
			_parametros.add("command");
			_parametros.add("login2");
			_parametros.add("username");
			_parametros.add(_pref.getString("USER", ""));
			_parametros.add("password");
			_parametros.add(_pref.getString("PASSWORD", ""));
			
			logInConnection _connection = new logInConnection(_parametros);
			_connection.execute();	
        }else
        {
        	Log.d("SI BACKGROUND", "LOGIN NO HECHO");
        }
        
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }
	
	private void mostrarNotification(long id,String msg)
	{
		Intent notIntent = new Intent(this, Main.class);
		PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);
		
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
		    // Do something for HONEYCOMB and above versions
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setDefaults(NotificationCompat.PRIORITY_DEFAULT)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("EHC notification.")
			.setLargeIcon((((BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher)).getBitmap()))
			.setContentText(msg)
			.setTicker(msg)
			.setAutoCancel(true)
			.setContentIntent(contIntent)
			.setDefaults(Notification.DEFAULT_SOUND);			
				
			mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify((int)id, mBuilder.build());	
		} 
		else
		{
		    // do something for phones running an SDK before HONEYCOMB
		    // Do something for HONEYCOMB and above versions
			Notification mBuilder = new Notification(R.drawable.ic_launcher,msg,id);
			
			mBuilder.setLatestEventInfo(this, "EHC notification.", msg, contIntent);
			mBuilder.flags |= Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL;	
				
			mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify((int)id, mBuilder);	
			
		}
	}
	
	public static void closeNotifications()
	{
		if(mNotificationManager!=null)mNotificationManager.cancelAll();
	}

	/**
	 * Check if the application is in background.
	 * @return
	 */
	private boolean isApplicationBroughtToBackground() 
	{
	    ActivityManager _am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> _tasks = _am.getRunningTasks(1);
	    if (!_tasks.isEmpty()) 
	    {
	        ComponentName topActivity = _tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(this.getPackageName())) 
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
	private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	private ArrayList<String> _parametros = new ArrayList<String>();
    	
    	public logInConnection(ArrayList<String> parametros) 
    	{
			// TODO Auto-generated constructor stub
    		_parametros = parametros;
		}
    	  	
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();        
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{		             
				//Query
				//Variable 'Data' saves the query response
				JSONObject _data = Post.getServerData(_parametros,_ip);
				Log.d("DATA", _data.toString());
				
				try 
				{
					_data.getJSONObject("error");				
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				if (_data != null && _data.length() > 0) 
				{				
					JSONObject _json_data = _data.getJSONObject("result");
					//log(json_data.toString());
					
					if (_json_data.getInt("IDUSER")==0) 
					{ 
						Log.d("ERROR","Incorrect user");
					}
					else
					{ 	
						//////////////////////////////////////////////
						SharedPreferences _pref = getSharedPreferences("LOG",Context.MODE_PRIVATE);
				        Editor _editor=_pref.edit();
				        _editor.putString("LOGIN", "TRUE");
				        _editor.commit();
				        //////////////////////////////////////////////
				        
						//Save the profile's information.
						JSON.saveProfileInfo(_json_data,GCMIntentService.this);
						//Save the house's configuration
						JSON.saveConfig(_json_data.get("JSON"),GCMIntentService.this);
					}
				}
				else 
				{
					Log.d("JSON", "ERROR" + _data.toString());
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
			super.onPostExecute(file_url);
		}
    }
}
