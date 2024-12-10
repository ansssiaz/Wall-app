package com.eltex.androidschool.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OffsetDecoration(
    @Px private val offset: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top += offset
        outRect.left += offset
        outRect.right += offset
        val lastVisibleItemPosition =
            (parent.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        val lastItemPosition = parent.adapter?.itemCount?.minus(1)
        if (lastVisibleItemPosition == lastItemPosition) {
            outRect.bottom += offset
        }
    }
}