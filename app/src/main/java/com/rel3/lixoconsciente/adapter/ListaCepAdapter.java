package com.rel3.lixoconsciente.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.rel3.lixoconsciente.R;

import java.util.List;

public class ListaCepAdapter extends BaseAdapter {

    private Activity mContext;
    private List<String> mList;
    private LayoutInflater mLayoutInflater = null;

    public ListaCepAdapter(Activity context, List<String> lista){
        this.mContext = context;
        this.mList = lista;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ListaCepViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
            viewHolder = new ListaCepViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ListaCepViewHolder) v.getTag();
        }
        viewHolder.mTVItem.setText(mList.get(position));

        return v;
    }
}
