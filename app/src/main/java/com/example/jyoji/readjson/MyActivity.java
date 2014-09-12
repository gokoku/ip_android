package com.example.jyoji.readjson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;


public class MyActivity extends Activity implements View.OnClickListener {

    public static IpAccess Access;
    public static TextView Tv1, Tv2;
    private JsonLoader jsonloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        // インスタンスを用意する
        jsonloader = new JsonLoader();
        Access = new IpAccess();
        Tv1 = (TextView) findViewById(R.id.textView1);
        Tv2 = (TextView) findViewById(R.id.textView2);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        if(networkCheck(this.getApplicationContext())) {
            Toast.makeText(this,"Connected!!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("情報の取得に失敗しました。");
            alertDialogBuilder.setMessage("通信情報を確認して、再起動をお願いいたします。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(MODE_PRIVATE);
                        }
                    });
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        jsonloader.execute("http://show-me-ip.herokuapp.com/");
    }
    public void onClick(View v) {
        Tv1.setText("");
        Tv2.setText("");
        jsonloader = null;
        jsonloader = new JsonLoader();
        jsonloader.execute("http://show-me-ip.herokuapp.com/");
    }


    public static boolean networkCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    // 取得したJSONデータを格納するコンテナ
    public class IpAccess {
        String ip;
        String agent;
    }

    // 表示の更新をする
    public static void updateTextView() {
        Tv1.setText(Access.ip);
        Tv2.setText(Access.agent);
    }
}
