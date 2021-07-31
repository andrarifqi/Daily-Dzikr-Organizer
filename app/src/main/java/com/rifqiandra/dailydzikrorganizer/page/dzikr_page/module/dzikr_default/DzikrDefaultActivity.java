package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.form.FormAddActivity;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.cla.C_Item_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.cla.C_L_A_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.details.DefaultDetailsActivity;
import com.rifqiandra.dailydzikrorganizer.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DzikrDefaultActivity extends AppCompatActivity {

    NonScrollListView listView;
    C_L_A_DzikrName adapter_data;
    final ArrayList<C_Item_DzikrName> list_data = new ArrayList<C_Item_DzikrName>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dzikr_default);


        listView = (NonScrollListView) (findViewById(R.id.listview));
        load_data_page();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_dzikr_default = (TextView) view.findViewById(R.id.t_id_dzikrname);
                String iddzikrdefault = id_dzikr_default.getText().toString();
                Intent intent = new Intent(getBaseContext(), DefaultDetailsActivity.class);
                intent.putExtra("id_dzikr_default", iddzikrdefault);
                startActivity(intent);
            }
        });
    }
    void load_data_page() {
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
                                String t1 = tmp_data.optString("id_dzikr_default");
                                String t2 = tmp_data.optString("dzikr_default_name");
                                list_data.add(new C_Item_DzikrName(t1,t2));
                            }
                            adapter_data = new C_L_A_DzikrName(getBaseContext(),DzikrDefaultActivity.this,R.layout.layout_item_dzikrname, list_data);
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
                param.put("call", "dzikr_default");
                return param;
            }
        };
        Volley.newRequestQueue(this).add(req);    }
}