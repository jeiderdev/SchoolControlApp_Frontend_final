package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.models.UserDto

class UserAdapter(private val users: List<UserDto>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvUserId)
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvEmail: TextView = view.findViewById(R.id.tvUserEmail)
        val tvActive: TextView = view.findViewById(R.id.tvUserActive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.tvId.text = "ID: ${user.id}"
        holder.tvName.text = user.name
        holder.tvEmail.text = user.email
        holder.tvActive.text = if (user.active) "Activo" else "Inactivo"
    }

    override fun getItemCount(): Int = users.size
}
