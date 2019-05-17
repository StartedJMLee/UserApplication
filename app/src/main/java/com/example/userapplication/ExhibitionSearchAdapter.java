package com.example.userapplication;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ExhibitionSearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public ExhibitionSearchAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.row_exhibitionlist, null);
            viewHolder = new ViewHolder();
            viewHolder.label = (TextView)convertView.findViewById(R.id.label);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.label.setText(list.get(position));




        return convertView;
    }

    class ViewHolder{
        public TextView label;
    }
}
