package com.nightkyb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nightkyb.ui.adapter.listener.OnItemClickListener
import com.nightkyb.ui.adapter.listener.OnItemLongClickListener
import java.lang.reflect.ParameterizedType

/**
 * 支持[ViewBinding]的[RecyclerView.Adapter]基类。
 *
 * @author nightkyb created on 2020/5/14.
 */
abstract class BindingAdapter<VB : ViewBinding, T> : RecyclerView.Adapter<BindingViewHolder<VB>>() {
    protected lateinit var context: Context
    val itemList = arrayListOf<T>()
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<VB> {
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
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(
                        holder.itemView,
                        itemList[adapterPosition],
                        adapterPosition
                    )
                }
            }
        }

        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener {
                val adapterPosition = holder.adapterPosition

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongClickListener?.onItemLongClick(
                        holder.itemView,
                        itemList[adapterPosition],
                        adapterPosition
                    ) ?: false
                } else {
                    false
                }
            }
        }

        bind(holder, itemList[position], position)
    }

    override fun onBindViewHolder(
        holder: BindingViewHolder<VB>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            bind(holder, itemList[position], position, payloads)
        }
    }

    override fun getItemCount() = itemList.size

    abstract fun bind(holder: BindingViewHolder<VB>, item: T, position: Int)

    open fun bind(holder: BindingViewHolder<VB>, item: T, position: Int, payloads: List<Any>) {}

    fun update(newItemList: List<T>?) {
        itemList.clear()

        if (newItemList != null) {
            itemList.addAll(newItemList)
        }

        notifyDataSetChanged()
    }

    fun addAll(newItemList: List<T>) {
        itemList.addAll(newItemList)

        if (itemList.size - newItemList.size != 0) {
            notifyItemRangeInserted(itemList.size - newItemList.size, newItemList.size)
        } else {
            notifyDataSetChanged()
        }
    }

    fun addAll(vararg newItems: T) {
        addAll(newItems.asList())
    }

    fun update(position: Int, item: T) {
        itemList[position] = item
        notifyItemChanged(position)
    }

    fun remove(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        // 通知数据与界面重新绑定，解决位置错乱问题
        notifyItemRangeChanged(position, itemList.size - position)
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>) {
        this.onItemLongClickListener = onItemLongClickListener
    }
}
