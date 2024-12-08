package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentBarcodeScannerBinding
import com.jeong.sesac.sai.util.BaseFragment

class BarcodeScannerFragment : BaseFragment<FragmentBarcodeScannerBinding>(FragmentBarcodeScannerBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScannerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            barcodeScannerBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentBarcodeScanner_to_fragmentFoundNoteDetail)
            }
        }
    }
}