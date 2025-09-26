package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.TeacherEmail
import com.example.schoolcontrol.models.InstitutionalEmailResponse

class EmailAdapter(
    private var items: MutableList<TeacherEmail>,
    private val onEdit: (TeacherEmail) -> Unit
) : RecyclerView.Adapter<EmailAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvTeacherName)
        val tvEmail: TextView = v.findViewById(R.id.tvEmail)
        val btnEdit: Button = v.findViewById(R.id.btnEditEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val itt = items[position]
        holder.tvName.text = itt.teacher_name ?: "Profesor"
        holder.tvEmail.text = itt.email ?: ""
        holder.btnEdit.setOnClickListener { onEdit(itt) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<TeacherEmail>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
