package framework;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


public class JSON 
{

	private static final String TAG_NAME = "name";
	private static final String TAG_USER = "User";
	private static final String TAG_ROOMS = "Rooms";
	private static final String TAG_ITEMS = "items";

	public String loadJSONFromAsset(Context c) 
	{
        String json = null;
        try 
        {
         //InputStream is = getAssets().open("config.json");
            InputStream is = c.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

	public void loadJSON(Context c) 
	{

		JSONObject obj=null;
		try 
		{
			obj = new JSONObject(loadJSONFromAsset(c));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		try 
		{
			JSONObject habitaciones = obj.getJSONObject(TAG_ROOMS);
			for (int i = 0; i < habitaciones.length(); i++)
			{
				JSONObject habitacion = habitaciones.getJSONObject("R"+i);
				
				System.out.println(habitacion.getString(TAG_NAME));
				
				Log.e("COLIN_TAG", habitacion.getString(TAG_NAME));
				JSONArray items = habitacion.getJSONArray(TAG_ITEMS);
				
				for (int j = 0; j < items.length(); j++)
				{
					Log.e("COLIN_TAG",items.getString(j));
					System.out.println(items.getString(j));
				}
			}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
	}
}