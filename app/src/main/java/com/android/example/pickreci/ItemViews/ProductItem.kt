package com.android.example.pickreci.ItemViews

import android.widget.ImageView
import android.widget.TextView
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ProductItem(val product: Product): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.imageView2)
        val productName = viewHolder.itemView.findViewById<TextView>(R.id.productNameText)
        val price = viewHolder.itemView.findViewById<TextView>(R.id.productPriceTv)


        productName.text = product.productName
        price.text = product.price.toString()
        Picasso.get().load(product.productImg).into(image)



    }

    override fun getLayout(): Int {
        return R.layout.row_product
    }

}