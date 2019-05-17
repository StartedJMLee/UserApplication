package com.example.userapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WorkPageActivity extends AppCompatActivity {
    private TextView workname, workaccount_view, workauthor_view, worksite_view;
    private Button add_btn;
    private EditText editComment;
    private String userID;

    private int status; //0:관람객 1:작가
    private int id;
    private Work work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);

        workname = findViewById(R.id.workname);
        workaccount_view = findViewById(R.id.workaccount_view);
        workauthor_view = findViewById(R.id.workauthor_view);
        worksite_view = findViewById(R.id.worksite_view);

        id = getIntent().getIntExtra("id",-1); //예외처리 필요
        //테스트용 값 ---------------------
        id = 0;
        //---------------------------------
        //수정요망
        //Work 더미데이터 - 서버에서 id에 따라 값 불러와 생성자로 Work 객체 만드는 걸로 수정.
        //코멘트 불러오는거 만들어야함...
        if(id==0){
            work = new Work(id,"Bigmouth Strikes Again", "The Smiths","Morrisey & Marr","Menchester");
        }
        else if(id==1){
            work = new Work(id,"Blue Monday", "New Order","Post Joy Division","Menchester");
        }
        //계정
        userID = "tempID";
        //유저id와 작품의 작가id를 match하여 status 판단하는 함수필요.
        // makeAccountActivity에서 : 작가로 계정 생성 시 전시프로젝트에서 등록한 메일과 match해서 권한 검증하고, 작품 id랑 작가 id 연결시킴.
        //연결한 작품id와 work객체의 id 일치하는지 확인해서 일치하면 status 1, 아니면 status 0.
        //결과적으로 작가 status인 페이지에서는 파란 뷰타입으로 댓글이 달리고 관객 status인 페이지에서는 일반 뷰타입으로 댓글 달 수 있음.
        if (id ==0) {status = 1;} else {status = 0;} //일단 작품 0의 작가인 걸로 가정.

        //위젯
        workname.setText(work.getName());
        workaccount_view.setText(work.getAccount());
        workauthor_view.setText(work.getAuthor());
        worksite_view.setText(work.getSite());


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
        //dataList.add(new CardItem(i + "번째", "댓글내용" + i));
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
               dataList.add(0,new CardItem( userID, comment, status));
               adapter.notifyItemInserted(0);
               Toast.makeText(getApplicationContext(),"코멘트 등록",Toast.LENGTH_SHORT).show();
               //json
                JSONObject commentjs = new JSONObject();
                try {
                    commentjs.put(userID,comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentarr.put(commentjs);

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(WorkPageActivity.this);
                                builder.setMessage("삭제되었습니다")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(WorkPageActivity.this);
                                builder.setMessage("삭제되지 않았습니다")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                WorkCommentRequest workCommentRequest = new WorkCommentRequest(userID, work.getName(), dataList.get(0).getContents(), id, listener );
                RequestQueue queue = Volley.newRequestQueue(WorkPageActivity.this);
                queue.add(workCommentRequest);


            }
        });
    }

}

