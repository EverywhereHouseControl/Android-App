package gcmService;

import java.util.Calendar;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ehc.net.Main;
import ehc.net.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends IntentService
{
	public static NotificationManager mNotificationManager = null; 
	
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
//	private boolean isApplicationBroughtToBackground() 
//	{
//	    ActivityManager _am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//	    List<RunningTaskInfo> _tasks = _am.getRunningTasks(1);
//	    if (!_tasks.isEmpty()) 
//	    {
//	        ComponentName topActivity = _tasks.get(0).topActivity;
//	        if (!topActivity.getPackageName().equals(this.getPackageName())) {
//	            return true;
//	        }
//	    }
//
//	    return false;
//	}
}
