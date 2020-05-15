package com.dh.testproject.activity.viewpager

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dh.testproject.R
import kotlinx.android.synthetic.main.activity_mutable_collection_base.*

abstract class MutableCollectionBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutable_collection_base)

        init()
    }

    private fun init() {
        viewPager.adapter = createViewPagerAdapter()
        itemSpinner.adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
                    ((convertView as TextView?) ?: TextView(parent?.context)).apply {
                        if (Build.VERSION.SDK_INT >= 17) {
                            textDirection = View.TEXT_DIRECTION_LOCALE
                        }
                        text = getItem(position)
                    }

            override fun getItem(position: Int): String = items.getItemById(getItemId(position))

            override fun getItemId(position: Int): Long = items.itemId(position)

            override fun getCount(): Int = items.size

        }

        buttonGoTo.setOnClickListener(onClickListener)
        buttonRemove.setOnClickListener(onClickListener)
        buttonAddBefore.setOnClickListener(onClickListener)
        buttonAddAfter.setOnClickListener(onClickListener)
    }

    abstract fun createViewPagerAdapter(): RecyclerView.Adapter<*>


    val items: ItemsViewModel by viewModels()
    //val items: ItemsViewModel = ViewModelProvider(this.viewModelStore,this.defaultViewModelProviderFactory).get(ItemsViewModel::class.java)

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.buttonGoTo -> viewPager.setCurrentItem(itemSpinner.selectedItemPosition, true)
            R.id.buttonRemove -> changeDataSet { items.removeAt(itemSpinner.selectedItemPosition) }
            R.id.buttonAddAfter -> changeDataSet { items.addNewAt(itemSpinner.selectedItemPosition + 1) }
            R.id.buttonAddBefore -> changeDataSet { items.addNewAt(itemSpinner.selectedItemPosition) }
        }
    }

    fun changeDataSet(performChanges: () -> Unit) {
        if (useDiffUtil.isChecked) {
            /** using [DiffUtil] */
            val idsOld = items.createIdSnapshot()
            performChanges()
            val idsNew = items.createIdSnapshot()
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        idsOld[oldItemPosition] == idsNew[newItemPosition]

                override fun getOldListSize(): Int = idsOld.size

                override fun getNewListSize(): Int = idsNew.size

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                        areItemsTheSame(oldItemPosition, newItemPosition)

            }, true).dispatchUpdatesTo(viewPager.adapter!!)
        } else {
            /** without [DiffUtil] */
            val oldP = viewPager.currentItem
            val currentItemId = items.itemId(oldP)
            performChanges()
            viewPager.adapter!!.notifyDataSetChanged()
            if (items.contains(currentItemId)) {
                val newPosition =
                        (0 until items.size).indexOfFirst { items.itemId(it) == currentItemId }
                viewPager.setCurrentItem(newPosition, false)
            }
        }

        // item spinner update
        (itemSpinner.adapter as BaseAdapter).notifyDataSetChanged()
    }
}


