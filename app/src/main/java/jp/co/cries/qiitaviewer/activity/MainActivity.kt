package jp.co.cries.qiitaviewer.activity

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import jp.co.cries.qiitaviewer.R
import jp.co.cries.qiitaviewer.fragments.ArticleListFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var getButton: ButtonOnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ArticleListFragment() as? ArticleListFragment
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        get_article_button.apply {
            setOnClickListener {
                getButton.onClick()
            }
            setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.scaleX = 0.9f
                        view.scaleY = 0.9f
                    }

                    else -> {
                        view.scaleX = 1f
                        view.scaleY = 1f
                    }
                }

                return@setOnTouchListener false
            }
        }



        fragment?.let {
            /** 初期表示を新着記事の一覧にする。 */
            fragmentTransaction
                .replace(R.id.container, it)
                .commit()
        }
    }


    interface ButtonOnClickListener {
        fun onClick()
    }

    fun setButtonOnClickListener(listener: ButtonOnClickListener) {
        this.getButton = listener
    }
}
