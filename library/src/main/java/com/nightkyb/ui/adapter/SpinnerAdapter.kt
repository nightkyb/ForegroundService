package com.nightkyb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 支持[ViewBinding]的Spinner组件使用的Adapter基类。
 *
 * @author nightkyb created on 2020/9/10.
 */
abstract class SpinnerAdapter<VB : ViewBinding, T> : BaseAdapter() {
    protected lateinit var context: Context
    val itemList = arrayListOf<T>()

    override fun getCount() = itemList.size

    override fun getItem(position: Int): T = itemList[position]

    override fun getItemId(position: Int) = 0L

    @Suppress("UNCHECKED_CAST")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val newConvertView: View
        val holder: SpinnerViewHolder<VB>

        if (convertView == null) {
            context = parent.context

            // 利用反射，调用指定ViewBinding中的inflate方法填充视图
            val type = javaClass.genericSuperclass
            val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            val binding = method.invoke(null, LayoutInflater.from(context), parent, false) as VB
            newConvertView = binding.root
            holder = SpinnerViewHolder(binding)
            newConvertView.tag = holder
        } else {
            holder = convertView.tag as SpinnerViewHolder<VB>
            newConvertView = convertView
        }

        bind(holder, itemList[position], position)

        return newConvertView
    }

    abstract fun bind(holder: SpinnerViewHolder<VB>, item: T, position: Int)

    fun update(newItemList: List<T>) {
        itemList.clear()
        itemList.addAll(newItemList)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    open class SpinnerViewHolder<VB : ViewBinding>(val binding: VB)
}
