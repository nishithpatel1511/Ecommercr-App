package com.pearl.a19012011050_assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pearl.a19012011050_assignment2.R
import com.pearl.a19012011050_assignment2.model.ProductCartItem

class ProductCartAdapter(private var context:Context, private var cartModel:List<ProductCartItem>): RecyclerView.Adapter<ProductCartAdapter.CartViewHolder>(){
    class CartViewHolder(cartView:View):RecyclerView.ViewHolder(cartView) {

        var productName:TextView
        var productImage:ImageView
        var productPrice:TextView
        var productCategory:TextView
        var productQuantity:TextView
        var qIncrement:Button
        var qDecrement:Button
        init {
            productName = cartView.findViewById(R.id.cartName)
            productImage = cartView.findViewById(R.id.cartImageView)
            productPrice = cartView.findViewById(R.id.cartPrice)
            productCategory = cartView.findViewById(R.id.cartCategory)
            productQuantity = cartView.findViewById(R.id.cartQuantity)
            qIncrement = cartView.findViewById(R.id.cartPlus)
            qDecrement = cartView.findViewById(R.id.cartMinus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(LayoutInflater.from(context).inflate(R.layout.product_cart_view, parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var cartItem:ProductCartItem = cartModel[position]
        holder.productName.text = cartItem.productName
        Glide.with(context).load(cartItem.productImage).into(holder.productImage)
        holder.productPrice.text = cartItem.productPrice
        holder.productCategory.text = cartItem.productCategory
        holder.productQuantity.text = cartItem.productQuantity.toString()
    }

    override fun getItemCount(): Int {
        return cartModel.size
    }

}