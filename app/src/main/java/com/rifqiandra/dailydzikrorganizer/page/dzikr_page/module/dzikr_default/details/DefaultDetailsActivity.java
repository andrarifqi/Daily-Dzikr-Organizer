package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.details;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rifqiandra.dailydzikrorganizer.BuildConfig;
import com.rifqiandra.dailydzikrorganizer.R;
import com.rifqiandra.dailydzikrorganizer.appconfig.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultDetailsActivity extends AppCompatActivity {

    TextView name_dzikr_default,lafadz_dzikr_default,target_dzikr_default;
    String id_dzikr_default;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_details);
        name_dzikr_default = (TextView) findViewById(R.id.name_dzikr_default);
        lafadz_dzikr_default = (TextView) findViewById(R.id.lafadz_dzikr_default);
        target_dzikr_default = (TextView) findViewById(R.id.target_dzikr_default);

        Intent intent = getIntent();
        id_dzikr_default = intent.getStringExtra("id_dzikr_default");
        if(id_dzikr_default != null) {
            load_data_details();
        }
    }


    void load_data_details() {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getBaseContext(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if (status) {
                        String data_baris = data.getString("baris");
                        JSONObject tmp_data = new JSONObject(data_baris);
                        String val1 = tmp_data.getString("dzikr_default_name");
                        String val2 = tmp_data.getString("dzikr_default_description");
                        String val3 = tmp_data.getString("dzikr_default_target");
                        name_dzikr_default.setText("Dzikr Name : " + val1);
                        lafadz_dzikr_default.setText(val2);
                        target_dzikr_default.setText("Dzikr Target : " + val3);
                    } else {
                        Toast.makeText(getBaseContext(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param =  new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("call", "dzikrname_detail_default");
                param.put("id_dzikr_default", id_dzikr_default);
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}