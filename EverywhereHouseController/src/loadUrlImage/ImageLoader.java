package loadUrlImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import ehc.net.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader 
{
	
	MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    private ImageView _image;
    private int _fullMode;
  //handler to display images in UI thread
    private Handler handler = new Handler();
    
    public ImageLoader(Context context)
    {
        fileCache=new FileCache(context);
        // Creates a thread pool that reuses a fixed number of
        // threads operating off a shared unbounded queue.
        executorService=Executors.newFixedThreadPool(5);
    }
  
    int stub_id = R.drawable.ic_launcher;
    public void DisplayImage(String url, int loader, ImageView imageView, int fullMode)
    {
    	_image = imageView;
        stub_id = loader;
        imageViews.put(imageView, url);
        _fullMode = fullMode;
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }
  
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
  
    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
        Log.d("FILE",f.getName());
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
        {
        	Log.d("FILE",url + " está en cache");
        	return b;
        }
        //from web
        try {
//        	clearCache();
        	Log.d("FILE",url + " está en server");
            Bitmap bitmap=null;
            Log.d("FILE","0");
            URL imageUrl = new URL(url);
            Log.d("FILE","1");
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            Log.d("FILE","2");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            Log.d("FILE","3");
            conn.setInstanceFollowRedirects(true);
            Log.d("FILE","4");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            	  Log.d("FILE", "http response code is " + conn.getResponseCode());
            	  return null;
            }
            InputStream is=conn.getInputStream();
            Log.d("FILE","5");
            OutputStream os = new FileOutputStream(f);
            Log.d("FILE","6");
            Utils.CopyStream(is, os);
            Log.d("FILE","7");
            os.close();
            Log.d("FILE","8");
            bitmap = decodeFile(f);
            Log.d("FILE","9");
            
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }
  
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
        	// Assume documentId points to an image file. Build a thumbnail no
	        // larger than twice the sizeHint
        	 BitmapFactory.decodeFile(f.getAbsolutePath());
        	
            BitmapFactory.Options _options = new BitmapFactory.Options();
	        _options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(f.getAbsolutePath(), _options);
	        
	        int _width = _options.outWidth;
	        int _height = _options.outHeight;
	        float scaleWidth = ((float)  _image.getHeight()) / _width;
	        float scaleHeight = ((float) _image.getWidth()) / _height;
	        // create a matrix for the manipulation
	        Matrix matrix = new Matrix();
	        // resize the bit map
	        matrix.postScale(scaleWidth, scaleHeight);
	        // recreate the new Bitmap
	        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()), 0, 0, _width, _height,matrix, false);
	        
	        return resizedBitmap;	        
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        return null;
    }
  
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i)
        {
            url=u;
            imageView=i;
        }
    }
  
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
  
        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
            // The runnable will be run on the thread to which this handler is attached.
            // BitmapDisplayer run method will call
            handler.post(bd);
        }
    }
  
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
  
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }
  
    public void clearCache() 
    {
        memoryCache.clear();
        fileCache.clear();
    }
}
