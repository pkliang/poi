package com.pkliang.poi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pkliang.poi.core.view.StateData
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import com.pkliang.poi.domain.nearby.usecase.GetArticleDetailsUseCase
import com.pkliang.poi.domain.nearby.usecase.GetNearbyArticlesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MapViewModelShould {
    @Rule
    @JvmField
    val testRule: TestRule = InstantTaskExecutorRule()

    private val getNearbyArticlesUseCase = mockk<GetNearbyArticlesUseCase>()
    private val getArticleDetailsUseCase = mockk<GetArticleDetailsUseCase>()
    private val observer = spyk<Observer<StateData>>()

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        mapViewModel = MapViewModel(getNearbyArticlesUseCase, getArticleDetailsUseCase)
    }

    @Test
    fun `emmit correct states when getNearbyArticles called and a list of articles returned`() {
        val article = mockk<Article>()
        every {
            getNearbyArticlesUseCase(Unit)
        } returns Observable.just(listOf(article))
        mapViewModel.mapState.observeForever(observer)


        mapViewModel.getNearbyArticles()


        verifySequence {
            observer.onChanged(StateData.Uninitialized)
            observer.onChanged(StateData.Loading)
            observer.onChanged(StateData.Success(listOf(article)))
        }
    }

    @Test
    fun `emmit correct states when getNearbyArticles called and an error returned`() {
        val exception = mockk<Exception>()
        every {
            getNearbyArticlesUseCase(Unit)
        } returns Observable.error(exception)
        mapViewModel.mapState.observeForever(observer)


        mapViewModel.getNearbyArticles()


        verifySequence {
            observer.onChanged(StateData.Uninitialized)
            observer.onChanged(StateData.Loading)
            observer.onChanged(StateData.Error(exception))
        }
    }

    @Test
    fun `emmit correct states when getArticleDetails called and article details returned`() {
        val articleDetails = mockk<ArticleDetails>()
        val id = 1L
        every {
            getArticleDetailsUseCase(id)
        } returns Observable.just(articleDetails)
        mapViewModel.articleDetailsState.observeForever(observer)


        mapViewModel.getArticleDetails(id)


        verifySequence {
            observer.onChanged(StateData.Loading)
            observer.onChanged(StateData.Success(articleDetails))
        }
    }

    @Test
    fun `emmit correct states when getArticleDetails called and an error returned`() {
        val exception = mockk<Exception>()
        val id = 1L
        every {
            getArticleDetailsUseCase(id)
        } returns Observable.error(exception)
        mapViewModel.articleDetailsState.observeForever(observer)


        mapViewModel.getArticleDetails(id)


        verifySequence {
            observer.onChanged(StateData.Loading)
            observer.onChanged(StateData.Error(exception))
        }
    }
}
