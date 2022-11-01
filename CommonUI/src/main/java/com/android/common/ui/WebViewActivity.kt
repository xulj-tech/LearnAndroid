package com.android.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.view.KeyEvent
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import com.android.common.R
import com.android.common.base.BaseActivity
import com.android.common.databinding.ActivityWebviewBinding
import com.android.common.util.Constants
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient

class WebViewActivity : BaseActivity<ActivityWebviewBinding>() {

    private var mAgentWeb: AgentWeb? = null

    companion object {

        fun startActivity(context: Context?, url: String?) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constants.WEB_URL, url)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_webview

    override fun init() {
        setSupportActionBar(mBind.toolbar)
        initData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initData() {
        val pathUrl = intent.getStringExtra(Constants.WEB_URL)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBind.webView, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(getWebViewClient())
            .createAgentWeb()
            .ready()
            .go(pathUrl)
        val webSetting = mAgentWeb?.webCreator?.webView?.settings
        webSetting?.javaScriptEnabled = true
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            mBind.toolbar.title = title
        }
    }

    private fun getWebViewClient(): com.just.agentweb.WebViewClient {
        return object : com.just.agentweb.WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                handler.proceed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb?.handleKeyEvent(keyCode, event) == true) {
            true
        } else super.onKeyDown(keyCode, event)
    }
}