package jp.co.cries.qiitaviewer.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.co.cries.qiitaviewer.R

/**
 * 新着記事一覧に表示するRowのViewHolder
 */
class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
    /** 記事作成者アイコン */
    val icon: ImageView = view.findViewById(R.id.articleIcon)

    /** 記事タイトル */
    val title: TextView = view.findViewById(R.id.articleTitle)

    /** 記事作成日時 */
    val date: TextView = view.findViewById(R.id.articleCreateDate)

    /** 記事作成者名 */
    val userName: TextView = view.findViewById(R.id.articleUserName)
}