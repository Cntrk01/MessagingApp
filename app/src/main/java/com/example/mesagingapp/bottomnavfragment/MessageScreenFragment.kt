package com.example.mesagingapp.bottomnavfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mesagingapp.R
import com.example.mesagingapp.adapter.ChatRecyclerAdapter
import com.example.mesagingapp.data.MessageData
import com.example.mesagingapp.databinding.FragmentMessageScreenBinding
import com.example.mesagingapp.util.showSuccesfullToast
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class MessageScreenFragment : Fragment() {
    private val args by navArgs<MessageScreenFragmentArgs>()
    private var _binding: FragmentMessageScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapter: ChatRecyclerAdapter
    private var chatsList = arrayListOf<MessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatRecyclerAdapter()
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tanımla()
        getMessage()
    }

    private fun tanımla() {
        val request = RequestOptions()
        request.placeholder(R.drawable.usericon)
        Glide.with(requireContext()).setDefaultRequestOptions(request).load(args.user.userImage)
            .into(binding.messageUserImage)
        binding.messageUserName.text = args.user.userName
        binding.messageBackButton.setOnClickListener {
            val intent = MessageScreenFragmentDirections.actionMessageFragmentToHomeFragment()
            Navigation.findNavController(requireView()).navigate(intent)
        }

//        if (binding.sendMessageText.text.toString().trim().isNotEmpty()) {
//            Log.d("Loglama","working")
//            binding.sendMessageButton.setOnClickListener {
//                Log.d("Loglama2","working")
//                message(binding.sendMessageText.text.toString())
//                binding.sendMessageText.setText("")
//            }
//        }
        binding.sendMessageButton.setOnClickListener {
            if (binding.sendMessageText.text.toString().isNotBlank()) {
                message(binding.sendMessageText.text.toString())
                binding.sendMessageText.text.clear()
            }
        }

    }

    private fun message(message: String) {
        val id =
            firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.email!!)
                .collection(args.user.userName).document(args.user.userName).id
        val hashMapMessage = HashMap<String, Any>()
        hashMapMessage.put("message", message)
        hashMapMessage.put("sender", firebaseAuth.currentUser!!.displayName!!)
        hashMapMessage.put("receiver", args.user.userName)
        hashMapMessage.put("time", Timestamp.now())

        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.displayName!!)
            .collection(args.user.userName).document(args.user.userName).collection(id)
            .add(hashMapMessage).addOnCompleteListener {

            }
        firebaseFirestore.collection("MESSAGE").document(args.user.userName)
            .collection(firebaseAuth.currentUser!!.displayName!!)
            .document(firebaseAuth.currentUser!!.displayName!!).collection(id).add(hashMapMessage)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                }
            }
//        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.displayName!!).collection(args.user.userName).document(id).set(hashMapMessage).addOnCompleteListener {
//            if(it.isSuccessful){
//                firebaseFirestore.collection("MESSAGE").document(args.user.userName).collection(firebaseAuth.currentUser!!.displayName!!).document(id).set(hashMapMessage).addOnCompleteListener {
//                    if(it.isSuccessful){
//
//                    }
//                }
//            }
//        }
        //BU KOD DA ÇALIŞIYOR YUKARIDAKİ YERİNE
//        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.uid!!)
//            .collection(args.user.userUID).add(hashMapMessage).addOnCompleteListener {
//                if(it.isSuccessful){
//                    firebaseFirestore.collection("MESSAGE").document(args.user.userUID)
//                        .collection(firebaseAuth.currentUser!!.uid!!).add(hashMapMessage).addOnCompleteListener {
//                            showSuccesfullToast(it.toString(),"Mesaj Başarılı",requireContext())
//                        }
//                }
//            }


    }

    private fun getMessage() {
        val id = firebaseFirestore.collection("MESSAGE")
            .document(firebaseAuth.currentUser!!.displayName!!).collection(args.user.userName)
            .document(args.user.userName).id
        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.displayName!!)
            .collection(args.user.userName).document(args.user.userName).collection(id)
            .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                if (error != null) {
                    showWarningToast("Hata", "Hata Var", requireContext())
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val doc = value.documents
                            chatsList.clear()
                            for (i in doc) {
                                val message = i.get("message").toString()
                                val receiver = i.get("receiver").toString()
                                val sender = i.get("sender").toString()
                                val mesajModel = MessageData(message, receiver, sender)
                                Log.e("mesajlarr", mesajModel.toString())
                                chatsList.add(mesajModel)
                                adapter.chats = chatsList
                            }
                            adapter.notifyDataSetChanged()
                        }

                    }
                }
            }

//        val id2 = firebaseFirestore.collection("MESSAGE")
//            .document(firebaseAuth.currentUser!!.displayName!!).collection(args.user.userName)
//            .document(args.user.userName).id
//        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.displayName!!)
//            .collection(args.user.userName).document(args.user.userName).collection(id2)
//            .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
//                if (error != null) {
//                    showWarningToast("Hata", "Hata Var", requireContext())
//                } else {
//                    if (value != null) {
//                        if (!value.isEmpty) {
//                            val doc = value.documents
//                            chatsList.clear()
//                            for (i in doc) {
//                                val message = i.get("message").toString()
//                                val receiver = i.get("receiver").toString()
//                                val sender = i.get("sender").toString()
//                                val mesajModel = MessageData(message, receiver, sender)
//                                Log.e("mesajlarr", mesajModel.toString())
//                                chatsList.add(mesajModel)
//                                adapter.chats = chatsList
//                            }
//                            adapter.notifyDataSetChanged()
//                        }
//
//                    }
//                }
//            }

//        firebaseFirestore.collection("MESSAGE").document(firebaseAuth.currentUser!!.uid!!)
//            .collection(args.user.userUID).orderBy("time",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
//                if(error !=null){
//                    showWarningToast("Hata","Hata Var",requireContext())
//                }else{
//                    if(value !=null){
//                        if (!value.isEmpty){
//                            val doc=value.documents
//                            for (i in doc){
//                                val message=i.get("message").toString()
//                                val receiver=i.get("receiver").toString()
//                                val sender=i.get("sender").toString()
//                                val mesajModel=MessageData(message, receiver, sender)
//                                Log.e("mesajlarr",mesajModel.toString())
//                            }
//                        }
//                    }
//                }
//            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}