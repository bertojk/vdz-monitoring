package com.example.vdzmonitoring.ui.summarize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.ui.home.HomeActivity
import com.example.vdzmonitoring.ui.selectroute.RouteItem
import com.example.vdzmonitoring.ui.selectroute.SelectRouteViewModel
import com.example.vdzmonitoring.ui.selectroute.SelectRouteViewModelFactory
import com.example.vdzmonitoring.util.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_select_route.*
import kotlinx.android.synthetic.main.activity_summarize.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SummarizeActivity :
    AppCompatActivity(),
    KodeinAware{

    override val kodein by kodein()

    private val factory: SummarizeViewModelFactory by instance()

    private lateinit var viewModel: SummarizeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summarize)

        viewModel = ViewModelProviders.of(this, factory).get(SummarizeViewModel::class.java)

        summarize_gotohome.setOnClickListener {
            changeActivity(HomeActivity::class.java)
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                changeActivity(HomeActivity::class.java)
            }
        })

        bindUI()
    }

    private fun bindUI() = CoroutineScope(Dispatchers.Main).launch {
        summarize_progress_bar.show()
        viewModel.apply {
            summarizeListInit(intent.getLongExtra(TAG_CURRENT_ROUTE_LOG_ID, 0L))
            summarizeList.observe(this@SummarizeActivity, Observer {
                summarize_progress_bar.hide()
                initRecyclerView(it.toSummaryItem())
            })
        }
    }

    private fun initRecyclerView(summaryItems: List<SummaryItem>) {
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(summaryItems)
        }

        summarize_recycler_view.apply {
            layoutManager = LinearLayoutManager(summarize_recycler_view.context)
            adapter = mAdapter
        }

    }

    private fun List<Summary>.toSummaryItem(): List<SummaryItem> {
        return this.map {
            SummaryItem(it)
        }
    }


}
