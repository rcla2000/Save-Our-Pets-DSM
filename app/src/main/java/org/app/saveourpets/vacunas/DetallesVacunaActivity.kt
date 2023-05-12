package org.app.saveourpets.vacunas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import org.app.saveourpets.R
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.razas.RazasActivity

class DetallesVacunaActivity : AppCompatActivity() {
    private lateinit var tvVacuna : TextView
    private lateinit var tvDescripcion : TextView
    private lateinit var btnVolver : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_vacuna)
        mostrarInfoVacuna()
        accionBtnVolver()
    }

    private fun obtenerDatosVacuna() : Vacuna {
        val idVacuna : Int = intent.getIntExtra("id_vacuna", -1)
        val nombre : String = intent.getStringExtra("vacuna").toString()
        val descripcion : String = intent.getStringExtra("descripcion").toString()
        val vacuna = Vacuna(idVacuna, nombre, descripcion)
        return vacuna
    }

    private fun mostrarInfoVacuna() {
        tvVacuna = findViewById<TextView>(R.id.item_title)
        tvDescripcion = findViewById<TextView>(R.id.item_descripcion)

        val vacuna : Vacuna = obtenerDatosVacuna()

        // Asignando a los elementos la información de la vacuna
        tvVacuna.text = vacuna.vacuna
        tvDescripcion.text = vacuna.descripcion
    }

    private fun accionBtnVolver() {
        btnVolver = findViewById<Button>(R.id.btn_volver)

        btnVolver.setOnClickListener {
            val intent = Intent(this, VacunasActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_especies -> {
                val intent = Intent(this, ListarEspeciesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_vacunas -> {
                val intent = Intent(this, VacunasActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_razas -> {
                val intent = Intent(this, RazasActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}