package com.android.example.pickreci.Home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.Constants
import com.android.example.pickreci.ItemViews.TypeItem
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        initTypeItemView()
        setTitle()
        return v
    }


    private fun initTypeItemView() {
        adapter.clear()
<<<<<<< HEAD
        adapter.add(TypeItem((Constants.BREAKFAST), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.LUNCHANDDINNER), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.MERYENDA), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.SEAFOOD), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.SEASONING), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.POULTRY), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.VEGETABLES), R.drawable.type_bf))
=======
        adapter.add(TypeItem((Constants.BREAKFAST), R.drawable.breakfast))
        adapter.add(TypeItem((Constants.LINNER), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.MERYENDA), R.drawable.type_bf))
        adapter.add(TypeItem((Constants.SEAFOOD), R.drawable.seafood))
        adapter.add(TypeItem((Constants.POULTRY), R.drawable.poultry))
        adapter.add(TypeItem((Constants.SEASONING), R.drawable.seasonings))
        adapter.add(TypeItem((Constants.VEGETABLES), R.drawable.vegetables))
>>>>>>> e385fa669314b2cf4a10f5a156b7394185ab24af
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
