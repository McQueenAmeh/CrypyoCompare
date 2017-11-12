package com.example.quin.crypyocompare;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class home_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    bAdapter bAdapter;
    ListView listView;

    public  Spinner cryptoSpinner;
    public Spinner currencySpinner;
    ProgressDialog progressDialog;

    /* These Arraylists are for two main things
    1. To store the value of the data.
    2. To ensure that when data is being refreshed it doesn't iterate into another item or position
     */
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
    Boolean doubleTapBack = false; // for on backPress

    Dialog mDialog;
    String cont;
    String con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listView = (ListView) findViewById(R.id.listView);
        mDialog = new Dialog(this);

        Animation blinking_anim = AnimationUtils.loadAnimation(this, R.anim.blinking_anim);

        listView.startAnimation(blinking_anim);

        Bundle bundle = getIntent().getExtras();
        name.add("BitCoin");
        value.add(bundle.getString("value"));
        curren.add("Nigerian Naira");
        nameName.add("BTC");
        currenCurren.add("NGN");
        nName.add("BitCoin");
        curen.add("Nigerian Naira");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.black), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Swipe down to refresh
                swipeRefreshLayout.setRefreshing(true);

                name.clear();value.clear();curren.clear();
                refresh();
            }
        });



        bAdapter = new bAdapter(home_page.this, name, curren,value, nName,curen,nameName,currenCurren);

        listView.setAdapter(bAdapter);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBackPressed() {
        if (doubleTapBack){
            super.onBackPressed();
            return;
        }
        this.doubleTapBack = true;
        Toast.makeText(this, "Click again to exit", LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapBack = false;
            }
        }, 2000);

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
                        //Do in background -- Get data and parse it.
                        URL url = new URL("https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" +
                                "ETH,BTC" + "&tsyms=" +"DZD,BIF,XAF,XOF,EGP,ETB,GHS,KES,AOA,LSL,MUR,MAD,NGN,NAD,BWP,RWF,ZAR,TZS,UGX,ZMW");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        while (line != null) {
                            line = bufferedReader.readLine();
                            data += line;
                        }

                        if (!data.isEmpty()){

                            JSONObject jsonObject = new JSONObject(data);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("RAW");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(nameName.get(i));
                            JSONObject jsonObject3 = jsonObject2.getJSONObject(currenCurren.get(i));
                            price = jsonObject3.getString("PRICE");

                            value.add(price);
                            name.add(nName.get(i));
                            curren.add(curen.get(i));
                        }

                    }catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }i++;
                } while(i<nameName.size());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //tell listView that something has changed
                        bAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void showDialog(View view) {

        //Add new

        mDialog.setContentView(R.layout.popup_view);
        cryptoSpinner = (Spinner) mDialog.findViewById(R.id.cryptoSpinner);
        currencySpinner = (Spinner) mDialog.findViewById(R.id.currencySpinner);
        Button button = (Button) mDialog.findViewById(R.id.button2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crypto,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cryptoSpinner.setAdapter(adapter);
        cryptoSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.currency,android.R.layout.simple_spinner_item);

        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currencySpinner.setAdapter(adapt);

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

                listView.setAdapter(bAdapter);
            }
        });
        mDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //for Overflow menu
        Intent intent;
        switch (item.getItemId()) {
            case R.id.overflowAbout:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);

                intent = new Intent(home_page.this, About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(activity, "Loading", "Retrieving Data");

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

        @SuppressLint("CommitPrefEdits")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!data.isEmpty()){
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

                progressDialog.dismiss();

                bAdapter.notifyDataSetChanged();
            }else {
                final AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setMessage("Problem connecting to server.");
                adb.setNegativeButton("Cancel", new AlertDialog.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                    }
                });
                adb.setPositiveButton("Try Again", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                        new BackGround(activity, con, cont).execute();
                    }
                });
                adb.setCancelable(false);
                adb.show();
            }


        }
    }
}