package gcmService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.*;

import ehc.net.CreateUser;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


public class GCM
{
	private Activity _context;

	private String _file;
	
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
	public boolean checkPlayServices() 
	{
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
	
	private void parser()
	{
		try 
		{
			InputStream _is = _context.openFileInput("profileInformation.json");
			int _size = _is.available();
	        byte[] buffer = new byte[_size];
	        _is.read(buffer);
	        _is.close();
	        this._file =null;
	        this._file = new String(buffer, "UTF-8");
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
	public String getRegistrationId() throws JSONException
	{
	    SharedPreferences prefs = _context.getSharedPreferences(
	    		CreateUser.class.getSimpleName(),
	        Context.MODE_PRIVATE);
	 
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	 
	    if (registrationId.length() == 0)
	    {
	        Log.d("GCM", "Registro GCM no encontrado.");
	        return "";
	    }
	 
	    String registeredUser =
	    prefs.getString(PROPERTY_USER, "user");
	 
	    int registeredVersion =
	    prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	 
	    long expirationTime =
	        prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);
	 
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
	    String expirationDate = sdf.format(new Date(expirationTime));
	 
	    Log.d("GCM", "Registro GCM encontrado (usuario=" + registeredUser +
	    ", version=" + registeredVersion +
	    ", expira=" + expirationDate + ")");
	 
	    int currentVersion = getAppVersion(_context);
	    
	    parser();
	    JSONObject _obj = new JSONObject(_file);
	 
	    if (registeredVersion != currentVersion)
	    {
	        Log.d("GCM", "Nueva versión de la aplicación.");
	        return "";
	    }
	    else if (System.currentTimeMillis() > expirationTime)
	    {
	        Log.d("GCM", "Registro GCM expirado.");
	        return "";
	    }
	    else if (_obj.getString("USERNAME").equals(registeredUser))
	    {
	        Log.d("GCM", "Nuevo nombre de usuario.");
	        return "";
	    }
	 
	    return registrationId;
	}
	 
	private static int getAppVersion(Context context)
	{
	    try
	    {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	 
	        return packageInfo.versionCode;
	    }
	    catch (NameNotFoundException e)
	    {
	        throw new RuntimeException("Error al obtener versión: " + e);
	    }
	}

}
