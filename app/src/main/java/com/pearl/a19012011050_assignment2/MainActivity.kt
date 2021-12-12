package com.pearl.a19012011050_assignment2

import android.app.ActionBar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationid = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.side_nav)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            it.setChecked(true)
            when(it.itemId){
                R.id.side_home -> {
                    replaceFragmet(HomeFragment())
                }
                R.id.side_mobile -> Toast.makeText(this, "Clicked Mobile",Toast.LENGTH_SHORT).show()

                R.id.side_laptop -> Toast.makeText(this, "Clicked Laptop",Toast.LENGTH_SHORT).show()
                R.id.side_tablet -> Toast.makeText(this, "Clicked Tablet",Toast.LENGTH_SHORT).show()
                R.id.side_login -> {
                    replaceFragmet(LoginFragment())
                }
                R.id.side_editprofile -> Toast.makeText(this, "Clicked Edit Profile",Toast.LENGTH_SHORT).show()
                R.id.side_logout -> {
                    auth.signOut()
                    replaceFragmet(HomeFragment())
                    createNotificationChannel("Log Out", "Loggeed Out Successfully")
                    sendNotification("Log Out", "Loggeed Out Successfully")
                }
                R.id.side_contactus -> Toast.makeText(this, "Clicked contactus",Toast.LENGTH_SHORT).show()
                R.id.side_addproduct ->{
                    replaceFragmet(AddProductFragment())
                }

                R.id.side_cart->{
                    replaceFragmet(CartFragment())
                }
            }
            drawerLayout.closeDrawers()
            true
        }
        if(auth.currentUser == null){
            replaceFragmet(LoginFragment())
        } else {
            replaceFragmet(HomeFragment())
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragmet(fragment: Fragment){
        val fm:FragmentManager = supportFragmentManager
        val ft:FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.frameLayout, fragment)
        ft.commit()
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
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.common_full_open_on_phone)
            .setContentTitle(title)
            .setContentText(discription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationid, builder.build())
        }
    }
}