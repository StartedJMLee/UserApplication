package com.example.userapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    static final private String URL = "http://lloasd33.cafe24.com/userLogin.php";
    private Map<String, String> parameter;


    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameter = new HashMap();
        parameter.put("userID", userID);
        parameter.put("password", userPassword);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameter;
    }
}
