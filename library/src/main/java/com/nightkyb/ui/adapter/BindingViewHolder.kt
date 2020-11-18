package com.nightkyb.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 支持[ViewBinding]的[RecyclerView.ViewHolder]。
 *
 * @author nightkyb created on 2020/5/14.
 */
open class BindingViewHolder<VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)