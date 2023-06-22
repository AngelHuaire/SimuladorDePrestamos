package com.example.proyectof

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mensaje: AlertDialog.Builder
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        MyToolbar().show(this,"Datos del Cliente", false)
        auth = FirebaseAuth.getInstance()
        mensaje = AlertDialog.Builder(this)


        val bundle: Bundle? = intent.extras
        val usuarioi = bundle?.get("email")
        binding.txtUsuarioI.text = usuarioi.toString()
        binding.txtUsuarioI.text = "$usuarioi"


        binding.btnFechaIngreso.setOnClickListener {
            setDate()
        }

        binding.btnConsultar.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtNombres.text) || TextUtils.isEmpty(binding.edtDni.text) ||
                TextUtils.isEmpty(binding.edtTrabajo.text) || TextUtils.isEmpty(binding.edtCargo.text) ||
                TextUtils.isEmpty(binding.edtFechaIngreso.text)){
                val builder = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Complete todos los datos")
                    .setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }else {
                var intents = Intent(applicationContext, SueldosActivity::class.java)
                intents.putExtra("nombreCliente",binding.edtNombres.text)
                startActivity(intents)
            }

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




    private fun setDate(){
        val fecha = Calendar.getInstance()

        val date = DatePickerDialog.OnDateSetListener{
            view: DatePicker?, year: Int, month:Int, dayOfMonth:Int ->
            fecha[Calendar.YEAR] = year
            fecha[Calendar.MONTH] = month
            fecha[Calendar.DAY_OF_MONTH] = dayOfMonth
            val dateFormat = "dd-MMMM-yyyy"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

            binding.edtFechaIngreso.text = simpleDateFormat.format(fecha.time)

            var fechaActual = Date(System.currentTimeMillis())

            val difFechas = fechaActual.getTime()-fecha.timeInMillis
            val segundos = difFechas/1000
            val minutos = segundos/60
            val horas = minutos/60
            val dias = horas/24
            Toast.makeText(this, "Dias transcurrido $dias", Toast.LENGTH_SHORT).show()


        }

      val limitar =  DatePickerDialog(
            this@MainActivity,date,
            fecha[Calendar.YEAR],
            fecha[Calendar.MONTH],
            fecha[Calendar.DAY_OF_MONTH]
        )
        limitar.show()
        limitar.datePicker.maxDate = fecha.timeInMillis


    }

}