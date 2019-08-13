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
    companion object {
        const val PER_PAGE_NUMBER = 5
    }

    /**  */
    private lateinit var itemRepository: ItemRepository
    /**  */
    private var articleList: List<ItemEntity> = mutableListOf()
    /**  */
    private lateinit var itemTouchHelper: ItemTouchHelper
    /**  */
    private var adapter: ItemsRecyclerAdapter? = null
    /**  */
    private var index: Int = 0

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
        this.itemRepository.getItemList(1, perPage = PER_PAGE_NUMBER) {
            /** 取得した記事のリストをセットする */
            this.articleList = it

            /** Viewの初期化 */
            initRecyclerView(view)
        }

        (this.activity as? MainActivity)?.setButtonOnClickListener(getButtonOnclickListener)
    }


    /**
     *
     */
    private fun initRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.article_list)
        this.adapter = ItemsRecyclerAdapter(this.articleList, this.activity as Context)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = this.adapter

        this.itemTouchHelper = ItemTouchHelper(ItemMotionHelper())
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(itemTouchHelper)
    }

    /**
     *
     */
    private val getButtonOnclickListener = object : MainActivity.ButtonOnClickListener {
        override fun onClick() {

            val page = articleList.size / PER_PAGE_NUMBER
            // 20件取得しきれている場合 0
            val completePerPage = articleList.size % PER_PAGE_NUMBER

            if (completePerPage == 0
                && page != index
            ) {
                Toast.makeText(activity as Context ,"記事を取得します" ,Toast.LENGTH_LONG).show()

                index = page + 1
                itemRepository.getItemList(index, PER_PAGE_NUMBER) {
                    /** 取得した記事のリストをセットする */
                    it.forEach { itemEntity ->
                        articleList.plus(itemEntity)
                    }

                    adapter?.notifyDataSetChanged()

                    Log.d("" ,"現在リストのサイズ ->  ${articleList.size}")
                    articleList.forEach {item ->
                        Log.d("" ,"$item")
                    }
                }
            }
        }
    }


    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
