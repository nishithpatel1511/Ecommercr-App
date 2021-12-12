package com.pearl.a19012011050_assignment2

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

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
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val login_btn:Button = view.findViewById(R.id.loginButton)
        val email:EditText = view.findViewById(R.id.loginEmail)
        val password:EditText = view.findViewById(R.id.loginPassword)

        login_btn.setOnClickListener {
            val email:String = email.text.toString()
            val password:String = password.text.toString()
            auth = FirebaseAuth.getInstance()
            auth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        val ft:FragmentTransaction = fm.beginTransaction()
                        ft.replace(R.id.frameLayout, HomeFragment())
                        ft.commit()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
        val signup_btn:Button = view.findViewById(R.id.signupButton)
        signup_btn.setOnClickListener {

            val fm: FragmentManager = requireActivity().supportFragmentManager
            val ft:FragmentTransaction = fm.beginTransaction()
            ft.replace(R.id.frameLayout, SignupFragment())
            ft.commit()
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

