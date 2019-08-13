package jp.co.cries.qiitaviewer.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.co.cries.qiitaviewer.R
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.util.RoundedTransformation

class ItemsRecyclerAdapter(private val itemsList: List<ItemEntity> ,private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.row_article ,parent ,false)

        return ItemViewHolder(rowView)
    }

    override fun getItemCount(): Int = this.itemsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]

        if (holder is ItemViewHolder) {
            val transform = RoundedTransformation(15 ,1)
            val width = 60
            val height = 60

            /** 作成者のアイコン */
             Picasso.with(this.context).load(item.user.profileImage)
                 .resize(width ,height)
                 .centerCrop()
                 .transform(transform)
                 .into(holder.icon)

            /** 記事タイトル */
            holder.title.text = item.title
            holder.title.ellipsize = TextUtils.TruncateAt.END
            holder.title.setHorizontallyScrolling(true)

            /** 記事作成日付 */
            holder.date.text = item.createDate

            /** 記事作成者名 */
            holder.userName.text = (if (!item.user.name.isNullOrEmpty()) item.user.name else item.user.userName)
            holder.userName.ellipsize = TextUtils.TruncateAt.END
            holder.userName.setHorizontallyScrolling(true)
        }
    }
}