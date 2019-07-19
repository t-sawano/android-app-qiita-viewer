package jp.co.cries.qiitaviewer.service

import jp.co.cries.qiitaviewer.entity.ItemEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemService {
    /**
     *
     * @param page 表示するページ番号
     * @param perPage 一度に表示する件数
     */
    @GET("items")
    fun items(
        @Query("page") page: Int ,
        @Query("par_page") perPage: Int
    ): Call<List<ItemEntity>>
}