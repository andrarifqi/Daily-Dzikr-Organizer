package com.rifqiandra.dailydzikrorganizer.page.report_page;

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
import com.rifqiandra.dailydzikrorganizer.page.report_page.cla.C_Item_DzikrName;
import com.rifqiandra.dailydzikrorganizer.page.report_page.cla.C_L_A_DzikrName;
import com.rifqiandra.dailydzikrorganizer.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReportFragment extends Fragment {

    TextView tv_dzikrname,tv_current_recitation,tv_dzikr_target;
    C_L_A_DzikrName adapter_data;
    final ArrayList<C_Item_DzikrName> list_data = new ArrayList<C_Item_DzikrName>();
    NonScrollListView listView;
    String id_dzikrname;
    int limit_counter;


    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_report, container, false);
        tv_current_recitation = (TextView) root.findViewById(R.id.tv_current_recitation);
        tv_dzikrname = (TextView) root.findViewById(R.id.tv_dzikrname);
        tv_dzikr_target = (TextView) root.findViewById(R.id.tv_dzikr_target);
        listView = (NonScrollListView) (root.findViewById(R.id.listview));
        load_data_details();
        return root;
    }

    void load_data_details() {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "Data JSON " + response, Toast.LENGTH_LONG).show();
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
                                String t1 = tmp_data.optString("dzikr_name");
                                String t2 = tmp_data.optString("dzikr_target");
                                String t3 = tmp_data.optString("dzikr_total");
                                limit_counter = Integer.parseInt(t2);
                                list_data.add(new C_Item_DzikrName(t1,t2,t3));
                            }
                            adapter_data = new C_L_A_DzikrName(getActivity(),R.layout.layout_item_report, list_data);
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
                Toast.makeText(getActivity(), "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param =  new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("call", "report");
                param.put("username_detail", AppConfig.getDeviceId(getActivity()));
                param.put("id_dzikrname", id_dzikrname);
                return param;
            }
        };
        Volley.newRequestQueue(getActivity()).add(req);
    }


}