package com.pearl.a19012011050_assignment2

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.pearl.a19012011050_assignment2.adapter.ProductCategoryAdapter
import com.pearl.a19012011050_assignment2.model.ProductCategory
import com.pearl.a19012011050_assignment2.model.ProductModel
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var productCategoryRecycler:RecyclerView
    lateinit var productCategoryAdapter:ProductCategoryAdapter

    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val imageView:ImageView = view.findViewById(R.id.offerSliderImageView)
        val an1: AnimationDrawable = imageView.background as AnimationDrawable
        an1.start()


        val productCategory:MutableList<ProductCategory> = ArrayList()
        val productRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("product")
        productRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(categorySnapshot in snapshot.children){
                        var prodCat:String = categorySnapshot.key.toString()
                        var productList:MutableList<ProductModel> = ArrayList()
                        for (productSnapshot in categorySnapshot.children){
                            val product = productSnapshot.getValue(Product::class.java)
                            if (product !=null ){
                                productList.add(ProductModel(product.productName, product.productImageUrl, prodCat))
                            }
                        }
                        productCategory.add(ProductCategory(prodCat, productList))
                    }
                    productCategoryRecycler = view.findViewById(R.id.productCategoryRecycler)
                    val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
                    productCategoryRecycler!!.layoutManager = layoutManager
                    productCategoryAdapter = ProductCategoryAdapter(requireContext(), productCategory)
                    productCategoryRecycler!!.adapter = productCategoryAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun setProductCategoryRecycler(productCategory:List<ProductCategory>){
    }
}