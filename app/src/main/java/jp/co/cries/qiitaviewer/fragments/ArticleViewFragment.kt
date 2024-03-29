package jp.co.cries.qiitaviewer.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.cries.qiitaviewer.R

/**
 * 記事一覧画面からタップした記事の中身をWebViewerで表示するフラグメント
 */
class ArticleViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_view, container, false)
    }


}
