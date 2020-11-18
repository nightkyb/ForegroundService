package com.nightkyb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.nightkyb.ui.adapter.listener.OnItemClickListener
import com.nightkyb.ui.adapter.listener.OnItemLongClickListener

/**
 * 能直接根据视图id访问视图的Android Extensions风格的[RecyclerView.Adapter]
 *
 * @author nightkyb created on 2019/5/21.
 */
abstract class RecyclerAdapter<T> : RecyclerView.Adapter<RecyclerViewHolder>() {
    protected lateinit var context: Context
    val itemList = arrayListOf<T>()
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(getLayout(), parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
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
        holder: RecyclerViewHolder,
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

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun bind(holder: RecyclerViewHolder, item: T, position: Int)

    open fun bind(holder: RecyclerViewHolder, item: T, position: Int, payloads: List<Any>) {}

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
