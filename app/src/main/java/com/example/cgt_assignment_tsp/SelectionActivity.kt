package com.example.cgt_assignment_tsp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions

class SelectionActivity : AppCompatActivity() , OnMapReadyCallback {
        private lateinit var mapView: MapView
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST){

            }
            setContentView(R.layout.activity_selection)
            //setting up the map view
            mapView = findViewById(R.id.mapView)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this)
        }
        override fun onMapReady(googleMap: GoogleMap) {
            //no of places value is received from the previous activity
            val noOfPlaces=intent.getStringExtra("places")
            var location = LatLng(11.0168, 76.9558)
            var i=0
            var placesArray= arrayListOf<Place>()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7f))

            googleMap.setOnMapClickListener { clickedLatLng ->
                val latitude = clickedLatLng.latitude
                val longitude = clickedLatLng.longitude
                //LatLng is a predefined dataclass under GoogleMap
                location=LatLng(latitude,longitude)

                //adding marker to the clicked points
                googleMap.addMarker(MarkerOptions().position(LatLng(latitude,longitude)).title("TSP_places"))

                Log.d("lat", "$latitude")
                Log.d("lon", "$longitude")
                val next: Button =findViewById(R.id.next_act2)
                if (i== (noOfPlaces?.toInt() ?: 0) ){
                    //functionality of the next button - when all places are selected
                    next.setOnClickListener {
                        //selected place is added to the placesArray array
                        placesArray.add(Place(i,latitude,longitude))
                        Toast.makeText(this," All PLaces Selected ",Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,ViewActivity::class.java)

                        //places array is passed onto the next activity
                        intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
                        //no of places value is passed onto the next activity
                        intent.putExtra("places",noOfPlaces)
                        Log.d("array","$placesArray")
                        startActivity(intent)
                    }
                }
                else{
                    //functionality of the next button - when there are more places to be selected
                next.setOnClickListener {
                    i=i+1

                    //selected place is added to the placesArray array
                    placesArray.add(Place(i,latitude,longitude))
                    Toast.makeText(this," Place $i Selected",Toast.LENGTH_SHORT).show()
                    if (i== (noOfPlaces?.toInt() ?: 0) -1){
                        next.text="Submit"
                        i=i+1
                    }

                }}
    }
}}