package com.example.mesagingapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mesagingapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email=binding.eposta.text
        val password=binding.sifre.text

        binding.loginButton.setOnClickListener {
            if(TextUtils.isEmpty(email)){
                Toast.makeText(activity,"Kullanıcı Adı Boş Olamaz",Toast.LENGTH_SHORT).show()
            }else if(TextUtils.isEmpty(password)){
                Toast.makeText(activity,"Şifre Boş Olamaz",Toast.LENGTH_SHORT).show()
            }
            else{
                //Giriş Başarılıysa Main Ekranına gidecek
                auth.signInWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener {
                    val intent=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    Navigation.findNavController(view).navigate(intent)
                }.addOnFailureListener {
                    Toast.makeText(activity,it.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerButton.setOnClickListener {
            //kayıt ekranına yönlenecek

            val intent=LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(view).navigate(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentLoginBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}