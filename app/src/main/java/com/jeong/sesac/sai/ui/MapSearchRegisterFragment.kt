package com.jeong.sesac.sai.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jeong.sesac.domain.model.PlaceInfo
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSearchRegisterBinding
import com.jeong.sesac.sai.recycler.map.MapAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.entity.UiState
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
import com.kakao.vectormap.label.LodLabel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class MapSearchRegisterFragment :
    BaseFragment<FragmentSearchRegisterBinding>(FragmentSearchRegisterBinding::inflate) {
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var preference: AppPreferenceManager
    private lateinit var startLocation: LatLng
    private var startZoomLevel = 15

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: KakaoMapViewModel by viewModels {
        appViewModelFactory
    }
    private lateinit var label: Label
    private var libraryLabels = mutableListOf<LodLabel>()
    private lateinit var mapAdapter: MapAdapter


    /**
     * 1. 사용가능한 네트워크임?
     *  1) true
     *    - location 권한 승인여부
     *     (1) location 권한 승인되어있음
     *     -> gps 켜져있는지 확인
     *      -> gps o -> 이제 할 일 작성
     *      -> gps x -> 켜기 요청 -> 켜짐 : 할일 작성 / 안 켜짐 : Toast(gps가 꺼져있습니다)
     *
     *      (2) lcoation 권한 승인 안 되어있음
     *      - 승인요청
     *       (1) 승인함
     *       -> gps 켜져있는지 확인
     *         -> gps o -> 이제 할 일 작성
     *         -> gps x -> 켜기 요청 -> 켜짐 : 할일 작성 / 안 켜짐 : Toast(gps가 꺼져있습니다)
     *       (2) 승인 안 함
     *         -> 한번 더 권한 요청(승인 안 하면 서비스 이용 못함)
     *          -> 승인 x -> 토스트(서비스 이용 못함)
     *          -> 승인 ㅇ ->
     *            -> gps 켜져있는지 확인
     *             -> gps o -> 이제 할 일 작성
     *             -> gps x -> 켜기 요청 -> 켜짐 : 할일 작성 / 안 켜짐 : Toast(gps가 꺼져있습니다)
     *  2) false
     *    Toast(네트워크 환경을 체크해주세요)
     * */

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

        // preference에 있는 값(위도, 경도)로 초기화(처음에는 서울시청)
        startLocation = LatLng.from(
            preference.lastLat,
            preference.lastLng
        )

        mapView = binding.mapView
        // 지도 시작
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(err: Exception?) {
            }
        }, readyCallback)
        return view
    }

    // mapView.start할 때 들어갈 콜백
    private val readyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(map: KakaoMap) {
            kakaoMap = map

            binding.progressBar.progressCircular.visibility = View.VISIBLE
            kakaoMap.setOnLodLabelClickListener { _, _, label ->
                // 클릭된 라벨의 위치
                val clickedPosition = label.position
                // 클릭된 라벨의 도서관 이름(setText로 라벨에 등록한 도서관이름)
                val libraryName = label.texts[0]


                // 해당 라벨로 이동(줌레벨 16)
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(clickedPosition, 16)
                kakaoMap.moveCamera(cameraUpdate)

                viewLifecycleOwner.lifecycleScope.launch {
                    val currentState = viewModel.librariesInfoState.value

                    // viewModel.librariesInfo에서 이름 같은거만 찾아서
                    when (currentState) {
                        is UiState.Loading -> {
                            binding.progressBar.progressCircular.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.progressCircular.visibility = View.GONE
                            currentState.data.find { it.place_name == libraryName }
                                ?.let { library ->
                                    showLibraryInfo(library)
                                }
                        }

                        is UiState.Error -> {
                            binding.progressBar.progressCircular.visibility = View.GONE
                            throw Error("에러입니다")
                        }
                    }
                }
            }

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                startLocation
            )
            kakaoMap.moveCamera(cameraUpdate);

            // 카메라 이동이 끝날 때마다 실행될 이벤트에서 리턴한 위치의 경도, 위도 받아서 preference에 저장
            kakaoMap.setOnCameraMoveEndListener { _, cp, _ ->
                startLocation = cp.position
                preference.lastLat = cp.position.latitude
                preference.lastLng = cp.position.longitude
            }
        }

        override fun getPosition(): LatLng = startLocation
        override fun getZoomLevel(): Int = startZoomLevel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
            isFitToContents = true
        }

        mapAdapter = MapAdapter(
            { libraryInfo ->
                val registerAction =
                    MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentRegisterNote(
                        libraryInfo.place_name
                    )
                findNavController().navigate(registerAction)
            },

            // 추후 쪽지 찾기때 변경해야함
//            onFindCallback = { libraryInfo ->
//                val findAction = MapSearchRegisterFragmentDirections.actionFragmentSearchRegisterToFragmentSearch(libraryInfo.place_name)
//                findNavController().navigate(findAction)
//            }
        )

        with(binding) {
            rvLibraryInfo.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = this@MapSearchRegisterFragment.mapAdapter
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }

        /**
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
        binding.btnReSearchPlaces.clicks()
            .throttleFirst(throttleTime)
            .onEach {
                val currentLocation = LatLng.from(
                    preference.lastLat,
                    preference.lastLng
                )

                viewModel.getLibrariesInfo(currentLocation.longitude, currentLocation.latitude)
            }.launchIn(lifecycleScope)

        // 위에서 요청하고 여기서는 최신의 데이터를 뷰모델에서 받아서 라벨 생성
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.librariesInfoState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        with(binding) {
                            progressBar.progressCircular.visibility = View.VISIBLE
                            tvLibraryInfo.setText("gps를 켜서 도서관을 찾아보세요")
                        }
                    }

                    is UiState.Success -> {
                        with(binding) {
                            progressBar.progressCircular.visibility = View.GONE
                            tvLibraryInfo.visibility = View.VISIBLE
                        }
                        val library = state.data
                        if (library.isEmpty()) {
                            binding.tvLibraryInfo.setText("gps를 켜서 도서관을 찾아보세요!")
                        } else {
                            binding.tvLibraryInfo.visibility = View.GONE
                            mapAdapter.submitList(library)
                        }

                        label.remove()
                        libraryLabels.forEach { it.remove() }
                        libraryLabels.clear()

                        currentLocationListener()

                        library.forEach { library ->
                            val libraryPosition = LatLng.from(
                                library.y.toDouble(),
                                library.x.toDouble()
                            )
                            createLocationLabel(libraryPosition, library.place_name)
                        }
                    }

                    is UiState.Error -> {
                        with(binding) {
                            progressBar.progressCircular.visibility = View.GONE
                            tvLibraryInfo.setText("도서관 정보를 불러오는데 실패했습니다")
                        }
                    }
                }
            }
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

    override fun onStop() {
        super.onStop()
        Log.d("map onStop", "onStop")
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
                currentLocationListener()
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

    // 현재 정확한 위치 받기
    private fun currentLocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000L
        ).run {
            setWaitForAccurateLocation(true)
            setMinUpdateIntervalMillis(3000L)
            setIntervalMillis(3000L)
            setMaxUpdateDelayMillis(5000L)
                .build()
        }
        Log.d("mLocationRequest", "$locationRequest")

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                val currentLatLng = LatLng.from(location.latitude, location.longitude)

                createCurrentLocationLabel(currentLatLng)
                val updateCamera = CameraUpdateFactory.newCenterPosition(currentLatLng)
                kakaoMap.moveCamera(updateCamera)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    private fun createCurrentLocationLabel(position: LatLng) {
        val styles = kakaoMap.labelManager!!.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(
                    R.drawable.icon_marker
                )
            )
        )

        val options = LabelOptions.from(position)
            .setStyles(styles)

        val layer = kakaoMap.labelManager!!.layer
        layer!!.addLabel(options)

        label = layer!!.addLabel(options)
    }

    // Lodlabel(마커들) 생성
    private fun createLocationLabel(position: LatLng, place_name: String) {

        val styles = kakaoMap.labelManager!!.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(
                    R.drawable.icon_marker
                ).setTextStyles(20, R.color.black)
            )
        )

        val options = LabelOptions.from(position)
            .setStyles(styles).setTexts(place_name)

        val layer = kakaoMap.labelManager!!.lodLayer
        val label = layer!!.addLodLabel(options)

        libraryLabels.add(label)
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

}


