package com.example.quin.crypyocompare;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
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

        mDialog1 = new Dialog(activity);
        mDialog1.setContentView(R.layout.popup);
        mDialog2 = new Dialog(activity);
        mDialog2.setContentView(R.layout.popup2);

        /* Two dialogs,
        One for Crypto to Currency
        and the other from currency to crypto
         */

        TextView crptoCurrencyTextView = (TextView) mDialog1.findViewById(R.id.cryptoCurrencyTextView);
        EditText cryptoEditText= (EditText) mDialog1.findViewById(R.id.cryptoEditText);
        TextView currencyTextView = (TextView) mDialog1.findViewById(R.id.currencyTextView);
        final TextView curreCurrencyTextView = (TextView) mDialog1.findViewById(R.id.curreCurrencyTextView);
        TextView switchTextView = (TextView) mDialog1.findViewById(R.id.switchTextView);

        TextView stextView2 = (TextView) mDialog2.findViewById(R.id.switchTextView);
        TextView stextView3 = (TextView) mDialog2.findViewById(R.id.cryptoCurrencyTextView);
        TextView stextView4 = (TextView) mDialog2.findViewById(R.id.currencyTextView);
        final TextView scryptoEditText = (TextView) mDialog2.findViewById(R.id.cryptoEditText);
        final EditText scurrencyEditText = (EditText) mDialog2.findViewById(R.id.curreCurrencyTextView);

        bitCoin.setText( name.get(position));
        currency.setText(curren.get(position));
        currencyValue.setText(value.get(position));


        crptoCurrencyTextView.setText(bitCoin.getText().toString());
        currencyTextView.setText(currency.getText().toString());
        curreCurrencyTextView.setText(currencyValue.getText().toString());

        stextView3.setText(bitCoin.getText().toString());
        stextView4.setText(currency.getText().toString());

        String initial = "1";
        final String currenInitial = currencyValue.getText().toString();

        final double cInit = Double.parseDouble(currenInitial);
        final double init = Double.parseDouble(initial);

        cryptoEditText.setText(initial);
        curreCurrencyTextView.setText(currenInitial);

        scryptoEditText.setText(initial);
        scurrencyEditText.setText(currenInitial);


        switchTextView.setOnClickListener(new View.OnClickListener() {
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
                curreCurrencyTextView.setText(Double.toString(stuff));
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


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(activity, cardView);
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Delete")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("Delete");
                            adb.setMessage("Are you sure you want to delete?");
                            adb.setNegativeButton("NO", null);
                            adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    name.remove(position);
                                    curren.remove(position);
                                    value.remove(position);
                                    nName.remove(position);
                                    curen.remove(position);
                                    nameName.remove(position);
                                    currenCurren.remove(position);

                                    bAdapter.this.notifyDataSetChanged();
                                }
                            });
                            adb.show();
                            popup.dismiss();
                        }
                        else if (item.getTitle().equals("Crypto to Currency")){
                            mDialog1.show();
                            popup.dismiss();
                        }

                        else{
                            mDialog2.show();
                            popup.dismiss();
                        }

                        return true;
                    }
                });

                popup.show();
            }

        });

        return convertView;
    }

}