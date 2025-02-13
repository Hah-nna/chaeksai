package com.jeong.sesac.sai.ui.libraryMap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentLibraryMapBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.map.MapAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.label.TrackingManager
import com.kakao.vectormap.label.TransformMethod
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class LibraryMapFragment :
    BaseFragment<FragmentLibraryMapBinding>(FragmentLibraryMapBinding::inflate) {
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var preference: AppPreferenceManager
    private lateinit var startLocation: LatLng
    private var startZoomLevel = 15

    private val viewModel: KakaoMapViewModel by viewModels {
        appViewModelFactory
    }

    private lateinit var trackingManager: TrackingManager

    private var currentLocationLabel: Label? = null
    private var headingLabel: Label? = null

    private var libraryLabels = mutableSetOf<LodLabel>()
    private lateinit var mapAdapter: MapAdapter

    private val interaction_distance = 5.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // preference 초기화
        preference = AppPreferenceManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        /**
         * preference에 있는 값(위도, 경도)로 초기화(처음에는 서울시청)
         * */
        startLocation = LatLng.from(
            preference.lastLat,
            preference.lastLng
        )

        setRecyclerView()

        mapView = binding.mapView
        // 지도 시작
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(err: Exception?) {}
        }, readyCallback)
        return view
    }

    /**
     * mapView.start할 때 들어갈 콜백
     */
    private val readyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(map: KakaoMap) {
            kakaoMap = map
            trackingManager = kakaoMap.trackingManager!!
            binding.progressBar.progressCircular.isVisible = true

            kakaoMap.setOnLodLabelClickListener { _, _, label ->
                // 클릭된 라벨의 위치와 이름
                val clickedPosition = label.position
                val libraryName = label.texts[0]

                // viewModel에서 현재 위치를 가져와서 거리 계산
                viewModel.currentLocationState.value?.let { currentLocation ->
                    val distance = calculateDistance(
                        currentLocation.latitude, currentLocation.longitude,
                        clickedPosition.latitude, clickedPosition.longitude
                    )

                    // 디버깅을 위한 로그 추가
                    Log.d("Distance", "Distance to $libraryName: $distance meters")

                    if (distance <= interaction_distance) {
                        // 해당 라벨로 이동(줌레벨 16)
                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(clickedPosition, 16)
                        kakaoMap.moveCamera(cameraUpdate)

                        viewLifecycleOwner.lifecycleScope.launch {
                            val currentState = viewModel.librariesInfoState.value
                            when (currentState) {
                                is UiState.Loading -> {
                                    binding.progressBar.progressCircular.visibility = View.VISIBLE
                                }
                                is UiState.Success -> {
                                    binding.progressBar.progressCircular.visibility = View.GONE
                                    currentState.data.find { it.place == libraryName }?.let { library ->
                                        showLibraryInfo(library)
                                    }
                                }
                                is UiState.Error -> {
                                    binding.progressBar.progressCircular.visibility = View.GONE
                                    throw Error("에러입니다")
                                }
                            }
                        }
                    } else {
                        showLibraryInfo("도서관에 더 가까이 가야 열람할 수 있습니다 (${distance.toInt()}m 남았습니다)")
                    }
                } ?: run {
                    // 현재 위치를 가져올 수 없는 경우
                    showLibraryInfo("현재 위치를 확인할 수 없습니다")
                }
                true
            }

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(startLocation)
            kakaoMap.moveCamera(cameraUpdate)
        }

        override fun getPosition(): LatLng = startLocation
        override fun getZoomLevel(): Int = startZoomLevel
    }


    private fun calculateDistance(
        currentLat: Double, currentLng: Double,
        clickedLat: Double, clickedLng: Double
    ): Float {
        val result = FloatArray(1)
        Location.distanceBetween(currentLat, currentLng, clickedLat, clickedLng, result)
        return result[0]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view가 started 상태일 때마다 권한 체크 실행
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isNetworkAvailable()) {
                    showRequestRuntimePermission()
                } else {
                    Toast.makeText(requireContext(), "네트워크 환경을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * 현재위치 버튼을 누르면 권한, gps 체크
         * 1. 사용가능한 네크워크인지 체크
         *  -> true -> 런타임 퍼미션 요청
         *  -> false -> 네트워크 환경을 확인해주세요
         * */
        binding.btnCurrentLocation.clicks().throttleFirst(throttleTime).onEach {
            if (isNetworkAvailable()) {
                showRequestRuntimePermission()
            } else {
                Toast.makeText(requireContext(), "네트워크 환경을 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        // preference에 저장된 위치에서 5km내의 도서관 목록 get요청
//        binding.btnReSearchPlaces.clicks()
//            .throttleFirst(throttleTime)
//            .onEach {
//                getLibraries()
//            }.launchIn(lifecycleScope)

    }

    private fun setRecyclerView() {
        // 도서관 리사이클러뷰 콜백 설정
        mapAdapter = MapAdapter(
            onRegisterCallback = { libraryInfo ->
                try {
                    val action = LibraryMapFragmentDirections
                        .actionFragmentLibraryMapFragmentToFragmentLibraryWriteNote(libraryInfo.place)
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("Navigation", "Navigation failed", e)
                }
            },
            onLibraryNotesCallback = { libraryInfo ->
                val findAction =
                    LibraryMapFragmentDirections.actionFragmentLibraryMapFragmentToFragmentLibraryNoteList(
                        libraryInfo.place
                    )
                findNavController().navigate(findAction)
            },
            viewLifecycleOwner.lifecycleScope,
        )

        // 리사이클러뷰 설정
        with(binding) {
            rvLibraryInfo.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = this@LibraryMapFragment.mapAdapter
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }
    }

    // 네크워크를 사용할 수 있는지 확인
    private fun isNetworkAvailable(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = cm.activeNetwork ?: return false
        val networkCapabilities = cm.getNetworkCapabilities(nw) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    // gps on, off 확인 함수
    private fun isGPSAvailable(): Boolean {
        val lm =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * api 33이상과 이하를 나누어서 분기를 나눔
     * 로케이션에 대한 권한 승인 요청을 함
     */
    private fun showRequestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TedPermission.create()
                .setPermissionListener(tedPermissionListerCallback)
                .setDeniedMessage("계속하려면 기기에서 위치정보를 사용해야합니다")
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).check()
        } else {
            // api 33 아래
            TedPermission.create()
                .setPermissionListener(tedPermissionListerCallback)
                .setDeniedMessage("계속하려면 기기에서 위치정보를 사용해야합니다")
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).check()
        }
    }

    /**
     * showRequestRuntimePermission()의 setPermissionListener에서 콜백으로 호출하는 함수
     * 승인을 했다면 gps가 on / off인지 확인한다
     * 승인을 안 했다면 승인해달라는 토스트 띄우기
     * */
    private var tedPermissionListerCallback: PermissionListener = object : PermissionListener {
        // 로케이션 권한 승인 되었을 때
        override fun onPermissionGranted() {
            Toast.makeText(requireActivity(), "퍼미션 승인되어있음", Toast.LENGTH_SHORT).show()
            // 승인되었으면 gps 켜져있는지 확인하고
            // 켜져있으면 할 일 적기(따로 함수로 빼서)
            if (isGPSAvailable()) {
                Toast.makeText(requireActivity(), "gps 켜져있어유", Toast.LENGTH_SHORT).show()
                // 현재 위치 받아서 이동하고 마커 띄우는 함수

                updateLocationAndPoi()
//                getLibraries()
            } else {
                // gps 안 켜져 있으면 gps 설정
                showGPSSettingDialog()
            }
        }

        // 로케이션 권한 승인 안 되었을 때
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(requireActivity(), "위치사용 권한을 켜주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLocationAndPoi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getCurrentLocation()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.currentLocationState.collectLatest { location ->
                    location?.let {
                        updateCurrentLocationLabel(
                            LatLng.from(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
                }
            }
        }

        observeLibraryInfo()
    }

    private fun observeLibraryInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.librariesInfoState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.progressCircular.isVisible = true
                    }

                    is UiState.Success -> {
                        binding.progressBar.progressCircular.isVisible = false
                        UiState.Success(state.data)
                        val libraries = state.data

                        if (libraries.isEmpty()) {
                            showLibraryInfo("주변 도서관이 없어 다시 찾는 중입니다")
                        } else {
                            val firstLibraryDistance = libraries[0].distance.toInt()
                            showLibraryInfo(if (firstLibraryDistance <= 100) "주변 도서관이 없어 다시 찾는 중입니다" else "100m 이내에 도서관이 없어 더 넓은 범위를 검색합니다\n가장 가까운 도서관은 ${libraries[0].place}이고, ${libraries[0].distance}m 거리에 있습니다")
                        }

                        // 기존 도서관 마커들 제거
                        libraryLabels.forEach { it.remove() }
                        libraryLabels.clear()


                        binding.tvLibraryInfo.isVisible = true
                        // 새로운 도서관 마커들 생성
                        libraries.forEach { library ->
                            val libraryPosition = LatLng.from(
                                library.lat.toDouble(),
                                library.lng.toDouble()
                            )
                            createLocationLabel(libraryPosition, library.place)
                        }
                        // 리사이클러뷰 업데이트
                        mapAdapter.submitList(libraries)

                    }

                    is UiState.Error -> {
                        binding.progressBar.progressCircular.isVisible = false
                        binding.tvLibraryInfo.setText("도서관 정보를 불러오는데 실패했습니다")
                    }
                }
            }
        }
    }

    // gps on 설정하는 함수
    private fun showGPSSettingDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("GPS")
            .setMessage("GPS 서비스가 꺼져있습니다. 설정으로 이동하시겠습니까?")
            .setNegativeButton("취소") { dialog, _ ->
                Toast.makeText(requireContext(), "GPS 취소", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setPositiveButton("설정") { dialog, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .show()
    }


    private fun updateCurrentLocationLabel(currentLocation: LatLng) {
        if (currentLocationLabel == null) {
            val locationStyle = kakaoMap.labelManager!!.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.ic_current_location)
                        .setAnchorPoint(0.5f, 0.5f)
                )
            )

            val headingStyle = kakaoMap.labelManager!!.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.ic_red_direction_area)
                        .setAnchorPoint(0.5f, 1.0f)
                )
            )

            val layer = kakaoMap.labelManager!!.layer
            // 현재 위치 마커 생성
            currentLocationLabel = layer!!.addLabel(
                LabelOptions.from(currentLocation).setRank(10)
                    .setStyles(locationStyle)
                    .setTransform(TransformMethod.AbsoluteRotation_Decal)
            )

            // 방향 표시 마커 생성
            headingLabel = layer.addLabel(
                LabelOptions.from(currentLocation).setRank(9)
                    .setStyles(headingStyle)
                    .setTransform(TransformMethod.AbsoluteRotation_Decal)
            )

            currentLocationLabel?.let {
                currentLocationLabel!!.addSharePosition(headingLabel)
                trackingManager.startTracking(currentLocationLabel)
                trackingManager.setTrackingRotation(true)
            }
        } else {
            currentLocationLabel?.moveTo(currentLocation)
        }
        val updateCamera = CameraUpdateFactory.newCenterPosition(currentLocation)
        kakaoMap.moveCamera(updateCamera)
    }

    // Lodlabel(마커들) 생성
    private fun createLocationLabel(position: LatLng, place_name: String) {
        viewModel.currentLocationState.value?.let { currentLocation ->
            val distance = calculateDistance(
                currentLocation.latitude, currentLocation.longitude,
                position.latitude, position.longitude
            )

            Log.d("라벨", "라벨 $place_name, distance: $distance meters")

            val styles = kakaoMap.labelManager!!.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(
                        if(distance <= interaction_distance) {
                            R.drawable.ic_active_marker
                        } else {
                            R.drawable.ic_inactive_marker
                        }
                    ).setTextStyles(24, Color.BLACK, 2, Color.WHITE)
                )
            )

            val options = LabelOptions.from(position)
                .setStyles(styles).setTexts(LabelTextBuilder().setTexts(place_name))

            val layer = kakaoMap.labelManager!!.lodLayer
            val label = layer!!.addLodLabel(options)

            libraryLabels.add(label)
        }
    }

    // label 눌렀을 때 보여줄 bottom sheet와 리사이클러뷰어댑터 설정
    private fun showLibraryInfo(lib: PlaceInfo) {
        val bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            isFitToContents = true
        }

        mapAdapter.submitList(listOf(lib))
    }

    private fun showLibraryInfo(message: String) {
        binding.tvProgressState.apply {
            text = message
            alpha = 1f
            isVisible = true

            animate()
                .alpha(0f)
                .setStartDelay(5000)
                .setDuration(500)
                .withEndAction {
                    visibility = View.GONE
                }
                .start()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()

        if (::kakaoMap.isInitialized) {
            val currentPosition = LatLng.from(
                preference.lastLat,
                preference.lastLng
            )

            // 현재 카메라 위치와 preference에 저장된 위치가 다른 경우에만 이동
            if (startLocation != currentPosition) {
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(currentPosition)
                kakaoMap.moveCamera(cameraUpdate)
                startLocation = currentPosition
                binding.btnReSearchPlaces.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        Log.d("map onPause", "onPause")
    }
}


