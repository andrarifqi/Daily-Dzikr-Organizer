package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.form;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class FormAddActivity extends AppCompatActivity {

    EditText input_dzikrname,input_lafadz_dzikr,input_target,input_date;
    Button add_btn;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    String id_dzikrname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add);
        input_dzikrname = (EditText) findViewById(R.id.input_dzikrname);
        input_lafadz_dzikr = (EditText) findViewById(R.id.input_lafadz_dzikr);
        input_target = (EditText) findViewById(R.id.input_target);
        input_date = (EditText) findViewById(R.id.input_date);
        add_btn = (Button) findViewById(R.id.add_btn);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String v1 = input_dzikrname.getText().toString().trim();
                String v2 = input_lafadz_dzikr.getText().toString().trim();
                String v3 = input_target.getText().toString().trim();
                String v4 = input_date.getText().toString().trim();
                if(!v1.isEmpty() && !v2.isEmpty() && !v3.isEmpty() && !v4.isEmpty()) {
                    simpan_data(v1, v2, v3, v4);
                }
            }
        });
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        Intent intent = getIntent();
        id_dzikrname = intent.getStringExtra("id_dzikrname");
        if(id_dzikrname != null) {
            load_data_details();
        }
    }
    void simpan_data(final String t1, final String t2, final String t3, final String t4) {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_WRITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getBaseContext(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try{
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if(status) {
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Terjadi kesalahan mohon coba lagi.", Toast.LENGTH_LONG).show();
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
                Map<String, String> param = new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("write", "dzikrname");
                param.put("id_dzikrname", id_dzikrname);
                param.put("dzikr_name", t1);
                param.put("dzikr_description", t2);
                param.put("dzikr_target", t3);
                param.put("dzikr_date", t4);
                param.put("username", AppConfig.getDeviceId(getBaseContext()));
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
    void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                input_date.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
                        String val1 = tmp_data.getString("dzikr_name");
                        String val2 = tmp_data.getString("dzikr_description");
                        String val3 = tmp_data.getString("dzikr_target");
                        String val4 = tmp_data.getString("dzikr_date");
                        input_dzikrname.setText(val1);
                        input_lafadz_dzikr.setText(val2);
                        input_target.setText(val3);
                        input_date.setText(val4);
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
                param.put("call", "dzikrname_detail");
                param.put("id_dzikrname", id_dzikrname);
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}