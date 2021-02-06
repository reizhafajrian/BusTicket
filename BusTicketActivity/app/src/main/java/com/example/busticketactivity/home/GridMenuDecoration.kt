package com.example.busticketactivity.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridMenuDecoration(val data: space) : RecyclerView.ItemDecoration() {
    data class space(
        var spanCount: Int = 0,
        var spacing: Int = 0,
        var edge: Boolean = false
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % data.spanCount
        if (data.edge) {
            outRect.left = data.spacing - column * data.spacing / data.spanCount
            outRect.right = (column + 1) * data.spacing / data.spanCount

            if (position < data.spanCount) {
                outRect.top = data.spacing
            }
            outRect.bottom = data.spacing
        } else {
            outRect.left =
                column * data.spacing / data.spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right =
                data.spacing - (column + 1) * data.spacing / data.spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= data.spanCount) {
                outRect.top = data.spacing; // item top
            }
        }
    }

}