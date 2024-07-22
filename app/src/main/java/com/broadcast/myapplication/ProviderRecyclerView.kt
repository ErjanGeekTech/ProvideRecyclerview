package com.broadcast.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.broadcast.myapplication.adapter.AdapterInteractionListener
import com.broadcast.myapplication.adapter.FingerprintAdapter
import com.broadcast.myapplication.adapter.Item
import com.broadcast.myapplication.adapter.ItemFingerprint

class ProviderRecyclerView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : RecyclerView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private var parentAdapter: FingerprintAdapter = FingerprintAdapter(emptyList())
    private var concatAdapter: ConcatAdapter? = null

    fun addFingerprint(vararg fingerprints: ItemFingerprint<*, *>) {
        parentAdapter = FingerprintAdapter(fingerprints.toList())
        adapter = parentAdapter
    }

    fun addFingerprint(fingerprints: List<ItemFingerprint<*, *>>) {
        parentAdapter = FingerprintAdapter(fingerprints)
        adapter = parentAdapter
    }

    fun initConcatAdapter(vararg adapter: Adapter<out ViewHolder>) {
        val adapters = adapter.toMutableList()
        adapters.add(0, parentAdapter)
        concatAdapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            adapters
        )
    }

    fun addItemDecoration(vararg decorations: RecyclerView.ItemDecoration) {
        decorations.forEach { itemDecoration ->
            addItemDecoration(itemDecoration)
        }
    }

    fun update(diffUtil: FingerprintDiffUtil) {
        parentAdapter.update(diffUtil)
    }

    fun print() {
        Log.e("anime", "${parentAdapter.dataSource?.getItems().toString()}")
    }

    fun dataSource(dataSource: AdapterDataSource) {
        parentAdapter.dataSource = dataSource
    }

    fun click(adapterInteractionListener: AdapterInteractionListener) {
        parentAdapter.adapterInteractionListener = adapterInteractionListener
    }
}

interface AdapterDataSource {
    fun getItemCount(): Int

    fun getItemByPosition(position: Int): Item

    fun getItems(): List<Item>
}