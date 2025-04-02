package ir.mahan.wikifoodia.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentLuckyBinding
import ir.mahan.wikifoodia.databinding.FragmentWebViewBinding


@AndroidEntryPoint
class WebViewFragment : Fragment() {
    // binding object
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    // Args
    private val args by navArgs<WebViewFragmentArgs>()
    private lateinit var url: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Back
            backImg.setOnClickListener { findNavController().popBackStack() }
            //WebView
            args.let {
                url = it.url
            }
            setupWebView(url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                builtInZoomControls = false
                useWideViewPort = true
                domStorageEnabled = true
                databaseEnabled = true
                webViewClient = WebViewClient()
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = true
                setSupportZoom(true)
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress<100) {
                            binding.webViewLoading.isVisible = true
                        }
                        if (newProgress==100) {
                            binding.webViewLoading.isVisible = false
                        }
                        binding.webViewLoading.progress = newProgress
                    }
                }
            }// Settings Scope
            loadUrl(url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}