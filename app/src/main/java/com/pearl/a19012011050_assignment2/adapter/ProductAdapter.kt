package com.pearl.a19012011050_assignment2.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.pearl.a19012011050_assignment2.R
import com.pearl.a19012011050_assignment2.model.ProductModel
import org.w3c.dom.Text
import java.io.File

class ProductAdapter(private var context: Context, private var productModel: List<ProductModel>):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private lateinit var myListner: onItemClickListener
    interface onItemClickListener{
        fun onProductClick(position: Int, itemName: TextView, itemCategory:TextView)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        myListner = listener
    }

    class ProductViewHolder(productView:View, listener: onItemClickListener):RecyclerView.ViewHolder(productView){
        var productImage:ImageView
        var productName:TextView
        var productCategory:TextView
        init {
            productImage = productView.findViewById(R.id.productImageView)
            productName = productView.findViewById(R.id.productNameView)
            productCategory= productView.findViewById(R.id.productCategoryView)
            productView.setOnClickListener{
                listener.onProductClick(adapterPosition, productName, productCategory)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.product_recycler, parent, false), myListner)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        Glide.with(context).load(productModel[position].productImage.toString()).into(holder.productImage)
        holder.productName.setText(productModel[position].productName)
        holder.productCategory.setText(productModel[position].productCat)

    }

    override fun getItemCount(): Int {
        return productModel.size
    }
}