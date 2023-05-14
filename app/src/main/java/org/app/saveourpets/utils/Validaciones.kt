package org.app.saveourpets.utils

class Validaciones {
    companion object {
        fun estaVacio(cadena : String) : Boolean {
            if (cadena.isEmpty()) {
                return true
            }
            return false
        }
        fun validarEmail(email: String): Boolean {
            val regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            return regex.matches(email)
        }

        fun validarTelefono(telefono : String) : Boolean {
            val regex = Regex("^[267][0-9]{7}$")
            return regex.matches(telefono)
        }

        fun validarDui(dui : String) : Boolean {
            val regex = Regex("^[0-9]{8}-[0-9]$")
            return regex.matches(dui)
        }
    }
}