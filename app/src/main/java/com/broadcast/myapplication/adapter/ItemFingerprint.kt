package com.broadcast.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.broadcast.myapplication.databinding.ItemTitleBinding
import com.broadcast.myapplication.model.FeedTitle

interface ItemFingerprint<V : ViewBinding, I : Item> {

    fun isRelativeItem(item: Item): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): V

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<V, I> {
        return BaseViewHolder<V, I>(getViewBinding(layoutInflater, parent)).also {
            initBlock(it)
        }
    }

    fun initBlock(viewHolder: BaseViewHolder<V, I>) {}

    fun getDiffUtil(): DiffUtil.ItemCallback<I>

}