package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Reklamotv on 8.12.2017.
 */

public class FoodActivity extends Activity {

    Button degistir;
    Button tarif;
    TextView content;
    ImageView image;
    private MobileServiceTable<Foods> foodTable;

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
                                foodTable.where().field("name").eq(value).execute().get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content.setText(result.get(0).getContent());
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
}
