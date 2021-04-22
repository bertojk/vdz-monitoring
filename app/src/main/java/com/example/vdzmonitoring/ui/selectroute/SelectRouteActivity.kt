package com.example.vdzmonitoring.ui.selectroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.ui.routing.RoutingActivity
import com.example.vdzmonitoring.util.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_select_route.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SelectRouteActivity :
    AppCompatActivity(),
    KodeinAware{

    override val kodein by kodein()

    private val factory: SelectRouteViewModelFactory by instance()

    private lateinit var viewModel: SelectRouteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_route)

        viewModel = ViewModelProviders.of(this, factory).get(SelectRouteViewModel::class.java)

        select_route_start.setOnClickListener {
            changeActivity(RoutingActivity::class.java,
                param = TAG_ROUTE_ID_SELECTED to viewModel.selectedRouteIndex.value!!)
        }

        bindUI()
    }

    private fun bindUI() = CoroutineScope(Dispatchers.Main).launch {
        select_route_progress_bar.show()
        viewModel.routes.await().observe(this@SelectRouteActivity, Observer {
            select_route_progress_bar.hide()
            initRecyclerView(it.toRouteItems(viewModel.selectedRouteIndex.value!!.toInt()))

            viewModel.selectedRouteIndex.observe(this@SelectRouteActivity, Observer {idx ->
                initRecyclerView(it.toRouteItems(idx.toInt()))
                if(idx != -1L) select_route_start.show()
            })
        })
    }

    private fun initRecyclerView(routeItems: List<RouteItem>) {
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(routeItems)
        }.also {
            it.setOnItemClickListener { item, _ ->
                viewModel.changeIndex(it.getAdapterPosition(item).toLong())
            }
        }

        select_route_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }

    private fun List<Route>.toRouteItems(selectedIndex: Int): List<RouteItem> {
        return this.map {
            RouteItem(it, selectedIndex)
        }
    }
}
