package jp.co.cries.qiitaviewer.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.service.ItemService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Callback as Callback

/**
 * Qiita APIを操作するクラス
 * URI: https://qiita.com/api/v2/ ...
 */
class ItemRepository {
    companion object {
        /** Qiitaの新着記事取得 RUI */
        const val URI_OF_API = "https://qiita.com/api/v2/"
    }

    private var itemService: ItemService

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(URI_OF_API)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        itemService = retrofit.create(ItemService::class.java)
    }

    /**
     *
     * @param page 表示するページ番号
     * @param perPage 1ページに表示する件数
     * @param callback なにかしてくらさい
     */
    fun getItemList(page: Int ,perPage:  Int ,callback: (List<ItemEntity>) -> Unit) {
        itemService.items(page ,perPage).enqueue(object : Callback<List<ItemEntity>> {

            override fun onResponse(call: Call<List<ItemEntity>>? ,response: Response<List<ItemEntity>>?) {
                response?.let {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            callback(it)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemEntity>>, t: Throwable) {
                Log.d("" ,"取得に失敗しました")
                t.stackTrace.let {
                    Log.d("" ,"$it")
                }
            }
        })
    }
}