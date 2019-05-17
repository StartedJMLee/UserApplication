package com.example.userapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Search2Activity extends AppCompatActivity {

    final static private String TAG_EXJSON = "showexlist";
    private List<String> exListData;
    private ListView exListView;
    private EditText editSearch;
    private SearchAdapter adapter;
    private ArrayList<String> mArraylist;
    private String postdata;
    private String selectExname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        editSearch = (EditText) findViewById(R.id.searchExView);
        exListView = (ListView) findViewById(R.id.exList);

        exListData = new ArrayList<String>();

        setExList();

        mArraylist = new ArrayList<>();
        mArraylist.addAll(exListData);
        adapter = new SearchAdapter(exListData,this);
        exListView.setAdapter(adapter);

        //검색 기능 구현 ------------------------------------------
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        exListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectExname = (String)adapter.getItem(position);
                Intent intent = new Intent(Search2Activity.this, SearchActivity.class);
                intent.putExtra("exName", selectExname);
                startActivity(intent);


            }
        });



    }

    public void search(String text){

        exListData.clear();
        if(text.length() == 0){
            exListData.addAll(mArraylist);
        }

        else{
            for(int i = 0; i<mArraylist.size(); i++)
            {
                if(mArraylist.get(i).toLowerCase().contains(text)){
                    exListData.add(mArraylist.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    //데이터베이스에 저장되어 있는 전시 목록 불러오기
    public void setExList() {

        GetExList getexlist = null;
        try {
            getexlist = new GetExList();
            postdata = getexlist.execute("http://lloasd33.cafe24.com/showexhibitionlist3.php").get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(postdata);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_EXJSON);
            for(int i = 0; i <jsonArray.length(); i++){
                exListData.add(jsonArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // volley 이용시 동기식으로 통신할 방법이 없어 사용 불가
        /*Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    for(int i = 0; i <jsonArray.length(); i++){
                        exListData.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Search2Activity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://lloasd33.cafe24.com/showexhibitionlist3.php", listener, null){
        };
        queue.add(request);*/
    }


    // -----서버와 통신 부분
    private class GetExList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                String url = strings[0];


                URL URLObject = new URL(url);
                HttpURLConnection con = (HttpURLConnection) URLObject.openConnection();


                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();


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