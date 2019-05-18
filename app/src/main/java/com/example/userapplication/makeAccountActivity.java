package com.example.userapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class makeAccountActivity extends AppCompatActivity {
    private Button done_btn;
    private EditText editUserID;
    private EditText editPassword;
    private EditText editUserEmail;
    private Spinner typeChoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);

        editUserID = findViewById(R.id.editUserID);
        editPassword = findViewById(R.id.editPassword);
        editUserEmail = findViewById(R.id.edituserEmail);
        typeChoice = findViewById(R.id.typeChoice);



        done_btn = findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int type;
                if(typeChoice.getSelectedItem().toString().equals("작가"))
                {
                    type = 1;
                }
                else
                {
                    type = 0;
                }

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(makeAccountActivity.this);
                                builder.setMessage("회원가입에 성공했습니다")
                                        .setPositiveButton("확인", yesButtonClickListener)
                                        .create()
                                        .show();



                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(makeAccountActivity.this);
                                builder.setMessage("회원가입 실패")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                makeAccountRequest request = new makeAccountRequest(editUserID.getText().toString(), editPassword.getText().toString(), editUserEmail.getText().toString(), type, listener);
                RequestQueue queue = Volley.newRequestQueue(makeAccountActivity.this);
                queue.add(request);





            }
        });

    }

    private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(makeAccountActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
