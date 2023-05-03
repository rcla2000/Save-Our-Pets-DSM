package org.app.saveourpets.vacunas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R

class VacunaAdapter(private val elementos : List<Vacuna>): RecyclerView.Adapter<VacunaAdapter.ViewHolder>() {
    private var btnActualizarListener : OnBtnActualizarListener? = null
    private var btnEliminarListener : OnBtnEliminarListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_vacunas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elementos[position]
        holder.title.text=item.vacuna
        holder.subtitle.text = ""

        if (item.descripcion.length>90) {
            holder.description.text = item.descripcion.substring(0,87) + "..."
        }else{
            holder.description.text = item.descripcion
        }

        holder.imagen.setImageResource(R.drawable.vacuna_icon)

        holder.btnActualizar.setOnClickListener {
            btnActualizarListener?.onBtnActualizarClick(item)
        }

        holder.btnEliminar.setOnClickListener {
            btnEliminarListener?.onBtnEliminarClick(item)
        }
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen : ImageView = view.findViewById(R.id.imgViewer)
        val title: TextView = view.findViewById(R.id.item_title)
        val subtitle : TextView = view.findViewById(R.id.item_subtitle)
        val description: TextView = view.findViewById(R.id.item_descripcion)
        val btnActualizar : Button = view.findViewById<Button>(R.id.btnUpdate)
        val btnEliminar : Button = view.findViewById<Button>(R.id.btnDelete)
    }

    fun setOnBtnEliminarListener(listener: OnBtnEliminarListener?) {
        this.btnEliminarListener = listener
    }

    fun setOnBtnActualizarListener(listener: OnBtnActualizarListener?) {
        this.btnActualizarListener = listener
    }
    interface OnBtnActualizarListener {
        fun onBtnActualizarClick(vacuna: Vacuna)
    }
    interface OnBtnEliminarListener {
        fun onBtnEliminarClick(vacuna: Vacuna)
    }
}