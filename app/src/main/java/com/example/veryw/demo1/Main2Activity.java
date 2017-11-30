package com.example.veryw.demo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private SlidingMenu mMenu;

    private ListView listView;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);

        List<String> data=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("第"+i+"个条目");
        }
//        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data));


    }

    public void toggleMenu(View view) {
        mMenu.toggle();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了第"+position+"个条目", Toast.LENGTH_SHORT).show();
    }
}
