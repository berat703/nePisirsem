package com.example.neyemekpisirsem.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.*;
import com.example.neyemekpisirsem.view.RegisterActivity;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;
import com.example.neyemekpisirsem.view.*;
/**
 * Created by Pınar Köroğlu on 24.11.2017.
 */

public class register_presenter extends ArrayAdapter<Users> {
    Button register;



    Context mContext;

    int mLayoutResourceId;

    public register_presenter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final Users currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);


        final Users users = new Users();



        register=(Button)row.findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


            }
        });
        row.setTag(users);

        return row;
    }
}

