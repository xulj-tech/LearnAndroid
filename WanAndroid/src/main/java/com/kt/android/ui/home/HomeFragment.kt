package com.kt.android.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.common.base.BaseFragment
import com.android.common.ui.WebViewActivity
import com.kt.android.R
import com.kt.android.adapter.HomeAdapter
import com.kt.android.bean.BannerData
import com.kt.android.bean.HomeData
import com.kt.android.databinding.FragmentHomeBinding
import com.kt.android.util.GlideImageLoader
import com.kt.android.util.SpacesItemDecoration
import com.kt.android.util.dp
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        private const val TAG = "HomeFragment"
    }
    private val pageSize = 20
    private lateinit var homeViewModel: HomeViewModel
    private var mBanner: Banner? = null
    private var mAdapter: HomeAdapter? = null
    private var homeDataList: ArrayList<HomeData>? = null
    private var bannerDataList: ArrayList<BannerData>? = null

    override fun getLayoutID(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initData()
        initView()
    }


    private fun initData() {
        homeViewModel.requestBannerData()
        homeViewModel.requestHomeList(0)
    }

    private fun initView() {
        homeDataList = ArrayList<HomeData>()
        bannerDataList = ArrayList<BannerData>()
        val mHeaderGroup =
            LayoutInflater.from(activity).inflate(R.layout.home_banner, null) as LinearLayout
        mBanner = mHeaderGroup.findViewById(R.id.head_banner)
        mHeaderGroup.removeView(mBanner)

        mBind.recyclerView.layoutManager = LinearLayoutManager(activity)
        mBind.recyclerView.addItemDecoration(SpacesItemDecoration(8f.dp.toInt()))
        mAdapter = HomeAdapter(R.layout.home_list_item, homeDataList)
        mAdapter!!.addHeaderView(mBanner!!)
        mBind.recyclerView.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            WebViewActivity.startActivity(activity,mAdapter?.getItem(position)?.link)
        }

        mBind.swipeRefreshLayout.setOnRefreshListener {
            mBind.swipeRefreshLayout.isRefreshing = true
            mAdapter?.loadMoreModule?.isEnableLoadMore = false
            homeViewModel.requestHomeList(0)
        }

        mAdapter?.loadMoreModule?.setOnLoadMoreListener {
            mAdapter?.let {
                mBind.swipeRefreshLayout.isRefreshing = false
                it.loadMoreModule.isEnableLoadMore = true
                val page = it.data.size / pageSize + 1
                homeViewModel.requestHomeList(page)
            }
        }
        mAdapter?.loadMoreModule?.isAutoLoadMore = true
        mAdapter?.loadMoreModule?.isEnableLoadMoreIfNotFullPage = false

        homeViewModel.bannerLiveData.observe(viewLifecycleOwner) { result ->
            val bannerResult = result.getOrNull()
            if (null == bannerResult || bannerResult.data.isEmpty()) {
                Log.d(TAG, "get banner data failed")
                return@observe
            }
            bannerDataList?.let {
                it.addAll(bannerResult.data)
                showBannerData(it)
            }
        }

        homeViewModel.homeLiveData.observe(viewLifecycleOwner) { result ->
            val bannerResult = result.getOrNull()
            if (null == bannerResult || bannerResult.data.datas.isEmpty()) {
                Log.d(TAG, "get home data failed")
                return@observe
            }
            mBind.swipeRefreshLayout.isRefreshing = false
            mAdapter?.loadMoreModule?.isEnableLoadMore = true
            val data = bannerResult.data.datas
            Log.d(TAG, "data size -- ${data.size}")
            val page = data.size / pageSize + 1
            if (page == 0) {
                mAdapter?.setList(data)
            } else {
                mAdapter?.addData(data)
            }
            if (data.size < pageSize) {
                mAdapter?.loadMoreModule?.loadMoreEnd()
            } else {
                mAdapter?.loadMoreModule?.loadMoreComplete()
            }
        }
    }

    private fun showBannerData(bannerDataList: List<BannerData>) {
        val bannerImageList: MutableList<String?> = ArrayList()
        val mBannerTitleList: MutableList<String> = ArrayList()
        for (data in bannerDataList) {
            bannerImageList.add(data.imagePath)
            mBannerTitleList.add(data.title)
        }
        mBanner?.let {
            //设置banner样式
            it.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            //设置图片加载器
            it.setImageLoader(GlideImageLoader())
            //设置图片集合
            it.setImages(bannerImageList)
            //设置banner动画效果
            it.setBannerAnimation(Transformer.DepthPage)
            //设置标题集合（当banner样式有显示title时）
            it.setBannerTitles(mBannerTitleList)
            //设置自动轮播，默认为true
            it.isAutoPlay(true)
            //设置轮播时间
            it.setDelayTime(3000)
            //设置指示器位置（当banner模式中有指示器时）
            it.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            it.start()
        }
    }
}