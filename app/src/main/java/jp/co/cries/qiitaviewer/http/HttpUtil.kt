package jp.co.cries.qiitaviewer.http

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request


class HttpUtil {

    /**
     * 検証用
     * 記事を全件取得する？はず
     */
    fun getApiItems(uri: String): String {
        Log.d("HttpUtil" ,"uri -> $uri")

        // OkHttp3を呼び出して、GETする
        val client = OkHttpClient()
        val request = Request.Builder()
                        .url(uri)
                        .build()

        val call = client.newCall(request)

        val response = call.execute()
        val body = response.body

        var result = ""

        body?.let {
            result = it.string()

            Log.d("ApiListener" ,"レスポンスを取得しました。")
        }

        /** 取得に失敗した場合は空文字が返却される */
        return result
    }


}