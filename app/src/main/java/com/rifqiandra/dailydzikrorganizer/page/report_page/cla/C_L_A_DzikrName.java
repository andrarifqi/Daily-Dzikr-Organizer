package com.rifqiandra.dailydzikrorganizer.page.report_page.cla;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rifqiandra.dailydzikrorganizer.R;

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
        return Long.parseLong(data.get(position).get_dzikrname());
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

            holder.Vt1 = (TextView) row.findViewById(R.id.tv_dzikrname);
            holder.Vt2 = (TextView) row.findViewById(R.id.tv_current_recitation);
            holder.Vt3 = (TextView) row.findViewById(R.id.tv_dzikr_target);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final C_Item_DzikrName item = data.get(position);

        holder.Vt1.setText(item.get_dzikrname());
        holder.Vt2.setText("Current Recitation : " + item.get_c_recitation());
        holder.Vt3.setText("Dzikr Target : " + item.get_dzikr_target());


        return row;
    }

    static class ViewHolder {
        TextView Vt1,Vt2,Vt3;
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