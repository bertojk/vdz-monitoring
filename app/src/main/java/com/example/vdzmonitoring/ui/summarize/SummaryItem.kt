package com.example.vdzmonitoring.ui.summarize

import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.databinding.ItemSummaryBinding
import com.xwray.groupie.databinding.BindableItem

class SummaryItem(
    private val summary: Summary
) : BindableItem<ItemSummaryBinding>() {

    override fun getLayout(): Int = R.layout.item_summary

    override fun bind(viewBinding: ItemSummaryBinding, position: Int) {
        viewBinding.summary = summary
    }
}