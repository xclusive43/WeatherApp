package com.xclusive.weather


import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View.*

import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.xclusive.weather.databinding.ActivityMainBinding

import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var imagestatus: ImageView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = getColor(R.color.lignt_black)
        textView = findViewById(R.id.cityname)
        imagestatus = findViewById(R.id.status_iamge)

        Glide.with(this).load(R.drawable.sunny).into(imagestatus)

        getweatherdata(intent.getStringExtra("LAT"), intent.getStringExtra("LOG"))


    }

    private fun getweatherdata(lat: String?, log: String?) {
        val queue = Volley.newRequestQueue(this)
        val baseurl =
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${log}&appid=d782c3641dcc9cbcb8528dfb0245b3e4"
        val jsonrequest = JsonObjectRequest(Request.Method.GET,
            baseurl, null,
            { response ->
                if (response != null) {
                    parsejsondata(response)
                    // Log.e("DATA", response.toString())
                }
            }, { error ->
                Log.e("DATA", error.toString())
            }
        )

        queue.add(jsonrequest)
    }

    @SuppressLint("SetTextI18n")
    private fun parsejsondata(response: JSONObject) {
        binding.cityname.text = response.get("name").toString()
        //binding.datetime.text = response.
        binding.currentStatus.text =
            response.getJSONArray("weather").getJSONObject(0).getString("main")
        binding.humidityval.text = response.getJSONObject("main").getString("humidity") + "%"
        var temp = response.getJSONObject("main").getString("temp")
        var tempmin = response.getJSONObject("main").getString("temp_min")
        var tempmax = response.getJSONObject("main").getString("temp_max")
        var feelslike = response.getJSONObject("main").getString("feels_like")
        var cloudy = response.getJSONObject("clouds").getString("all")

        if (cloudy.equals("0") || cloudy ==null){
            binding.cloudyval.visibility = GONE
        }
        else{
            binding.cloudyval.visibility = VISIBLE
            binding.cloudyval.text = "${cloudy}% cloudy"

        }

        binding.degree.text = "${getcelcious(temp)}\u00B0C"
        binding.maxtemp.text = "${getcelcious(tempmax)}\u00B0C"
        binding.mintemp.text = "${getcelcious(tempmin)}\u00B0C"
        binding.feellike.text = "${getcelcious(feelslike)}\u00B0C"
        binding.datetime.text ="Today, ${getCurrentDateTime()}"

    }

    private fun getcelcious(value: String): String {
        return (((value).toFloat() - 273.15).toInt()).toString()
    }

    fun getCurrentDateTime(): Int {
        return Calendar.getInstance().time.month
    }


}