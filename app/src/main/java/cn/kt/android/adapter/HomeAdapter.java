package cn.kt.android.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import java.util.List;
import cn.kt.android.R;
import cn.kt.android.bean.HomeData;

public class HomeAdapter extends BaseQuickAdapter<HomeData, HomeAdapter.HomeListViewHolder> implements LoadMoreModule {
    public HomeAdapter(int layoutResId, @Nullable List<HomeData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(HomeListViewHolder helper, HomeData item) {
        helper.setText(R.id.homeItemAuthor, item.author);
        helper.setText(R.id.homeItemDate, item.niceDate);
        helper.setText(R.id.homeItemTitle, item.title);
        helper.setText(R.id.homeItemType, item.chapterName);
    }

    public static class HomeListViewHolder extends BaseViewHolder {

        public TextView homeItemAuthor;
        public TextView homeItemDate;
        public TextView homeItemTitle;
        public TextView homeItemType;
        public ImageView homeItemLike;

        public HomeListViewHolder(View view) {
            super(view);
            homeItemAuthor=view.findViewById(R.id.homeItemAuthor);
            homeItemDate=view.findViewById(R.id.homeItemDate);
            homeItemTitle=view.findViewById(R.id.homeItemTitle);
            homeItemType=view.findViewById(R.id.homeItemType);
            homeItemLike=view.findViewById(R.id.homeItemLike);
        }
    }

}
