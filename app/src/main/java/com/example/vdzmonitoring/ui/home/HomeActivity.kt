package com.example.vdzmonitoring.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.databinding.ActivityHomeBinding
import com.example.vdzmonitoring.ui.selectroute.SelectRouteActivity
import com.example.vdzmonitoring.ui.summarize.SummarizeActivity
import com.example.vdzmonitoring.util.TAG_CURRENT_ROUTE_LOG_ID
import com.example.vdzmonitoring.util.changeActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeActivity :
    AppCompatActivity(),
    KodeinAware {

    override val kodein by kodein()

    private val factory: HomeViewModelFactory by instance()

    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        viewModel = ViewModelProviders.of(this, factory)
            .get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.userDetail.observe(this, Observer {
            if(it != null)
                binding.homeName.text = it.firstName
        })

        binding.homeStart.setOnClickListener {
            changeActivity(SelectRouteActivity::class.java, clearStack = false)
        }

        setSupportActionBar(toolbar as Toolbar)

        bindUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout(this.baseContext)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindUI() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.histories.await().observe(this@HomeActivity, Observer {
            initRecyclerView(it.toHistoryItem())
        })
    }

    private fun initRecyclerView(routeItems: List<HistoryItem>) {
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(routeItems)
        }.also {
            it.setOnItemClickListener{ item, _ ->
                changeActivity(
                    SummarizeActivity::class.java,
                    clearStack = false,
                    param =
                        TAG_CURRENT_ROUTE_LOG_ID to
                        (item as HistoryItem).history.routeLog!!.routeLogId

                )
            }
        }

        home_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }

    private fun List<History>.toHistoryItem() =
        this.map {
            HistoryItem(it)
        }
}
