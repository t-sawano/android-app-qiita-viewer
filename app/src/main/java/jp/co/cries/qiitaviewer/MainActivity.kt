package jp.co.cries.qiitaviewer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.repository.ItemRepository
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ArticleListFragment() as? ArticleListFragment
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragment?.let {
            fragment.setContext(this)

            /** 初期表示を新着記事の一覧にする。 */
            fragmentTransaction
                .replace(R.id.container, it)
                .commit()
        }

        buttonGetArticle?.setOnClickListener {
            Log.d("MainActivity#onCreate" ,"記事を取得するボタンが押下されました。")

        }

    }

}
