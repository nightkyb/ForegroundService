package com.nightkyb.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

/**
 * 能直接根据视图id访问视图的Android Extensions风格的[RecyclerView.ViewHolder]
 *
 * @author nightkyb created on 2019/5/21.
 */
// Need to specify ContainerOptions in order for caching to work.
// See: https://youtrack.jetbrains.com/oauth?state=%2Fissue%2FKT-28617
@ContainerOptions(cache = CacheImplementation.HASH_MAP)
class RecyclerViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer