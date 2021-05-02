package com.android.example.pickreci.Home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.Constants
import com.android.example.pickreci.ItemViews.TypeItem
import com.android.example.pickreci.Market.CartActivity
import com.android.example.pickreci.Market.ProductsActivity
import com.android.example.pickreci.R
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class Home : Fragment() {


    private lateinit var recyclerView: RecyclerView
    var adapter = GroupAdapter<ViewHolder>()
    lateinit var v: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        initTypeItemView()
        setTitle()
        return v
    }


    private fun initTypeItemView() {
        adapter.clear()
        adapter.add(TypeItem((Constants.BREAKFAST), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.LUNCH), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.DINNER), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.MERYENDA), R.drawable.type_bf))
    }

    private fun init() {
        recyclerView = v.findViewById(R.id.rv)
        recyclerView.adapter = adapter
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        setTitle()
    }

    private fun setTitle() {
        (activity as AppCompatActivity)!!.supportActionBar!!.title = "RECIPICK"
    }

}
