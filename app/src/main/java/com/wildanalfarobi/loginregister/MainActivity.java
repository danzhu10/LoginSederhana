package com.wildanalfarobi.loginregister;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Alfarobi on 02/11/2015.
 */
public class MainActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView hello = (TextView)findViewById(R.id.textView);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        hello.setText("Selamat datang "+username);
        hello.setTextSize(16);
    }
}
