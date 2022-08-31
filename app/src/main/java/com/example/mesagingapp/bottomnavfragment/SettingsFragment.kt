package com.example.mesagingapp.bottomnavfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mesagingapp.R
import com.example.mesagingapp.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var firebaseFirestore : FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileUpdateButton.setOnClickListener {


        }


//        binding.profileUpdateButton.setOnClickListener {
//
//
//        }
//        val docRef = firebaseFirestore.collection("USERS").document(
//            "08p4RIY2NqmoYXPz8kyQ").update()
//
//          firebaseFirestore.collection("USERS").addSnapshotListener { value, error ->
//            val documents = value?.documents
//              if (documents != null) {
//                  for (document in documents) {
//                         document.getDocumentReference()
//
//                  }
//              }
//        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun updateDatabase() {
        val username = binding.profileIsim.text.toString()
        val email = binding.profileEmail.text.toString()
        val password = binding.profileSifre.text.toString()
        val state = binding.profileState.text.toString()

    }

}