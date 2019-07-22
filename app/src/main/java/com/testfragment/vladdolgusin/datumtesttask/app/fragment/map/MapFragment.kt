package com.testfragment.vladdolgusin.datumtesttask.app.fragment.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.testfragment.vladdolgusin.datumtesttask.R
import com.testfragment.vladdolgusin.datumtesttask.app.data.City
import com.testfragment.vladdolgusin.datumtesttask.app.presentor.Impl.MapPresentorImpl


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mMap: GoogleMap
private lateinit var mapView: MapView
private lateinit var cameraPosition: CameraPosition
private lateinit var locationManager: LocationManager
private lateinit var handler: Handler

class MapFragment : Fragment(), MapFragmentContract, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, LocationListener{

    private var param1: String? = null
    private var param2: String? = null
    private var mPresenter: MapPresentorImpl? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MAP", "MapFragment onCreate ${this}")
        retainInstance = true
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        handler = Handler()
        mPresenter = MapPresentorImpl()
        mPresenter?.let { it.attachView(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("MAP", "MapFragment onCreateView ${this}")
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView) as MapView

        mapView.let {
            it.onCreate(null)
            it.onResume()
            it.getMapAsync(this)
        }
        locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager

        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(activity)
        mMap = googleMap!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.addMarker(MarkerOptions().position(LatLng(47.269103, 39.865190)).title("Start"))
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            mPresenter?.loadListCities(it.latitude, it.longitude)
        }
        changeMapState(requestPermission())
    }


    @SuppressLint("MissingPermission")
    fun changeMapState(checkPermission: Boolean) {
        if (checkPermission) {
            if (checkLocation()) {
                if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude, locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).longitude)
                } else {
                    if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                        setNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude, locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).longitude)
                    } else {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this, Looper.getMainLooper())
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this, Looper.getMainLooper())
                        }
                        setDefaultLocation()
                    }
                }
            }
        } else {
            setDefaultLocation()
        }
    }

    private fun checkLocation(): Boolean {
        if(!isLocationEnabled()) {
            showAlert()
            setDefaultLocation()
        }
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this.context!!)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return if (activity!!.checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && activity!!.checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                if (activity!!.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    ||  activity!!.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                } else {
                    activity!!.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE)
                }
                false
            } else {
                true
            }
        }else{
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    changeMapState(true)
                } else {
                    changeMapState(false)
                }
                return
            }
            else -> {
                changeMapState(false)
            }
        }
    }

    fun setDefaultLocation(){
        cameraPosition = CameraPosition.Builder().target(LatLng(47.470970, 40.107919)).zoom(14f).bearing(5f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition ),
            300, null)
    }

    fun setNewLocation(lat: Double, long: Double){
        cameraPosition = CameraPosition.Builder().
            target(LatLng(lat, long))
            .zoom(14f)
            .bearing(5f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition ),
            300, null)
    }

    override fun onLocationChanged(location: Location?) {
        handler.post {
            Log.i("MAP", "LOC")
            location?.let {
                setNewLocation(it.latitude, it.longitude)
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i("MAP", "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.i("MAP", "onProviderEnabled")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MAP", "onResume")
        //changeMapState(requestPermission())
    }

    override fun onProviderDisabled(provider: String?) {
        Log.i("MAP", "onProviderDisabled")
    }

    override fun onLoadCities(cities: List<City>) {
        Log.i("MAP", "load data - ${cities.size}")
        for (city in cities) {
            mMap.addMarker(MarkerOptions().position(LatLng(city.lat, city.lon)).title(city.name))
        }
    }

    override fun onError() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.let { it.detachView() }
    }
}
