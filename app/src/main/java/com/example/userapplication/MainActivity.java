package com.example.userapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button search_btn;
    private VisitedPages visitedPages;
    private int usertype;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitedPages.getInstance();
        if(visitedPages !=null) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, visitedPages.getVisitedWorkNames());
            ListView listview = (ListView) findViewById(R.id.visitedView);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get TextView's Text.
                    String workName = (String) parent.getItemAtPosition(position);
                    // TODO
                    Intent intent = new Intent(MainActivity.this, WorkPageActivity.class);
                    intent.putExtra("workName", workName);
                    startActivity(intent);
                }
            });
        }

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
}
