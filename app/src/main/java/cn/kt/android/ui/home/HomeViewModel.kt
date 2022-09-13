package cn.kt.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.kt.android.bean.BannerData
import cn.kt.android.bean.HomeDataList
import cn.kt.android.net.ApiService
import cn.kt.android.net.BaseResult
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val bannerLiveData: MutableLiveData<Result<BaseResult<List<BannerData>>>> = MutableLiveData()
    val homeLiveData: MutableLiveData<Result<BaseResult<HomeDataList>>> = MutableLiveData()

    fun requestBannerData() {
        viewModelScope.launch {
            val result = try {
                Result.success(ApiService.getApi().bannerData())
            } catch (e: Exception) {
                Result.failure(e)
            }
            bannerLiveData.value = result
        }
    }

    fun requestHomeList(page:Int){
        viewModelScope.launch {
            val result = try {
                Result.success(ApiService.getApi().getHomeList(page))
            } catch (e: Exception) {
                Result.failure(e)
            }
            homeLiveData.value = result
        }
    }

}