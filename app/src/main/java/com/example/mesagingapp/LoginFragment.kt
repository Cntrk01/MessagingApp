package com.example.mesagingapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.ui.text.input.ImeAction
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mesagingapp.databinding.FragmentLoginBinding
import com.example.mesagingapp.util.showInfoToast
import com.example.mesagingapp.util.showSuccesfullToast
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var fireStore:FirebaseFirestore
    private lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        fireStore= FirebaseFirestore.getInstance()
        name=""
        firebaseUser=auth.currentUser!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email=binding.eposta.text
        val password=binding.sifre.text

        binding.sifre.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {

                auth.signInWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener {
                   if(it.isSuccessful){
//                       val userProfileContextCompat=UserProfileChangeRequest.Builder()
//                           .setPhotoUri()
                       val intent=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                       Navigation.findNavController(view).navigate(intent)
                       showSuccesfullToast("Başarılı Giriş ","Hoşgeldin ",requireContext())
                   }
                }
            }
            true
        }

        binding.loginButton.setOnClickListener {
            if(email.toString()==""){
                showWarningToast("E-Mail Boş Olamaz",null,requireContext())
            }else if(password.toString()==""){
                showWarningToast("Şifre Boş Olamaz",null,requireContext())
            }
            else{
                //Giriş Başarılıysa Main Ekranına gidecek
                auth.signInWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener {
                        val intent=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        Navigation.findNavController(view).navigate(intent)
                        showSuccesfullToast("Başarılı Giriş ","Hoşgeldin ",requireContext())
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