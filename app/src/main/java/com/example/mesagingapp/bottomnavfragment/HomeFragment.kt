package com.example.mesagingapp.bottomnavfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mesagingapp.R
import com.example.mesagingapp.User
import com.example.mesagingapp.UsersRecyclerView
import com.example.mesagingapp.databinding.FragmentHomeBinding
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
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
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

        firebaseAuth.currentUser!!.uid.let {
            firebaseFirestore.collection("USERS").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    showWarningToast(error.localizedMessage,null,requireContext())
                }
                userList.clear()
                val documents = snapshot?.documents
                if (documents != null) {
                    for (document in documents) {

                        val userName = document.get("KullaniciAdi") as String
                        val imageUrl = document.get("GorselUrl").toString()
                        val user = User(userName, userImage = imageUrl, "state")
                        val dad = firebaseAuth.currentUser!!.displayName
                        val foto=document.get("GorselUrl")


                        binding.userHomeName.text= dad
                        if (!imageUrl.isEmpty()){
                            Glide.with(requireContext())
                                .load(firebaseAuth.currentUser!!.photoUrl)
                                .into(binding.userHomeImageview)
                            //Glide.with(requireContext()).load(user.userImage).into(binding.userHomeImageview)
                        }
                        if (user.userName != firebaseAuth.currentUser!!.displayName){
                            userList.add(user)
                        }


                    }
                    adapter.notifyDataSetChanged()

                }
            }
        }

        }


//        firebaseFirestore.collection("USERS").get().addOnSuccessListener {
//            if (it.isEmpty) {
//                showWarningToast("MESSAGE",null,requireContext())
//            }
//            val documents = it.documents
//            for (document in documents) {
//                val userName = document.get("KullaniciAdi") as String
//                val imageUrl = document.get("GorselUrl") as String
//                val user = User(userName, userImage = imageUrl, "state")
//                binding.userHomeName.text=userName
//                userList.add(user)
//                if (!imageUrl.isEmpty()){
//                       Glide.with(requireContext()).load(imageUrl).into(binding.userHomeImageview)
//                   }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}