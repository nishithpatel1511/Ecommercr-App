package com.pearl.a19012011050_assignment2

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

// TODO: Rename parameter arguments, choose names that match
private lateinit var imageUri:Uri
private lateinit var imageUrl:String
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view:View = inflater.inflate(R.layout.fragment_add_product, container, false)
        val pname:EditText = view.findViewById(R.id.product_name)
        val pprice:EditText = view.findViewById(R.id.product_price)
        val pimage:ImageView = view.findViewById(R.id.uploadImage)
        val pcat:EditText = view.findViewById(R.id.product_cat)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageUri = it
                pimage.setImageURI(it)
            })


        val cImage:Button = view.findViewById(R.id.choose_btn)
        cImage.setOnClickListener {
            getImage.launch("image/*")
        }
        val addbtn:Button = view.findViewById(R.id.add)
        addbtn.setOnClickListener {
            val name:String = pname.text.toString()
            val price:String = pprice.text.toString()
            val category:String = pcat.text.toString()

            val fileName = "product/"+name+"/IMG_"+Date()
            val storageReference = FirebaseStorage.getInstance().getReference(fileName)
            storageReference.putFile(imageUri).addOnProgressListener {
                addbtn.setEnabled(false)
            }
            storageReference.putFile(imageUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener {
                    imageUrl = it.result.toString()
                    val databaseReference:DatabaseReference = FirebaseDatabase.getInstance().getReference()
                    val product = Product(name, category, price, fileName, imageUrl)
                    databaseReference.child("product/"+category+"/"+name).setValue(product).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(activity, "Product Added Sucessfully", Toast.LENGTH_SHORT).show()
                            pname.text.clear()
                            pprice.text.clear()
                            pcat.text.clear()
                            pimage.setImageURI(null)
                            addbtn.setEnabled(true)
                        }
                    }
                }
            }
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
         * @return A new instance of fragment AddProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}