package com.pkliang.poi.core.view

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class RxViewModel : ViewModel() {

    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        clearCompositeDisposable()
    }

    private fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}
