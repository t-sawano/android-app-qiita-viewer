package jp.co.cries.qiitaviewer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.cries.qiitaviewer.R

/**
 * 記事の一覧を表示するフラグメント
 * リストのRowをタップすると記事の中身を表示する...といいなぁ
 */
class ArticleListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }


}
