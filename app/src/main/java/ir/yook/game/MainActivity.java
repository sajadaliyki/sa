package ir.yook.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String HOME_URL = "file:///android_asset/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setTextZoom(100);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);

        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setBackgroundColor(0xFF100821);
        webView.loadUrl(HOME_URL);

        setContentView(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView == null) {
            super.onBackPressed();
            return;
        }

        String currentUrl = webView.getUrl();
        if (currentUrl != null && !currentUrl.endsWith("index.html") && webView.canGoBack()) {
            webView.goBack();
            return;
        }

        webView.evaluateJavascript(
                "(typeof handleAndroidBack === 'function') ? handleAndroidBack() : false",
                handled -> {
                    if (!"true".equals(handled)) {
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.evaluateJavascript(
                    "try{document.querySelectorAll('audio').forEach(a=>a.pause())}catch(e){}",
                    null
            );
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.evaluateJavascript(
                    "try{if(typeof ensureMusic==='function'&&typeof musicEnabled!=='undefined'&&musicEnabled)ensureMusic()}catch(e){}",
                    null
            );
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
