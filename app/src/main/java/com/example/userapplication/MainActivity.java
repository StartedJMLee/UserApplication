package com.example.userapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button search_btn;
    private int usertype;
    private String userID;
    ArrayAdapter adapter;
    private VisitedPages visitedPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID = getIntent().getStringExtra("userID");
        usertype = getIntent().getIntExtra("usertype", 0);

        //싱글턴
        visitedPages = VisitedPages.getInstance();

        //리스트뷰 & 어댑터
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,visitedPages.getVisitedWorkNames());
        ListView listview = (ListView) findViewById(R.id.visitedView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WorkPageActivity.class);
                //작품 정보 전달
                List<Work> worklist = visitedPages.getVisitedWorks();
                intent.putExtra("workname", worklist.get(position).getName());
                intent.putExtra("userID", userID);
                intent.putExtra("usertype", usertype);
                intent.putExtra("authorname", worklist.get(position).getAuthorName());
                intent.putExtra("workdescription", worklist.get(position).getDescription());
                intent.putExtra("worksector", worklist.get(position).getSector());
                startActivity(intent);
            }
        });

        //activity intent
        Button scan_btn = (Button) findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(
                        MainActivity.this,
                        QRcodeScanner.class);
                intent.putExtra("userID", userID);
                intent.putExtra("usertype", usertype);
                startActivity(intent);
            }
        });

        //activity intent
        Button search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(
                        MainActivity.this,
                        Search2Activity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("usertype", usertype);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
