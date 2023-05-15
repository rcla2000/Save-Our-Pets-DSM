package org.app.saveourpets.usuarios.particular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import org.app.saveourpets.R
import org.app.saveourpets.reportes.CrearReporteActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.usuarios.Sesion

class PerfilActivity : AppCompatActivity() {
    private lateinit var tvNombres : TextView
    private lateinit var tvApellidos : TextView
    private lateinit var tvTelefono : TextView
    private lateinit var tvDui : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvFechaNacimiento : TextView
    private lateinit var tvDireccion : TextView
    private lateinit var btnEditarPerfil : Button
    private lateinit var btnVolver : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        cargarDatosPerfil()
        accionBtnVolver()
        accionBtnEditarPerfil()
    }

    private fun cargarDatosPerfil() {
        tvNombres = findViewById(R.id.tv_nombres)
        tvApellidos = findViewById(R.id.tv_apellidos)
        tvTelefono = findViewById(R.id.tv_telefono)
        tvDui = findViewById(R.id.tv_dui)
        tvEmail = findViewById(R.id.tv_email)
        tvFechaNacimiento = findViewById(R.id.tv_fecha_nacimiento)
        tvDireccion = findViewById(R.id.tv_direccion)

        tvNombres.text = Sesion.usuario.nombres
        tvApellidos.text = Sesion.usuario.apellidos
        tvTelefono.text = Sesion.usuario.telefono
        tvDui.text = Sesion.usuario.DUI
        tvEmail.text = Sesion.usuario.email
        tvFechaNacimiento.text = Sesion.usuario.fecha_nacimiento
        tvDireccion.text = Sesion.usuario.direccion
    }

    private fun accionBtnEditarPerfil() {
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil)
        btnEditarPerfil.setOnClickListener {
            val intent = Intent(this, ActualizarPerfilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun accionBtnVolver() {
        btnVolver = findViewById(R.id.btn_volver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuParticularActivity::class.java)
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