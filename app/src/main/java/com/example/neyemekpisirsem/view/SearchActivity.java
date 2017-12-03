package com.example.neyemekpisirsem.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.neyemekpisirsem.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by demet on 2.12.2017.
 */

public class SearchActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ListView list = (ListView) findViewById(R.id.listViewSearch);
        ArrayList<String> foodListArray=new ArrayList<>();
        foodListArray.addAll(Arrays.asList(getResources().getStringArray(R.array.foodListArray)));

        adapter=new ArrayAdapter<>(SearchActivity.this,android.R.layout.simple_list_item_1, foodListArray);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
