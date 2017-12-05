package com.example.neyemekpisirsem.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.neyemekpisirsem.model.Users;

/**
 * Created by demet on 26.11.2017.
 */

public class login_presenter extends ArrayAdapter<Users> {

    Context mContext;

    int mLayoutResourceId;

    public login_presenter(Context context, int layoutResourceId) {
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


        row.setTag(users);

        return row;
    }

}
