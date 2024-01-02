package com.example.feedarticlesjetpack.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val registerViewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitRegisterFragment.setOnClickListener {
            val username = binding.etLoginRegisterFragment.text.toString()
            val password = binding.etPasswordRegisterFragment.text.toString()
            val confirmPassword = binding.etConfirmPasswordRegisterFragment.text.toString()

            if (password == confirmPassword) {
                registerViewModel.register(username, password)
            } else {
                Toast.makeText(requireContext(), R.string.toast_passwords_not_identical, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerResult.observe(viewLifecycleOwner) { apiResponse ->
            if (apiResponse.status == 1) {
                findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
            }
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}

