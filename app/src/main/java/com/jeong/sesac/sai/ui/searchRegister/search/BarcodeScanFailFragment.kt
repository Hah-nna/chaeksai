package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentBarcodeScanFailBinding
import com.jeong.sesac.sai.databinding.FragmentBarcodeScannerBinding
import com.jeong.sesac.sai.util.BaseFragment

class BarcodeScanFailFragment :
    BaseFragment<FragmentBarcodeScanFailBinding>(FragmentBarcodeScanFailBinding::inflate) {

    private val args: BarcodeScanFailFragmentArgs by navArgs()
    private val noteInfo by lazy { args.findNoteInfo }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScanFailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnGoToScan.setOnClickListener {
                val action = BarcodeScanFailFragmentDirections
                    .actionFragmentBarcodeScannerFailToFragmentBarcodeScanner(noteInfo)
                findNavController().navigate(action)
            }
        }
    }
}