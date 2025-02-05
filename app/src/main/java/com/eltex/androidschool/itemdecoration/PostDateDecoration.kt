package com.eltex.androidschool.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.eltex.androidschool.R
import com.eltex.androidschool.feature.posts.ui.PostUiModel
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class PostDateDecoration(
    val getPost: (adapterPosition: Int) -> PostUiModel?,
    private val context: Context,
) : ItemDecoration() {
    private val topOffset =
        context.resources.getDimensionPixelSize(R.dimen.date_decoration_top_offset)
    private val dateDecorationTextSize =
        context.resources.getDimension(R.dimen.date_decoration_text_size)
    private val textMargin =
        context.resources.getDimensionPixelSize(R.dimen.date_decoration_text_margin)
    private val textVerticalOffset =
        context.resources.getDimensionPixelSize(R.dimen.date_decoration_text_vertical_offset)

    private val paint = Paint().apply {
        color = Color.GRAY
        textSize = dateDecorationTextSize
    }

    private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy")
    private val today = dateFormat.format(Instant.now().atZone(ZoneId.systemDefault()))
    private val yesterday = dateFormat.format(
        Instant.now().minus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault())
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || isDateChanged(position)) {
            outRect.top = topOffset
        } else {
            outRect.top = 0
        }
    }

    /**
     * Проверяет, изменилась ли дата по сравнению с предыдущим постом
     * @param position - индекс проверяемого поста
     */
    private fun isDateChanged(position: Int): Boolean {
        val currentPostDate = getPost(position)?.published?.substring(0, 8)
        val previousPostDate = getPost(position - 1)?.published?.substring(0, 8)

        return !isSameDate(currentPostDate, previousPostDate)
    }

    /**
     * Проверяет, находятся ли две даты в один и тот же день
     * @param date1 - первая дата
     * @param date2 - вторая дата
     */
    private fun isSameDate(date1: String?, date2: String?): Boolean {
        if (date1 == null || date2 == null) return false
        return date1 == date2
    }

    /**
     * Добавляет группам постов разделители по датам - надписи "Сегодня", "Вчера" или дата в формате "dd MMM yyyy"
     * @param c - объект класса Canvas, на который будет происходить рисование
     * @param parent -ссылка на RecyclerView, который содержит все элементы списка
     * @param state - объект, который содержит информацию о текущем состоянии RecyclerView
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val post = getPost(position) // Получаем пост по позиции
            if (post != null) {
                if (position == 0 || isDateChanged(position)) {
                    val date = getPost(position)?.published?.substring(0, 8)
                    val label = when {
                        isSameDate(date, today) -> context.getString(R.string.date_today)
                        isSameDate(date, yesterday) -> context.getString(R.string.date_yesterday)
                        else -> date
                    }
                    if (label != null) {
                        c.drawText(
                            label,
                            textMargin.toFloat(),
                            child.top - textVerticalOffset.toFloat(),
                            paint
                        )
                    }
                }
            }
        }
    }
}