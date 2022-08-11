package com.example.mesagingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.mesagingapp.adapter.UsersRecyclerView.UsersViewHolder
import com.example.mesagingapp.bottomnavfragment.HomeFragmentDirections
import com.example.mesagingapp.data.User
import com.example.mesagingapp.databinding.ItemRowMessageBinding
import com.google.firebase.auth.FirebaseAuth


class UsersRecyclerView(val userList: List<User>, val context: Context, private val listener:OnItemClickListener) : RecyclerView.Adapter<UsersViewHolder>() {

    inner class UsersViewHolder(val binding: ItemRowMessageBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = ItemRowMessageBinding.inflate(LayoutInflater.from(parent.context),null,false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user=FirebaseAuth.getInstance()
        holder.binding.itemTextViewUserName.text = userList[position].userName
        holder.binding.itemTextViewState.text = userList[position].userState
        Glide.with(context).load(userList[position].userImage.toUri()).into(holder.binding.itemCircleImageView)

        holder.itemView.setOnClickListener {
            val poz=userList.get(position)
            listener.onItemClick(poz)

//            val intent=HomeFragmentDirections.actionHomeFragment2ToMessageScreenFragment(poz)
//            Navigation.findNavController(it).navigate(intent)
        }



    }
    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    override fun getItemCount() = userList.size
}