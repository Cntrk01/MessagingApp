package com.example.mesagingapp.bottomnavfragment


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mesagingapp.R
import com.example.mesagingapp.data.User
import com.example.mesagingapp.adapter.UsersRecyclerView
import com.example.mesagingapp.databinding.FragmentHomeBinding
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment(),UsersRecyclerView.OnItemClickListener {
    private lateinit var firebaseAuth: FirebaseAuth
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
        adapter = UsersRecyclerView(userList,requireContext(),this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun getUsersFromFirebase() {

            firebaseFirestore.collection("USERS").addSnapshotListener { snapshot, error ->
                if (error != null) {
                    showWarningToast(error.localizedMessage,null,requireContext())
                }
                userList.clear()
                val documents = snapshot?.documents
                if (documents != null) {
                    for (document in documents) {
                        val userName = document.get("KullaniciAdi") as String?
                        val imageUrl = document.get("GorselUrl").toString()
                        val user = User(userName!!, userImage = imageUrl, "state")
                        userList.add(user)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        firebaseFirestore.collection("USERS").whereEqualTo("E_posta",firebaseAuth.currentUser!!.email).addSnapshotListener { value, error ->
            if (error != null) {
                showWarningToast(error.localizedMessage,null,requireContext())
            }
            val documents = value?.documents
            if (documents != null) {
                for (document in documents) {
                    val userName = document.get("KullaniciAdi").toString()
                    val imageUrl = document.get("GorselUrl").toString()
                    binding.userHomeName.text=userName
                    if (!imageUrl.isEmpty()){
                        val request=RequestOptions()
                        request.placeholder(R.drawable.usericon)
                        Glide.with(requireContext()).setDefaultRequestOptions(request).load(imageUrl).into(binding.userHomeImageview)
                    }
                }
                }

            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(user: User) {
        val action = HomeFragmentDirections.actionHomeFragment2ToMessageScreenFragment(user)
        findNavController().navigate(action)
    }
}