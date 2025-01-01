package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentSearchRegisterBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.lang.Exception

class MapSearchRegisterFragment :
    BaseFragment<FragmentSearchRegisterBinding>(FragmentSearchRegisterBinding::inflate) {
    private lateinit var mapView: MapView


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

        with(binding) {
            btnRegister.clicks().onEach {
                val registerAction = MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentRegisterNote("새싹도서관")
                findNavController().navigate(registerAction)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            btnFind.clicks().onEach {
                val findAction = MapSearchRegisterFragmentDirections
                    .actionFragmentSearchRegisterToFragmentSearch(
                        WeeklyNoteMockData.notesList.toTypedArray()
                    )
                findNavController().navigate(findAction)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }


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