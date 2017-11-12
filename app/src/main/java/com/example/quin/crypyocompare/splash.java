package com.example.quin.crypyocompare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class splash extends AppCompatActivity {

    public ProgressBar progressBar;
    private Intent i;
    private String data = "";
    private int stuff = 0;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ImageView textView = (ImageView) findViewById(R.id.textView);
        i = new Intent(this, home_page.class);

        Animation transit = AnimationUtils.loadAnimation(this, R.anim.transit);

        progressBar.startAnimation(transit);
        textView.startAnimation(transit);
        new BackGround(this).execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (stuff == 1){
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        i.putExtra("value",value);

                        startActivity(i);
                        finish();
                    }}
            }; timer.start();

        }else{
            value = "";

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setMessage("Problem connecting to server.");
            adb.setNegativeButton("Exit", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    splash.this.finish();
                    Process.killProcess(Process.myPid());
                }
            });
            adb.setPositiveButton("Try Again", new AlertDialog.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new BackGround(splash.this).execute();
                }
            });
            adb.setCancelable(false);
            adb.show();

        }

    }

    public class BackGround extends AsyncTask<String,Integer,String> {

        private Context context;
        private splash activity;


        BackGround(splash activity) {
            super();
            this.activity = activity;
            this.context = this.activity.getApplicationContext();
        }

        @Override
        protected String doInBackground(String... params) {

                try {
                    URL url = new URL("https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC,ETH&tsyms=NGN");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data += line;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonObject1 = jsonObject.getJSONObject("RAW");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("BTC");
                JSONObject jsonObject3 = jsonObject2.getJSONObject("NGN");

                value = jsonObject3.getString("PRICE");


                if (!value.isEmpty())
                    stuff = 1;
                else {
                    stuff = 2;
                }

            } catch ( JSONException e) {
                e.printStackTrace();
            }

            onRestart();
        }

    }
}
