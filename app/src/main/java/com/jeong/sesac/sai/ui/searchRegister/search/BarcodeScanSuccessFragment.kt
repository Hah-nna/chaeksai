package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentBarcodeScanSuccessBinding
import com.jeong.sesac.sai.util.BaseFragment

class BarcodeScanSuccessFragment : BaseFragment<FragmentBarcodeScanSuccessBinding>(FragmentBarcodeScanSuccessBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScanSuccessBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnConfirm.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentBarcodeScanner_to_fragmentFoundNoteDetail)
            }
        }
    }
}