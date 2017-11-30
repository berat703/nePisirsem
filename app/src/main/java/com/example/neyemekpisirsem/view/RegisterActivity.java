package com.example.neyemekpisirsem.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.Users;
import com.example.neyemekpisirsem.presenter.login_presenter;
import com.example.neyemekpisirsem.presenter.register_presenter;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;



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
    EditText edit4;
     EditText edit5;
    Button register;
    // View view = setContentView(R.layout.activity_login);
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.t_username);
        password=(EditText)findViewById(R.id.t_password);
        email=(EditText)findViewById(R.id.t_email);
        // edit4=(EditText)findViewById(R.id.editText4);
        // edit5=(EditText)findViewById(R.id.editText5);
        register=(Button)findViewById(R.id.registerButton);
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

           /* loginView = new loginView(this, R.layout.row_list_to_do);
            ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
            listViewToDo.setAdapter(loginView);
*/
            // Load the items from the Mobile Service
            //  refreshItemsFromTable();

        } catch (MalformedURLException e) {
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
            // refreshItemsFromTable();
        }

        return true;
    }


    public void checkItem(final Users item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(item);
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

        // Create a new item


        final Users item = new Users();
        item.setUsername(username.getText().toString());
        item.setPassword(password.getText().toString());
        item.setEmail(email.getText().toString());


            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        checkItem(item);
                        final Users entity = addItemInTable(item);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.add(entity);
                            }
                        });


                    } catch (Exception e) {
                        createAndShowDialogFromTask(e, "Error");
                        // Log.d("Hata", "message" + e);
                    }
                    return null;
                }
            };

        runAsyncTask(task);
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
                    tableDefinition.put("username", ColumnDataType.Boolean);
                    tableDefinition.put("password", ColumnDataType.Boolean);
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


    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();



            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);


            return resultFuture;
        }
    }
}
