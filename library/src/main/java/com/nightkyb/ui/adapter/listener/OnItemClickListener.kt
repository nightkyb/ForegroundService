package com.nightkyb.ui.adapter.listener

import android.view.View

fun interface OnItemClickListener<T> {
    fun onItemClick(itemView: View, item: T, position: Int)
}