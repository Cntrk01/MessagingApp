package com.example.mesagingapp.bottomnavfragment

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mesagingapp.R
import com.example.mesagingapp.User
import com.example.mesagingapp.UsersRecyclerView
import com.example.mesagingapp.databinding.FragmentHomeBinding
import com.example.mesagingapp.databinding.ItemRowMessageBinding
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class HomeFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: UsersRecyclerView

    private var userList = arrayListOf<User>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsersFromFirebase()
        adapter = UsersRecyclerView(userList,requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter



    }

    fun getUsersFromFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("USERS").addSnapshotListener { snapshot, error ->
            if (error != null) {
                showWarningToast(error.localizedMessage,null,requireContext())
                return@addSnapshotListener
            }
            userList.clear()
            val documents = snapshot?.documents
            if (documents != null) {
                for (document in documents) {
                    val userName = document.get("KullaniciAdi") as String
                    val imageUrl = document.get("GorselUrl").toString()
                    val user = User(userName, userImage = imageUrl, "state")
                    userList.add(user)


                }
                adapter.notifyDataSetChanged()

            }
        }

        }

//        firebaseFirestore.collection("USERS").get().addOnSuccessListener { snapshot ->
//            if (snapshot.isEmpty) {
//                return@addOnSuccessListener
//            }
//            val documents = snapshot.documents
//            for (document in documents) {
//                val userName = document.get("KullaniciAdi") as String
//                val imageUrl = document.get("GorselUrl") as String
//                val user = User(userName, userImage = imageUrl, "state")
//                userList.add(user)
//            }
//
//        }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}