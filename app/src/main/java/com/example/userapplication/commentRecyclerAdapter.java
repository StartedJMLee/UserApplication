package com.example.userapplication;

import android.content.Context;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    public commentRecyclerAdapter(String workName, String userID, Context context, List<CardItem> dataList, int itemLayout, JSONArray commentarr, int status) {
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
        if (mDataList.get(position).getType() == 0) { //관객
            return TYPE_AUD;
        } else { //작가
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
        public ViewHolder(View itemView) {
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
                    final Button replyEdit_btn = (Button) view.findViewById(R.id.reply_btn);
                    final EditText replyEditText = (EditText) view.findViewById(R.id.replyEditText);

                    //대댓글 등록
                    replyEditText.setText("To" + mDataList.get(getAdapterPosition()).getName() + " : ");
                    final AlertDialog dialog = builder.create();
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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        void deleteComment(int pos) {
            if (userID == mDataList.get(pos).getName()) {
                try {
                    Toast.makeText(context, "코멘트 삭제", Toast.LENGTH_SHORT).show();
                    mDataList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mDataList.size());
                    //json
                    commentarr.remove(pos);
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(context, "삭제할 권한이 없습니다", Toast.LENGTH_SHORT).show();
            }
        }

        void addComment(String userID, String comment, String workName) { //대댓글 기능 구현 위해 position추가
            mDataList.add(0, new CardItem(userID, comment, status));
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
            WorkCommentRequest workCommentRequest = new WorkCommentRequest(userID, workName, mDataList.get(0).getContents(), 0, listener); //수정 필요
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(workCommentRequest);
        }
    }
}

