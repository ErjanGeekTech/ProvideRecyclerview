package com.broadcast.myapplication.adapter.fingerprints

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.broadcast.myapplication.AdapterDataSource
import com.broadcast.myapplication.FingerprintDiffUtil
import com.broadcast.myapplication.R
import com.broadcast.myapplication.adapter.BaseViewHolder
import com.broadcast.myapplication.adapter.Item
import com.broadcast.myapplication.adapter.ItemFingerprint
import com.broadcast.myapplication.adapter.decorations.HorizontalDividerDecoration
import com.broadcast.myapplication.databinding.ItemHorizontalListBinding
import com.broadcast.myapplication.databinding.ItemTitleBinding
import com.broadcast.myapplication.model.FeedTitle
import com.broadcast.myapplication.model.HorizontalItems

class HorizontalItemsFingerprint(
    private val outerDivider: Int,
    private val viewPool: RecyclerView.RecycledViewPool,
    private val onSavePost: (outPosition: Int, innerAdapterPosition: Int) -> Unit,
) : ItemFingerprint<ItemHorizontalListBinding, HorizontalItems> {

    var list = emptyList<Item>()

    override fun isRelativeItem(item: Item) = item is HorizontalItems

    override fun getLayoutId() = R.layout.item_horizontal_list

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemHorizontalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun initBlock(viewHolder: BaseViewHolder<ItemHorizontalListBinding, HorizontalItems>) =
        with(viewHolder) {
            val fingerprints = listOf(
                PostFingerprint({
                    onSavePost(absoluteAdapterPosition, it)
                }, 600)
            )
            with(binding.rvHorizontalItems) {
                binding.rvHorizontalItems.addFingerprint(fingerprints)

                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false).also {
                    it.initialPrefetchItemCount = 4
                }

                setRecycledViewPool(viewPool)
                addItemDecoration(HorizontalDividerDecoration(50, outerDivider))
            }

            binding.text.setOnClickListener {
                Log.e("anime", "OutPosition  adapter: $absoluteAdapterPosition")
            }

            bind { item ->
                binding.rvHorizontalItems.dataSource(object : AdapterDataSource {
                    override fun getItemCount(): Int = item.items.size

                    override fun getItemByPosition(position: Int) = item.items[position]

                    override fun getItems(): List<Item> = item.items
                })

                val fingerprintDiffUtil =
                    FingerprintDiffUtil(fingerprints, list, item.items)
                list = item.items.toList()
                binding.rvHorizontalItems.update(fingerprintDiffUtil)
            }

            bind { item, payloads ->
                val fingerprintDiffUtil =
                    FingerprintDiffUtil(fingerprints, list, item.items)
                list = item.items.toList()
                binding.rvHorizontalItems.update(fingerprintDiffUtil)
            }
        }


    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalItems>() {
        override fun areItemsTheSame(oldItem: HorizontalItems, newItem: HorizontalItems) =
            oldItem.items == newItem.items

        override fun areContentsTheSame(oldItem: HorizontalItems, newItem: HorizontalItems) =
            oldItem == newItem

        override fun getChangePayload(oldItem: HorizontalItems, newItem: HorizontalItems): Any? {
            if (oldItem.isChange != newItem.isChange) return true
            return super.getChangePayload(oldItem, newItem)
        }
    }
}