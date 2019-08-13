package jp.co.cries.qiitaviewer.helper

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import jp.co.cries.qiitaviewer.adapter.ItemViewHolder
import kotlin.math.max
import kotlin.math.min

class ItemMotionHelper : ItemTouchHelper.Callback() {

    private fun RecyclerView.ViewHolder.cast() = this as? ItemViewHolder

    private val mItemTouchHelper = ItemTouchHelper(this)

    // Swipe Menu が表示されている Item の foreground
    private var mLockedForeground: View? = null

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
                getDefaultUIUtil().clearView(it)
                mLockedForeground = null
            }
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ) or makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_DRAG,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT
        ) or makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_SWIPE,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        recyclerView.adapter?.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.alpha = 0.8f
        }
    }

    // 2. 行が選択解除された時 (ドロップされた時) このコールバックが呼ばれる。ハイライトを解除する。
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = 1.0f
    }

    // メニューが半分開いたらSwipeにする
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val holder = viewHolder.cast()

        holder?.let {
            return holder.background.width.toFloat() / holder.itemView.width
        }
        return -1f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val foreground = viewHolder.cast()?.foreground

        foreground?.let {
            // 右方向にSwipeした場合
            if (direction == ItemTouchHelper.END) {
                // Itemを固定する
                mLockedForeground = foreground
                foreground.addOnLayoutChangeListener(mUnlockForeground)
            }

            // 左方向にSwipeした場合
            if (direction == ItemTouchHelper.START) {
                // 固定されている場合は解除する
                if (foreground == mLockedForeground) {
                    mLockedForeground = null
                }
            }
        }

        // RecoverAnimation を削除する(notifyItemRemovedの代わり)
        // onChildDrawのコメントの内容を解消するために必要
        mItemTouchHelper?.onChildViewDetachedFromWindow(viewHolder.itemView)
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
        val holder = viewHolder as ItemViewHolder
//        val isDragging = dY < 0 || dY > 0

        when {
            dX == 0f -> { // Drag(移動) or 横移動の終端
                // itemViewを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.itemView, dX, dY, actionState, isCurrentlyActive)

                // 縦移動の場合は、固定を解除する
                if (dY != 0f) {
                    unlockForeground(holder, isForce = true)
                }
            }
            dX > 0f -> { // Swipe(メニュー表示)
                // 固定している場合は何もしない
                if (holder.foreground == mLockedForeground) return

                // backgroundの幅までしか開かない
                val maxWidth = holder.background.width.toFloat()
                val x = min(dX, maxWidth)

                // backgroundを固定して、foregroundだけを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, actionState, isCurrentlyActive)
                getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, 0f, actionState, isCurrentlyActive)

                unlockForeground(holder)
            }
            dX < 0f -> { // Swipe(元に戻す)
                // 固定していない場合は何もしない
                if (holder.foreground != mLockedForeground) return

                // backgroundの幅の間でのみ動く
                val maxWidth = holder.background.width.toFloat()
                val x = max(min(dX + maxWidth, maxWidth), 0f)

                // backgroundを固定して、foregroundだけを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, actionState, isCurrentlyActive)
                getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, 0f, actionState, isCurrentlyActive)
            }
            else -> {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    private fun unlockForeground(selected: ItemViewHolder, isForce: Boolean = false) {
        // 固定を入れ替えるために、固定済みのItemを解除する
        mLockedForeground?.let {
            if (it != selected.foreground || isForce) {
                getDefaultUIUtil().clearView(it)
                mLockedForeground = null
            }
        }
    }
}