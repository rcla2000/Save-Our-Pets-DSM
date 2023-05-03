package org.app.saveourpets.utils

class Validaciones {
    companion object {
        fun estaVacio(cadena : String) : Boolean {
            if (cadena.isEmpty()) {
                return true
            }
            return false
        }
    }
}