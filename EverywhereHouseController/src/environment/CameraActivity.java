package environment;

import ehc.net.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CameraActivity extends Activity {
	 
		private WebView webView;
	 
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.camera);
	 
			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl("http://www.google.com");
	 
		}
}