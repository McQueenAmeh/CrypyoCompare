package com.example.quin.crypyocompare;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import static android.widget.Toast.LENGTH_SHORT;

public class home_page extends Activity implements AdapterView.OnItemSelectedListener{

    bAdapter bAdapter;
    ListView listView;
    public  Spinner cryptoSpinner;
    public Spinner currencySpinner;
    ProgressDialog progressDialog;
    private  ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> curren = new ArrayList<>();

    private ArrayList<String> nameName = new ArrayList<>();
    private ArrayList<String> currenCurren = new ArrayList<>();

    private ArrayList<String> nName = new ArrayList<>();
    private ArrayList<String> curen = new ArrayList<>();

    int coin;
    int country;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean doubleTapBack = false;
    Dialog mDialog;
    String cont;
    String con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listView = (ListView) findViewById(R.id.listView);
        mDialog = new Dialog(this);


        final Bundle myBundle = getIntent().getExtras();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        Toast.makeText(this, "Long Click To Delete", Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                name.clear();value.clear();curren.clear();

                refresh();
            }
        });

        name = myBundle.getStringArrayList("name");
        curren = myBundle.getStringArrayList("currency");
        value = myBundle.getStringArrayList("value");

        nName.addAll(name);
        curen.addAll(curren);

        nameName.add("BTC");nameName.add("ETH");
        currenCurren.add("NGN");currenCurren.add("NGN");

        bAdapter = new bAdapter(home_page.this, name, curren,value, nName,curen,nameName,currenCurren);
        listView.setAdapter(bAdapter);
    }

    @Override
    public void onBackPressed() {
        if (doubleTapBack){
            super.onBackPressed();
            return;
        }
        this.doubleTapBack = true;
        Toast.makeText(this, "Please click Back again to exit", LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapBack = false;
            }
        }, 2000);


    }

    public void showDialog(View view) {

        mDialog.setContentView(R.layout.popup_view);
        cryptoSpinner = (Spinner) mDialog.findViewById(R.id.cryptoSpinner);
        currencySpinner = (Spinner) mDialog.findViewById(R.id.currencySpinner);
        Button button = (Button) mDialog.findViewById(R.id.button2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crypto,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.currency,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cryptoSpinner.setAdapter(adapter);
        cryptoSpinner.setOnItemSelectedListener(this);

        currencySpinner.setAdapter(adapt);
        currencySpinner.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coin = cryptoSpinner.getSelectedItemPosition();
                country = currencySpinner.getSelectedItemPosition();

                String[] ISO = getResources().getStringArray(R.array.ISO);
                String[] cryptoISO = getResources().getStringArray(R.array.cryptoIso);
                cont = ISO[country];
                con = cryptoISO[coin];

                mDialog.dismiss();
                new BackGround(home_page.this, con, cont).execute();
                progressDialog = ProgressDialog.show(home_page.this, "Loading", "Refreshing Data");

                Thread timer = new Thread(){
                    public void run(){
                        try {

                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            progressDialog.dismiss();
                        }
                    }
                }; timer.start();

                listView.setAdapter(bAdapter);


            }
        });
        mDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void refresh(){
        new Thread(new Runnable() {
            String line = "";
            String data = "";
            String price="";
            int i = 0;

            @Override
            public void run() {

                do {

                    try {

                        URL url = new URL("https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" +
                                "ETH,BTC" + "&tsyms=" +"DZD,BIF,XAF,XOF,EGP,ETB,GHS,KES,AOA,LSL,MUR,MAD,NGN,NAD,BWP,RWF,ZAR,TZS,UGX,ZMW");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        while (line != null) {
                            line = bufferedReader.readLine();
                            data += line;
                        }
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("RAW");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject(nameName.get(i));
                        JSONObject jsonObject3 = jsonObject2.getJSONObject(currenCurren.get(i));
                        price = jsonObject3.getString("PRICE");

                        value.add(price);
                        name.add(nName.get(i));
                        curren.add(curen.get(i));


                    }catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }i++;
                } while(i<nameName.size());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public class BackGround extends AsyncTask<String,Integer,String> {

        private Context context;
        private home_page activity;
        private String spinnerName, spinnerCurrency;
        private String naira;
        private String bitName;
        private String data = "";
        String price="";

        BackGround(home_page activity, String name, String currency) {
            super();

            this.activity = activity;
            this.spinnerName = name;
            this.spinnerCurrency = currency;
            this.context = this.activity.getApplicationContext();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" + spinnerName + "&tsyms=" +spinnerCurrency);
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
                JSONObject jsonObject2 = jsonObject1.getJSONObject(spinnerName);
                JSONObject jsonObject3 = jsonObject2.getJSONObject(spinnerCurrency);

                price = jsonObject3.getString("PRICE");
                naira = currencySpinner.getSelectedItem().toString();
                bitName = cryptoSpinner.getSelectedItem().toString();

            } catch ( JSONException e) {
                e.printStackTrace();
            }

            name.add(bitName);
            nName.add(bitName);
            curen.add(naira);
            curren.add(naira);
            value.add(price);
            currenCurren.add(cont);
            nameName.add(con);

            bAdapter.notifyDataSetChanged();
        }
    }
}