package framework;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
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
	private String respuesta = "";

	public Post(){}
	
	/**
	 * Devuelve un JSONArray con la respuesta a una consulta
	 * @param parametros = consulta
	 * @param URL
	 * @return la respuesta a la consulta
	 */
	public JSONArray getServerData(ArrayList<String> _parametros, String _URL) 
	{				 					 
		conectaPost( _parametros, _URL );
		
		if (is != null) 
		{				                                                
			getRespuestaPost();
		}
		
		if (respuesta != null /*&& respuesta.trim() != ""*/) 
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
	 * @param parametros = consulta
	 * @param URL
	 */
	private void conectaPost(ArrayList<String> _parametros, String _URL) 
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
			
			Log.e("","is");
			Log.e("", is.toString() );
			
		} catch (Exception e) 
		{				                                                
			Log.e("log_tag", "Error in http connection " + e.toString());				                         
		}			
	}

	/**
	 * Parsea la respuesta a la consulta y la mete en el string "respuesta"		 
	 */
	private void getRespuestaPost() 
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
			respuesta = sb.toString();				 
			Log.e("log_tag", "Cadena JSon " + respuesta.toString());
			
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
			JSONObject json = new JSONObject( respuesta );
			jArray = json.getJSONArray( "result" );
			
		} catch ( Exception e ) 
		{
			System.out.print( "ERROR:" + e );
		} 
						                                                
		return jArray;		                         		 
	}
			 
	
	
}
