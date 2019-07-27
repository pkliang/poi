package com.pkliang.poi.domain.core

import io.reactivex.Observable

abstract class UseCase<Type, in Params> {

    abstract fun run(params: Params): Observable<Type>

    operator fun invoke(params: Params): Observable<Type> = run(params)
}
