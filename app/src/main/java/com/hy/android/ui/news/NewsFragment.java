package com.hy.android.ui.news;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.hy.android.R;
import com.hy.android.adapter.ChannelPagerAdapter;
import com.hy.android.base.BaseFragment;
import com.hy.android.base.SupportFragment;
import com.hy.android.bean.Channel;
import com.hy.android.database.ChannelDao;
import com.hy.android.event.NewChannelEvent;
import com.hy.android.event.SelectChannelEvent;
import com.hy.android.ui.news.contract.NewsContract;
import com.hy.android.ui.news.presenter.NewsPresenter;
import com.hy.android.widget.ChannelDialogFragment;
import com.hy.android.widget.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class NewsFragment extends BaseFragment implements NewsContract.View {

    private static final String TAG = "NewsFragment";

    @BindView(R.id.viewpager)
    CustomViewPager mViewpager;
    @BindView(R.id.iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout SlidingTabLayout;

    private ChannelPagerAdapter mChannelPagerAdapter;

    private List<Channel> mSelectedDatas;
    private List<Channel> mUnSelectedDatas;

    private int selectedIndex;
    private String selectedChannel;

    private NewsPresenter mPresenter;

    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news_new;
    }


    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mPresenter=new NewsPresenter();
        mPresenter.attachView(this);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                selectedChannel = mSelectedDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initData() {
        mSelectedDatas = new ArrayList<>();
        mUnSelectedDatas = new ArrayList<>();
        mPresenter.getChannel();
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> unSelectedDatas) {
        if (channels != null) {
            mSelectedDatas.clear();
            mSelectedDatas.addAll(channels);
            mUnSelectedDatas.clear();
            mUnSelectedDatas.addAll(unSelectedDatas);
            mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), channels);
            mViewpager.setAdapter(mChannelPagerAdapter);
            mViewpager.setOffscreenPageLimit(2);
            mViewpager.setCurrentItem(0, false);
            SlidingTabLayout.setViewPager(mViewpager);
        } else {
            toast("????????????");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateChannel(NewChannelEvent event) {
        if (event == null) return;
        if (event.selectedDatas != null && event.unSelectedDatas != null) {
            mSelectedDatas = event.selectedDatas;
            mUnSelectedDatas = event.unSelectedDatas;
            mChannelPagerAdapter.updateChannel(mSelectedDatas);
            SlidingTabLayout.notifyDataSetChanged();
            ChannelDao.saveChannels(event.allChannels);

            List<String> integers = new ArrayList<>();
            for (Channel channel : mSelectedDatas) {
                integers.add(channel.getChannelName());
            }
            if (TextUtils.isEmpty(event.firstChannelName)) {
                if (!integers.contains(selectedChannel)) {
                    selectedChannel = mSelectedDatas.get(selectedIndex).getChannelName();
                    mViewpager.setCurrentItem(selectedIndex, false);
                } else {
                    setViewpagerPosition(integers, selectedChannel);
                }
            } else {
                setViewpagerPosition(integers, event.firstChannelName);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectChannelEvent(SelectChannelEvent selectChannelEvent) {
        if (selectChannelEvent == null) return;
        List<String> integers = new ArrayList<>();
        for (Channel channel : mSelectedDatas) {
            integers.add(channel.getChannelName());
        }
        setViewpagerPosition(integers, selectChannelEvent.channelName);
    }

    /**
     * ?????? ???????????????
     *
     * @param integers
     * @param channelName
     */
    private void setViewpagerPosition(List<String> integers, String channelName) {
        if (TextUtils.isEmpty(channelName) || integers == null) return;
        for (int j = 0; j < integers.size(); j++) {
            if (integers.get(j).equals(channelName)) {
                selectedChannel = integers.get(j);
                selectedIndex = j;
                break;
            }
        }
        mViewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewpager.setCurrentItem(selectedIndex, false);
            }
        }, 100);
    }

    @OnClick(R.id.iv_edit)
    public void onViewClicked() {
        ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance(mSelectedDatas, mUnSelectedDatas);
        dialogFragment.show(getChildFragmentManager(), "CHANNEL");
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
        super.onDestroyView();
    }
}
