package imageTools;

import java.io.File;

import android.content.Context;

public class FileCache 
{
	//------------Variables-----------------------
    private File _cacheDir;
  //---------------------------------------------
    
    public FileCache( Context context )
    {
        //Find the dir to save cached images
        if ( android.os.Environment.getExternalStorageState().equals( android.os.Environment.MEDIA_MOUNTED ) )
            _cacheDir = new File( android.os.Environment.getExternalStorageDirectory(), "TempImages" );
        else
            _cacheDir=context.getCacheDir();
        if( !_cacheDir.exists() )
            _cacheDir.mkdirs();
    }
  
    /**
     * 
     * @param url
     * @return
     */
    public File getFile( String url )
    {
        String _filename=String.valueOf( url.hashCode() );
        File _f = new File( _cacheDir, _filename );
        return _f;
  
    }
  
    /**
     * 
     */
    public void clear()
    {
        File[] _files = _cacheDir.listFiles();
        if( _files==null )
            return;
        for( File _f : _files )
            _f.delete();
    }
  
}
