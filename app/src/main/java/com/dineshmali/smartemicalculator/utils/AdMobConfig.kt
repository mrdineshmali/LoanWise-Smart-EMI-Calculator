package com.dineshmali.smartemicalculator.utils

object AdMobConfig {
    /**
     * Official Google Test Ad Unit IDs.
     * Replace these with your production AdMob Ad Unit IDs before uploading to Play Store.
     */
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    /**
     * Dynamic runtime toggle flag to enable or disable monetization across the entire app.
     * Non-const to prevent Kotlin compiler constant inlining.
     * Set to true to show ads, or false to completely hide ads.
     */
    var IS_MONETIZATION_ENABLED: Boolean = false
}
