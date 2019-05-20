package com.example.userapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.userapplication.R.id.delete_btn;
import static com.example.userapplication.R.id.workname;


public class commentRecyclerAdapter extends RecyclerView.Adapter<commentRecyclerAdapter.ViewHolder> {
    private final List<CardItem> mDataList;
    private Context context;
    private int itemLayout;
    private JSONArray commentarr;
    private int status;
    private String workName;
    private String userID;
    //viewtype
    private final int TYPE_AUD = 0; //audience
    private final int TYPE_AUTH = 1; //author

    //생성자 - 값 전달
    public commentRecyclerAdapter(String workName, String userID, Context context, List<CardItem> dataList, int itemLayout, JSONArray commentarr, int status){
        mDataList = dataList;
        this.workName = workName;
        this.context = context;
        this.itemLayout = itemLayout;
        this.commentarr = commentarr;
        this.status = status;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
        if (viewType == TYPE_AUD) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_comment, parent, false); //xml을 자바객체로 변환, view를 생성
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_comment_color, parent, false);
        }
        return new ViewHolder(view); //생성한 view로 viewholder 생성해서 반환
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        //아이템 세팅
        CardItem item = mDataList.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.contents.setText(item.getContents());
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).getType() == 0){ //관객
            return TYPE_AUD;
        }else { //작가
            return TYPE_AUTH;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView contents;
        Button delete_btn;
        Button reply_btn;
        //뷰홀더 생성자
        public  ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contents = itemView.findViewById(R.id.contents);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    deleteComment(getAdapterPosition());
                }
            });
            reply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //대댓글 기능 구현
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    // 다이얼로그를 보여주기 위해 xml 파일을 사용
                    View view = LayoutInflater.from(context)
                            .inflate(R.layout.comment_dialog, null, false);
                    builder.setView(view);
                    Button replyEdit_btn = (Button) view.findViewById(R.id.replyEdit_btn);
                    final EditText replyEditText = (EditText) view.findViewById(R.id.replyEditText);

                    //대댓글 등록
                    replyEditText.setText("TO " + mDataList.get(getAdapterPosition()).getName() + " : ");
                    replyEditText.setSelection(replyEditText.getText().length());
                    final AlertDialog dialog = builder.create();
                    /*디버깅 해 보니 이 밑으로 오류가 납니다. 위의 final Button replyEdit_btn = (Button) view.findViewById(R.id.reply_btn); 수행 후
                    * replyEdit_btn의 값이 null로 나오는 걸로 보아 저부분에서 뭔가 문제가 있는 것 같습니다.
                    * replyEdit_btn 값이 null이어서 밑에  replyEdit_btn.setOnClickListener을 실행하면 바로 오류나서 앱이 종료되는 것 같습니다
                    * */
                    replyEdit_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String comment = replyEditText.getText().toString();
                            addComment(userID, comment, workName);
                            dialog.dismiss();

                            //여기에 notification보내는 코드 삽입
                            //조건 판정 - 일단 구현 후 추가.
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    void deleteComment(final int pos) {
        if (userID.equals(mDataList.get(pos).getName())) {
            int deletecommentid = 0;
            String postdata = null;

            deletecommentid = mDataList.get(pos).getCommentid();
            DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();

            try {
                postdata = deleteCommentRequest.execute("http://lloasd33.cafe24.com/deletecomment.php", String.valueOf(deletecommentid)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonResponse = new JSONObject(postdata);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {

                    mDataList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mDataList.size());
                    Toast.makeText(context, "코멘트 삭제", Toast.LENGTH_SHORT).show();
                    //json
                    commentarr.remove(pos);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(context, "삭제할 권한이 없습니다", Toast.LENGTH_SHORT).show();
        }
    }


        void addComment(String userID, String comment, String workName) { //대댓글 기능 구현 위해 position추가
        // commentid는 코멘트 저장할 때 db에서 알아서 값 넣어주게 해놔서 여기서는 그냥 0 넣는걸로 해놨습니다. 여기서 0 넣어도 디비에서는 이 값으로 저장 안 됩니다 그냥 더미데이터입니다
            mDataList.add(0, new CardItem(0, userID, comment, status));
            notifyItemInserted(0);
            Toast.makeText(context, "코멘트 등록", Toast.LENGTH_SHORT).show();
            //json
            JSONObject commentjs = new JSONObject();
            try {
                commentjs.put(userID, comment);
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

                        if (success) {

                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WorkCommentRequest workCommentRequest = new WorkCommentRequest(userID, workName, mDataList.get(0).getContents(), status, listener); //수정 필요
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(workCommentRequest);
    }

    // 코멘트 삭제 서버 통신 부분
    private class DeleteCommentRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                String url = strings[0];
                String commentid = strings[1];
                URL URLObject = new URL(url);
                HttpURLConnection con = (HttpURLConnection) URLObject.openConnection();

                String postparam = "commentid=" + commentid;

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
