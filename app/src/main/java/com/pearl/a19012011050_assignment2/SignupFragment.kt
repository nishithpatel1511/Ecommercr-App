package com.pearl.a19012011050_assignment2

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


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
        val view: View = inflater.inflate(R.layout.fragment_signup, container, false)

        val signupFname: EditText = view.findViewById(R.id.signupFname)
        val signupLname:EditText = view.findViewById(R.id.signupLname)
        val signupEmail:EditText = view.findViewById(R.id.signupEmail)
        val signupPhone:EditText = view.findViewById(R.id.signupPhone)
        val signupPasswd:EditText = view.findViewById(R.id.signupPasswd)
        val signupCPasswd:EditText = view.findViewById(R.id.signupCPasswd)
        val registeBtn: Button = view.findViewById(R.id.signupRegister)

        auth = Firebase.auth

        registeBtn.setOnClickListener {
            val fname: String = signupFname.text.toString()
            val lname: String = signupLname.text.toString()
            val email: String = signupEmail.text.toString()
            val phone: String = signupPhone.text.toString()
            val passwd: String = signupPasswd.text.toString()
            val cpasswd: String = signupCPasswd.text.toString()

            if (fname.isEmpty()) {
                signupFname.setError("Firstname is Empty")
                signupFname.requestFocus()
                return@setOnClickListener
            } else if (lname.isEmpty()) {
                signupLname.setError("Lastname is Empty")
                signupLname.requestFocus()
                return@setOnClickListener
            } else if (email.isEmpty()) {
                signupEmail.setError("Email is Empty")
                signupEmail.requestFocus()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupEmail.setError("Enter Valid Email")
                signupEmail.requestFocus()
                return@setOnClickListener
            } else if (phone.isEmpty()) {
                signupPhone.setError("Phone is Empty")
                signupPhone.requestFocus()
                return@setOnClickListener
            } else if (passwd.isEmpty()) {
                signupPasswd.setError("Password is Empty")
                signupPasswd.requestFocus()
                return@setOnClickListener
            } else if (passwd.length < 8) {
                signupPasswd.setError("Password is too Short")
                signupPasswd.requestFocus()
                return@setOnClickListener
            } else if (cpasswd.isEmpty()) {
                signupCPasswd.setError("Conform Password is Empty")
                signupCPasswd.requestFocus()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener{
                if (it.isSuccessful) {

                    val uid = auth.currentUser?.uid
                    val databaseReference:DatabaseReference = FirebaseDatabase.getInstance().reference

                    val user = User(fname, lname, phone)
                    if (uid != null){
                        databaseReference.child("Users/$uid").setValue(user).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(activity, "User Create Successfully", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                val fm: FragmentManager = requireActivity().supportFragmentManager
                                val ft:FragmentTransaction = fm.beginTransaction()
                                ft.replace(R.id.frameLayout, LoginFragment())
                                ft.commit()

                            } else {
                                Toast.makeText(activity, "user Data is Not added",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }




                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


