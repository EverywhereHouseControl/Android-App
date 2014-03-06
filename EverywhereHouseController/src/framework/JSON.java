package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


public class JSON 
{
	String file = null;
	private static final String TAG_NAME = "name";
	private static final String TAG_USER = "User";
	private static final String TAG_ROOMS = "Rooms";
	private static final String TAG_ITEMS = "items";
	
	
	

	public JSON(Context c) 
	{
        try 
        {
            InputStream is = c.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            file = new String(buffer, "UTF-8");

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

	public void loadJSON() throws JSONException 
	{
		JSONObject obj = new JSONObject(file);
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
	
	public ArrayList<String> getRooms() throws JSONException 
	{
		JSONObject obj = new JSONObject(file);
		ArrayList<String> rooms = new ArrayList<String>();

		try 
		{
			JSONObject habitaciones = obj.getJSONObject(TAG_ROOMS);
			for (int i = 1; i < habitaciones.length(); i++)
			{
				JSONObject habitacion = habitaciones.getJSONObject("R"+i);
				rooms.add(habitacion.getString(TAG_NAME));
			}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		return rooms;
	}
	
	public ArrayList<String> getItems() throws JSONException 
	{
		
		JSONObject obj = new JSONObject(file);
		ArrayList<String> rooms = new ArrayList<String>();
		
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
					rooms.add(items.getString(j));
					Log.e("COLIN_TAG",items.getString(j));
					System.out.println(items.getString(j));
				}
			}
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		return rooms;
	}
}