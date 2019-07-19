package jp.co.cries.qiitaviewer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.co.cries.qiitaviewer.entity.ItemEntity
import jp.co.cries.qiitaviewer.repository.ItemRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var itemRepository: ItemRepository
    lateinit var itemList: List<ItemEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemRepository = ItemRepository()

        buttonGetArticle?.setOnClickListener {
            Log.d("MainActivity#onCreate" ,"記事を取得するボタンが押下されました。")

            itemRepository.getItemList(1 ,10) {
                itemList = it

                it.forEach {
                    Log.d("MainActivity" ,"${it}")
                }
            }


        }

    }

}
