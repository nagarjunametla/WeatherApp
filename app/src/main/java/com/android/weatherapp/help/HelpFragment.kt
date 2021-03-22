package com.android.weatherapp.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.weatherapp.R
import kotlinx.android.synthetic.main.fragment_help.*


class HelpFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.help)
        val webViewSettings = webView.settings
        webViewSettings.allowFileAccess = false
        webViewSettings.allowContentAccess = false
        webView.loadUrl("file:///android_asset/help.html")
    }


}