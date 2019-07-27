package com.pkliang.poi.testutils

import com.pkliang.poi.domain.core.scheduler.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulerMock : Scheduler {
    override fun io() = Schedulers.trampoline()

    override fun computation() = Schedulers.trampoline()

    override fun newThread() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}
