package com.example.quin.crypyocompare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class splash extends AppCompatActivity {

    public ProgressBar progressBar;
    private Intent i;
    private String data = "";
    private int stuff = 0;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> currency = new ArrayList<>();
    private ArrayList<String> nameName = new ArrayList<>();
    private ArrayList<String> currenCurren = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        i = new Intent(this, home_page.class);

        Animation transit = AnimationUtils.loadAnimation(this, R.anim.transit);
        progressBar.startAnimation(transit);
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
                        startActivity(i);
                        finish();
                    }}
            }; timer.start();
        }else{
            name.clear();value.clear();currency.clear();
            new BackGround(this).execute();

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
                    if (data.isEmpty() || data.equals("")){
                        Toast.makeText(activity,"Network Proble", Toast.LENGTH_SHORT).show();
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
                JSONObject jsonObject4 = jsonObject1.getJSONObject("ETH");
                JSONObject jsonObject5 = jsonObject4.getJSONObject("NGN");

                String price = jsonObject3.getString("PRICE");
                String naira = "Naira";

                if (!price.isEmpty())
                    stuff = 1;
                else {
                    stuff = 2;
                }

                String price1 = jsonObject5.getString("PRICE");


                name.add("BitCoin");name.add("Etherium");
                nameName.add("BTC");nameName.add("ETH");
                currenCurren.add("NGN");currenCurren.add("NGN");
                currency.add(naira);currency.add(naira);
                value.add(price);value.add(price1);


            } catch ( JSONException e) {
                e.printStackTrace();
            }

            i.putStringArrayListExtra("name", name);
            i.putStringArrayListExtra("currency", currency);
            i.putStringArrayListExtra("value", value);
            i.putStringArrayListExtra("bName", nameName);
            i.putStringArrayListExtra("cCurren", currenCurren);

            onRestart();

        }

    }
}
