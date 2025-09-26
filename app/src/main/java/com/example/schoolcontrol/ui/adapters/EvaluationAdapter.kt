package com.example.schoolcontrol.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.EvaluationDto
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.ui.GradesActivity

class EvaluationAdapter(
    private val evaluations: List<EvaluationDto>,
    private val grades: List<GradeDto>?,
    private val userRole: String?
) : RecyclerView.Adapter<EvaluationAdapter.EvaluationViewHolder>() {

    inner class EvaluationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEvalName: TextView = view.findViewById(R.id.tvEvalName)
        val tvEvalDescription: TextView = view.findViewById(R.id.tvEvalDescription)
        val tvEvalGrade: TextView = view.findViewById(R.id.tvEvalGrade)
        val btnEditGrades: Button = view.findViewById(R.id.btnEditGrades)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evaluation, parent, false)
        return EvaluationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EvaluationViewHolder, position: Int) {
        val evaluation = evaluations[position]
        holder.tvEvalName.text = evaluation.name
        holder.tvEvalDescription.text = evaluation.description ?: "Sin descripción"
        if (userRole == "STUDENT") {
            holder.tvEvalGrade.visibility = View.VISIBLE
            val grade = grades?.find { it.evaluationId == evaluation.id }?.score
            holder.tvEvalGrade.text = "Nota: ${grade ?: "Sin Nota"}"
        } else {
            holder.tvEvalGrade.visibility = View.GONE
        }

        holder.btnEditGrades.visibility = if (userRole == "TEACHER") View.VISIBLE else View.GONE

        holder.btnEditGrades.setOnClickListener {
            val intent = Intent(holder.itemView.context, GradesActivity::class.java)
            intent.putExtra("EVALUATION_ID", evaluation.id)
            intent.putExtra("SUBJECT_ID", evaluation.subjectId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = evaluations.size
}
