package com.broadcast.myapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.broadcast.myapplication.AdapterDataSource
import com.broadcast.myapplication.FingerprintDiffUtil

class FingerprintAdapter(
    private val fingerprints: List<ItemFingerprint<*, *>>,
) : RecyclerView.Adapter<BaseViewHolder<ViewBinding, Item>>() {

    var dataSource: AdapterDataSource? = null
    var adapterInteractionListener: AdapterInteractionListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return fingerprints.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }


    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        holder.itemView.setOnClickListener {
            adapterInteractionListener?.onItemClick(position, it)
        }

        dataSource?.getItemByPosition(position)?.let {
            holder._bind?.invoke(it)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, Item>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            dataSource?.getItemByPosition(position)?.let {
                holder._bindPayloads?.invoke(it, payloads)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ViewBinding, Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetached()
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataSource!!.getItemByPosition(position)
        return fingerprints.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found: $item")
    }

    override fun getItemCount(): Int = dataSource?.getItemCount() ?: 0

    fun update(diffUtil: FingerprintDiffUtil) {
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        diffResult.dispatchUpdatesTo(this)
    }
}

interface AdapterInteractionListener {
    fun onItemClick(position: Int, view: View)
}