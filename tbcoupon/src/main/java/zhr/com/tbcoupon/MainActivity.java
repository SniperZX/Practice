package zhr.com.tbcoupon;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    protected WebView webview;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
       (WebView) findViewById(R.id.webview);


        setWebviewSetting();




        webview.loadUrl("https://www.taobao.com");webview.setWebViewClient(new MyWebViewClient());
//        webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
    }

    public void setWebviewSetting(){
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private class MyWebViewClient extends WebViewClient{


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieManager cookieManager = CookieManager.getInstance();
            String cookies =  cookieManager.getCookie(getDOMAIN(url));
            Log.e("cookie","===>"+cookies);


        }

        public  String getDOMAIN(String url) {
            if (TextUtils.isEmpty(url)) {
                return "";
            }
            Uri uri = Uri.parse(url);
            return uri.getHost();
        }

    }


    private class MyWebChromeClient extends WebChromeClient{

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
        }
    }
}
