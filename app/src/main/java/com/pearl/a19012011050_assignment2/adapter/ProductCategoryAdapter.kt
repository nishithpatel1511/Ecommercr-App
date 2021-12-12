package com.pearl.a19012011050_assignment2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pearl.a19012011050_assignment2.ProductViewActivity
import com.pearl.a19012011050_assignment2.R
import com.pearl.a19012011050_assignment2.model.ProductCategory

class ProductCategoryAdapter(private var context: Context, private var productCategory:List<ProductCategory>):RecyclerView.Adapter<ProductCategoryAdapter.CategoryViewHolder>(){

    class CategoryViewHolder(productView:View):RecyclerView.ViewHolder(productView){
        var categoryName:TextView
        var productRecycler:RecyclerView
        init {
            categoryName = productView.findViewById(R.id.productCategoryName)
            productRecycler = productView.findViewById(R.id.productRecycler)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductCategoryAdapter.CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.product_category_recycler, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductCategoryAdapter.CategoryViewHolder,
        position: Int
    ) {
        holder.categoryName.text = productCategory[position].productCategory
        var productRecyclerAdapter = ProductAdapter(context, productCategory[position].products)
        productRecyclerAdapter.setOnItemClickListener(object :ProductAdapter.onItemClickListener{
            override fun onProductClick(position: Int, productName: TextView, itemCategory:TextView) {
                val intent:Intent = Intent(context, ProductViewActivity::class.java).putExtra("name", productName.text)
                    .putExtra("category", itemCategory.text)
                context.startActivity(intent)
            }
        })
        holder.productRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        holder.productRecycler.adapter = productRecyclerAdapter
    }

    override fun getItemCount(): Int {
        return productCategory.size
    }

}