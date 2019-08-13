package jp.co.cries.qiitaviewer.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.fragment.app.Fragment
import jp.co.cries.qiitaviewer.R

/**
 * 記事一覧画面からタップした記事の中身をWebViewerで表示するフラグメント
 */
class ArticleViewFragment : Fragment() {

    private lateinit var articleURI: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val web: WebView = view.findViewById(R.id.article_viewer)
        val backButton: ImageView = view.findViewById(R.id.return_button)

        web.webViewClient = WebViewClient()
        web.settings.javaScriptEnabled = true
        web.loadUrl(this.articleURI)

        backButton.setOnClickListener {
            val fragment = ArticleListFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.let {
                /** 初期表示を新着記事の一覧にする。 */
                fragmentTransaction
                    .replace(R.id.container, fragment)
                    .commit()
            }
        }
    }

    fun setURI(uri: String) {
        this.articleURI = uri
    }

}
