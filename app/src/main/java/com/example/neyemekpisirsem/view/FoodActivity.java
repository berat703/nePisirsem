package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private ProgressDialog mProgressBar;

    Button tarif;
    TextView content;
    ImageView image;
    private MobileServiceTable<Foods> foodTable;
    private MobileServiceList<Foods> tag;
    private MobileServiceClient mClient;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        degistir = (Button) findViewById(R.id.degistirButton);
        tarif= (Button) findViewById(R.id.tarifButton);
        content = (TextView) findViewById(R.id.foodContent);
        image = (ImageView) findViewById(R.id.foodImage);
        mProgressBar=new ProgressDialog(this);
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
            mProgressBar.setMessage("Bilgileriniz kontrol ediliyor.");
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.show();
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
                                mProgressBar.cancel();

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

    public void EndOfList(){
       try{
           AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
           dlgAlert.setMessage("Yemeklerin sonuna geldik.Farklı yemekler aramaya ne dersin?");
           dlgAlert.setTitle("Bunu söylemeye çekiniyoruz ama...");
           dlgAlert.setPositiveButton("OK", null);
           dlgAlert.setCancelable(true);
           dlgAlert.create().show();
           dlgAlert.setPositiveButton("OK",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                           startActivity(i);
                       }
                   });
       }catch(Exception e){
           Log.d("ad","ads...........:"+e);
       }

    }

    public void randomYemekGetir(View view) {

        final int zaman=100;
        mProgressBar.setMessage("Yemekler aranıyor...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        final Thread t=new Thread(){
            @Override
            public void run(){
                int ilerleme=0;
                while(ilerleme<zaman){
                    try{
                        Thread.sleep(400);
                        ilerleme=ilerleme+100;
                        mProgressBar.setProgress(ilerleme);
                    }catch (InterruptedException e){
                        e.printStackTrace();

                    }

                    if(rand_list.size()==tag.getTotalCount()){
                        mProgressBar.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EndOfList();
                            }
                        });
                    }
                    else{
                        while(!find_element(rand_deger)){
                            rand_deger = rand.nextInt(tag.getTotalCount());
                        }

                        if(find_element(rand_deger))
                        {
                            changeText(tag.get(rand_deger).getName());
                            rand_list.add(rand_deger);
                        }


                        mProgressBar.cancel();
                    }



                }
            }

        };
        t.start();
        Log.d("asd","asd");

    }
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
}
