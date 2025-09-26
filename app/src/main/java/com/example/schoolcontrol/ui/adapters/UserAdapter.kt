package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.UserResponse

class UserAdapter(
    private val items: MutableList<UserResponse>,
    private val onChangeRole: (UserResponse) -> Unit,
    private val onDelete: (UserResponse) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvUserName)
        val tvEmail: TextView = v.findViewById(R.id.tvUserEmail)
        val btnRole: Button = v.findViewById(R.id.btnChangeRole)
        val btnDelete: Button = v.findViewById(R.id.btnDeleteUser)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val grade = items[position]
        holder.tvName.text = grade.full_name
        holder.tvEmail.text = grade.email
        holder.btnRole.setOnClickListener { onChangeRole(grade) }
        holder.btnDelete.setOnClickListener { onDelete(grade) }
    }
    override fun getItemCount(): Int = items.size
    fun updateData(newItems: List<UserResponse>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
