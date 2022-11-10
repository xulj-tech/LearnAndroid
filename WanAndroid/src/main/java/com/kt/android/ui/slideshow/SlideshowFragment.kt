package com.kt.android.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.common.base.BaseFragment
import com.kt.android.R
import com.kt.android.databinding.FragmentSlideBinding

class SlideshowFragment : BaseFragment<FragmentSlideBinding>() {

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            mBind.textSlideshow.text = it
        })
    }

    override fun getLayoutID(): Int = R.layout.fragment_slide
}