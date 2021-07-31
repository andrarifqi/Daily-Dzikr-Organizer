package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rifqiandra.dailydzikrorganizer.BuildConfig;
import com.rifqiandra.dailydzikrorganizer.R;
import com.rifqiandra.dailydzikrorganizer.appconfig.AppConfig;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla.C_Item_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla.C_L_A_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.form.FormAddActivity;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.DzikrDefaultActivity;
import com.rifqiandra.dailydzikrorganizer.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDzikrActivity extends AppCompatActivity {

    FloatingActionButton fab ;
    NonScrollListView listView;
    C_L_A_DzikrName adapter_data;
    final ArrayList<C_Item_DzikrName> list_data = new ArrayList<C_Item_DzikrName>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dzikr);

        fab = (FloatingActionButton) (findViewById(R.id.fab));
        listView = (NonScrollListView) (findViewById(R.id.listview));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FormAddActivity.class);
                startActivity(intent);
            }
        });
        load_data_page();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_dzikrname = (TextView) view.findViewById(R.id.t_id_dzikrname);
                String iddzikrname = id_dzikrname.getText().toString();
                Intent intent = new Intent(getBaseContext(), FormAddActivity.class);
                intent.putExtra("id_dzikrname", iddzikrname);
                startActivity(intent);
            }
        });
    }
    void
    load_data_page() {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getBaseContext(), "Data JSON "+response, Toast.LENGTH_LONG).show();
                try{
                    list_data.clear();
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if(status) {
                        JSONArray data2 = data.optJSONArray("baris");
                        String rows = data.getString("rows");
                        if(rows.equals("0")) {

                        } else{
                            for(int i = 0; i < data2.length(); i++) {
                                JSONObject tmp_data = data2.getJSONObject(i);
                                String t1 = tmp_data.optString("id_dzikrname");
                                String t2 = tmp_data.optString("dzikr_name");
                                list_data.add(new C_Item_DzikrName(t1,t2));
                            }
                            adapter_data = new C_L_A_DzikrName(getBaseContext(),R.layout.layout_item_dzikrname, list_data);
                            listView.setAdapter(adapter_data);
                            adapter_data.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Error " +e, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("call", "dzikrname");
                param.put("username", AppConfig.getDeviceId(getBaseContext()));
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_data_page();
    }
}