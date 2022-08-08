package com.example.mesagingapp

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mesagingapp.databinding.FragmentLoginBinding
import com.example.mesagingapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var _binding:FragmentRegisterBinding
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    var secilenGorsel : Uri?=null
    var secilenBitmap: Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
    }

    private fun registerButonu(view: View){
        val reference=storage.reference
        val uuid= UUID.randomUUID()
        val gorselIsim="${uuid}.jpg"
        val gorselRef=reference.child("gorseller").child("${gorselIsim}")

        if(secilenGorsel !=null){
            gorselRef.putFile(secilenGorsel!!).addOnSuccessListener {task->
                val yuklenecekGorsel=reference.child("gorseller").child(gorselIsim)
                yuklenecekGorsel.downloadUrl.addOnSuccessListener {uri->
                        val downloadUrl=uri.toString()
                         veritabanınaKayıtEt(downloadUrl)
                }

            }.addOnFailureListener{
                Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }else{
            veritabanınaKayıtEt(null)
        }
    }

    private fun veritabanınaKayıtEt(url:String?) {
         val kullaniciAdi=binding.kullaniciAdi.text.toString()
         val e_posta=binding.eposta.text.toString()
         val sifree=binding.sifre.text.toString()
         val sifreTekrar=binding.sifreTekrar.text.toString()

        if(kullaniciAdi ==null){
            Toast.makeText(requireContext(),"Kullanıcı Adı Boş Olamaz", Toast.LENGTH_SHORT).show()
        }else if(e_posta==null){
            Toast.makeText(requireContext(),"E-Posta Boş Olamaz", Toast.LENGTH_SHORT).show()
        }else if(sifree==null){
            Toast.makeText(requireContext(),"Şifre Boş Olamaz", Toast.LENGTH_SHORT).show()
        }else if(sifreTekrar==null) {
            Toast.makeText(requireContext(), "Şifre Boş Olamaz", Toast.LENGTH_SHORT).show()
        }else if(!sifree.equals(sifreTekrar)) {
            Toast.makeText(requireContext(), "Şifre Birbirinden Farklı Olamaz !!", Toast.LENGTH_SHORT).show()
        }else{

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRegisterBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

}