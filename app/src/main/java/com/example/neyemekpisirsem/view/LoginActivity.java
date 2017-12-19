package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.Users;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

import static android.os.SystemClock.sleep;

/**
 * Created by Pınar Köroğlu on 24.11.2017.
 */

public class LoginActivity extends Activity {
    Button bt_login;
    ImageView image;
    Button bt_forgot;
    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUser;

    EditText userName;
    EditText lPass;
    private ProgressDialog mProgressBar;
    Boolean validation = false;

    String lt;
    String lg;
    int days;

    String ver;


    String User_id;

    String mypass;
    SharedPreferences myaccount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nepisirsem_login);
        bt_login = (Button) findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.userName);
        lPass = (EditText) findViewById(R.id.userPass);
        mProgressBar=new ProgressDialog(this);


        try {

            mClient = new MobileServiceClient(
                    "https://neyemekpisirsem.azurewebsites.net",
                    this);


            mUser = mClient.getTable(Users.class);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void add(View view) {

        if (userName.getText().toString().trim().equals("")) {
            userName.setError("Email boş bırakılamaz!");
            return;
        }

        if (lPass.getText().toString().trim().equals("")) {
            lPass.setError("Parola boş bırakılamaz!");
            return;
        }

        final String username;
        final String pwd;

        username = userName.getText().toString();
        pwd = lPass.getText().toString();
        mProgressBar.setMessage("Bilgileriniz kontrol ediliyor.");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        if(!validation){
            new AsyncTask<Void, Void, Void>() {


                @Override
                protected Void doInBackground(Void... params) {
                    Log.d("try", "do in background");
                    try {
                        final MobileServiceList<Users> result =
                                mUser.where().field("username").eq(username).and(mUser.where().field("password").eq(pwd)).execute().get();
                        Log.d("tag","asd"+result);

                        if(result.size()!=0){
                            validation=true;
                        }
                        if(validation==true){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(validation==true){
                                        mProgressBar.cancel();

                                        Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                                        startActivity(search);
                                    }

                                }
                            });
                        }
                        if (validation == false) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Email yada Parola Yanlış!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                }
                            });
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();

                        Log.d("Error", "catching the error");
                    }
                    return null;
                }

            }.execute();
        }
        else{

                mProgressBar.cancel();
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);


        }


    }

    public void getRegister(View view) {
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
    }
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

}




