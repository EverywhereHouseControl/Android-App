package imageTools;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class MemoryCache 
{
	//------------Variables-----------------------
	private Map<String, SoftReference<Bitmap>> _cache = Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
	//-----------------------------------
	
    public Bitmap get( String id )
    {
        if(!_cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref = _cache.get( id );
        return ref.get();
    }
  
    public void put( String id, Bitmap bitmap )
    {
        _cache.put( id, new SoftReference<Bitmap>( bitmap ) );
    }
  
    public void clear() 
    {
        _cache.clear();
    }
}
