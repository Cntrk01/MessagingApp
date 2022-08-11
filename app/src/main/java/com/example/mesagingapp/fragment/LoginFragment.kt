package com.example.mesagingapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mesagingapp.activity.MainActivity
import com.example.mesagingapp.databinding.FragmentLoginBinding
import com.example.mesagingapp.util.showSuccesfullToast
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        name=""
        fireStore.collection("USERS").whereEqualTo("E_posta",auth.currentUser?.email).addSnapshotListener { value, error ->
            if (error != null) {
                showWarningToast(error.localizedMessage, null, requireContext())
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        val documents = value.documents
                        for (i in documents) {
                            name = i.get("KullaniciAdi") as String
                        }
                    }
                }
            }
        }

        if(auth.currentUser !=null){
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = binding.eposta.text
        val password = binding.sifre.text




        binding.sifre.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {

                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        showSuccesfullToast("Başarılı", "Hoşgeldin ${name}", requireContext())
                        activity?.finish()
                    }
            }
            true
        }

        binding.loginButton.setOnClickListener {
            if (email.toString() == "") {
                showWarningToast("E-Mail Boş Olamaz", null, requireContext())
            } else if (password.toString() == "") {
                showWarningToast("Şifre Boş Olamaz", null, requireContext())
            } else {
                //Giriş Başarılıysa Main Ekranına gidecek

                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        showSuccesfullToast("Başarılı", "Hoşgeldin ${name}", requireContext())
                        activity?.finish()
//                    val intent=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
//                    Navigation.findNavController(view).navigate(intent)
                        //showSuccesfullToast("Başarılı Giriş ","Hoşgeldin ${name}",requireContext())
                    }
            }
        }

        binding.registerButton.setOnClickListener {
            //kayıt ekranına yönlenecek
            val intent =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(view).navigate(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}