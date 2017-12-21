package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;


public class FoodActivity extends Activity {

    private static Context ctx;
    List <Integer> rand_list = new ArrayList();
    int sayac=0;
    int rand_deger=0;
    Random rand = new Random();
    Button degistir;
    private ProgressDialog mProgressBar;
    Button tarif;
    TextView content;
    TextView malzeme_data;
   private static  ImageView image;
    private MobileServiceTable<Foods> foodTable;
    private MobileServiceList<Foods> tag;
    private MobileServiceClient mClient;
    URL url;

//    public FoodActivity(Context con){
//        super();
//        this.ctx = con;
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nepisirsem_food);
        ctx = this.getApplicationContext();
        degistir = (Button) findViewById(R.id.degistirButton);
        tarif= (Button) findViewById(R.id.tarifButton);
        content = (TextView) findViewById(R.id.foodContent);
        malzeme_data = (TextView) findViewById(R.id.malzeme);
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
            mProgressBar.setMessage("Yemekler araniyor.");
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

                        if (result.size() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialogNotFound("Aradığınız içeriği bulamadık.Bunları denemeye ne dersin?   patlıcan,bezelye,pirzola","Uppss!");
                                }
                            });
                        }
                    else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rand_deger = rand.nextInt(result.getTotalCount());
                                    if(find_element(rand_deger)){
                                        rand_list.add(rand_deger);
                                    }
                                    changeText(result.get(rand_deger).getName(),result.get(rand_deger).getDescription());
                                    LoadImageFromWebOperations(result.get(rand_deger).getPhoto());

                                }
                            });
                        }



                    } catch (Exception e) {
                        Log.d("hata", "" + e);
                    }

                    return null;

                }

            }.execute();
            mProgressBar.cancel();

        }

    }

    public static Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(Color.WHITE)
            .borderWidthDp(3)
            .cornerRadiusDp(30)
            .oval(true)
            .build();

    public static void LoadImageFromWebOperations(final String url) {
        try{

            Picasso.with(ctx.getApplicationContext()).load(url).placeholder(R.drawable.loading)
                    .error(R.color.white)
                    .transform(transformation)
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
            image.setBackground(null);
        }catch(Exception e){
            Log.d("asd","sadf"+e);
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

    public class CircleTransform implements Transformation {
        Context cont = ctx.getApplicationContext();

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


    public void changeText(final String data,final String malzeme){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                malzeme_data.setText(malzeme);
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
           final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
           dlgAlert.setMessage("Yemeklerin sonuna geldik.Farklı yemekler aramaya ne dersin?");
           dlgAlert.setTitle("Bunu söylemeye çekiniyoruz ama...");
           dlgAlert.setPositiveButton("OK", null);
           dlgAlert.setCancelable(true);
           dlgAlert.setPositiveButton("Tabii",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           finish();
                       }
                   });

           dlgAlert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.cancel();
               }
           });
           dlgAlert.create().show();

       }catch(Exception e){
           Log.d("ad","ads...........:"+e);
       }

    }

    public void randomYemekGetir(View view) {
        mProgressBar.setMessage("Yemekler aranıyor...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.show();


                    if(rand_list.size()==tag.getTotalCount()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EndOfList();
                            }
                        });
                    }
                    else{

                        image.setBackground(getResources().getDrawable(R.drawable.loading));
                        image.setImageDrawable(getResources().getDrawable(R.drawable.loading));
                        while(!find_element(rand_deger)){
                            rand_deger = rand.nextInt(tag.getTotalCount());
                        }

                        if(find_element(rand_deger))
                        {
                            changeText(tag.get(rand_deger).getName(),tag.get(rand_deger).getDescription());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadImageFromWebOperations(tag.get(rand_deger).getPhoto());
                                }
                            });
                            rand_list.add(rand_deger);
                        }


                    }
        image.setBackground(null);
        mProgressBar.cancel();
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void dialogNotFound(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Tabii ki!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();

    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }


    public void getRecipe(View view){
        Intent i = new Intent(getApplicationContext(), TarifActivity.class);
        i.putExtra("name", content.getText());
        startActivity(i);
    }

}
