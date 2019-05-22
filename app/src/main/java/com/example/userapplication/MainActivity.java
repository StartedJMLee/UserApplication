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
    private VisitedPages visited;
    private int usertype;
    private String userID;
    ArrayAdapter adapter;
    ListView listview;

    List<String> inAdapterList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visited = VisitedPages.getInstance();
        inAdapterList = visited.getVisitedWorkNames();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,inAdapterList);
        listview = (ListView) findViewById(R.id.visitedView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                    String workName = (String) parent.getItemAtPosition(position);
                    // TODO
                    Intent intent = new Intent(MainActivity.this, WorkPageActivity.class);
                //작품 정보 전달
                List<Work> worklist = visited.getVisitedWorks();
                intent.putExtra("workName", worklist.get(position).getName());
                intent.putExtra("userID", userID);
                intent.putExtra("usertype", usertype);
                intent.putExtra("authorname", worklist.get(position).getAuthorName());
                intent.putExtra("workdescription", worklist.get(position).getDescription());
                intent.putExtra("worksector", worklist.get(position).getSector());
                startActivity(intent);

            }
        });

        userID = getIntent().getStringExtra("userID");
        usertype = getIntent().getIntExtra("usertype", 0);

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
        //visited = VisitedPages.getInstance();
        //adapter.notifyDataSetChanged();
        inAdapterList = visited.getVisitedWorkNames();
        //Toast.makeText(getApplicationContext(), "싱글턴에"+visited.getVisitedWorks().size()+"개의 객체 저장", Toast.LENGTH_SHORT).show();
        //adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "어댑터데이터:"+ inAdapterList.get(inAdapterList.size()-1), Toast.LENGTH_SHORT).show();
        //adapter.notifyDataSetChanged();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,inAdapterList);
        listview = (ListView) findViewById(R.id.visitedView);
        listview.setAdapter(adapter);
    }
}
