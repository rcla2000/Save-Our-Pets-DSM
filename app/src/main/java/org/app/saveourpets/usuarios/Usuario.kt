package org.app.saveourpets.usuarios

data class Usuario(
    val id_usuario : Int,
    val id_tipo : Int,
    val nombres : String,
    val apellidos : String,
    val email: String,
    val telefono: String,
    val DUI : String,
    val direccion : String,
    val fecha_nacimiento : String,
    val contra : String
) {
    constructor(email: String, contra: String) : this(0,0,"","",email,"","","","",contra)
}
