package com.android.example.pickreci.Market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.example.pickreci.R

class CartActivity : Fragment() {

    private lateinit var v: View


    companion object {
        const val TAG = "CartTag"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_cart_activity, container, false)

        return v
    }

}