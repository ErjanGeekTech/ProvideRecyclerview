package com.broadcast.myapplication.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<out V : ViewBinding, I : Item>(
    val binding: V
) : RecyclerView.ViewHolder(binding.root) {

    var _bind: ((item: I) -> Unit)? = null
        private set

    var _bindPayloads: ((item: I, payloads: List<Any>) -> Unit)? = null
        private set

    fun bind(bindingBlock: (item: I) -> Unit) {
        if (_bind != null) {
            throw IllegalStateException("bind { ... } is already defined. Only one bind { ... } is allowed.")
        }
        _bind = bindingBlock
    }

    fun bind(bindingBlock: (item: I, payloads: List<Any>) -> Unit) {
        if (_bindPayloads != null) {
            throw IllegalStateException("bind { ... } is already defined. Only one bind { ... } is allowed.")
        }
        _bindPayloads = bindingBlock
    }

    open fun onViewDetached() = Unit
}