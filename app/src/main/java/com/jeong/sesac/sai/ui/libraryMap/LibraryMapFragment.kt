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
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.PolygonStyles
import com.kakao.vectormap.shape.PolygonStylesSet
import com.kakao.vectormap.shape.ShapeManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


const val INTERACTION_DISTANCE = 100

class LibraryMapFragment :
    BaseFragment<FragmentLibraryMapBinding>(FragmentLibraryMapBinding::inflate) {
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var preference: AppPreferenceManager
    private lateinit var mapAdapter: MapAdapter
    private var startZoomLevel = 15

    private lateinit var defaultLocation: LatLng
    private var lastLocation: LatLng? = null
    private var isFirstLocationUpdate = false

    private lateinit var trackingManager: TrackingManager
    private lateinit var shapeManager: ShapeManager

    private var currentLocationLabel: Label? = null
    private var headingLabel: Label? = null
    private var libraryLabels = mutableMapOf<String, LodLabel>()
    private var libraryPolygons = mutableMapOf<String, Polygon>()
    private var libraryStates = mutableMapOf<String, Boolean>()

    private val viewModel: KakaoMapViewModel by viewModels {
        appViewModelFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // preference 초기화
        preference = AppPreferenceManager.getInstance(requireContext())
        // 기본 위치(서울시청)
        defaultLocation = LatLng.from(preference.lastLat, preference.lastLng)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        setRecyclerView()

        mapView = binding.mapView
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
            shapeManager = kakaoMap.shapeManager!!
            binding.progressBar.progressCircular.isVisible = true

            kakaoMap.setOnLodLabelClickListener { _, _, label ->
                val libraryName = label.texts[0]

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.currentLocationState
                        .combine(viewModel.librariesInfoState) { location, librariesState ->
                            Pair(location, librariesState)
                        }
                        .collectLatest { (currentLocation, librariesState) ->
                            when (librariesState) {
                                is UiState.Success -> {
                                    // 도서관 정보를 가져오기 성공했을 때
                                    librariesState.data.find { it.place == libraryName }
                                        ?.let { library ->
                                            currentLocation?.let { location ->
                                                // 현재 위치에서 실시간으로 거리 계산
                                                val realTimeDistance = calculateDistance(
                                                    location.latitude, location.longitude,
                                                    library.lat.toDouble(), library.lng.toDouble()
                                                )

                                                if (realTimeDistance <= INTERACTION_DISTANCE) {
                                                    // 상호작용 가능거리(100m) 내에 있을 때
                                                    showLibraryInfo(
                                                        library.copy(
                                                            distance = realTimeDistance.toString()
                                                        )
                                                    )
                                                } else {
                                                    // 상호작용 가능 거리(100m)보다 멀때
                                                    showLibraryInfo(
                                                        "도서관에 더 가까이 가야 열람할 수 있습니다\n" +
                                                                "${realTimeDistance.toInt()}m 남았습니다"
                                                    )
                                                }
                                            } ?: showLibraryInfo("현재 위치를 확인할 수 없습니다\n위치 권한을 확인해주세요")
                                        } ?: showLibraryInfo("도서관 정보를 찾을 수 없습니다")
                                }

                                is UiState.Loading -> {
                                    binding.progressBar.progressCircular.isVisible = true
                                    showLibraryInfo("도서관 정보를 불러오는 중입니다")
                                }

                                is UiState.Error -> {
                                    binding.progressBar.progressCircular.isVisible = false
                                    val errorMessage = when {
                                        librariesState.error.contains("network") ->
                                            "네트워크 연결을 확인해주세요"

                                        librariesState.error.contains("permission") ->
                                            "위치 권한이 필요합니다"

                                        else -> "도서관 정보를 불러오는데 실패했습니다\n${librariesState.error}"
                                    }
                                    showLibraryInfo(errorMessage)
                                }
                            }
                        }
                }
                true
            }
        }

        override fun getPosition(): LatLng = defaultLocation
        override fun getZoomLevel(): Int = startZoomLevel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Sheet 설정
        val bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheet.visibility = View.GONE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        /**
         * view가 started 상태일 때마다 권한 체크 실행
         *  */
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

    }

    /**
     * 리사이클러뷰 셋팅
     * */
    private fun setRecyclerView() {
        // 도서관 리사이클러뷰 콜백 설정
        mapAdapter = MapAdapter(
            onRegisterCallback = { libraryInfo ->
                try {
                    val action = LibraryMapFragmentDirections
                        .actionFragmentLibraryMapFragmentToFragmentLibraryWriteNote(libraryInfo.place)
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("Navigation", "Navigation 에러: ${e.message}")
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

    /**
     * 네크워크를 사용할 수 있는지 확인
     */
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
            // 승인되었으면 gps 켜져있는지 확인
            if (isGPSAvailable()) {
                Toast.makeText(requireActivity(), "gps 켜져있어유", Toast.LENGTH_SHORT).show()

                updateLocationAndPoi()
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

    /**
     * 위치와 정보를 업데이트 함
     */
    private fun updateLocationAndPoi() {
        viewModel.updateLocations()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentLocationState.collectLatest { location ->
                    location?.let {
                        val currentLocation = LatLng.from(location.latitude, location.longitude)
                        if (!isFirstLocationUpdate) {
                            val cameraUpdate =
                                CameraUpdateFactory.newCenterPosition(currentLocation)
                            kakaoMap.moveCamera(cameraUpdate)
                            isFirstLocationUpdate = true
                        }
                        updateCurrentLocationLabel(currentLocation)
                        lastLocation = currentLocation
                    }
                }
            }
        }
        observeLibraryInfo()
    }

    private var isStart = false
    private fun observeLibraryInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.librariesInfoState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.progressCircular.isVisible = true
                    }

                    is UiState.Success -> {
                        binding.progressBar.progressCircular.isVisible = false
                        val libraries = state.data

                        showLibraryInfo("도서관의 원 안으로 들어가면\n쪽지와 서평을 열람할 수 있습니다!\n원으로 이동해보세요")

                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.currentLocationState.collectLatest { location ->
                                if (!isStart) {
                                    createLabels(libraries, location!!)
                                    isStart = true
                                }
                                updateCurrentLocationLabel(
                                    LatLng.from(
                                        location!!.latitude,
                                        location.longitude
                                    )
                                )
                                updateLabelStyle(libraries, location)
                            }
                        }

                        if (libraries.isEmpty()) {
                            showLibraryInfo("주변 도서관이 없어 다시 찾는 중입니다")
                        } else {
                            val closestLibrary = libraries.minByOrNull { it.distance.toDouble() }
                            showLibraryInfo(
                                if (closestLibrary!!.distance.toDouble() <= 100.0) "${closestLibrary.place}의 쪽지와 서평을 열람할 수 있습니다!\n버튼을 눌러주세요" else "가장 가까운 도서관은 ${closestLibrary.place}이고,\n${closestLibrary.distance}m 거리에 있습니다\n" +
                                        "원 안으로 들어가면 쪽지와 서평을 열람할 수 있습니다!"
                            )
                        }

                        binding.tvLibraryInfo.isVisible = true

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

    // createLabels
    private fun createLabels(data: List<PlaceInfo>, location: Location) {

        libraryLabels.forEach { (_, label) -> label.remove() }
        libraryPolygons.forEach { (_, polygon) -> polygon.remove() }
        libraryLabels.clear()
        libraryPolygons.clear()
        libraryStates.clear()

        data.forEach { library ->
            val position = LatLng.from(library.lat.toDouble(), library.lng.toDouble())
            val distance = calculateDistance(
                location.latitude, location.longitude,
                position.latitude, position.longitude
            )

            val isInRange = distance <= INTERACTION_DISTANCE
            libraryStates[library.id] = isInRange

            val styles = kakaoMap.labelManager!!.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(
                        if (isInRange) R.drawable.ic_active_marker
                        else R.drawable.ic_inactive_marker
                    ).setTextStyles(24, Color.BLACK, 2, Color.WHITE)
                )
            )

            val options = LabelOptions.from(position)
                .setStyles(styles).setTexts(LabelTextBuilder().setTexts(library.place))

            val layer = kakaoMap.labelManager!!.lodLayer
            val newLabel = layer!!.addLodLabel(options)
            libraryLabels[library.id] = newLabel

            // 폴리곤 스타일 변경(반경 100m)
            val circleOptions: PolygonOptions =
                PolygonOptions.from(DotPoints.fromCircle(position, 100f))
                    .setStylesSet(
                        if (isInRange) PolygonStylesSet.from(PolygonStyles.from(Color.parseColor("#99078c03")))
                        else PolygonStylesSet.from(PolygonStyles.from(Color.parseColor("#99E2E2E2")))
                    )
            val newPolygon = shapeManager.getLayer().addPolygon(circleOptions)
            libraryPolygons[library.id] = newPolygon
        }
    }


    // 도서관 라벨 및 폴리곤이 도서관과 현재 위치의 거리에 따라 스타일 업데이트
    private fun updateLabelStyle(data: List<PlaceInfo>, location: Location) {
        data.forEach { library ->
            val position = LatLng.from(library.lat.toDouble(), library.lng.toDouble())
            val distance = calculateDistance(
                location.latitude, location.longitude,
                position.latitude, position.longitude
            )

            val isInRange = distance <= INTERACTION_DISTANCE
            val prevState = libraryStates[library.id] ?: false

            if (prevState != isInRange) {
                libraryStates[library.id] = isInRange
                libraryLabels[library.id]?.remove()
                libraryPolygons[library.id]?.remove()
            }

            libraryLabels[library.id]?.let { prevLabel ->
                prevLabel.remove()

                val styles = kakaoMap.labelManager!!.addLabelStyles(
                    LabelStyles.from(
                        LabelStyle.from(
                            if (isInRange) R.drawable.ic_active_marker
                            else R.drawable.ic_inactive_marker
                        ).setTextStyles(24, Color.BLACK, 2, Color.WHITE)
                    )
                )
                val options = LabelOptions.from(position)
                    .setStyles(styles).setTexts(LabelTextBuilder().setTexts(library.place))

                val layer = kakaoMap.labelManager!!.lodLayer
                val newLabel = layer!!.addLodLabel(options)
                libraryLabels[library.id] = newLabel


                libraryPolygons[library.id]?.let { prevPolygon ->
                    prevPolygon.remove()

                    // 폴리곤 스타일 변경(반경 100m)
                    val circleOptions: PolygonOptions =
                        PolygonOptions.from(DotPoints.fromCircle(position, 100f))
                            .setStylesSet(
                                if (isInRange) PolygonStylesSet.from(
                                    PolygonStyles.from(
                                        Color.parseColor(
                                            "#99078c03"
                                        )
                                    )
                                )
                                else PolygonStylesSet.from(PolygonStyles.from(Color.parseColor("#99E2E2E2")))
                            )
                    val newPolygon = shapeManager.getLayer().addPolygon(circleOptions)
                    libraryPolygons[library.id] = newPolygon

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
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    // 현재 사용자 위치 라벨을 만드는 함수
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
//                trackingManager.startTracking(currentLocationLabel)
                trackingManager.setTrackingRotation(true)
            }
        } else {
            currentLocationLabel?.moveTo(currentLocation)
        }
    }

    // 지도 Lodlabel(마커들) 생성
    private fun updateLabels(data: List<PlaceInfo>, currentLocation: Location) {
        data.forEach { library ->
            val position = LatLng.from(library.lat.toDouble(), library.lng.toDouble())
            val distance = calculateDistance(
                currentLocation.latitude, currentLocation.longitude,
                position.latitude, position.longitude
            )

            libraryLabels[library.id]?.remove()

            val styles = kakaoMap.labelManager!!.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(
                        if (distance <= INTERACTION_DISTANCE) {
                            R.drawable.ic_active_marker
                        } else {
                            R.drawable.ic_inactive_marker
                        }
                    ).setTextStyles(24, Color.BLACK, 2, Color.WHITE)
                )
            )

            val options = LabelOptions.from(position)
                .setStyles(styles).setTexts(LabelTextBuilder().setTexts(library.place))

            val layer = kakaoMap.labelManager!!.lodLayer
            val newLabel = layer!!.addLodLabel(options)
            libraryLabels[library.id] = newLabel

            // 폴리곤 스타일 변경(반경 100m)
            libraryPolygons[library.id]?.remove()
            val circleOptions: PolygonOptions =
                PolygonOptions.from(DotPoints.fromCircle(position, 100f))
                    .setStylesSet(
                        PolygonStylesSet.from(PolygonStyles.from(Color.parseColor("#99E2E2E2")))
                    )
            val polygon = shapeManager.getLayer().addPolygon(circleOptions)
            libraryPolygons[library.id] = polygon
        }
    }

    // label 눌렀을 때 보여줄 bottom sheet와 리사이클러뷰어댑터 설정
    private fun showLibraryInfo(lib: PlaceInfo) {
        val bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.visibility = View.VISIBLE

        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            isFitToContents = true
        }

        mapAdapter.submitList(listOf(lib))
    }

    // 사용자에게 보여줄 메세지 텍스트 뷰
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

    /**
     * 현재 위치와 클릭한 도서관의 라벨까지 얼만큼 거리가 차이나는지를 리턴함
     * */
    private fun calculateDistance(
        currentLat: Double, currentLng: Double,
        clickedLat: Double, clickedLng: Double
    ): Float {
        val result = FloatArray(1)
        Location.distanceBetween(currentLat, currentLng, clickedLat, clickedLng, result)
        return result[0]
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()

        if (::kakaoMap.isInitialized && !isFirstLocationUpdate) {
            lastLocation?.let { location ->
                updateCurrentLocationLabel(location)
//                isFirstLocationUpdate = true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        Log.d("map onPause", "onPause")
    }
}


