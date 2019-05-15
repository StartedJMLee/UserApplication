package com.example.userapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    private Button login_btn;
    private Button makeAccount_btn;
    private EditText idText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                intent.putExtra("userID", jsonResponse.getString("userID"));
                                intent.putExtra("usertype", jsonResponse.getInt("usertype"));
                                startActivity(intent);
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                                builder.setMessage("로그인에 실패했습니다")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(idText.getText().toString(), passwordText.getText().toString(), listener);
                RequestQueue queue = Volley.newRequestQueue(StartActivity.this);
                queue.add(loginRequest);


            }
        });

        makeAccount_btn = findViewById(R.id.makeAccount_btn);
        makeAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, makeAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
