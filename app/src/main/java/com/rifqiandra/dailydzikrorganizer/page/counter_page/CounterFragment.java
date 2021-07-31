package com.rifqiandra.dailydzikrorganizer.page.counter_page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.rifqiandra.dailydzikrorganizer.page.PageActivity;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla.C_Item_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla.C_L_A_DzikrName;
import com.rifqiandra.dailydzikrorganizer.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CounterFragment extends Fragment {

    NonScrollListView listView;
    C_L_A_DzikrName adapter_data;
    final ArrayList<C_Item_DzikrName> list_data = new ArrayList<C_Item_DzikrName>();
    ImageView btn_count;
    TextView ttl_count,text_dzikrname;
    int counter = 0;
    ImageView btn_save;
    int limit_counter;
    String id_dzikrname;

    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_counter, container, false);
        if(AppConfig.DATA_SEND != null) {
            //Toast.makeText(getActivity(),"Data send " +AppConfig.DATA_SEND, Toast.LENGTH_LONG).show();
            id_dzikrname = AppConfig.DATA_SEND;
            load_data_details();
        }else {
            counter = 0;
        }
        btn_save = (ImageView) root.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_counter();
            }
        });
        ttl_count = (TextView) root.findViewById(R.id.ttl_count);
        text_dzikrname = (TextView) root.findViewById(R.id.text_dzikrname);
        btn_count = (ImageView) root.findViewById(R.id.btn_count);
        btn_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter < limit_counter) {
                    counter ++;
                    ttl_count.setText(""+counter);
                }else {
                    Toast.makeText(getActivity(), "Dzikr target has been achieved! ", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    void save_counter() {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_WRITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try{
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if(status) {
                        showDialog();
                    } else {
                        Toast.makeText(getActivity(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Terjadi kesalahan mohon coba lagi.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("write", "save_counter");
                param.put("id_dzikrname", id_dzikrname);
                param.put("dzikr_total", String.valueOf(counter));
                param.put("username_detail", AppConfig.getDeviceId(getActivity()));
                return param;
            }
        };
        Volley.newRequestQueue(getActivity()).add(req);
    }

    void load_data_details() {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if (status) {
                        String data_baris = data.getString("baris");
                        JSONObject tmp_data = new JSONObject(data_baris);
                        String val1 = tmp_data.getString("dzikr_name");
                        String val2 = tmp_data.getString("dzikr_target");
                        limit_counter = Integer.parseInt(val2);
                        text_dzikrname.setText(val1);
                        if(tmp_data.getString("dzikr_total") != null) {
                            ttl_count.setText(tmp_data.getString("dzikr_total"));
                            counter = Integer.parseInt(tmp_data.getString("dzikr_total"));
                        } else {
                            counter = 0;
                            ttl_count.setText("0");
                        }
                    } else {
                        Toast.makeText(getActivity(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
//                    Toast.makeText(getActivity(), "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param =  new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("call", "dzikr_action");
                param.put("username_detail", AppConfig.getDeviceId(getActivity()));
                param.put("id_dzikrname", id_dzikrname);
                return param;
            }
        };
        Volley.newRequestQueue(getActivity()).add(req);
    }
    void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Daily Dzikr Organizer");

        alertDialogBuilder.setMessage("Your recitation has been saved!");
        alertDialogBuilder.setIcon(R.drawable.app_logo);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PageActivity.navController.navigate(R.id.navigation_today);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}