package com.example.neyemekpisirsem.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.neyemekpisirsem.R;
import com.example.neyemekpisirsem.model.*;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by Pınar Köroğlu on 24.11.2017.
 */

public class register_presenter extends ArrayAdapter<Users> {
    Button button;
    EditText edit1;
    EditText edit2;
    EditText edit3;
    EditText edit4;
    EditText edit5;

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
/*
        button=(Button)row.findViewById(R.id.button1);
        edit1=(EditText)row.findViewById(R.id.editText1);
        edit2=(EditText)row.findViewById(R.id.editText2);
        edit3=(EditText)row.findViewById(R.id.editText3);
        edit4=(EditText)row.findViewById(R.id.editText4);*/
        // edit5=(EditText)row.findViewById(R.id.editText5);
        final Users users = new Users();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {




                users.setName(edit1.getText().toString());
                users.setEmail(edit3.getText().toString());
                users.setUsername(edit2.getText().toString());
                users.setPassword(edit4.getText().toString());




            }
        });
        row.setTag(users);

        return row;
    }
}

