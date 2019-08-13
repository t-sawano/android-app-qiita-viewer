package jp.co.cries.qiitaviewer.helper

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import jp.co.cries.qiitaviewer.adapter.ItemViewHolder
import jp.co.cries.qiitaviewer.adapter.ItemsRecyclerAdapter
import jp.co.cries.qiitaviewer.entity.ItemEntity
import java.sql.Time
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ItemMotionHelper : ItemTouchHelper.Callback() {
    /**  */
    var mItemTouchHelper: ItemTouchHelper? = null

    private lateinit var articleList: MutableList<ItemEntity>

    // Swipe Menu が表示されている Item の foreground
    private var mLockedForeground: View? = null
    private var mLockedBackground: View? = null
    private var recyclerViewAdapter: ItemsRecyclerAdapter? = null

    private fun RecyclerView.ViewHolder.cast() = this as? ItemViewHolder

    // RecyclerView に変更があった場合に固定を解除する
    private val mUnlockForeground = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            // 複数のリスナーが登録されないようにする
            view?.removeOnLayoutChangeListener(this)
            // 固定されている場合は解除する
            mLockedForeground?.let {
                clearLockedForeground()
            }
        }
    }

    private val mUnlockedBackground = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            // 複数のリスナーが登録されないようにする
            view?.removeOnLayoutChangeListener(this)
            // 固定されている場合は解除する
            mLockedBackground?.let {
                clearLockedBackground()
            }
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = viewHolder.adapterPosition

        val item = articleList.removeAt(from)
        articleList.add(to, item)

        recyclerView.adapter?.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.alpha = 0.5f
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
        super.clearView(recyclerView, viewHolder)
    }

    // メニューが半分開いたらSwipeにする
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val holder = viewHolder.cast()

        holder?.let {
            return holder.background.width.toFloat() / holder.itemView.width / -3
        }
        return super.getSwipeThreshold(viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val holder = viewHolder.cast()

        holder?.let {
            val foreground = it.foreground
            val background = it.background

            when (direction) {
                // 右にスワイプした場合
                ItemTouchHelper.END -> {
                    // 固定されている場合は解除する
                    if (foreground == mLockedForeground
                        && background == mLockedBackground
                    ) {
                        mLockedForeground = null
                        mLockedBackground = null
                    }
                }

                // 左にスワイプした場合
                ItemTouchHelper.START -> {
                    // Itemを固定する
                    mLockedForeground = foreground
                    mLockedBackground = background

                    foreground.addOnLayoutChangeListener(mUnlockForeground)
                    background.addOnLayoutChangeListener(mUnlockedBackground)

                    mLockedBackground?.setOnClickListener(
                        deleteButtonClickListener(
                            recyclerViewAdapter,
                            holder.adapterPosition
                        )
                    )
                }
            }

            mItemTouchHelper?.onChildViewDetachedFromWindow(holder.itemView)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val holder = viewHolder.cast()

        holder?.let {
            when {
                dX == 0f -> {
                    if (dY != 0f) {
                        unlockItemView(holder, isForce = true)
                    }
                }

                /** 右にスワイプ */
                dX > 0f -> {
                    // 固定していない場合は何もしない
                    if (holder.foreground != mLockedForeground) return

                    val maxWidth = holder.background.width.toFloat()
                    val x = max(maxWidth - dX, 0f)

                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        holder.background,
                        0f,
                        0f,
                        actionState,
                        isCurrentlyActive
                    )
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        holder.foreground,
                        -x,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    return@onChildDraw
                }

                /** 左にスワイプ */
                dX < 0f -> {
                    // 固定している場合は何もしない
                    if (holder.foreground == mLockedForeground) return

                    recyclerViewAdapter = recyclerView.adapter as? ItemsRecyclerAdapter

                    val maxWidth = holder.background.width.toFloat()
                    val x = max(min(-dX, maxWidth), 0f)

                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        holder.background,
                        0f,
                        0f,
                        actionState,
                        isCurrentlyActive
                    )
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        holder.foreground,
                        -x,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                    unlockItemView(holder)
                    return@onChildDraw
                }
            }

            // 指定の動作以外はデフォルトにする
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun deleteButtonClickListener(adapter: ItemsRecyclerAdapter?, position: Int) = View.OnClickListener {
        adapter?.let { adapter ->
            articleList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun unlockItemView(selected: ItemViewHolder, isForce: Boolean = false) {
        // 固定を入れ替えるために、固定済みのItemを解除する
        // foreground
        mLockedForeground?.let {
            if (it != selected.foreground || isForce) {
                this.clearLockedForeground()
            }
        }
        // background
        mLockedBackground?.let {
            if (it != selected.background || isForce) {
                this.clearLockedBackground()
            }
        }
    }

    private fun clearLockedForeground() {
        getDefaultUIUtil().clearView(mLockedForeground)
        mLockedForeground = null
    }

    private fun clearLockedBackground() {
        getDefaultUIUtil().clearView(mLockedBackground)
        mLockedBackground?.setOnClickListener(null)
        mLockedBackground = null
    }

    fun setArticleList(list: MutableList<ItemEntity>) {
        this.articleList = list
    }
}