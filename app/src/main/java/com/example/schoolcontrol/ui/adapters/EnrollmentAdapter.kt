package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.EnrollmentDto

class EnrollmentAdapter(
    private val enrollments: List<EnrollmentDto>
) : RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder>() {

    class EnrollmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvStudentName)
        val tvEmail: TextView = view.findViewById(R.id.tvStudentEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_enrollment, parent, false)
        return EnrollmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnrollmentViewHolder, position: Int) {
        val enrollment = enrollments[position]
        val student = enrollment.student
        holder.tvName.text = student?.name ?: "Sin nombre"
        holder.tvEmail.text = student?.email ?: "Sin email"
    }

    override fun getItemCount(): Int = enrollments.size
}
