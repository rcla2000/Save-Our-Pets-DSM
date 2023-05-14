package org.app.saveourpets.usuarios.particular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.app.saveourpets.R

class MenuParticularActivity : AppCompatActivity() {
    private lateinit var btnRepotar : Button
    private lateinit var btnAdoptar : Button
    private lateinit var btnPerfil : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_particular)
    }

    private fun acciones() {
        btnPerfil = findViewById(R.id.btn_perfil)
        btnAdoptar = findViewById(R.id.btn_adoptar)
        btnRepotar = findViewById(R.id.btn_reportar)

        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}