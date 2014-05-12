package gcmService;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import serverConnection.Post;

import com.google.android.gms.common.*;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;


public class GCM
{
	private static Activity _context;

	private static String _file;
	
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
	private static final String PROPERTY_USER = "user";
	
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    


	
	public void setContext(Activity context)
	{
		_context = context;
	}
	
	/**
	En cuanto a la lógica para hacer el chequeo podremos ayudarnos de la clase GooglePlayServicesUtil, 
	que dispone del método isGooglePlayServicesAvailable() para hacer la verificación. 
	En caso de no estar disponibles (si el método devuelve un valor distinto a SUCCESS) 
	aún podemos mostrar un diálogo de advertencia al usuario dando la posibilidad de instalarlos. 
	Esto lo haremos llamando al método getErrorDialog() de la misma clase GooglePlayServicesUtil.
	**/
	public static boolean checkPlayServices(Activity context) 
	{
		_context = context;
		
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_context);
	    if (resultCode != ConnectionResult.SUCCESS)
	    {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
	        {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, _context,
	            		PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        }
	        else
	        {
	            Log.i("GCM", "Dispositivo no soportado.");
	        }
	        return false;
	    }
	    return true;
	}
	
	
	/**
	En el método getRegistrationId() lo primero que haremos será recuperar la preferencia 
	PROPERTY_REG_ID. Si ésta no está informada saldremos inmediatamente del método para 
	proceder a un nuevo registro.
	
	Si por el contrario ya teníamos un registration_id guardado podríamos seguir utilizándolo 
	sin tener que registrarnos de nuevo (lo devolveremos como resultado), pero habrá tres 
	situaciones en las que queremos volver a realizar el registro para asegurarnos de que 
	nuestra aplicación pueda seguir recibiendo mensajes sin ningún problema:
	
	    Si el nombre de usuario ha cambiado.
	    Si la versión de la aplicación ha cambiado.
	    Si se ha sobrepasado la fecha de caducidad del código de registro.
	
	Para verificar esto nuestro método recuperará cada una de las preferencias compartidas, 
	realizará las verificaciones indicadas y en caso de cumplirse alguna de ellas saldrá del 
	método sin devolver el antiguo registration_id para que se vuelva a realizar el registro.
	 **/
	public static String getRegistrationId(Activity context) throws JSONException
	{
		_context = context;
		
//	    SharedPreferences prefs = _context.getSharedPreferences(
//	    		_class.getSimpleName(),
//	        Context.MODE_PRIVATE);
//	 
//	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		
		SharedPreferences _pref = _context.getSharedPreferences("LOG",Context.MODE_PRIVATE);
		String registrationId = _pref.getString("ID", "");
	 
	    if (registrationId.length() == 0)
	    {
	        Log.d("GCM", "Registro GCM no encontrado.");
	        return "";
	    }
	 
	    String registeredUser =
	    		_pref.getString(PROPERTY_USER, "user");
	 
	    int registeredVersion =
	    		_pref.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	 
	    long expirationTime =
	    		_pref.getLong(PROPERTY_EXPIRATION_TIME, -1);
	    
	   Log.d("GCM", registeredUser +" "+ Integer.toString(registeredVersion));
	 
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
	    String expirationDate = sdf.format(new Date(expirationTime));
	 
	    Log.d("GCM", "Registro GCM encontrado (usuario=" + registeredUser +
	    ", version=" + registeredVersion +
	    ", expira=" + expirationDate + ")");
	 
//	    int currentVersion = getAppVersion(_context);
	    
//	  //It's load the profile's information.
//	  _file = JSON.loadUserInformation(_context);
//	  
//	  JSONObject _obj = new JSONObject(_file);
//	 
//	    if (registeredVersion != currentVersion)
//	    {
//	        Log.d("GCM", "Nueva versión de la aplicación.");
//	        return "";
//	    }
//	    else if (System.currentTimeMillis() > expirationTime)
//	    {
//	        Log.d("GCM", "Registro GCM expirado.");
//	        return "";
//	    }
//	    else if (_obj.getString("USERNAME").equals(registeredUser))
//	    {
//	        Log.d("GCM", "Nuevo nombre de usuario.");
//	        return "";
//	    }
	 
	    return registrationId;
	}
	 
//	private static int getAppVersion(Context context)
//	{
//	    try
//	    {
//	        PackageInfo packageInfo = context.getPackageManager()
//	                .getPackageInfo(context.getPackageName(), 0);
//	 
//	        return packageInfo.versionCode;
//	    }
//	    catch (NameNotFoundException e)
//	    {
//	        throw new RuntimeException("Error al obtener versión: " + e);
//	    }
//	}
	
	/**
	 * GCM registration for new user.
	 * @author Miguel
	 *
	 */
	public static class TaskRegisterGCM extends AsyncTask<String,Integer,String>
	{
		//Project Number: 701857172243 
		private String SENDER_ID = "701857172243";
		private GoogleCloudMessaging _gcm;
		private Context _context;
		
		public TaskRegisterGCM(Context context)
		{
			_context = context;
		}
		
	    @Override
	    protected String doInBackground(String... params)
	    {
	            String msg = "";
	            try
	            {
	                if (_gcm == null)
	                {
	                    _gcm = GoogleCloudMessaging.getInstance(_context);
	                }
	 
	                //Registration into servers GCM
	                String _regid = _gcm.register(SENDER_ID);
	                
	                SharedPreferences _pref = _context.getSharedPreferences("LOG",Context.MODE_PRIVATE);
	                Editor _editor=_pref.edit();
			        _editor.putString("ID", _regid);
			        _editor.commit();
	                
	                Log.d("GCM", "Registrado en GCM: registration_id=" + _regid);
	                
	                ArrayList<String> _parametros = new ArrayList<String>();
	                _parametros.add("command");
					_parametros.add("login3");
					_parametros.add("username");
					_parametros.add(_pref.getString("USER", ""));
					_parametros.add("password");
					_parametros.add(_pref.getString("PASSWORD", ""));
					_parametros.add("regid");
					_parametros.add(_regid);
					_parametros.add("os");
					_parametros.add("Android");
					
					Log.d("PARAMETROS",_parametros.toString());
					
					 _editor.putString("ID", _regid);
					 
					//Variable 'Data' saves the query response
					JSONObject _data = Post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
					Log.d("ID",_data.toString());
	                
	            }
	            catch (IOException ex)
	            {
	                Log.d("GCM", "Error registro en GCM:" + ex.getMessage());
	            }
	 
	            return msg;
	        }
	}

}
