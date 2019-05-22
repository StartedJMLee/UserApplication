package com.example.userapplication;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.userapplication.VisitedPages.getInstance;

public class WorkPageActivity extends AppCompatActivity {
    private TextView workname, workaccount_view, workauthor_view, worksite_view;
    private Button add_btn;
    private EditText editComment;

    final static private String TAG_COMMENTJSON = "showcommentlist";
    //작품 정보
    private String author_id;
    private String workName;
    private String author;
    private String account;
    private String site;
    //권한 관련
    private String userID;
    private int usertype;


    private int status; //0:관람객 1:작가
    //싱글톤 객체
    private VisitedPages visitedPages;
    //임시 변수
   // private int id; //임시 변수
    private List<CardItem> savedCommentDate;
    private List<CardItem> commentData;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_page);
        workName = getIntent().getStringExtra("workname");
        usertype = getIntent().getIntExtra("usertype",0);
        userID = getIntent().getStringExtra("userID");
        author = getIntent().getStringExtra("authorname");
        account = getIntent().getStringExtra("workdescription");
        site = getIntent().getStringExtra("worksector");


        //View Binding
        workname = findViewById(R.id.workname);
        workaccount_view = findViewById(R.id.workaccount_view);
        workauthor_view = findViewById(R.id.workauthor_view);
        worksite_view = findViewById(R.id.worksite_view);

        savedCommentDate = new ArrayList<>();
        setSavedData();
        commentData = new ArrayList<>();
        commentData.addAll(savedCommentDate);

        //visitedPages 싱글톤
        visitedPages = VisitedPages.getInstance();
        visitedPages.addToVisitedWorks(new Work(workName,author,account,site));




        //status 판단 함수
        /*유저id와 작품의 작가id를 match하여 status 판단하는 함수필요.
        makeAccountActivity에서 : 작가로 계정 생성 시 전시프로젝트에서 등록한 메일과 match해서 권한 검증하고, 작품 id랑 작가 id 연결시킴.
        연결한 작품id와 work객체의 id 일치하는지 확인해서 일치하면 status 1, 아니면 status 0.
        결과적으로 작가 status인 페이지에서는 파란 뷰타입으로 댓글이 달리고 관객 status인 페이지에서는 일반 뷰타입으로 댓글 달 수 있음. */

        /*
        status 선택 함수 구현 완료

        usertype 은 회원 가입할 때 작가 선택할 시 usertype = 1로 저장되고 관람객은 0으로 저장되도록 변경했습니다 . 변경 전에는 작가 = 0 관람객 = 1 로 저장되었었습니다
         */

        status = SetStatus(userID, workName);

        //임시 status


        //work information view
        workname.setText(workName);
        workaccount_view.setText(account);
        workauthor_view.setText(author);
        worksite_view.setText(site);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.comment_recycler);

        //Set LayoutManager & Animation
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager((layoutManager));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Make DataList ( CardItem 객체 리스트)

        final JSONArray commentarr = new JSONArray();

        //댓글 불러오기 더미데이터로 구현(서버 추가)
     //   for (int i = 0; i < 5; i++) {
       //     dummyComment.add(new CardItem(i + "번째", "댓글내용" + i,0));
        //}
        //if (!dummyComment.isEmpty()){
         //   for (int i = 0; i < dummyComment.size(); i++) {
          //  dataList.add(new CardItem(i + "번째", "댓글내용" + i,0));
           // }
       // }

        //Adapter
        final commentRecyclerAdapter adapter = new commentRecyclerAdapter(workName,userID, this, commentData, R.layout.row_comment, commentarr, status); //어댑터 수정
        recyclerView.setAdapter(adapter);

        //코멘트 등록
        add_btn = (Button) findViewById(R.id.add_btn);
        editComment = findViewById(R.id.editComment);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editComment.getText().toString();
                adapter.addComment(userID,comment,workName);  //id?
            }
        });

    }

   public void setSavedData(){
        GetCommentList getCommentList = new GetCommentList();
        String postdata = null;

        try {
            postdata = getCommentList.execute("http://lloasd33.cafe24.com/showcommentlist.php", workName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       try {
           JSONObject jsonObject = new JSONObject(postdata);
           JSONArray jsonArray = jsonObject.getJSONArray(TAG_COMMENTJSON);
           for (int i = 0; i < jsonArray.length(); i++) {
                CardItem cardItem = new CardItem(jsonArray.getJSONObject(i).getInt("commentid"),jsonArray.getJSONObject(i).getString("userID"), jsonArray.getJSONObject(i).getString("comment"),jsonArray.getJSONObject(i).getInt("status"));
               savedCommentDate.add(0,cardItem);
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
    }

    private class GetCommentList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                String url = strings[0];
                String workname = strings[1];
                URL URLObject = new URL(url);
                HttpURLConnection con = (HttpURLConnection) URLObject.openConnection();

                String postparam = "workname=" + workname;

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(postparam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream = con.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                result =  sb.toString();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }

        }
    }


    // ------------------ 로그인 해 있는 user의 id와 선택한 작품의 workname을 이용해 선택한 작품이 로그인한 유저의 작품인지 선택
    public int SetStatus(String userID, String workname){
        Getcomparison getcomparison = new Getcomparison();
        String postdata = null;
        boolean success = true;
        try {
            postdata = getcomparison.execute("http://lloasd33.cafe24.com/comparison.php", userID, workname).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonResponse = new JSONObject(postdata);

            success = jsonResponse.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // usertype이 0이면 관람객이므로 무조건 status는 0
        if(usertype == 0){return 0;}
        else {
            // 선택 작품이 로그인한 유저의 작품이면 success = true 로 값 적용됨
            if (success) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    //Setstatus의 서버 통신 부분
    private class Getcomparison extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                String url = strings[0];
                String userID = strings[1];
                String workname = strings[2];
                URL URLObject = new URL(url);
                HttpURLConnection con = (HttpURLConnection) URLObject.openConnection();

                String postparam = "userID=" + userID + "&workname=" + workname;

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(postparam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream = con.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                result =  sb.toString();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }

        }
    }
}



