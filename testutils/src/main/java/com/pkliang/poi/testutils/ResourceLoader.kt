package com.pkliang.poi.testutils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.URLDecoder

object ResourceLoader {

    fun loadContentFromFile(fileName: String? = null): String {
        if (fileName.isNullOrEmpty()) {
            return ""
        }

        val inputStream = this::class.java.classLoader?.getResourceAsStream(fileName)
        BufferedReader(InputStreamReader(inputStream) as Reader?).use { br ->
            val sb = StringBuilder()
            var line = br.readLine()

            while (line != null) {
                sb.append(line.trim())
                line = br.readLine()
            }
            return sb.toString()
        }
    }

    fun getResourcePath(fileName: String): String =
        URLDecoder.decode(this::class.java.classLoader?.getResource(fileName)?.file, "UTF-8")
}
