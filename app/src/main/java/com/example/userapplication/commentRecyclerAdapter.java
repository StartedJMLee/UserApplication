package com.example.userapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.userapplication.R.id.delete_btn;


public class commentRecyclerAdapter extends RecyclerView.Adapter<commentRecyclerAdapter.ViewHolder> {
    private final List<CardItem> mDataList;
    private Context context;
    private int itemLayout;
    private JSONArray commentarr;

    //생성자
    public commentRecyclerAdapter(Context context, List<CardItem> dataList, int itemLayout, JSONArray commentarr){
        mDataList = dataList;
        this.context = context;
        this.itemLayout = itemLayout;
        this.commentarr = commentarr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false); //xml을 자바객체로 변환, view를 생성
        return new ViewHolder(view); //생성한 view로 viewholder 생성해서 반환
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        //아이템 세팅
        CardItem item = mDataList.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.contents.setText(item.getContents());
        /*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(v.getId()== delete_btn){
                    Toast.makeText(context,"삭제됨", Toast.LENGTH_SHORT).show();
                    mDataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDataList.size());
                }
        });
        */
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
        //뷰홀더 생성자
        public  ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contents = itemView.findViewById(R.id.contents);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void delete(int pos){
        try {
            Toast.makeText(context,"코멘트 삭제", Toast.LENGTH_SHORT).show();
            mDataList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, mDataList.size());
        } catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        //json
        commentarr.remove(pos);

    }
}
