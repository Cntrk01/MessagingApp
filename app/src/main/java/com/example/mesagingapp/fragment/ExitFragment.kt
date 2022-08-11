package com.example.mesagingapp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.mesagingapp.activity.LoginActivity
import com.example.mesagingapp.databinding.FragmentExitBinding
import com.example.mesagingapp.databinding.FragmentHomeBinding
import com.example.mesagingapp.util.showSuccesfullToast
import com.google.firebase.auth.FirebaseAuth


class ExitFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentExitBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       auth= FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExitBinding.inflate(inflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cikisYap.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Hesaptan Çıkılsın Mı ?")
                builder.setMessage("Hesaptan Çıkış Yapmak İstediğinizden Emin Misiniz ?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                builder.setPositiveButton("EVET") { dialogInterface, which ->
                    auth.signOut()
                    val intent=Intent(requireContext(),LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    showSuccesfullToast(
                        "Başarılı",
                        "Başarıyla Çıkış Yapıldı",
                        requireContext()
                    )
                }
                builder.setNegativeButton("HAYIR") { dialogInterface, which ->
                    showSuccesfullToast(
                        "Hayır",
                        "Hesaptan Çıkış Gerçekleşmedi",
                        requireContext()
                    )
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}