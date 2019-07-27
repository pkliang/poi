package com.pkliang.poi.testutils

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber

fun <T> Single<T>.testFirstValue(): T = test().assertNoErrors().values()[0]

fun <T> Observable<T>.testFirstValue(): T = test().assertNoErrors().values()[0]

fun <T> Flowable<T>.testFirstValue(): T = test().assertNoErrors().values()[0]

fun Completable.assertCompleted(): TestObserver<Void> = test().assertNoErrors().assertComplete()

fun <T> Observable<T>.assertResult(predicate: (T) -> Boolean): TestObserver<T> =
    test().assertNoErrors().assertValue { predicate(it) }

fun <T> Single<T>.assertResult(predicate: (T) -> Boolean): TestObserver<T> =
    test().assertNoErrors().assertValue { predicate(it) }

fun <T> Flowable<T>.assertResult(predicate: (T) -> Boolean): TestSubscriber<T> =
    test().assertNoErrors().assertValue { predicate(it) }

fun <T> Single<T>.testErrorFirstValue(): Throwable = test().errors()[0]

fun <T> Observable<T>.testErrorFirstValue(): Throwable = test().errors()[0]

fun <T> Flowable<T>.testErrorFirstValue(): Throwable = test().errors()[0]

fun Completable.testErrorFirstValue(): Throwable = test().errors()[0]
