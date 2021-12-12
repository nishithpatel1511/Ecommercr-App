package com.pearl.a19012011050_assignment2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.pearl.a19012011050_assignment2.adapter.ProductCartAdapter
import com.pearl.a19012011050_assignment2.model.ProductCart
import com.pearl.a19012011050_assignment2.model.ProductCartItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cartRecycler:RecyclerView
    lateinit var cartAdapter:ProductCartAdapter


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
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        var myArrayList:ArrayList<ProductCartItem> = ArrayList()
        val auth = Firebase.auth
        if(auth.currentUser != null){
            val uid = auth.currentUser!!.uid
            val cartRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users/${uid}/cart")
            cartRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(productSnapshot in snapshot.children){
                            val product = productSnapshot.getValue(ProductCart::class.java)
                            if(product != null){
                                val prodRef = FirebaseDatabase.getInstance().getReference(product.product_path.toString())
                                prodRef.addValueEventListener(object: ValueEventListener{
                                    override fun onDataChange(prodSnapshot: DataSnapshot) {
                                        if(prodSnapshot.exists()){
                                            val prod = prodSnapshot.getValue(Product::class.java)
                                            myArrayList.add(ProductCartItem(
                                                prod?.productName.toString(),prod?.productCategory.toString(),
                                                prod?.productImageUrl.toString(), prod?.productPrice.toString(),
                                                product.product_quantity!!.toInt()))
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            }
                        }
                        Toast.makeText(context, "This", Toast.LENGTH_SHORT).show()
                        cartRecycler = view.findViewById(R.id.cartItemRecyler)
                        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
                        cartRecycler!!.layoutManager = layoutManager
                        cartAdapter = ProductCartAdapter(requireContext(), myArrayList)
                        cartRecycler!!.adapter = cartAdapter
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}