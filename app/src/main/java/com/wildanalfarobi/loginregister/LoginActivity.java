package com.wildanalfarobi.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wildanalfarobi.loginregister.helper.AppController;
import com.wildanalfarobi.loginregister.helper.URLController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameET,passwordET;
    Button loginBT,registerBT;
    ProgressDialog dialog;
    Toast toast;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
//        this is comment
        dialog=new ProgressDialog(this);
        usernameET=(EditText)findViewById(R.id.usernameET);
        passwordET=(EditText)findViewById(R.id.passwordET);
        loginBT=(Button)findViewById(R.id.loginBT);
        registerBT=(Button)findViewById(R.id.registerBT);
        loginBT.setOnClickListener(this);
        registerBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBT:
                String username=usernameET.getText().toString();
                String password=passwordET.getText().toString();
                if (username.length() > 0 && password.length() > 0) {
                    loginCheck(username, password);
                }else {
                    showToast("Username or password is empty");
                }
                break;
            case R.id.registerBT:
                startActivity(new Intent(v.getContext(),RegisterActivity.class));
                break;
        }
    }

    private void loginCheck(final String username, final String password) {
        dialog.setMessage("Loading ...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST,
                URLController.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }else {
                        final String errorMsg = jObj.getString("error_msg");
                        showToast(errorMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    showToast("Sorry, there is an error");
                }
            }
        })
        {
            @Override
            protected Map<String ,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }
    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
