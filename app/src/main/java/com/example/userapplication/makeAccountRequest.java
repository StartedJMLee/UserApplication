package com.example.userapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class makeAccountRequest extends StringRequest {

    static final private String URL = "http://lloasd33.cafe24.com/userRegister.php";
    private Map<String, String> parameter;


    public makeAccountRequest(String userID, String password, String userEmail, String userName, int usertype, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameter = new HashMap();
        parameter.put("userID", userID);
        parameter.put("password", password);
        parameter.put("userEmail", userEmail);
        parameter.put("authorname", userName);
        parameter.put("usertype", String.valueOf(usertype));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameter;
    }
}
