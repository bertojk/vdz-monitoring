package com.example.vdzmonitoring.ui.home

import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.databinding.ItemHistoryBinding
import com.xwray.groupie.databinding.BindableItem

class HistoryItem(
    val history: History
) : BindableItem<ItemHistoryBinding>() {

    override fun getLayout(): Int = R.layout.item_history

    override fun bind(viewBinding: ItemHistoryBinding, position: Int) {
        viewBinding.history = history
    }
}