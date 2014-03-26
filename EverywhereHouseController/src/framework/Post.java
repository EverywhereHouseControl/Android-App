package framework;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class Post 
{	                        
	private InputStream is = null;		                         
	private String response = "";

	public Post(){}
	
	/**
	 * Returns a JSONArray with the response to a query
	 * @param _parametros
	 * @param _URL
	 * @return
	 */
	public JSONArray getServerData(ArrayList<String> _parameters, String _URL) 
	{				 					 
		connectionPost( _parameters, _URL );
		
		if (is != null) 
		{				                                                
			getResponsePost();
		}
		
		if (response != null /*&& response.trim() != ""*/) 
		{		
			return getJsonArray();				                                                                        
		} 
		else 
		{				                                                                                               
			return null;				                                                                        
		}				                                                
	}
	
	/**
	 * 
	 * @param _parametros
	 * @param _URL
	 */
	private void connectionPost(ArrayList<String> _parametros, String _URL) 
	{				                         
		ArrayList<BasicNameValuePair> nameValuePairs;	                         
		try 
		{                                              
			HttpClient httpclient = new DefaultHttpClient();                                              
			HttpPost httppost = new HttpPost(_URL);				                                                
			//------------------------
			httppost.setURI( new URI(_URL) );
			//-------------------------
			nameValuePairs = new ArrayList<BasicNameValuePair>();			
			                                                
			if (_parametros != null) 
			{				                                                                        
				for (int i = 0; i < _parametros.size() - 1; i += 2) 
				{			 
					nameValuePairs.add(new BasicNameValuePair( _parametros.get(i), _parametros.get(i + 1)));				                                                                        
				}				 
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));				                                                
			}
					
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();  
			
			//Log.e("","is");
			//Log.e("", is.toString() );
			
		} catch (Exception e) 
		{				                                                
			Log.e("log_tag", "Error in http connection " + e.toString());				                         
		}			
	}

	/**
	 * Parsea la respuesta a la consulta y la mete en el string "respuesta"		 
	 */
	private void getResponsePost() 
	{				 
		try 
		{				 
			BufferedReader reader = new BufferedReader( new InputStreamReader(is, "iso-8859-1"), 8 );				 
			StringBuilder sb = new StringBuilder();				 
			String line = null;
			 
			while ((line = reader.readLine()) != null) 
			{				                         
				sb.append(line + "\n");	
				Log.e("line",line.toString());
			}				 
			is.close();				 
			response = sb.toString();				 
			//Log.e("log_tag", "String JSon " + response.toString());
			
		} catch (Exception e) 
		{				                         
			Log.e("log_tag", "Error converting result " + e.toString());				                         
		}
			 
	}
	
	/**
	 * Convierte el string "respuesta" en un JSONArray
	 * @return
	 */
	private JSONArray getJsonArray() 
	{				                         
		JSONArray jArray = null;
		try 
		{	
			JSONObject json = new JSONObject( response );
			jArray = json.getJSONArray( "result" );
			
		} catch ( Exception e ) 
		{
			System.out.print( "ERROR:" + e );
		} 
						                                                
		return jArray;		                         		 
	}
			 
	/**
	 * Method that encrypts the password
	 * @param s
	 * @return
	 */
	public String md5(String s) 
	{
	    try 
	    {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) 
	    {
	        e.printStackTrace();
	    }
	    return "";
	}
	
}
