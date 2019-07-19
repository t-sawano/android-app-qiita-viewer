package jp.co.cries.qiitaviewer


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.cries.qiitaviewer.R
import jp.co.cries.qiitaviewer.adapter.ItemsRecyclerAdapter
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.repository.ItemRepository
import kotlinx.android.synthetic.main.fragment_article_list.*

/**
 * 記事の一覧を表示するフラグメント
 * リストのRowをタップすると記事の中身を表示する...といいなぁ
 */
class ArticleListFragment : Fragment() {

    private lateinit var itemRepository: ItemRepository
    private lateinit var activityContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.itemRepository = ItemRepository()

        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** 一旦1ページ目の２０件の記事を取得する */
        this.itemRepository.getItemList(1 ,20) {
            /** 取得した記事のリストをセットする */
//            this.itemsList = it

            it.let {
                val recyclerView = articleList
                val adapter = ItemsRecyclerAdapter(it ,activityContext)

                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(this.activity)
                recyclerView.adapter = adapter
            }
        }
    }

    fun setContext(context: Context) {
        this.activityContext = context
    }

}
