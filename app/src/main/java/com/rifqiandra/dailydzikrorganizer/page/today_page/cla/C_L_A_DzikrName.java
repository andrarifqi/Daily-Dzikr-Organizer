package com.rifqiandra.dailydzikrorganizer.page.today_page.cla;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rifqiandra.dailydzikrorganizer.BuildConfig;
import com.rifqiandra.dailydzikrorganizer.R;
import com.rifqiandra.dailydzikrorganizer.appconfig.AppConfig;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.form.FormAddActivity;
import com.rifqiandra.dailydzikrorganizer.page.today_page.reminder.AlertReceiver;
import com.rifqiandra.dailydzikrorganizer.page.today_page.reminder.TimePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class C_L_A_DzikrName extends ArrayAdapter<C_Item_DzikrName> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<C_Item_DzikrName> data = new ArrayList<C_Item_DzikrName>();
    private Filter filter;
    View row;
    public TimePicker view;
    public int hourOfDay, minute;

    public C_L_A_DzikrName(Context context, int layoutResourceId, ArrayList<C_Item_DzikrName> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
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
        final ViewHolder holder;

        if (row==null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.Vt1 = (TextView) row.findViewById(R.id.t_id_dzikrname);
            holder.Vt2 = (TextView) row.findViewById(R.id.t_dzikrname);
            holder.btn_delete = (LinearLayout) row.findViewById(R.id.btn_delete);
            holder.btn_reminder = (LinearLayout) row.findViewById(R.id.btn_reminder);
            holder.btn_edit = (LinearLayout) row.findViewById(R.id.btn_edit);
            holder.layout_action_default = (LinearLayout) row.findViewById(R.id.layout_action_default);
            holder.layout_action_custom = (LinearLayout) row.findViewById(R.id.layout_action_custom);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final C_Item_DzikrName item = data.get(position);

        holder.Vt1.setText(item.get_id());
        holder.Vt2.setText(item.get_dzikrname());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item, item.get_id());
            }
        });
        holder.btn_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction(), "time picker");
                onTimeSet(view, hourOfDay, minute);
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormAddActivity.class);
                intent.putExtra("id_dzikrname", item.get_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.layout_action_custom.setVisibility(View.VISIBLE);
        holder.layout_action_default.setVisibility(View.GONE);

        return row;
    }
    void delete_dzikr(final C_Item_DzikrName item, final String id_dzikrname) {
        StringRequest req = new StringRequest(Request.Method.POST, AppConfig.API_WRITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getBaseContext(), "Data JSON " + response, Toast.LENGTH_LONG).show();
                try{
                    JSONObject data = new JSONObject(response);
                    boolean status = data.getBoolean("status");
                    if(status) {
                        remove(item);
                    } else {
                        Toast.makeText(context, "Terjadi kesalahan, Mohon coba lagi.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terjadi kesalahan, mohon coba lagi.", Toast.LENGTH_LONG).show();
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
                param.put("write", "delete_dzikr");
                param.put("id_dzikrname", id_dzikrname);
                return param;
            }
        };
        Volley.newRequestQueue(context).add(req);

    }

    void showDialog(final C_Item_DzikrName item, final String id_dzikrname) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Daily Dzikr Organizer");

        alertDialogBuilder.setMessage("Are you sure want to delete this dzikr?");
        alertDialogBuilder.setIcon(R.drawable.app_logo);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete_dzikr(item, id_dzikrname);
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    static class ViewHolder {
        TextView Vt1,Vt2;
        LinearLayout layout_action_default,layout_action_custom,btn_delete,btn_reminder,btn_edit;
        TimePicker view;

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        startAlarm(c);
    }


    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager)this.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1 * 65 * 1000, pendingIntent);
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


    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}