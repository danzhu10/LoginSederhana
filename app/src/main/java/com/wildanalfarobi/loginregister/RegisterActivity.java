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

/**
 * Created by Alfarobi on 02/11/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText nameET,usernameET,passwordET;
    Button registerBT;
    ProgressDialog dialog;
    Toast toast;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;
        dialog=new ProgressDialog(this);
        usernameET=(EditText)findViewById(R.id.usernameET);
        passwordET=(EditText)findViewById(R.id.passwordET);
        nameET=(EditText)findViewById(R.id.nameET);
        registerBT=(Button)findViewById(R.id.registerBT);
        registerBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBT:
                String username=usernameET.getText().toString();
                String password=passwordET.getText().toString();
                String name=nameET.getText().toString();
                if (username.length() > 0 && password.length() > 0 && name.length() > 0) {
                    userRegister(username, password, name);
                }else {
                    showToast("Field is empty");
                }
                break;
        }
    }

    private void userRegister(final String username, final String password, final String name) {
        dialog.setMessage("Loading ...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST,
                URLController.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        finish();
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
                params.put("name",name);
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
