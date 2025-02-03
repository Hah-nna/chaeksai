package com.jeong.sesac.sai.ui.libraryMap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.databinding.FragmentLibraryMapSearchBinding
import com.jeong.sesac.sai.recycler.map.MapSearchAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges

class MapSearchFragment :
    BaseFragment<FragmentLibraryMapSearchBinding>(FragmentLibraryMapSearchBinding::inflate) {
    private lateinit var searchListAdapter: MapSearchAdapter
    private val viewModel: KakaoMapViewModel by activityViewModels {
        appViewModelFactory
    }
    private var place = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
        setRecycler()
    }

    private fun setRecycler() {
        searchListAdapter = MapSearchAdapter(
            lifecycleScope = viewLifecycleOwner.lifecycleScope
        ) { placeInfo ->
            viewModel.setSelectedPlace(placeInfo)
            findNavController().popBackStack()

        }

        binding.rvSearchResults.apply {
            adapter = searchListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUI() {
        with(binding) {
            btnBack.clicks().onEach {
                findNavController().popBackStack()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            etvSearch.apply {
                requestFocus()
                textChanges().debounce(300L).onEach { text ->
                    place = text.toString()
                    // viewModel.searchPlaces(text.toString())
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }


}
