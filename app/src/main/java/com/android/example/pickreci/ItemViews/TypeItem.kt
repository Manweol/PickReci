package com.android.example.pickreci.ItemViews

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.android.example.pickreci.R
import com.android.example.pickreci.SpecificRecipePerType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class TypeItem (val type: String, val imageResource: Int): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.circleImageView)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.textView5)
        val button = viewHolder.itemView.findViewById<FloatingActionButton>(R.id.floatingActionButton)


        title.text = type.toUpperCase()
        image.setImageResource(imageResource)
        button.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, SpecificRecipePerType::class.java)
            intent.putExtra(TAG, type)
            viewHolder.itemView.context.startActivity(intent)
        }


    }

    override fun getLayout(): Int {
        return R.layout.row_type
    }
    companion object{
        const val TAG = "TypeItemTag"
    }
}