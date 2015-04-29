package com.tests.titouan.bluetoothtests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RequestActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        textView = (TextView) findViewById(R.id.text);
    }

    private class getRouteAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progress;
        private String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress = new ProgressDialog(RequestActivity.this);
                    progress.setMessage("Trying to get json");
                    progress.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    textView.setText(json);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ServiceHandler serviceHandler = new ServiceHandler();
            json = serviceHandler.makeServiceCall("http://private-5543d-smartvelov.apiary-mock.com/route", ServiceHandler.GET);

            return null;
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.get){
            new getRouteAsyncTask().execute();
        }
    }
}
