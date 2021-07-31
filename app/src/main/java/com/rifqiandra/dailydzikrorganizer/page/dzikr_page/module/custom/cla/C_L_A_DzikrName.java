package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rifqiandra.dailydzikrorganizer.R;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.form.FormAddActivity;

import java.util.ArrayList;
import java.util.List;

public class C_L_A_DzikrName extends ArrayAdapter<C_Item_DzikrName>{

    private Context context;
    private int layoutResourceId;
    private ArrayList<C_Item_DzikrName> data = new ArrayList<C_Item_DzikrName>();
    private Filter filter;
    View row;

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
        final C_L_A_DzikrName.ViewHolder holder;

        if (row==null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new C_L_A_DzikrName.ViewHolder();

            holder.Vt1 = (TextView) row.findViewById(R.id.t_id_dzikrname);
            holder.Vt2 = (TextView) row.findViewById(R.id.t_dzikrname);
            holder.layout_action_default = (LinearLayout) row.findViewById(R.id.layout_action_default);
            holder.layout_action_custom = (LinearLayout) row.findViewById(R.id.layout_action_custom);
            holder.btn_reminder = (LinearLayout) row.findViewById(R.id.btn_reminder);
            holder.btn_edit = (LinearLayout) row.findViewById(R.id.btn_edit);
            holder.btn_delete = (LinearLayout) row.findViewById(R.id.btn_delete);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final C_Item_DzikrName item = data.get(position);

        holder.Vt1.setText(item.get_id());
        holder.Vt2.setText(item.get_dzikrname());
        holder.layout_action_custom.setVisibility(View.VISIBLE);
        holder.layout_action_default.setVisibility(View.GONE);
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormAddActivity.class);
                intent.putExtra("id_dzikrname", item.get_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return row;
    }
    static class ViewHolder {
        TextView Vt1,Vt2;
        LinearLayout layout_action_default,layout_action_custom, btn_reminder, btn_edit, btn_delete;
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