package com.example.neyemekpisirsem.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.Foods;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TarifActivity extends AppCompatActivity {
    private static Context ctx;
    private MobileServiceTable<Foods> foodTable;
    private MobileServiceList<Foods> _content;
    private MobileServiceClient mClient;
    TextView description;
    TextView content2;
    TextView title;
    int rand_deger=0;
    private static ImageView image;
    Random rand = new Random();
    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nepisirsem_tarif);
        ctx = this.getApplicationContext();
        content2=(TextView)findViewById(R.id.content);
        title=(TextView)findViewById(R.id.titleText);
        image=(ImageView)findViewById(R.id.imageContent);
        description=(TextView)findViewById(R.id.description);
        try {

            mClient = new MobileServiceClient(
                    "https://neyemekpisirsem.azurewebsites.net",
                    this);

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
        final String value = extras.getString("name");

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    final MobileServiceList<Foods> result =
                            //    mUser.select("email").execute().get();
                     foodTable.where().field("name").eq(value).execute().get();
                    _content=result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rand_deger = rand.nextInt(result.getTotalCount());
                           title.setText(result.get(rand_deger).getName());
                           content2.setText(result.get(rand_deger).getContent());
                           description.setText(result.get(rand_deger).getDescription());
                            LoadImageFromWebOperations(result.get(rand_deger).getPhoto());
                        }
                    });

                } catch (Exception e) {
                    Log.d("hata", "" + e);
                }

                return null;

            }

        }.execute();


    }

    public static void LoadImageFromWebOperations(String url) {

        Picasso.with(ctx.getApplicationContext()).load(url).placeholder(R.mipmap.ic_balik)
                .error(R.mipmap.ic_balik)
                .resize(500,500)
                .into(image,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {
                        Log.d("tag","BASARILI");
                    }

                    @Override
                    public void onError() {
                        Log.d("tag","BASARISIZ");

                    }
                });

    }


    public void onceki(View view) {
        Intent i = new Intent(getApplicationContext(),FoodActivity.class);
        startActivity(i);
    }
}
