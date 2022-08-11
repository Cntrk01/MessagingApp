package com.example.mesagingapp.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mesagingapp.R
import com.example.mesagingapp.activity.MainActivity
import com.example.mesagingapp.databinding.FragmentRegisterBinding
import com.example.mesagingapp.util.showSuccesfullToast
import com.example.mesagingapp.util.showWarningToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage


    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //izin verilmiş
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageViewUser.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            secilenGorsel
                        )
                    binding.imageViewUser.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register(view)
        gorselEklemeFonk()

    }

    private fun gorselEklemeFonk() {
        binding.imageViewUser.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {//izin verilmiş galeriye git
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
    }

    private fun register(view: View) {
        val reference = storage.reference
        val uuid = UUID.randomUUID()
        val gorselIsim = "${uuid}.jpg"
        val gorselRef = reference.child("gorseller").child("${gorselIsim}")
        binding.registerButton.setOnClickListener {
            if (secilenGorsel != null) {
                gorselRef.putFile(secilenGorsel!!).addOnSuccessListener { task ->
                    val yuklenecekGorsel = reference.child("gorseller").child(gorselIsim)
                    yuklenecekGorsel.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        veritabanınaKayıtEt(downloadUrl, view)
                    }

                }.addOnFailureListener {
                    showWarningToast("Hata", it.localizedMessage, requireContext())
                }
            } else {
                veritabanınaKayıtEt(null, view)
            }
        }
    }

    private fun veritabanınaKayıtEt(url: String?, view: View) {

        val kullaniciAdi = binding.kullaniciAdi.text.toString()
        val e_posta = binding.eposta.text.toString()
        val sifree = binding.sifre.text.toString()
        val sifreTekrar = binding.sifreTekrar.text.toString()

        if (kullaniciAdi == "") {
            showWarningToast("Kullanıcı Adı Boş Olamaz", "Kullanıcı Adını Doldurunuz", requireContext())
        } else if (e_posta == "") {
            showWarningToast("E-Posta Boş Olamaz", "E-Postayı Doldurunuz", requireContext())
        } else if (sifree == "") {
            showWarningToast("Şifre Boş Olamaz", "Şifreyi Doldurunuz", requireContext())
        } else if (sifreTekrar == "") {
            showWarningToast("Şifre Boş Olamaz", "Şifreyi Doldurunuz", requireContext())
        } else if (!sifreTekrar.equals(sifree)) {
            showWarningToast("Şifre Birbirinden Farklı Olamaz", null, requireContext())
        } else {
            val registerHashMap = HashMap<String, Any>()
            registerHashMap.put("KullaniciAdi", kullaniciAdi)
            registerHashMap.put("E_posta", e_posta)
            registerHashMap.put("Sifre", sifree)

            if (url != null) {
                registerHashMap.put("GorselUrl", url)
            } else {
                registerHashMap.put("GorselUrl", R.drawable.usericon)
            }

            firestore.collection("USERS").add(registerHashMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e("Data","Data Başarıyla Eklendi")
                }
            }
            auth.createUserWithEmailAndPassword(e_posta, sifree)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        showSuccesfullToast("Başarılı", kullaniciAdi, requireContext())
                    }

                }
        }


    }
}


