package com.example.neyemekpisirsem.view;

import android.app.Activity;
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
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class FoodActivity extends Activity {

    int rand_deger=0;
    Random rand = new Random();
    Button degistir;
    Button tarif;
    TextView content;
    ImageView image;
    private MobileServiceTable<Foods> foodTable;
    private MobileServiceList<Foods> tag;

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        degistir = (Button) findViewById(R.id.degistirButton);
        tarif= (Button) findViewById(R.id.tarifButton);
        content = (TextView) findViewById(R.id.foodContent);
        image = (ImageView) findViewById(R.id.foodImage);

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
                                Random rand = new Random();
                                int rand_deger = rand.nextInt(result.getTotalCount());
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
        Log.d("asd","asd");
        rand_deger = rand.nextInt(tag.getTotalCount());
       // content.setText(tag.get(rand_deger).getContent());
        changeText(tag.get(rand_deger).getName());

        /**finish();
        startActivity(getIntent());**/
    }
}
