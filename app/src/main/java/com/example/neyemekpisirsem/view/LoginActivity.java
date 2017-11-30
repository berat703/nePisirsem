package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Pınar Köroğlu on 24.11.2017.
 */

public class LoginActivity extends Activity {
    Button bt_login;
    Button bt_cancel;
    Button bt_forgot;
    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUser;

    EditText lEmail;
    EditText lPass;
    private ProgressBar mProgressBar;

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
        setContentView(R.layout.activity_login);
        bt_login = (Button) findViewById(R.id.loginButton);
        bt_cancel = (Button) findViewById(R.id.cancel);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);




        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://neyemekpisirsem.azurewebsites.net",
                    this)/*.withFilter(new LoginActivity.ProgressFilter())*/;

            // Get the Mobile Service Table instance to use
            mUser = mClient.getTable(Users.class);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lEmail = (EditText) findViewById(R.id.t_username);
        lPass = (EditText) findViewById(R.id.t_password);



        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fp = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(fp);
            }
        });
    }

    public void add(View view) {

        if (lEmail.getText().toString().trim().equals("")) {
            lEmail.setError("Email is required!");
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            return;
            //You can Toast a message here that the Username is Empty
        }

        if (lPass.getText().toString().trim().equals("")) {
            lPass.setError("Password is required!");
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            return;
            //You can Toast a message here that the Username is Empty
        }


        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        final String mail;
        final String pwd;

        mail = lEmail.getText().toString();
        pwd = lPass.getText().toString();


        new AsyncTask<Void, Void, Void>() {

            Boolean validation = false;

            @Override
            protected Void doInBackground(Void... params) {
                Log.d("try", "do in background");
                try {
                    final MobileServiceList<Users> result =
                        //    mUser.select("email").execute().get();
                            mUser.where().field("email").eq(mail).and(mUser.where().field("password").eq(pwd)).execute().get();
                             Log.d("tag","asd"+result);


                    Log.d("try", "got the result");


                    for (Users item : result) {

                        validation = item.getPassword().toString().equalsIgnoreCase(pwd);


                        Log.d("try", "" + item);


                        User_id = item.getId().toString();  // you can get columns data like this


                        runOnUiThread(new Runnable() {

                            public void run() {


                                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                Intent MainActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(MainActivity);


                            }


                        });


                    }


                    if (validation == false) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), "Email or Password is Wrong", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
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


    //   @Override
  /*  public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();



            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);


            return resultFuture;
        }
    }

}




