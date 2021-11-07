package com.xclusive.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.*

class SplashScreen : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loc()
        } else { ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),
            44
        )
        }
    }

    private fun loc() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(OnCompleteListener { task: Task<Location?> ->
            val location = task.result
            if (location == null) {
                Newlocation()
                //Toast.makeText(this,"called1",Toast.LENGTH_LONG).show()

            }else{
               // Toast.makeText(this,"called2",Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("LAT",location.latitude.toString())
                    intent.putExtra("LOG",location.longitude.toString())
                    startActivity(intent)
                    finish()
                },1000)

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

//                    val States =  addresses[0].adminArea
//                    Log.e("MAIN",States)
//                    Log.e("MAIN",addresses[0].locality)
//                    Log.e("MAIN",addresses[0].countryCode)
//                    Log.e("MAIN",addresses[0].subLocality)
//


                    val locale = resources.configuration.locale.displayCountry

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun Newlocation() {
          var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates =1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationcallback, Looper.myLooper())

    }

    private val locationcallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            var lastlocation: Location? = p0?.lastLocation
        }
    }
}