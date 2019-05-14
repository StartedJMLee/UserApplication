package com.example.userapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {
    TextView resultView1;
    TextView resultView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        resultView1 = (TextView)findViewById(R.id.resultView1);
        resultView2 = (TextView)findViewById(R.id.resultView2);
        resultView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, WorkPageActivity.class);
                //WorkPageActivity에 Work의 id 전달
                intent.putExtra("id",0);
                startActivity(intent);
            }
        });
        resultView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, WorkPageActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

    }
}
