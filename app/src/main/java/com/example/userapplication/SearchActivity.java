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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String userID;
    private int usertype;
    final static private String TAG_WORKJSON = "showworklist";
    private TextView exTextView;
    private List<String> workListData;
    private List<String> authornameData;
    private List<String> workdescriptionData;
    private List<String> worksectorData;

    private ListView workListView;
    private EditText editSearch;
    private SearchAdapter adapter;
    private ArrayList<String> mArraylist;
    private String postdata;
    private String selectWorkname;
    private String exName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editSearch = (EditText)findViewById(R.id.searchWorkView);
        exTextView = (TextView)findViewById(R.id.selectedExname);
        workListView = (ListView)findViewById(R.id.workList);

        Intent intent = getIntent();
        exName = intent.getStringExtra("exName");
        exTextView.setText(exName);
        userID = getIntent().getStringExtra("userID");
        usertype = getIntent().getIntExtra("usertype", 0);


        workListData = new ArrayList<>();
        authornameData = new ArrayList<>();
        workdescriptionData = new ArrayList<>();
        worksectorData = new ArrayList<>();

        setWorkList();

        mArraylist = new ArrayList<>();
        mArraylist.addAll(workListData);
        adapter = new SearchAdapter(workListData, this);
        workListView.setAdapter(adapter);

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

        workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectWorkname = (String)adapter.getItem(position);
                int pos = mArraylist.indexOf(selectWorkname);

                // workPageActivity에 필요한 정보들, qr코드로 들어가는 부분에도 이 정보들을 workPageActivity에 보내야 함
                Intent intent = new Intent(SearchActivity.this, WorkPageActivity.class);
                intent.putExtra("workname", selectWorkname);
                intent.putExtra("userID", userID);
                intent.putExtra("usertype", usertype);
                // 정보들 하나의 class로 만들어서 관리할 수 있는데 일단 작동되는지 보려고 이렇게 구현했습니다
                intent.putExtra("authorname", authornameData.get(pos));
                intent.putExtra("workdescription", workdescriptionData.get(pos));
                intent.putExtra("worksector", worksectorData.get(pos));
                startActivity(intent);
            }
        });


    }

    public void search(String text){

        workListData.clear();
        if(text.length() == 0){
            workListData.addAll(mArraylist);
        }

        else{
            for(int i = 0; i<mArraylist.size(); i++)
            {
                if(mArraylist.get(i).toLowerCase().contains(text)){
                    workListData.add(mArraylist.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void setWorkList() {

        GetWorkList getWorkList = null;
        try {
            getWorkList = new GetWorkList();
            postdata = getWorkList.execute("http://lloasd33.cafe24.com/showworklist2.php",exName).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(postdata);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_WORKJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                workListData.add(jsonArray.getJSONObject(i).getString("workname"));
                authornameData.add(jsonArray.getJSONObject(i).getString("authorname"));
                workdescriptionData.add(jsonArray.getJSONObject(i).getString("workdescription"));
                worksectorData.add(jsonArray.getJSONObject(i).getString("worksector"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class GetWorkList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                String url = strings[0];
                String name = strings[1];

                URL URLObject = new URL(url);
                HttpURLConnection con = (HttpURLConnection) URLObject.openConnection();

                String postparam = "name=" + name;

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
