[![CircleCI](https://circleci.com/gh/pkliang/poi/tree/master.svg?style=svg&circle-token=b7a1a99a3359cf3220e70832f778490450482f48)](https://circleci.com/gh/pkliang/poi/tree/master)
# poi
Android application to fetch the current location of the device and get nearby English language Wikipedia articles.

# Tech Stack

* [Kotlin][1]
* [RxJava & RxAndroid][2]
* [Dagger 2][3]
* [Retrofit][4]
* [OkHttp][5]
* [Junit][6]
* [Mockk][7]
* [Hamcrest][8]
* [MockWebServer][9]
* [Architecture Components][10]


# Architecture: Clean + MVVM

There are three main modules,

* `domain` contains business objects,  usecases and repositories. It is a java module.

* `data` provides repository implementations.

* `app` is the presentation layer which uses MVVM pattern. Dagger is used as DI in this module

<img width="1381" alt="captura de pantalla 2018-02-23 a la s 11 39 01" src="https://user-images.githubusercontent.com/5893477/36608070-4cd45166-188e-11e8-977a-fc6e1cd8b359.png">

# Testing

* domain module: JUnit and Mockk for unit tests are used here.

* data module: Robolectric, MockWebServer, JUnit and Mockk for unit tests are used here.

* data module: JUnit and Mockk for unit tests are used here. There is no UI tests(Espresso) due to time limit.

# Code Standard

[Detekt][10] is used for static code analysis for Kotlin.

.idea/codeSytles folder is shared in the repository.

# Branches
* Main branch is `master`

* Work on `feature branches` and do PRs to merge them to `master`.

# Continues integration
* CircleCI is used as CI

# Known issues
* [RxLocation][11] is used for retrieving location, more tests needed on different phone models for these library.


[1]: https://kotlinlang.org/
[2]: https://github.com/ReactiveX/RxAndroid
[3]: https://github.com/google/dagger
[4]: https://github.com/square/retrofit
[5]: https://github.com/square/okhttp
[6]: http://developer.android.com/intl/es/reference/junit/framework/package-summary.html
[7]: https://mockk.io/
[8]: http://hamcrest.org/
[9]: https://github.com/square/okhttp/tree/master/mockwebserver
[10]: https://developer.android.com/topic/libraries/architecture/index.html
[11]: https://github.com/patloew/RxLocation
[12]: https://github.com/arturbosch/detekt
