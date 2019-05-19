package com.example.userapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class QRcodeScanner extends AppCompatActivity {
  //  private Button scan_btn;
    private String authorname;
    private String account;
    private String site;
    private String userID;
    private int usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
      //  scan_btn = (Button)findViewById(R.id.scan_btn);
        final Activity activity = this;
        userID = getIntent().getStringExtra("userID");
        usertype = getIntent().getIntExtra("usertype", 0);

       // scan_btn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
         //   }
       // });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String postdata = null;
        if(result !=null){
            if(result.getContents()==null) {
                Toast.makeText(this, "스캔 취소", Toast.LENGTH_LONG).show();
            }
            else{
                String workName = result.getContents();
                GetworkData getworkData = new GetworkData();
                try {
                    postdata = getworkData.execute("http://lloasd33.cafe24.com/qrcodeworkdata.php", workName).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonResponse = new JSONObject(postdata);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        authorname = jsonResponse.getString("authorname");
                        account = jsonResponse.getString("workdescription");
                        site = jsonResponse.getString("worksector");
                        Intent intent = new Intent(this, WorkPageActivity.class);
                        intent.putExtra("workname",workName);
                        intent.putExtra("authorname", authorname);
                        intent.putExtra("workdescription", account);
                        intent.putExtra("worksector", site);
                        intent.putExtra("userID", userID);
                        intent.putExtra("usertype", usertype);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "정보를 찾지 못했습니다. 스캔 취소", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetworkData extends AsyncTask<String, Void, String> {

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
}

