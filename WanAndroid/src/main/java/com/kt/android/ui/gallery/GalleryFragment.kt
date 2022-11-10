package com.kt.android.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.common.base.BaseFragment
import com.kt.android.R
import com.kt.android.databinding.FragmentGalleryBinding

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            mBind.textGallery.text = it
        })
    }

    override fun getLayoutID(): Int = R.layout.fragment_gallery

}