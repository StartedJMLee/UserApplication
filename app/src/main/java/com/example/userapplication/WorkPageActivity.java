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
    private VisitedPages visitedPages;
    private Work work;
    private int status; //0:관람객 1:작가

    //임시 변수
    private int id; //임시 변수
    private List<CardItem> dummyComment;
    /*
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);
        String workName = getIntent().getStringExtra("workName");

        //View Binding
        workname = findViewById(R.id.workname);
        workaccount_view = findViewById(R.id.workaccount_view);
        workauthor_view = findViewById(R.id.workauthor_view);
        worksite_view = findViewById(R.id.worksite_view);

        //visitedPages 싱글톤
        visitedPages.getInstance();

        //테스트용 값 ---------------------
        id = 0;
        //---------------------------------

        // -  더미데이터
        if (id == 0) {
            work = new Work(id, workName, "The Smiths", "Morrisey & Marr", "Menchester");
            visitedPages.addToVisitedWorkNames(workName);

        }
        /*else if (id == 1) {
            work = new Work(id, "Blue Monday", "New Order", "Post Joy Division", "Menchester");
            visitedPages.addToVisitedWorkNames("Blue Monday");
        }*/

        //계정
        userID = "tempID";

        //유저id와 작품의 작가id를 match하여 status 판단하는 함수필요.
        /*makeAccountActivity에서 : 작가로 계정 생성 시 전시프로젝트에서 등록한 메일과 match해서 권한 검증하고, 작품 id랑 작가 id 연결시킴.
        연결한 작품id와 work객체의 id 일치하는지 확인해서 일치하면 status 1, 아니면 status 0.
        결과적으로 작가 status인 페이지에서는 파란 뷰타입으로 댓글이 달리고 관객 status인 페이지에서는 일반 뷰타입으로 댓글 달 수 있음. */

        //임시 status
        if (id == 0) {
            status = 1;
        } else {
            status = 0;
        }

        //work information view
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
        final List<CardItem> dataList = new ArrayList<>();
        final JSONArray commentarr = new JSONArray();

        //댓글 불러오기 더미데이터로 구현(서버 추가)
        for (int i = 0; i < 5; i++) {
            dummyComment.add(new CardItem(i + "번째", "댓글내용" + i,0));
        }
        if (!dummyComment.isEmpty()){
            for (int i = 0; i < dummyComment.size(); i++) {
            dataList.add(new CardItem(i + "번째", "댓글내용" + i,0));
            }
        }

        //Adapter
        final commentRecyclerAdapter adapter = new commentRecyclerAdapter(this,this, dataList, R.layout.row_comment, commentarr); //어댑터 수정
        recyclerView.setAdapter(adapter);

        //코멘트 등록
        add_btn = (Button) findViewById(R.id.add_btn);
        editComment = findViewById(R.id.editComment);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editComment.getText().toString();
                adapter.addComment(userID,comment,id,work.getName());
            }
        });

    }
}

