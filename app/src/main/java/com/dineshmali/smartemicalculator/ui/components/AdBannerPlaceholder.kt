package com.dineshmali.smartemicalculator.ui.components

import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.dineshmali.smartemicalculator.utils.AdMobConfig

@Composable
fun AdBannerPlaceholder(modifier: Modifier = Modifier) {
    if (!AdMobConfig.IS_MONETIZATION_ENABLED) return

    var isAdLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                val displayMetrics = ctx.resources.displayMetrics
                val widthPixels = displayMetrics.widthPixels
                val density = displayMetrics.density
                val adWidth = (widthPixels / density).toInt().coerceAtLeast(320)

                val adaptiveAdSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, adWidth)

                AdView(ctx).apply {
                    setAdSize(adaptiveAdSize)
                    adUnitId = AdMobConfig.BANNER_AD_UNIT_ID
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            isAdLoaded = true
                            Log.d("AdMob", "Banner Ad loaded successfully!")
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            isAdLoaded = false
                            Log.e("AdMob", "Banner Ad failed to load: ${error.message} (code: ${error.code})")
                        }
                    }
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
