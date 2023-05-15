package org.app.saveourpets.usuarios.particular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import org.app.saveourpets.R
import org.app.saveourpets.reportes.CrearReporteActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.mascotas.ListaMascotasAdoptablesActivity

class MenuParticularActivity : AppCompatActivity() {
    private lateinit var btnReportar : Button
    private lateinit var btnAdoptar : Button
    private lateinit var btnPerfil : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_particular)
        acciones()
    }

    private fun acciones() {
        btnPerfil = findViewById(R.id.btn_perfil)
        btnAdoptar = findViewById(R.id.btn_adoptar)
        btnReportar = findViewById(R.id.btn_reportar)

        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnReportar.setOnClickListener {
            val intent = Intent(this, CrearReporteActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnAdoptar.setOnClickListener {
            val intent = Intent(this, ListaMascotasAdoptablesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_usuario, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reportar -> {
                val intent = Intent(this, CrearReporteActivity::class.java)
                startActivity(intent)
                finish()
            }
            /*R.id.action_adoptar -> {
                val intent = Intent(this, VacunasActivity::class.java)
                startActivity(intent)
                finish()
            }*/
            R.id.action_perfil -> {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_cerrar_sesion -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}