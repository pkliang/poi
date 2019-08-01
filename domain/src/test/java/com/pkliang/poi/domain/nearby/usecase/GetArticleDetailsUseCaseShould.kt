package com.pkliang.poi.domain.nearby.usecase

import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import com.pkliang.poi.testutils.SchedulerMock
import com.pkliang.poi.testutils.assertResult
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Test

class GetArticleDetailsUseCaseShould {
    private val scheduleMock = SchedulerMock()
    private val articleRepository = mockk<ArticleRepository>()
    private val getArticleDetailsUseCase = GetArticleDetailsUseCase(articleRepository, scheduleMock)

    @Test
    fun `call getArticleDetails() when invoked`() {
        val articles = mockk<ArticleDetails>()
        every {
            articleRepository.getArticleDetails(any())
        } returns Observable.just(articles)


        getArticleDetailsUseCase(1).subscribe()


        verify {
            articleRepository.getArticleDetails(1)
        }
        confirmVerified(articleRepository)
    }

    @Test
    fun `receive article details when invoked`() {
        val articles = mockk<ArticleDetails>()
        every {
            articleRepository.getArticleDetails(any())
        } returns Observable.just(articles)


        getArticleDetailsUseCase(1).assertResult { it == articles }
    }
}
