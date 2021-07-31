package com.rifqiandra.dailydzikrorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.rifqiandra.dailydzikrorganizer.page.PageActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout btn_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (LinearLayout) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getBaseContext(), PageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}