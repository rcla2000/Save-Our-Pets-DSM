package org.app.saveourpets.especies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R

class EspecieAdapter(private val especies : List<Especie>): RecyclerView.Adapter<EspecieAdapter.ViewHolder>() {
    private var listener : OnItemClickListener? = null
    private var btnEliminarListener : OnBtnEliminarListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_especies, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = especies[position]
        holder.nombreespecie.text=item.nombre
        holder.subitem.text = ""
        holder.description.text = item.id_especie.toString()

        if(item.nombre == "Gato"){
            holder.imgespecie.setImageResource(R.drawable.cat_icon)
        }else if(item.nombre == "Perro"){
            holder.imgespecie.setImageResource(R.drawable.dog_icon)
        }else{
           holder.imgespecie.setImageResource(R.drawable.catdog_icon)
        }

        holder.btnActualizar.setOnClickListener {
            listener?.onItemClick(item)
        }

        holder.btnEliminar.setOnClickListener {
            btnEliminarListener?.onBtnEliminarClick(item)
        }
    }

    override fun getItemCount(): Int {
        return especies.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgespecie : ImageView = view.findViewById(R.id.imgEspecie)
        val nombreespecie: TextView = view.findViewById(R.id.item_especie)
        val subitem : TextView = view.findViewById(R.id.item_subespecie)
        val description: TextView = view.findViewById(R.id.item_descripcion)
        val btnActualizar : Button = view.findViewById<Button>(R.id.btnUpdate)
        val btnEliminar : Button = view.findViewById<Button>(R.id.btnDelete)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setOnBtnEliminarListener(listener: OnBtnEliminarListener?) {
        this.btnEliminarListener = listener
    }
    interface OnItemClickListener {
        fun onItemClick(especie: Especie)
    }

    interface OnBtnEliminarListener {
        fun onBtnEliminarClick(especie: Especie)
    }
}