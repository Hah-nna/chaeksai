package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentBarcodeScanFailBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

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
            btnGoToScan.clicks().onEach {
                val action = BarcodeScanFailFragmentDirections
                    .actionFragmentBarcodeScannerFailToFragmentBarcodeScanner(noteInfo)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}