package com.example.mesagingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mesagingapp.UsersRecyclerView.UsersViewHolder
import com.example.mesagingapp.databinding.ItemRowMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UsersRecyclerView(val userList: List<User>,val context: Context) : RecyclerView.Adapter<UsersViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseFirestore: FirebaseFirestore

    inner class UsersViewHolder(val binding: ItemRowMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = ItemRowMessageBinding.inflate(LayoutInflater.from(parent.context),null,false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {

        holder.binding.itemTextViewUserName.text = userList[position].userName
        holder.binding.itemTextViewState.text = userList[position].userState

        Glide.with(context)
            .load(userList[position].userImage.toUri())

            .into(holder.binding.itemCircleImageView)



    }

    override fun getItemCount() = userList.size
}