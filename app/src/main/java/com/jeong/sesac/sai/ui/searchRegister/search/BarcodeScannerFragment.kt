package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentBarcodeScannerBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class BarcodeScannerFragment : BaseFragment<FragmentBarcodeScannerBinding>(FragmentBarcodeScannerBinding::inflate) {
    private val args : BarcodeScannerFragmentArgs by navArgs()
    private val noteInfo by lazy { args.findNoteInfo }


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
            btnBarcodeScanSuccess.clicks().onEach {
                val action = BarcodeScannerFragmentDirections.actionFragmentBarcodeScannerToFragmentBarcodeScannerSuccess(noteInfo)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            btnBarcodeScanFail.clicks().onEach {
                val action = BarcodeScannerFragmentDirections.actionFragmentBarcodeScannerToFragmentBarcodeScannerFail(noteInfo)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}