package com.example.proyectof

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var mensaje: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()
        mensaje = AlertDialog.Builder(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){

            R.id.nav_item_one -> startActivity(Intent(this, MainActivity::class.java))
            R.id.nav_item_two -> startActivity(Intent(this, SueldosActivity::class.java))
            R.id.nav_item_three -> startActivity(Intent(this, PrestamoActivity::class.java))
            R.id.nav_item_six -> cerrarSesion()
            R.id.nav_item_seven -> salirApp()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cerrarSesion(){
        mensaje.setTitle("Confirmación")
            .setMessage("¿Desea cerrar sesión?")
            .setCancelable(true)
            .setPositiveButton("Si"){dialogInterface, it ->
                auth.signOut()
                Toast.makeText(this,"Sesión cerrada correctamente", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .setNegativeButton("No"){dialogInterface, it -> dialogInterface.cancel()}
            .show()
    }

    private fun salirApp(){
        mensaje.setTitle("Confirmación")
            .setMessage("¿Desea Salir de la Aplicación?")
            .setCancelable(true)
            .setPositiveButton("Si"){dialogInterface, it ->
                auth.signOut()
                finishAffinity()
            }
            .setNegativeButton("No"){dialogInterface, it -> dialogInterface.cancel()}
            .show()
    }
}