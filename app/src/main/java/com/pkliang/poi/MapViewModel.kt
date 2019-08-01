package com.pkliang.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pkliang.poi.core.view.RxViewModel
import com.pkliang.poi.core.view.StateData
import com.pkliang.poi.domain.nearby.usecase.GetArticleDetailsUseCase
import com.pkliang.poi.domain.nearby.usecase.GetNearbyArticlesUseCase
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getNearbyArticlesUseCase: GetNearbyArticlesUseCase,
    private val getArticleDetailsUseCase: GetArticleDetailsUseCase
) : RxViewModel() {

    private val _mapState = MutableLiveData<StateData>()
    private val _articleDetailsState = MutableLiveData<StateData>()

    val mapState: LiveData<StateData>
        get() = _mapState

    val articleDetailsState: LiveData<StateData>
        get() = _articleDetailsState

    init {
        _mapState.value = StateData.Uninitialized
    }

    fun getNearbyArticles() {
        _mapState.value = StateData.Loading
        compositeDisposable.add(
            getNearbyArticlesUseCase(Unit).firstOrError().subscribe({
                _mapState.value = StateData.Success(it)
            }, {
                _mapState.value = StateData.Error(it)
            })
        )
    }

    fun getArticleDetails(id: Long) {
        _articleDetailsState.value = StateData.Loading
        val subscribe = getArticleDetailsUseCase(id).subscribe({
            _articleDetailsState.value = StateData.Success(it)
        }, {
            _articleDetailsState.value = StateData.Error(it)
        })
        compositeDisposable.add(subscribe)
    }
}
