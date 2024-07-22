package com.broadcast.myapplication.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.broadcast.myapplication.R
import com.broadcast.myapplication.adapter.BaseViewHolder
import com.broadcast.myapplication.adapter.Item
import com.broadcast.myapplication.adapter.ItemFingerprint
import com.broadcast.myapplication.databinding.ItemTitleBinding
import com.broadcast.myapplication.model.FeedTitle

class TitleFingerprint : ItemFingerprint<ItemTitleBinding, FeedTitle> {

    override fun isRelativeItem(item: Item) = item is FeedTitle

    override fun getLayoutId() = R.layout.item_title

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTitleBinding.inflate(layoutInflater, parent, false)


    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<FeedTitle>() {
        override fun areItemsTheSame(oldItem: FeedTitle, newItem: FeedTitle) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: FeedTitle, newItem: FeedTitle) = oldItem == newItem
    }

    override fun initBlock(viewHolder: BaseViewHolder<ItemTitleBinding, FeedTitle>) =
        with(viewHolder) {
            bind { item ->
                binding.tvFeedTitle.text = item.title
            }
        }
}
