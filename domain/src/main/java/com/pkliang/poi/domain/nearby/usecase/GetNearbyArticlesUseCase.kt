package com.pkliang.poi.domain.nearby.usecase

import com.pkliang.poi.domain.core.UseCase
import com.pkliang.poi.domain.core.scheduler.Scheduler
import com.pkliang.poi.domain.core.scheduler.extension.runOnIo
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import com.pkliang.poi.domain.nearby.repository.GeolocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetNearbyArticlesUseCase @Inject constructor(
    private val geolocationRepository: GeolocationRepository,
    private val articleRepository: ArticleRepository,
    private val scheduler: Scheduler
) : UseCase<List<Article>, Unit>() {

    override fun run(params: Unit): Observable<List<Article>> =
        geolocationRepository.getCurrentGeolocation().switchMap { geolocation ->
            articleRepository.getNearbyArticlesByGeoLocation(geolocation).onExceptionResumeNext {
                it.onNext(emptyList())
            }.runOnIo(scheduler)
        }
}
