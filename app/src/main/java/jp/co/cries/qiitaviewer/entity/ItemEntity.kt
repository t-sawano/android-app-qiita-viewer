package jp.co.cries.qiitaviewer.entity

import com.squareup.moshi.Json

data class ItemEntity(
    @Json(name = "id")
    val id: String ,
    @Json(name = "title")
    val title: String ,
//    @Json(name="body")
//    val body: String ,
    @Json(name = "created_at")
    val createDate: String ,
    @Json(name = "user")
    val user: UserEntity
)

data class UserEntity(
    @Json(name = "id")
    val userName: String ,
    @Json(name = "profile_image_url")
    val profileImage: String
)

/*
title
create_at
user:
    user_name
    icon_img
 */