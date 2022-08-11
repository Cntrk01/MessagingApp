package com.example.mesagingapp.bottomnavfragment.bottomscreenfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mesagingapp.R
import com.example.mesagingapp.databinding.ActivityLoginBinding.inflate
import com.example.mesagingapp.databinding.FragmentMessageScreenBinding
import com.example.mesagingapp.databinding.FragmentRegisterBinding


class MessageScreenFragment : Fragment() {
    private val args by navArgs<MessageScreenFragmentArgs>()
    private lateinit var _binding: FragmentMessageScreenBinding
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.apply {
            val request= RequestOptions()
            request.placeholder(R.drawable.usericon)
            Glide.with(requireContext()).setDefaultRequestOptions(request).load(args.user.userImage).into(messageUserImage)
            messageUserName.text=args.user.userName

        }

    }

}