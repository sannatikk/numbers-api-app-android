package com.example.nerdyfactsapp

import android.app.Application
import com.example.nerdyfactsapp.data.AppContainer
import com.example.nerdyfactsapp.data.DefaultAppContainer

class NerdyFactsApplication : Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}