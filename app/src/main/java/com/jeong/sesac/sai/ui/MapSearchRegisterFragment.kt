package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentSearchRegisterBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.material.checkedChanges
import ru.ldralighieri.corbind.view.clicks
import java.lang.Exception

class MapSearchRegisterFragment :
    BaseFragment<FragmentSearchRegisterBinding>(FragmentSearchRegisterBinding::inflate) {
    private lateinit var registerNotedAdapter : GridNotesAdapter
    private lateinit var findNotedAdapter : GridNotesAdapter
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSearchRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(err: Exception?) {
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
            }
        })

        registerNotedAdapter = GridNotesAdapter { registerNote ->
            val action = MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentRegisterNote("새싹도서관")
            findNavController().navigate(action)

        }

        findNotedAdapter = GridNotesAdapter { findNote ->
            val action = MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentSearchList(
                arrayOf(findNote)
            )
            findNavController().navigate(action)
        }

        with(binding) {
            /**
             *
             * */
            btnRegister.clicks().onEach {
                val registerAction = MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentRegisterNote("새싹도서관")
                findNavController().navigate(registerAction)
            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .launchIn(viewLifecycleOwner.lifecycleScope)

            btnFind.clicks().onEach {
                val findAction = MapSearchRegisterFragmentDirections
                    .actionFragmentSearchRegisterToFragmentSearchList(
                        WeeklyNoteMockData.notesList.toTypedArray()
                    )
                findNavController().navigate(findAction)
            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        registerNotedAdapter.submitList(WeeklyNoteMockData.notesList)
        findNotedAdapter.submitList(WeeklyNoteMockData.notesList)

    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}