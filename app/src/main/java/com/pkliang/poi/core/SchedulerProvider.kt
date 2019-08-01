package com.pkliang.poi.core

import com.pkliang.poi.domain.core.scheduler.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider : Scheduler {
    override fun io(): io.reactivex.Scheduler = Schedulers.io()

    override fun computation(): io.reactivex.Scheduler = Schedulers.computation()

    override fun newThread(): io.reactivex.Scheduler = Schedulers.newThread()

    override fun ui(): io.reactivex.Scheduler = AndroidSchedulers.mainThread()
}
