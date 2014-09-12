package com.example.jyoji.readjson;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Created by jyoji on 2014/09/10.
 */

public class JsonLoader extends AsyncTask<String, Integer, JSONObject> {
    private TextView tv[];
    private DefaultHttpClient httpClient;

    public JsonLoader() {
        httpClient = new DefaultHttpClient();
    }
    @Override
    protected JSONObject doInBackground(String... args) {
        execAPI(args[0]);
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        MyActivity.updateTextView();
    }

    // -------------------------------------------------------------
    private void execAPI(String url) {
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = executeRequest(request);

            // サーバからのステータスを取得する
            int statusCode = response.getStatusLine().getStatusCode();
            StringBuilder buf = new StringBuilder();
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            // サーバからのレスポンスを行単位に読み込む
            while((line = reader.readLine()) != null) {
                buf.append(line);
                Log.d("JsonReader", line);
            }
            if(statusCode == 200) {
                parseResponse(buf.toString());
            }
        } catch (IOException e) {
            Log.e("JsonReader", "IO error", e);
        } catch (JSONException e) {
            Log.e("JsonReader", "JSON error", e);
        }
    }

    private void parseResponse(String buf) throws JSONException {
        JSONObject rootObj = new JSONObject(buf);
        JSONArray results = rootObj.getJSONArray("access");
        JSONObject item = results.getJSONObject(0);
        // 表示用アイテムに登録する
        MyActivity.Access.ip = item.getString("ip");
        MyActivity.Access.agent = item.getString("agent");
        Log.d("JsonReader", item.getString("ip"));
        Log.d("JsonReader", item.getString("agent"));
    }

    private HttpResponse executeRequest(HttpRequestBase base) throws IOException {
        try {
            return httpClient.execute(base);
        } catch(IOException e) {
            base.abort();
            throw e;
        }
    }
}

