package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.Foods;
import com.example.neyemekpisirsem.model.Users;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;


public class FoodActivity extends Activity {

    List <Integer> rand_list = new ArrayList();
    int sayac=0;
    int rand_deger=0;
    Random rand = new Random();
    Button degistir;
    Button tarif;
    TextView content;
    ImageView image;
    private MobileServiceTable<Foods> foodTable;
    private MobileServiceList<Foods> tag;
    private ProgressDialog mProgressBar_;
    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        degistir = (Button) findViewById(R.id.degistirButton);
        tarif= (Button) findViewById(R.id.tarifButton);
        content = (TextView) findViewById(R.id.foodContent);
        image = (ImageView) findViewById(R.id.foodImage);
        mProgressBar_=new ProgressDialog(this);
        try {

            mClient = new MobileServiceClient(
                    "https://neyemekpisirsem.azurewebsites.net",
                    this)/*.withFilter(new com.example.neyemekpisirsem.view.RegisterActivity.ProgressFilter())*/;

            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            foodTable = mClient.getTable(Foods.class);


        }catch(Exception e){
            Log.e("Error...:","Hata"+e);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           final String value = extras.getString("deger");

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        final MobileServiceList<Foods> result =
                                //    mUser.select("email").execute().get();
                                foodTable.where().field("tag_name").eq(value).execute().get();
                        tag=result;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("berat","berat"+rand_list.contains(1));
                                rand_deger = rand.nextInt(result.getTotalCount());
                                if(find_element(rand_deger)){
                                    rand_list.add(rand_deger);
                                }
                                content.setText(result.get(rand_deger).getContent());
                            }
                        });

                    } catch (Exception e) {
                        Log.d("hata", "" + e);
                    }

                    return null;

                }

            }.execute();

        }

    }

    public boolean find_element (int deger) {
        boolean x=true;
        for (Integer number : rand_list) {
            if (number != deger) {
                x=true;

            }
            else{
                x=false;
                break;
            }
        }
        return x;
    }


/**
 * Son kalınan yer: int dizi elemanları 0 olarak atandıgı için index 0 da sorun olusturuyor
 *
 *
 * **/


    public void changeText(final String data){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content.setText(data);
                            }
                        });
                }catch(Exception e){
                    Log.d("asd","asd"+e);
                }
            }
        };

        t.start();
    }

    public void randomYemekGetir(View view) {
        final int zaman=100;
        mProgressBar_.setMessage("Yemekler aranıyor...");
        mProgressBar_.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar_.show();
        final Thread t=new Thread(){
            @Override
            public void run(){
                int ilerleme=0;
                while(ilerleme<=zaman){
                    try{
                        Thread.sleep(400);
                        ilerleme=ilerleme+50;
                        mProgressBar_.setProgress(ilerleme);

                    }catch (InterruptedException e){
                        e.printStackTrace();

                    } rand_deger = rand.nextInt(tag.getTotalCount());
                    if(find_element(rand_deger))
                    {
                        rand_list.add(rand_deger);
                        changeText(tag.get(rand_deger).getName());
                    }}
                    mProgressBar_.cancel();
            }

        };
        t.start();
        Log.d("asd","asd");

    }
}
