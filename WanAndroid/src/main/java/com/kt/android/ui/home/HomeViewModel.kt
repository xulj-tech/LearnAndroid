package com.kt.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.android.bean.BannerData
import com.kt.android.bean.HomeDataList
import com.kt.android.net.ApiService
import com.kt.android.net.BaseResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val bannerLiveData: MutableLiveData<Result<BaseResult<List<BannerData>>>> = MutableLiveData()
    val homeLiveData: MutableLiveData<Result<BaseResult<HomeDataList>>> = MutableLiveData()

    fun requestBannerData() {
        GlobalScope.launch {
            val result = try {
                Result.success(ApiService.getApi().bannerData())
            } catch (e: Exception) {
                Result.failure(e)
            }
            bannerLiveData.postValue(result)
        }
    }

    fun requestHomeList(page:Int){
        GlobalScope.launch {
            val result = try {
                Result.success(ApiService.getApi().getHomeList(page))
            } catch (e: Exception) {
                Result.failure(e)
            }
            homeLiveData.postValue(result)
        }
    }

}