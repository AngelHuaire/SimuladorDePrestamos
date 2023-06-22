package com.example.proyectof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityPrestamoBinding
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.*

class PrestamoActivity : AppCompatActivity() {

    lateinit var binding: ActivityPrestamoBinding
    private lateinit var mensaje: AlertDialog.Builder
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrestamoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_prestamo)

        MyToolbar().show(this,"Detalle de Prestamo", true)
        auth = FirebaseAuth.getInstance()
        mensaje = AlertDialog.Builder(this)

        muestraFecha()

        val bundle: Bundle? = intent.extras
        val montomaximo = "%.2f".format(bundle?.get("MontoMaximo"))
        binding.edtMontoMaximo.text = "$montomaximo"

        var edtTeaNuevo:Double = 28.3
        var edtDesgravamenNuevo:Double = 0.25
        var edtTemNuevo : Double =  edtTeaNuevo / 12
        var edtTceaNuevo:Double = edtDesgravamenNuevo + edtTeaNuevo + edtTemNuevo


        var PrestamoTotal:Double =  0.00
        var Amortizacion:Double =  0.00
        var Interes:Double =  0.00
        var DesgravamenFinal:Double =  0.00
        var ValorCuota:Double =  0.00

        binding.btnVer.setOnClickListener {

            if (binding.edtMontoDesembolsar.text.toString() == "" || binding.edtNumCuotas.text.toString() == "") {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ingrese monto a desembolsar y numero de cuotas")
                    .setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }else if(binding.edtMontoMaximo.text.toString().toDouble() < binding.edtMontoDesembolsar.text.toString().toDouble() ){
                val builder = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("El monto maximo del prestamo es S/.$montomaximo")
                    .setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {

            binding.txvTcea.text = "%.2f".format(edtTceaNuevo)
            binding.txvTea.text = "%.2f".format(edtTeaNuevo)
            binding.txvDesgravamen.text = "%.2f".format(edtDesgravamenNuevo)
            binding.txvTem.text = "%.2f".format(edtTemNuevo)

            PrestamoTotal = binding.edtMontoDesembolsar.text.toString().toDouble() +
                    ((binding.edtMontoDesembolsar.text.toString().toDouble() * edtTceaNuevo) / 100)

            ValorCuota = PrestamoTotal / binding.edtNumCuotas.text.toString().toDouble()

            Amortizacion = (ValorCuota * 80) / 100

            DesgravamenFinal = (ValorCuota * edtDesgravamenNuevo) / 100

            Interes = ValorCuota - (Amortizacion + DesgravamenFinal)
            fechaDeVencimiento()
                binding.txvNCuota.text = binding.edtNumCuotas.text
            binding.txvValorCuota.text = "%.2f".format(ValorCuota)
            binding.txvAmortizacion.text = "%.2f".format(Amortizacion)
            binding.txvInteres.text = "%.2f".format(Interes)
            binding.txvDesgravamenFinal.text = "%.2f".format(DesgravamenFinal)
            binding.txvPrestamoTotal.text = "%.2f".format(PrestamoTotal)

            }

        }

    }

    private fun muestraFecha(){
        val horaActual = Instant.now()
        //var fechaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        var fechaActual = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        binding.txvFechaActual.text = fechaActual.toString()

    }

    private fun fechaDeVencimiento(){
        val fecha = Calendar.getInstance()
        //val day: Int = fecha.get(Calendar.DAY_OF_MONTH)
        fecha.add(Calendar.MONTH, 1)


        val dateFormat = "dd-MMMM-yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

        binding.txvFechaVencimiento.text = simpleDateFormat.format(fecha.time)
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