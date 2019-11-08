package com.example.huangcl.batteryinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BatteryAdapter extends ArrayAdapter<Data> {

    List<Data> mDataList;
    Context mContext;
    int mResource;
    
    public BatteryAdapter(@NonNull Context context, int resource, @NonNull List<Data> objects) {
        super(context, resource, objects);
        
        mContext=context;
        mResource=resource;
        mDataList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Data data=mDataList.get(position);
        View view;
        ViewHolder holder;
        
        if(convertView==null) {
            view= LayoutInflater.from(mContext).inflate(mResource,parent,false);
            holder=new ViewHolder(view);
            
            view.setTag(holder);
        }
        else {
            view=convertView;
            holder=(ViewHolder)view.getTag();
        }
        
        holder.batteryTextView.setText(data.getData());
        
        return view;
    }
    
    class ViewHolder {
        TextView batteryTextView;
        
        public ViewHolder(View view) {
            batteryTextView=(TextView)view.findViewById(R.id.tv_battery_info);
        }
    }
}
