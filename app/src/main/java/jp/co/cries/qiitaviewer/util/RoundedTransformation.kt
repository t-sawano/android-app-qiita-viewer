package jp.co.cries.qiitaviewer.util

import android.graphics.*
import com.squareup.picasso.Transformation

class RoundedTransformation(
    private val radius: Int, private val margin: Int
) : Transformation {

    override fun transform(source: Bitmap?): Bitmap? {
        val paint = Paint()
        paint.isAntiAlias = true

        source?.let {
            paint.shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val output = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val radiusF = radius.toFloat()
            val marginF = margin.toFloat()
            canvas.drawRoundRect(
                RectF(marginF, marginF, it.width - marginF, it.height.toFloat()),
                radiusF,
                radiusF, paint
            )

            if (it != output) {
                source.recycle()
            }

            return output
        }

        return null
    }

    override fun key(): String {
        return "rounded"
    }
}