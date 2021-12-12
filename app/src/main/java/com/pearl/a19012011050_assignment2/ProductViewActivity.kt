package com.pearl.a19012011050_assignment2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.pearl.a19012011050_assignment2.model.ProductCart
import java.io.File

class ProductViewActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationid = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)
        val extras: Bundle? = getIntent().extras
        val pname:TextView = findViewById(R.id.productView_name)
        val pprice:TextView = findViewById(R.id.productView_price)
        val pcat:TextView = findViewById(R.id.productView_category)
        val pimage:ImageView = findViewById(R.id.productView_image)
        val pcart:Button = findViewById(R.id.addToCartBtn)
        val category = extras?.get("category")
        val name = extras?.get("name")
        pname.setText(name.toString())
        pcat.setText(category.toString())
        var auth:FirebaseAuth = Firebase.auth

        if (auth.currentUser != null) {
            val uid = auth.currentUser?.uid
            val cartRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users/$uid/cart/$name")
            cartRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        pcart.setEnabled(false)
                    } else{
                        pcart.setEnabled(true)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }

            })
            pcart.setOnClickListener {


                if (auth.currentUser!=null){
                    val databaseReference:DatabaseReference = FirebaseDatabase.getInstance().reference
                        .child("Users/$uid/cart/${pname.text}")
                    var product = ProductCart("product/${pcat.text}/${pname.text}",1)
                    databaseReference.setValue(product).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this@ProductViewActivity, "Product Added Successfully", Toast.LENGTH_SHORT).show()
                            createNotificationChannel("Added In Cart", pname.text.toString())
                            sendNotification("Added In Cart", pname.text.toString())

                        }
                    }
                } else{
                    Toast.makeText(this@ProductViewActivity,"Please log in", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val productRef: DatabaseReference = FirebaseDatabase.getInstance()
            .getReference("product/$category/$name")
        productRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   val product = snapshot.getValue(Product::class.java)
                   if (product != null){
                       Glide.with(this@ProductViewActivity).load(product.productImageUrl).into(pimage)
                       pprice.setText(product.productPrice)
                   }

               }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun createNotificationChannel(title:String, myDiscription:String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importace = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, title, importace).apply {
                description = myDiscription
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun  sendNotification(title:String, discription:String){
        val builder =NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.common_full_open_on_phone)
            .setContentTitle(title)
            .setContentText(discription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationid, builder.build())
        }
    }
}