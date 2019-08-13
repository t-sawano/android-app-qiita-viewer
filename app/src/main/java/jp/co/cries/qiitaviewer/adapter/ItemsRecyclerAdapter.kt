package jp.co.cries.qiitaviewer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.co.cries.qiitaviewer.R
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.fragments.ArticleListFragment
import jp.co.cries.qiitaviewer.fragments.ArticleViewFragment
import jp.co.cries.qiitaviewer.util.RoundedTransformation


class ItemsRecyclerAdapter(private val itemsList: List<ItemEntity>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var selectedTouchListener: OnTouchListener

    private lateinit var itemViewClickListener: OnClickListener

    companion object {
        const val IMAGE_ICON_WIDTH = 60
        const val IMAGE_ICON_HEIGHT = 60
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.row_article, parent, false)
        return ItemViewHolder(rowView)
    }

    override fun getItemCount(): Int = this.itemsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]

        if (holder is ItemViewHolder) bindingItemViewHolder(holder, item, position)
    }

    /**  */
    @SuppressLint("ClickableViewAccessibility")
    private fun bindingItemViewHolder(holder: ItemViewHolder, item: ItemEntity ,position: Int) {
        val transform = RoundedTransformation(15, 1)
        val width = IMAGE_ICON_WIDTH
        val height = IMAGE_ICON_HEIGHT


        /** 作成者のアイコン */
        Picasso.with(this.context).load(item.user.profileImage)
            .resize(width, height)
            .centerCrop()
            .transform(transform)
            .into(holder.icon)

        /** 記事タイトル */
        holder.title.text = item.title

        /** 記事作成日付 */
        holder.date.text = item.createDate

        /** 記事作成者名 */
        holder.userName.text = (if (!item.user.name.isNullOrEmpty()) item.user.name else item.user.userName)

        holder.draggingIcon.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                ACTION_DOWN -> {
                    selectedTouchListener.onTouch(holder)
                }
            }

            return@setOnTouchListener true
        }

        holder.foreground.setOnClickListener {
            itemViewClickListener.onClick(item)
        }

    }

    interface OnTouchListener {
        fun onTouch(viewHolder: RecyclerView.ViewHolder)
    }

    fun setSelectedListener(listener: OnTouchListener) {
        this.selectedTouchListener = listener
    }

    interface OnClickListener {
        fun onClick(item: ItemEntity)
    }

    fun setItemViewClickListener(listener: OnClickListener) {
        this.itemViewClickListener = listener
    }
}