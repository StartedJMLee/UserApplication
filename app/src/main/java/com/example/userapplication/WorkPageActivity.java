package com.example.userapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WorkPageActivity extends AppCompatActivity {
    private Button add_btn;
    private EditText editComment;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);
        name = "임시 아이디";

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.comment_recycler);

        //Set LayoutManager & Animation
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager((layoutManager));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Make DataList ( CardItem 객체 리스트)
        //서버에서 댓글 가져오는 부분 추가.
        final List<CardItem> dataList = new ArrayList<>();
        final JSONArray commentarr = new JSONArray();
        /*
        for (int i = 0; i < 5; i++) {
            dataList.add(new CardItem(i + "번째", "댓글내용" + i));
        }
        */


        //Adapter
        final commentRecyclerAdapter adapter = new commentRecyclerAdapter(this, dataList, R.layout.row_comment, commentarr);
        recyclerView.setAdapter(adapter);

        //코멘트 등록
        add_btn = (Button)findViewById(R.id.add_btn);
        editComment = findViewById(R.id.editComment);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String comment = editComment.getText().toString();
               dataList.add(0,new CardItem( name, comment));
               adapter.notifyItemInserted(0);
               Toast.makeText(getApplicationContext(),"코멘트 등록",Toast.LENGTH_SHORT).show();
               //json
                JSONObject commentjs = new JSONObject();
                try {
                    commentjs.put(name,comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentarr.put(commentjs);
            }
        });
    }

}

