package com.eltex.androidschool.itemdecoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.eltex.androidschool.model.Event
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class DateDecoration(
    private val eventsList: List<Event>,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || isDateChanged(position)) {
            outRect.top = 50
        } else {
            outRect.top = 0
        }
    }

    /**
     * Проверяет, изменилась ли дата по сравнению с предыдущим постом
     * @param position - индекс проверяемого поста
     */
    private fun isDateChanged(position: Int): Boolean {
        val currentPostDate = eventsList[position].published
        val previousPostDate = eventsList[position - 1].published

        return !isSameDate(currentPostDate, previousPostDate)
    }

    /**
     * Проверяет, находятся ли две даты в один и тот же день
     * @param date1 - первая дата
     * @param date2 - вторая дата
     */
    private fun isSameDate(date1: String, date2: String): Boolean {
        val format = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        val cal1 = LocalDateTime.parse(date1, format)
        val cal2 = LocalDateTime.parse(date2, format)
        return cal1.year == cal2.year &&
                cal1.dayOfYear == cal2.dayOfYear
    }

    /**
     * Добавляет группам постов разделители по датам - надписи "Сегодня", "Вчера" или дата в формате "dd MMM yyyy"
     * @param c - объект класса Canvas, на который будет происходить рисование
     * @param parent -ссылка на RecyclerView, который содержит все элементы списка
     * @param state - объект, который содержит информацию о текущем состоянии RecyclerView
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        val today = dateFormat.format(LocalDateTime.now())
        val yesterday = LocalDateTime.now().minusDays(1).format(dateFormat)

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position == 0 || isDateChanged(position)) {
                val date = eventsList[position].published
                val label = when {
                    isSameDate(date, today) -> "Сегодня"
                    isSameDate(date, yesterday) -> "Вчера"
                    else -> date.substringBefore(" ")
                }

                val paint = Paint().apply {
                    color = Color.GRAY
                    textSize = 40f
                }

                val x = child.left + 16
                val y = child.top - 20

                c.drawText(label, x.toFloat(), y.toFloat(), paint)
            }
        }
    }
}