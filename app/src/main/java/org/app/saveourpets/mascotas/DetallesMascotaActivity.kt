package org.app.saveourpets.mascotas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.app.saveourpets.R
import org.app.saveourpets.especies.Especie

class DetallesMascotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_mascota)
    }

    private fun obtenerMascota() : Especie {
        val idEspecie : Int = intent.getIntExtra("id_especie", -1)
        val nombre : String = intent.getStringExtra("nombre").toString()
        val especie = Especie(idEspecie, nombre)
        return especie
    }
}