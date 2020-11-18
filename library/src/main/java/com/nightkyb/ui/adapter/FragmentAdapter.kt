package com.nightkyb.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    constructor(
        fragmentActivity: FragmentActivity,
        fragments: List<Fragment>
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle, fragments)

    constructor(
        fragment: Fragment,
        fragments: List<Fragment>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, fragments)

    override fun createFragment(position: Int) = fragments[position]

    override fun getItemCount() = fragments.size
}
