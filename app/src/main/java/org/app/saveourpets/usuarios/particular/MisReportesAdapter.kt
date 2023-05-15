package org.app.saveourpets.usuarios.particular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R
import org.app.saveourpets.reportes.Reporte

class MisReportesAdapter(private val reportes : List<Reporte>): RecyclerView.Adapter<MisReportesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_reporte, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reportes[position]
        holder.tvDescripcion.text = item.descripcion
        holder.tvDireccion.text = item.direccion
    }

    override fun getItemCount(): Int {
        return reportes.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescripcion = view.findViewById<TextView>(R.id.tv_descripcion)
        val tvDireccion = view.findViewById<TextView>(R.id.tv_direccion)
    }

}