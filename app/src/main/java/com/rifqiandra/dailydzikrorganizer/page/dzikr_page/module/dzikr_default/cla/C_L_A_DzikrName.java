package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.cla;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C_L_A_DzikrName extends ArrayAdapter<C_Item_DzikrName>{

    private Context context;
    private Activity activity;
    private int layoutResourceId;
    private ArrayList<C_Item_DzikrName> data = new ArrayList<C_Item_DzikrName>();
    private Filter filter;
    View row;

    public C_L_A_DzikrName(Context context, Activity activity, int layoutResourceId, ArrayList<C_Item_DzikrName> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.activity = activity;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public C_Item_DzikrName getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).get_id());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        row = convertView;
        final C_L_A_DzikrName.ViewHolder holder;

        if (row==null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new C_L_A_DzikrName.ViewHolder();

            holder.Vt1 = (TextView) row.findViewById(R.id.t_id_dzikrname);
            holder.Vt2 = (TextView) row.findViewById(R.id.t_dzikrname);
            holder.layout_action_default = (LinearLayout) row.findViewById(R.id.layout_action_default);
            holder.layout_action_custom = (LinearLayout) row.findViewById(R.id.layout_action_custom);
            holder.btn_add_default = (LinearLayout) row.findViewById(R.id.btn_add_default);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final C_Item_DzikrName item = data.get(position);

        holder.Vt1.setText(item.get_id());
        holder.Vt2.setText(item.get_dzikrname());
        holder.layout_action_custom.setVisibility(View.GONE);
        holder.layout_action_default.setVisibility(View.VISIBLE);
        holder.btn_add_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListDzikr(item,item.get_id());
            }
        });

        return row;
    }
    static class ViewHolder {
        TextView Vt1,Vt2;
        LinearLayout layout_action_default,layout_action_custom, btn_add_default;
    }
    void addListDzikr(final C_Item_DzikrName item, final String id_dzikr_default) {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_WRITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try{
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if(status) {
                        PageActivity.navController.navigate(R.id.navigation_today);
                        showDialog(item);
                    } else {
                        Toast.makeText(context, "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terjadi kesalahan mohon coba lagi.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("package_name", BuildConfig.APPLICATION_ID);
                param.put("write", "clone_dzikr");
                param.put("id_dzikr_default", id_dzikr_default);
                param.put("username", AppConfig.getDeviceId(context));
                return param;
            }
        };
        Volley.newRequestQueue(context).add(req);
    }
    void showDialog(final C_Item_DzikrName item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle("Daily Dzikr Organizer");

        alertDialogBuilder.setMessage("Dzikr has been added!");
        alertDialogBuilder.setIcon(R.drawable.app_logo);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                remove(item);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
            filter = new AppFilter<C_Item_DzikrName>(data);
        return filter;
    }

    private class AppFilter<T> extends Filter {
        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself
                    if(object.toString().toLowerCase().contains(filterSeq))filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                //add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = filtered.size(); i < 1; i++)
                add((C_Item_DzikrName)filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
}