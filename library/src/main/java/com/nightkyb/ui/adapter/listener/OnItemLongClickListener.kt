package com.nightkyb.ui.adapter.listener

import android.view.View

fun interface OnItemLongClickListener<T> {
    fun onItemLongClick(itemView: View, item: T, position: Int): Boolean
}