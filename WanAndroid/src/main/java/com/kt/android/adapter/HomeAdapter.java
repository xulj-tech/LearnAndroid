package com.kt.android.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kt.android.R;
import com.kt.android.bean.HomeData;

import java.util.List;


public class HomeAdapter extends BaseQuickAdapter<HomeData, HomeAdapter.HomeListViewHolder> implements LoadMoreModule {
    public HomeAdapter(int layoutResId, @Nullable List<HomeData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(HomeListViewHolder helper, HomeData item) {
        helper.setText(R.id.tv_date, item.niceDate);
        helper.setText(R.id.tv_title, item.title);
    }

    public static class HomeListViewHolder extends BaseViewHolder {
        public TextView tv_title;
        public TextView tv_date;
        public ImageView img_like;

        public HomeListViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_date = view.findViewById(R.id.tv_date);
            img_like = view.findViewById(R.id.img_like);
        }
    }

}
