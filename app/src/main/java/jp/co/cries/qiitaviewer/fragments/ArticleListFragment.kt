package jp.co.cries.qiitaviewer.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.cries.qiitaviewer.R
import jp.co.cries.qiitaviewer.activity.MainActivity
import jp.co.cries.qiitaviewer.adapter.ItemsRecyclerAdapter
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.helper.ItemMotionHelper
import jp.co.cries.qiitaviewer.repository.ItemRepository

/**
 * 記事の一覧を表示するフラグメント
 * リストのRowをタップすると記事の中身を表示する...といいなぁ
 */
class ArticleListFragment : Fragment() {
    /**  */
    private lateinit var itemRepository: ItemRepository
    /**  */
    private var articleList: MutableList<ItemEntity> = mutableListOf()
    /**  */
    private lateinit var itemTouchHelper: ItemTouchHelper
    /**  */
    private var adapter: ItemsRecyclerAdapter? = null
    /**  */
    private var index: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.itemRepository = ItemRepository()
        /** 一旦1ページ目の２０件の記事を取得する */
        this.itemRepository.getItemList(index) { items ->
            /** 取得した記事のリストをセットする */
            this.articleList.addAll(items)
            this.index++

            /** Viewの初期化 */
            initRecyclerView(view)
        }

        (this.activity as? MainActivity)?.setButtonOnClickListener(getButtonOnclickListener)
    }


    /**
     * RecyclerViewに関するパラメータの初期化
     */
    private fun initRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.article_list)
        this.adapter = ItemsRecyclerAdapter(this.articleList, this.activity as Context)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = this.adapter

        val itemMotionHelper = ItemMotionHelper()

        this.itemTouchHelper = ItemTouchHelper(itemMotionHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        itemMotionHelper.mItemTouchHelper = this.itemTouchHelper
        itemMotionHelper.setArticleList(this.articleList)

        recyclerView.addItemDecoration(itemTouchHelper)

        this.adapter?.apply {
            setSelectedListener(selectedListener)
            setItemViewClickListener(itemViewClickListFragment)
        }
    }

    /** 選択ボタンタッチ時イベント */
    private val selectedListener = object : ItemsRecyclerAdapter.OnTouchListener {
        override fun onTouch(viewHolder: RecyclerView.ViewHolder) {
            startDragging(viewHolder)
        }
    }

    private val itemViewClickListFragment = object : ItemsRecyclerAdapter.OnClickListener {
        override fun onClick(item: ItemEntity) {
            val fragment = ArticleViewFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragment.setURI(item.url)

            fragmentTransaction?.apply {
                replace(R.id.container ,fragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    /**
     * 記事を取得するボタンクリック時イベント
     */
    private val getButtonOnclickListener = object : MainActivity.ButtonOnClickListener {
        override fun onClick() {
            Toast.makeText(activity as Context, "次の${ItemRepository.PER_PAGE_NUMBER}件を取得します", Toast.LENGTH_LONG).show()

            index++
            itemRepository.getItemList(index) {
                /** 取得した記事のリストをセットする */
                articleList.addAll(articleList.lastIndex, it)

                adapter?.notifyItemRangeInserted(articleList.size, ItemRepository.PER_PAGE_NUMBER)
            }
        }
    }

    /** すぐさまドラッグを開始する */
    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
