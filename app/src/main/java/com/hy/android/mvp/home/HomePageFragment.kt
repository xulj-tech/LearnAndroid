package com.hy.android.mvp.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.hy.android.R
import com.hy.android.activity.ArticleDetailActivity
import com.hy.android.adapter.HomePageAdapter
import com.hy.android.base.BaseFragment
import com.hy.android.mvp.entity.BannerEntity
import com.hy.android.mvp.entity.HomeEntity
import com.hy.android.utils.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_page_home.*

class HomePageFragment : BaseFragment(), HomeContract.View {

    private lateinit var mBanner: Banner
    private lateinit var mAdapter: HomePageAdapter
    private var isLoadMore = false
    private var page = 0

    private lateinit var homeList: ArrayList<HomeEntity.DataBean.DatasBean>

    private val mPresenter: HomePresenter by lazy {
        val presenter = HomePresenter()
        presenter.attachView(this)
        presenter
    }

    override fun getContentLayout(): Int = R.layout.fragment_page_home

    override fun initData() {
        mPresenter.getBanner()
        refresh()
    }

    private fun refresh() {
        isLoadMore = false
        page = 0
        mPresenter.getHomeList(page)
    }

    override fun bindView(view: View?, savedInstanceState: Bundle?) {
        homeList = ArrayList()
        val mHeaderGroup = LayoutInflater.from(activity).inflate(R.layout.home_banner, null) as LinearLayout
        mBanner = mHeaderGroup.findViewById(R.id.head_banner)
        mHeaderGroup.removeView(mBanner)

        mAdapter = HomePageAdapter(R.layout.home_list_item, homeList)
        recyclerView_home.layoutManager = LinearLayoutManager(activity)
        mAdapter.addHeaderView(mBanner)
        recyclerView_home.adapter = mAdapter

        refreshLayout.setOnRefreshListener {
            refresh()
        }

        refreshLayout.setOnLoadMoreListener {
            isLoadMore = true
            page++
            mPresenter.getHomeList(page)
        }

        mAdapter.setOnItemClickListener { _, _, position -> ArticleDetailActivity.startActivity(activity, homeList[position].title, homeList[position].link) }
    }

    override fun setBanner(bean: BannerEntity) {
        if (bean.data.size > 0) {
            showBannerData(bean.data)
        }
    }

    override fun setHomeList(bean: HomeEntity) {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()

        if (!isLoadMore) {
            homeList.clear()
        }
        homeList.addAll(bean.data.datas)
        if (bean.data.datas.size < 20) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        }

        mAdapter.notifyDataSetChanged()
    }

    private fun showBannerData(bannerDataList: List<BannerEntity.DataBean>) {
        val bannerImageList: MutableList<String?> = java.util.ArrayList()
        val mBannerTitleList: MutableList<String> = java.util.ArrayList()
        for (data in bannerDataList) {
            bannerImageList.add(data.imagePath)
            mBannerTitleList.add(data.title)
        }
        //??????banner??????
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
        //?????????????????????
        mBanner.setImageLoader(GlideImageLoader())
        //??????????????????
        mBanner.setImages(bannerImageList)
        //??????banner????????????
        mBanner.setBannerAnimation(Transformer.DepthPage)
        //????????????????????????banner???????????????title??????
        mBanner.setBannerTitles(mBannerTitleList)
        //??????????????????????????????true
        mBanner.isAutoPlay(true)
        //??????????????????
        mBanner.setDelayTime(bannerDataList.size * 400)
        //???????????????????????????banner???????????????????????????
        mBanner.setIndicatorGravity(BannerConfig.CENTER)

        //banner?????????????????????????????????????????????
        mBanner.start()
    }

    override fun onStop() {
        super.onStop()
        mBanner.let { mBanner.stopAutoPlay() }
    }

    override fun onResume() {
        super.onResume()
        mBanner.let { mBanner.startAutoPlay() }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}