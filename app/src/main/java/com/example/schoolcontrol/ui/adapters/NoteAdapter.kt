package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.GradeResponse

class NoteAdapter(
    private var items: MutableList<GradeResponse>,
    private val onEdit: (GradeResponse) -> Unit,
    private val onDelete: (GradeResponse) -> Unit
) : RecyclerView.Adapter<NoteAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvSubject: TextView = v.findViewById(R.id.tvSubjectItem)
        val tvGrade: TextView = v.findViewById(R.id.tvGradeItem)
        val btnEdit: Button = v.findViewById(R.id.btnEdit)
        val btnDelete: Button = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val grade = items[position]
        holder.tvSubject.text = grade.subject
        holder.tvGrade.text = grade.grade_value.toString()
        holder.btnEdit.setOnClickListener { onEdit(grade) }
        holder.btnDelete.setOnClickListener { onDelete(grade) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<GradeResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

