package com.raju.mvvm.testing.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.databinding.ItemUserBinding

class UserAdapter(private val clickListener: (user: User) -> Unit) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bindData(user)
        holder.itemView.setOnClickListener {
            clickListener.invoke(user)
        }
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(user: User) {
            binding.tvName.text = user.name
            binding.ivAvatar.load(user.avatarUrl)
        }
    }

    internal class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(p0: User, p1: User): Boolean {
            return p0.name == p1.name
        }

        override fun areContentsTheSame(p0: User, p1: User): Boolean {
            return p0 == p1
        }
    }
}