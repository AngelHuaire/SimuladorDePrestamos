package com.example.proyectof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityMainBinding
import com.example.proyectof.databinding.ActivitySueldosBinding
import com.google.firebase.auth.FirebaseAuth

class SueldosActivity : AppCompatActivity() {

    private lateinit var mensaje: AlertDialog.Builder
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sueldos)

        val bundle: Bundle? = intent.extras
        val nombreCliente = bundle?.get("nombreCliente")
        val txvNombreCliente = findViewById<TextView>(R.id.txvNombreCliente)
        txvNombreCliente.text = "Bienvenido: $nombreCliente"

        MyToolbar().show(this,"Datos del Sueldo", true)
        auth = FirebaseAuth.getInstance()
        mensaje = AlertDialog.Builder(this)

        val sueldoBase = findViewById<EditText>(R.id.edtSueldoBase)
        val variables = findViewById<EditText>(R.id.edtVariables)
        val aporteAfp = findViewById<EditText>(R.id.edtAporteAfp)
        val seguro = findViewById<EditText>(R.id.edtSeguro)
        val neto = findViewById<TextView>(R.id.edtNeto)

        val btnCalcularNeto = findViewById<Button>(R.id.btnCalcularNeto)
        val btnVerificar = findViewById<Button>(R.id.btnVerificar)

        var NetoNuevo: Double = 0.00

        btnCalcularNeto.setOnClickListener {
            if (sueldoBase.text.toString() == "" || variables.text.toString() == "" ||
                aporteAfp.text.toString()=="" || seguro.text.toString() == ""){
                val builder = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Complete todos los datos")
                    .setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                NetoNuevo = ((sueldoBase.text.toString().toDouble() + variables.text.toString().toDouble())-
                        (aporteAfp.text.toString().toDouble() + seguro.text.toString().toDouble()))

                neto.text = "%.2f".format(NetoNuevo)
            }
        }

        btnVerificar.setOnClickListener {
            var intentp = Intent(applicationContext, PrestamoActivity::class.java)
            intentp.putExtra("MontoMaximo",neto.text.toString().toDouble() * 10)
            startActivity(intentp)
        }




    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.itemCerrarSesion -> cerrarSesion()
            R.id.itemSalir -> salirApp()
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