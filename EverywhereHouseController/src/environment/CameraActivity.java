package environment;

import ehc.net.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CameraActivity extends Activity
{
	 
		private WebView webView;
	 
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.camera);
	 
			webView = (WebView) findViewById(R.id.webView1);
//			webView.getSettings().setJavaScriptEnabled(true);
//			webView.getSettings().setPluginsEnabled(true);
//			webView.loadUrl("http://192.168.2.117:8080/stream320.html");
			
			webView.getSettings().setJavaScriptEnabled(true);     
	           webView.getSettings().setLoadWithOverviewMode(true);
	          webView.getSettings().setUseWideViewPort(true);        
	           
	        webView.setWebChromeClient(new WebChromeClient());

	        webView.loadUrl("http://192.168.2.117:8080/stream320.html");
	 
		}
}