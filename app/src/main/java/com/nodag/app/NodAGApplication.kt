package com.nodag.app

import android.app.Application

class NodAGApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: NodAGApplication
            private set
    }
}
