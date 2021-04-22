package com.example.vdzmonitoring.ui.selectroute

import android.graphics.Color
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.databinding.ItemRouteBinding
import com.xwray.groupie.databinding.BindableItem

class RouteItem(
    private val route: Route,
    private val selectedItem: Int
) : BindableItem<ItemRouteBinding>() {

    override fun getLayout(): Int = R.layout.item_route

    override fun bind(viewBinding: ItemRouteBinding, position: Int) {
        viewBinding.route = route

        if(selectedItem == position)
            viewBinding.itemRouteCardview.setCardBackgroundColor(Color.GREEN)
        else
            viewBinding.itemRouteCardview.setCardBackgroundColor(Color.WHITE)
    }
}