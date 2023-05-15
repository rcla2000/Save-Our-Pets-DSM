package org.app.saveourpets.mascotas

data class Mascota(
    val id_mascota: Int,
    val nombre_mascota: String,
    val id_especie: Int,
    val especie: String,
    val id_raza: Int,
    val raza: String,
    val color_pelo: String,
    val fecha_nacimiento: String,
    val peso: Int,
    val esterilizado: String,
    val id_estado: Int,
    val estado: String
)
