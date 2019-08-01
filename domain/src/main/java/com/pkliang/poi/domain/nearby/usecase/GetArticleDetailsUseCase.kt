package com.pkliang.poi.domain.nearby.usecase

import com.pkliang.poi.domain.core.UseCase
import com.pkliang.poi.domain.core.scheduler.Scheduler
import com.pkliang.poi.domain.core.scheduler.extension.runOnIo
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetArticleDetailsUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val scheduler: Scheduler
) :
    UseCase<ArticleDetails, Long>() {
    override fun run(params: Long): Observable<ArticleDetails> =
        articleRepository.getArticleDetails(params).runOnIo(scheduler)
}
