package com.example.userapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //activity intent
        Button scan_btn = (Button) findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(
                        MainActivity.this,
                        QRcodeScanner.class);
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
                        SearchActivity.class);
                startActivity(intent);
            }
        });

    }
}
