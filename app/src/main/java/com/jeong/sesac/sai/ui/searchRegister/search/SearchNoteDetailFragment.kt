package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSearchListBinding
import com.jeong.sesac.sai.databinding.FragmentSearchNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment

class SearchNoteDetailFragment : BaseFragment<FragmentSearchNoteDetailBinding>(FragmentSearchNoteDetailBinding::inflate){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNoteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            BarcodeScannerBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSearchNoteDetail_to_fragmentBarcodeScanner)
            }
        }
    }
}