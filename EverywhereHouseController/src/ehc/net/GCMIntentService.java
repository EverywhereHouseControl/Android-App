package ehc.net;

import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMIntentService extends IntentService
{
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
             mostrarNotification(extras.getString("mensaje"));
            }
        }
        
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }
	
	private void mostrarNotification(String msg)
	{
		NotificationManager mNotificationManager =
		(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("EHC notification.")
		.setContentText(msg);
		
		Intent notIntent = new Intent(this, LogIn.class);
		PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);
		
		mBuilder.setContentIntent(contIntent);
		
		mNotificationManager.notify(R.drawable.ic_launcher, mBuilder.build());
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
	        if (!topActivity.getPackageName().equals(this.getPackageName())) {
	            return true;
	        }
	    }

	    return false;
	}
}
