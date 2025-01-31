package com.eltex.androidschool.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface SchedulersFactory {
    fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
    fun computation() = Schedulers.computation()
    fun io() = Schedulers.io()
    companion object {
        val DEFAULT = object : SchedulersFactory {}
    }
}