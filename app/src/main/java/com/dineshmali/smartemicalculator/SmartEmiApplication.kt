package com.dineshmali.smartemicalculator

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.dineshmali.smartemicalculator.data.local.AppDatabase
import com.dineshmali.smartemicalculator.data.repository.CalculationRepository

class SmartEmiApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CalculationRepository(database.savedCalculationDao()) }

    override fun onCreate() {
        super.onCreate()
        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this) {}
    }
}
