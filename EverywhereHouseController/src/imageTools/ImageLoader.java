package imageTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ehc.net.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader 
{
	//------------Variables-----------------------
	MemoryCache _memoryCache=new MemoryCache();
    FileCache _fileCache;
    private Map<ImageView, String> _imageViews = Collections.synchronizedMap( new WeakHashMap<ImageView, String>() );
    ExecutorService _executorService;
    private ImageView _image;
//    private int _fullMode;
  //handler to display images in UI thread
    private Handler _handler = new Handler();
    private int _stub_id = R.drawable.ic_launcher;
  //---------------------------------------------
    
    public ImageLoader( Context context )
    {
        _fileCache = new FileCache( context );
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        _executorService=Executors.newFixedThreadPool( 5 );
    }
  
    /**
     * 
     * @param url
     * @param loader
     * @param imageView
     * @param fullMode
     */
    public void DisplayImage( String url, int loader, ImageView imageView, int fullMode )
    {
    	_image = imageView;
        _stub_id = loader;
        _imageViews.put( imageView, url );
//        _fullMode = fullMode;
        Bitmap bitmap = _memoryCache.get( url );
        if( bitmap != null )
            imageView.setImageBitmap( bitmap );
        else
        {
            queuePhoto( url, imageView );
            imageView.setImageResource( loader );
        }
    }
  
    /**
     * 
     * @param url
     * @param imageView
     */
    private void queuePhoto( String url, ImageView imageView )
    {
        PhotoToLoad p = new PhotoToLoad( url, imageView );
        _executorService.submit( new PhotosLoader( p ) );
    }
  
    /**
     * 
     * @param url
     * @return
     */
    private Bitmap getBitmap( String url )
    {
        File _f = _fileCache.getFile( url );
        //from SD cache
        Bitmap _b = decodeFile( _f );
        if( _b != null )
        {
        	return _b;
        }
        //from web
        try 
        {
        	clearCache();
            Bitmap _bitmap = null;
            URL _imageUrl = new URL( url );
            HttpURLConnection _conn = ( HttpURLConnection )_imageUrl.openConnection();
            _conn.setDoInput( true );
            _conn.setDoOutput( true );
            _conn.setConnectTimeout( 30000 );
            _conn.setReadTimeout( 30000 );
            _conn.setInstanceFollowRedirects( true );
            if ( _conn.getResponseCode() != HttpURLConnection.HTTP_OK ) 
            {
            	  Log.d("FILE", "http response code is " + _conn.getResponseCode());
            	  return null;
            }
            InputStream _is = _conn.getInputStream();
            OutputStream _os = new FileOutputStream( _f );
            Utils.CopyStream( _is, _os );
            _os.close();
            _bitmap = decodeFile( _f );
            
            return _bitmap;
        } 
        catch ( Exception ex )
        {
           ex.printStackTrace();
           return null;
        }
    }
  
    //decodes image and scales it to reduce memory consumption
    /**
     * 
     * @param f
     * @return
     */
    private Bitmap decodeFile( File f )
    {
        try 
        {
            //decode image size
        	// Assume documentId points to an image file. Build a thumbnail no
	        // larger than twice the sizeHint
        	 BitmapFactory.decodeFile( f.getAbsolutePath() );
        	
            BitmapFactory.Options _options = new BitmapFactory.Options();
	        _options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile( f.getAbsolutePath(), _options );
	        
	        int _width = _options.outWidth;
	        int _height = _options.outHeight;
	        float _scaleWidth = ( ( float )  _image.getHeight() ) / _width;
	        float _scaleHeight = ( ( float ) _image.getWidth() ) / _height;
	        // create a matrix for the manipulation
	        Matrix _matrix = new Matrix();
	        // resize the bit map
	        _matrix.postScale( _scaleWidth, _scaleHeight );
	        // recreate the new Bitmap
	        Bitmap _resizedBitmap = Bitmap.createBitmap( BitmapFactory.decodeFile( f.getAbsolutePath() ), 0, 0, _width, _height, _matrix, false );
	        
	        return _resizedBitmap;	        
        } 
        catch ( Exception e ) 
        {
        	e.printStackTrace();
        }
        return null;
    }
  
    //Task for the queue
    private class PhotoToLoad
    {
    	//------------Variables-----------------------
        public String _url;
        public ImageView _imageView;
       //-------------------------------------------
        
        public PhotoToLoad( String u, ImageView i )
        {
            _url=u;
            _imageView=i;
        }
    }
  
    class PhotosLoader implements Runnable 
    {
    	//------------Variables-----------------------
        PhotoToLoad _photoToLoad;
      //----------------------------------
        
        PhotosLoader( PhotoToLoad photoToLoad )
        {
            _photoToLoad = photoToLoad;
        }
  
        @Override
        public void run() 
        {
            if( imageViewReused( _photoToLoad ) )
                return;
            Bitmap _bmp = getBitmap( _photoToLoad._url );
            _memoryCache.put( _photoToLoad._url, _bmp );
            if( imageViewReused( _photoToLoad ) )
                return;
            BitmapDisplayer _bd = new BitmapDisplayer( _bmp, _photoToLoad );
            // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
            // The runnable will be run on the thread to which this handler is attached.
            // BitmapDisplayer run method will call
            _handler.post( _bd );
        }
    }
  
    /**
     * 
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused( PhotoToLoad photoToLoad )
    {
        String tag = _imageViews.get( photoToLoad._imageView );
        if( tag == null || !tag.equals( photoToLoad._url ) )
            return true;
        return false;
    }
  
    /**
     * 
     * Used to display bitmap in the UI thread
     *
     */
    class BitmapDisplayer implements Runnable
    {
    	//------------Variables-----------------------
        Bitmap _bitmap;
        PhotoToLoad _photoToLoad;
      //-----------------------------------
        
        public BitmapDisplayer( Bitmap b, PhotoToLoad p )
        {
        	_bitmap = b;
        	_photoToLoad = p;
        }
        
        public void run()
        {
            if( imageViewReused( _photoToLoad ) )
                return;
            if( _bitmap != null )
                _photoToLoad._imageView.setImageBitmap( _bitmap );
            else
                _photoToLoad._imageView.setImageResource( _stub_id );
        }
    }
  
    public void clearCache() 
    {
        _memoryCache.clear();
        _fileCache.clear();
    }
}
