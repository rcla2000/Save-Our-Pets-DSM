package org.app.saveourpets.reportes

data class Reporte(
    val id_reporte : Int,
    val id_especie : Int,
    val descripcion : String,
    val direccion : String,
    val usuario : Int,
    val foto : String,
    val latitud : Double,
    val longitud : Double
)
