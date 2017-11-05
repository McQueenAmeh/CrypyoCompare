package com.example.quin.crypyocompare;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class bAdapter extends BaseAdapter {

    private Context activity;
    private ArrayList<String> name,curren, value, nName, curen, nameName, currenCurren;

    private Dialog mDialog1, mDialog2;

    bAdapter(Context a, ArrayList<String> b, ArrayList<String> c, ArrayList<String> d,ArrayList<String> e,ArrayList<String> f,ArrayList<String> g
             ,ArrayList<String> h){
        activity = a;
        this.name = b;
        this.curren = c;
        this.value = d;
        this.nName = e;
        this.curen = f;
        this.nameName = g;
        this.currenCurren = h;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list,null);
        }

        final TextView bitCoin = (TextView) convertView.findViewById(R.id.bitCoin);
        final TextView currency = (TextView) convertView.findViewById(R.id.currency);
        final TextView currencyValue = (TextView) convertView.findViewById(R.id.currencyValue);
        final CardView cardView = (CardView) convertView.findViewById(R.id.cardView);
        final RelativeLayout rL = (RelativeLayout) convertView.findViewById(R.id.rL);

        bitCoin.setText( name.get(position));
        currency.setText(curren.get(position));
        currencyValue.setText(value.get(position));

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setTitle("Delete");
                adb.setMessage("Are you sure you want to delete?");
                adb.setNegativeButton("NO",null);
                adb.setPositiveButton("YES", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rL.removeView(cardView);
                        name.remove(position);
                        value.remove(position);
                        curren.remove(position);
                        nName.remove(position);
                        curen.remove(position);
                        nameName.remove(position);
                        currenCurren.remove(position);

                    }
                });
                adb.show();

                return false;
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDialog1 = new Dialog(activity);
                mDialog2 = new Dialog(activity);
                mDialog1.setContentView(R.layout.popup);
                mDialog2.setContentView(R.layout.popup2);

                TextView textView2 = (TextView) mDialog1.findViewById(R.id.textView2);
                TextView textView3 = (TextView) mDialog1.findViewById(R.id.textView3);
                TextView textView4 = (TextView) mDialog1.findViewById(R.id.textView4);
                final EditText cryptoEditText = (EditText) mDialog1.findViewById(R.id.cryptoEditText);
                final TextView currencyEditText = (TextView) mDialog1.findViewById(R.id.currencyEditText);
                Button button2 = (Button) mDialog1.findViewById(R.id.button2);

                TextView stextView2 = (TextView) mDialog2.findViewById(R.id.textView2);
                TextView stextView3 = (TextView) mDialog2.findViewById(R.id.textView3);
                TextView stextView4 = (TextView) mDialog2.findViewById(R.id.textView4);
                final TextView scryptoEditText = (TextView) mDialog2.findViewById(R.id.cryptoEditText);
                final EditText scurrencyEditText = (EditText) mDialog2.findViewById(R.id.currencyEditText);
                Button sbutton2 = (Button) mDialog2.findViewById(R.id.button2);

                String initial = "1";
                final String currenInitial = currencyValue.getText().toString();

                final double cInit = Double.parseDouble(currenInitial);
                final double init = Double.parseDouble(initial);

                if (bitCoin.getText().toString().equals(" ")){
                    textView3.setText("1");
                    textView4.setText("1");

                }else {
                    textView3.setText(bitCoin.getText().toString());
                    stextView3.setText(bitCoin.getText().toString());
                }

                textView4.setText(currency.getText().toString());
                stextView4.setText(currency.getText().toString());

                cryptoEditText.setText(initial);
                currencyEditText.setText(currenInitial);
                scryptoEditText.setText(initial);
                scurrencyEditText.setText(currenInitial);

                button2.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        if(bitCoin.getText().toString().equals("BitCoin")){
                            currencyValue.setText(currencyEditText.getText().toString());
                        }else{
                            currencyValue.setText(currencyEditText.getText().toString());
                        }

                        mDialog1.dismiss();
                    }
                });
                sbutton2.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        if(bitCoin.getText().toString().equals("BitCoin")){
                            currencyValue.setText(scurrencyEditText.getText().toString());
                        }else{
                            currencyValue.setText(scurrencyEditText.getText().toString());
                        }

                        mDialog2.dismiss();
                    }
                });

                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog1.dismiss();
                        mDialog2.show();
                    }
                });
                stextView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog2.dismiss();
                        mDialog1.show();
                    }
                });

                cryptoEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        double stuff = 0;
                        if (s.toString().isEmpty() || s.toString().equals(".")|| s.toString().equals("")){
                            stuff = 0.;
                        }else{
                            try{
                                stuff = cInit * Double.parseDouble(s.toString());
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }

                        }
                        currencyEditText.setText(Double.toString(stuff));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                scurrencyEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        double stuff;
                        if (s.toString().isEmpty() || s.toString().equals(".")|| s.toString().equals("")){
                            stuff = 0.;
                        }else{
                            stuff = (init*Double.parseDouble(s.toString())) / cInit;
                        }
                        scryptoEditText.setText(Double.toString(stuff));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mDialog1.show();

            }
        });



        return convertView;
    }

}