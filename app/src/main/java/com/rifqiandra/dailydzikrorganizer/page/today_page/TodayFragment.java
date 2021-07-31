package com.rifqiandra.dailydzikrorganizer.page.today_page;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.rifqiandra.dailydzikrorganizer.page.PageActivity;
import com.rifqiandra.dailydzikrorganizer.page.today_page.cla.C_Item_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.today_page.cla.C_L_A_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.today_page.reminder.AlertReceiver;
import com.rifqiandra.dailydzikrorganizer.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;


public class TodayFragment extends Fragment {

    NonScrollListView listView;
    C_L_A_DzikrName adapter_data;
    final ArrayList<C_Item_DzikrName> list_data = new ArrayList<C_Item_DzikrName>();
    TextView text_view_date1,text_view_date2,text_view_date3;


    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_today, container, false);

        text_view_date1 = (TextView) (root.findViewById(R.id.text_view_date1));
        text_view_date2 = (TextView) (root.findViewById(R.id.text_view_date2));
        text_view_date3 = (TextView) (root.findViewById(R.id.text_view_date3));
        listView = (NonScrollListView) (root.findViewById(R.id.listview));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_dzikrname = (TextView) view.findViewById(R.id.t_id_dzikrname);
                String iddzikrname = id_dzikrname.getText().toString();
                AppConfig.DATA_SEND = iddzikrname;
                PageActivity.navController.navigate(R.id.navigation_counter);
            }
        });
        DateFormat dateFormat1 = new SimpleDateFormat("EEEE");
        DateFormat dateFormat2 = new SimpleDateFormat("MMM yyyy");
        DateFormat dateFormat3 = new SimpleDateFormat("dd");
        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        text_view_date1.setText(dateFormat1.format(date1));
        text_view_date2.setText(dateFormat2.format(date2));
        text_view_date3.setText(dateFormat3.format(date3));
        load_data_page();
        return root;

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
                                String t1 = tmp_data.optString("id_dzikrname");
                                String t2 = tmp_data.optString("dzikr_name");
                                list_data.add(new C_Item_DzikrName(t1,t2));
                            }
                            adapter_data = new C_L_A_DzikrName(getActivity(),R.layout.layout_item_dzikrname, list_data);
                            listView.setAdapter(adapter_data);
                            adapter_data.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Error " +e, Toast.LENGTH_LONG).show();
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
                param.put("username", AppConfig.getDeviceId(getContext()));
                return param;
            }
        };
        Volley.newRequestQueue(getActivity()).add(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data_page();
    }
}