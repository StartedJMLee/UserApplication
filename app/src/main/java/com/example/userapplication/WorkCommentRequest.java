package com.example.userapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



public class WorkCommentRequest extends StringRequest {

    final static private String URL =  "http://lloasd33.cafe24.com/workcommentsave.php";
    private Map<String, String> parameter;


    public WorkCommentRequest(String userID, String workname, String comment, int status, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameter = new HashMap();
        parameter.put("userID", userID);
        parameter.put("workname", workname);
        parameter.put("comment", comment);
        parameter.put("status", String.valueOf(status));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameter;
    }
}
