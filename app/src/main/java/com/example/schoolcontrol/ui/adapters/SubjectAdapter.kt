package com.example.schoolcontrol.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.SubjectDto
import com.example.schoolcontrol.ui.EditSubjectActivity
import com.example.schoolcontrol.ui.SubjectDetailActivity

class SubjectAdapter(
    private val subjects: List<SubjectDto>,
    private val userRole: String?
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvId)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTeacher: TextView = view.findViewById(R.id.tvTeacher)
        val btnManage: Button = view.findViewById(R.id.btnManage)
        val btnEvaluations: Button = view.findViewById(R.id.btnEvaluations)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.tvId.text = subject.id.toString()
        holder.tvName.text = subject.name
        holder.tvTeacher.text = subject.teacher?.name ?: "Sin asignar"

        // Mostrar botón de editar solo si es ADMIN
        holder.btnManage.visibility = if (userRole == "ADMIN") View.VISIBLE else View.GONE
        
        holder.btnManage.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditSubjectActivity::class.java)
            intent.putExtra("SUBJECT_ID", subject.id)
            holder.itemView.context.startActivity(intent)
        }

        // Click Evaluations
        holder.btnEvaluations.setOnClickListener {
            val intent = Intent(holder.itemView.context, SubjectDetailActivity::class.java)
            intent.putExtra("SUBJECT_ID", subject.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = subjects.size
}
