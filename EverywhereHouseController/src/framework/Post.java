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

import android.util.Log;

public class Post 
{	                        
	private InputStream _is = null;		                         
	private String _response = "";

	public Post(){}
	
	/**
	 * Returns a JSONArray with the response to a query
	 * @param _parametros
	 * @param _URL
	 * @return
	 */
	public JSONArray getServerData(ArrayList<String> parameters, String URL) 
	{				 					 
		connectionPost( parameters, URL );
		
		if (_is != null) 
		{				                                                
			getResponsePost();
		}
		
		if (_response != null /*&& response.trim() != ""*/) 
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
	private void connectionPost(ArrayList<String> parametros, String URL) 
	{				                         
		ArrayList<BasicNameValuePair> _nameValuePairs;	                         
		try 
		{                                              
			HttpClient _httpclient = new DefaultHttpClient();                                              
			HttpPost _httppost = new HttpPost(URL);				                                                
			//------------------------
			_httppost.setURI( new URI(URL) );
			//-------------------------
			_nameValuePairs = new ArrayList<BasicNameValuePair>();			
			                                                
			if (parametros != null) 
			{				                                                                        
				for (int i = 0; i < parametros.size() - 1; i += 2) 
				{			 
					_nameValuePairs.add(new BasicNameValuePair( parametros.get(i), parametros.get(i + 1)));				                                                                        
				}				 
				_httppost.setEntity(new UrlEncodedFormEntity(_nameValuePairs));				                                                
			}
					
			HttpResponse _response = _httpclient.execute(_httppost);
			HttpEntity _entity = _response.getEntity();
			_is = _entity.getContent();
			
		} 
		catch (Exception e) 
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
			BufferedReader _reader = new BufferedReader( new InputStreamReader(_is, "iso-8859-1"), 8 );				 
			StringBuilder _sb = new StringBuilder();				 
			String _line = null;
			 
			while ((_line = _reader.readLine()) != null) 
			{				                         
				_sb.append(_line + "\n");	
				Log.e("line",_line.toString());
			}				 
			_is.close();				 
			_response = _sb.toString();				 
			//Log.e("log_tag", "String JSon " + response.toString());
			
		} 
		catch (Exception e) 
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
		JSONArray _jArray = null;
		try 
		{	
			JSONObject _json = new JSONObject( _response );
			_jArray = _json.getJSONArray( "result" );
			
		} catch ( Exception e ) 
		{
			System.out.print( "ERROR:" + e );
		} 
						                                                
		return _jArray;		                         		 
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
	        MessageDigest _digest = java.security.MessageDigest.getInstance("MD5");
	        _digest.update(s.getBytes());
	        byte messageDigest[] = _digest.digest();

	        // Create Hex String
	        StringBuffer _hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            _hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return _hexString.toString();

	    } catch (NoSuchAlgorithmException e) 
	    {
	        e.printStackTrace();
	    }
	    return "";
	}
	
}
