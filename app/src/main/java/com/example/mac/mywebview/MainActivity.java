package com.example.mac.mywebview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText editText;
    private LocationManager lmgr;
    private MyListener myListener;
    private Myjs myjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        myListener = new MyListener();

        myjs = new Myjs();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }else {
            init();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        webView = (WebView)findViewById(R.id.webview);
        editText=(EditText)findViewById(R.id.edtext);
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,myListener);

        initWebView();
    }


    private class MyListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            webView.loadUrl("javascript:gomap("+ lat+","+lng+")");

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lmgr.removeUpdates(myListener);
    }

    private void initWebView(){
        webView.setWebViewClient(new WebViewClient());
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(myjs,"test");


        //webView.loadUrl("http://www.yahoo.com.tw");
        webView.loadUrl("file:///android_asset/test.html");

    }

    public void test1(View view){
        //webView.loadUrl("javascript:test3('"+editText.getText().toString()+"')");
        webView.loadUrl("javascript:gomap(21.948324, 120.779491)");
    }
    public void test2(View view){

    }
    public void test3(View view){

    }
    public void test4(View view){

    }
    private class Myjs{
        @JavascriptInterface
        protected void m1(String name){
            Log.i("test","ok"+ name);
        }

        @JavascriptInterface
        public void alert(String mesg){
            AlertDialog dialo = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Mesg");
            builder.setMessage(mesg);
            dialo = builder.create();
            dialo.show();
        }
    }



}
