package com.rel3.lixoconsciente.adapter;

import android.view.View;
import android.widget.TextView;

import com.rel3.lixoconsciente.R;

public class ListaCepViewHolder {

        public TextView mTVItem;

        public ListaCepViewHolder(View base) {
            mTVItem = (TextView) base.findViewById(R.id.listTV);
        }
}

