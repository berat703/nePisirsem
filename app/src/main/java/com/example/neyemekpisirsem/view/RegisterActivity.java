package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.Users;
import com.example.neyemekpisirsem.presenter.login_presenter;
import com.example.neyemekpisirsem.presenter.register_presenter;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;


public class RegisterActivity extends Activity {

    private MobileServiceClient mClient;

    private register_presenter mPresenter;

    private MobileServiceTable<Users> userTable;  //modelden

    private MobileServiceAuthenticationProvider auth;

    private login_presenter loginPresenter;

    private ProgressBar mProgressBar;
    EditText username;
    EditText password;
    EditText email;
    EditText name;
    EditText pass2;
    Button register;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nepisirsem_register);
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

            userTable = mClient.getTable(Users.class);
            initLocalStore().get();
            username=(EditText)findViewById(R.id.userName);
            pass2=(EditText)findViewById(R.id.userPass2);
            password=(EditText)findViewById(R.id.userPass);
            email=(EditText)findViewById(R.id.userEmail);

            register=(Button)findViewById(R.id.registerButton);
            name=(EditText)findViewById(R.id.userAd);


        }

        catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }

    }





    public void checkItemInTable(Users item) throws ExecutionException, InterruptedException {
        userTable.update(item).get();
    }

    public Users addItemInTable(Users item) throws ExecutionException, InterruptedException {
       Users entity = userTable.insert(item).get();
            return entity;

    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == R.id.menu_refresh) {
        }
        return true;
    }

    public void checkItem(final Users item) {
        if (mClient == null) {
            return;
        }



        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //checkItemInTable(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("asd","asd");
                        }
                    });
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }

    public void addItem(View view) {
        if (mClient == null) {
            return;
        }
        final Users item = new Users();
      String passw=password.getText().toString();
      String passw2=pass2.getText().toString();
        item.setUsername(username.getText().toString());
        item.setPassword(password.getText().toString());
        item.setEmail(email.getText().toString());
        item.setAuthor(false);
        item.setName(name.getText().toString());

        if(item.getUsername().matches("")){
            Toast.makeText(this, "Kullanıcı Adı boş bırakılamaz!", Toast.LENGTH_SHORT).show();

        }
        if(item.getPassword().matches("")){
            Toast.makeText(this, "Parola boş bırakılamaz!", Toast.LENGTH_SHORT).show();

        }
        if(item.getEmail().matches("")){
            Toast.makeText(this, "E-mail boş bırakılamaz!", Toast.LENGTH_SHORT).show();

        }
        if(!passw.equals(passw2))
        {
            Toast.makeText(this, "Parolalar eşleşmiyor!", Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(this,"Kaydınız başarı ile oluşturulmuştur.",Toast.LENGTH_LONG).show();
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {


                    try {
                        checkItem(item);
                        final Users entity = addItemInTable(item);

                    } catch (Exception e) {
                        createAndShowDialogFromTask(e, "Error");
                        Log.d("Hata", "message" + e);
                    }
                    return null;
                }
            };

            runAsyncTask(task);
    }
    }



    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();

                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("email", ColumnDataType.String);
                    tableDefinition.put("username", ColumnDataType.String);
                    tableDefinition.put("password", ColumnDataType.String);
                    tableDefinition.put("isAuthor", ColumnDataType.Boolean);
                    localStore.defineTable("Users", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
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

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public void redirect_to_login(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    public void getLogin(View view) {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

/*
    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();



            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);


            return resultFuture;
        }
    }*/
}
