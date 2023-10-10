package com.example.cgt_assignment_tsp

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
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
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SolutionViewActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST){
        }
        setContentView(R.layout.activity_solution_view)

        //setting up the map viewmapView = findViewById(R.id.mapView2)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        val back:Button=findViewById(R.id.backSolution)
        //places array value is received from the previous activity
        val placesArr = intent.getParcelableArrayListExtra<Place>("placesArray")
        //no of places value is received from the previous activity
        val noOfPlaces=intent.getStringExtra("places")

        //functionality of the back button
        back.setOnClickListener {
            val intent= Intent(this,ViewActivity::class.java)
            //places array is passed backed
            intent.putParcelableArrayListExtra("placesArray",placesArr as java.util.ArrayList<out Parcelable?>)
            //no of places value is passed backed
            intent.putExtra("places",noOfPlaces)
            startActivity(intent)
        }
        Toast.makeText(this," Click on each Marker to know the travel order", Toast.LENGTH_SHORT).show()
        Toast.makeText(this," This is the Solution of TSP", Toast.LENGTH_SHORT).show()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val start= intent.getStringExtra("start")?.toInt()
        Log.d("starting point", start.toString())

        data class Point(val x: Double, val y: Double)
        //function to calculate path length - distance between two points when latitude and longitude of two points are given
        fun calculateLength(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {
            val R = 6371.0
            val lat1Rad = Math.toRadians(lat1)
            val lon1Rad = Math.toRadians(lon1)
            val lat2Rad = Math.toRadians(lat2)
            val lon2Rad = Math.toRadians(lon2)

            val dlat = lat2Rad - lat1Rad
            val dlon = lon2Rad - lon1Rad

            //haversine formula
            val a = sin(dlat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dlon / 2).pow(2)
            val c = 2 * Math.asin(Math.sqrt(a))

            return R * c
        }

        //algorithm that gives the solution to the TSP
        fun nearestNeighborAlgorithm(points: List<Point>): List<Int> {
            val numPoints = points.size
            val unvisited = (1..numPoints).toMutableSet()
            val tour = mutableListOf<Int>()
            var currentPoint = start

            while (unvisited.isNotEmpty()) {
                unvisited.remove(currentPoint)
                if (unvisited.isEmpty()) {
                    break
                }
                val nearestNeighbor = unvisited.minByOrNull { x ->
                    calculateLength(points[currentPoint?.minus(1)!!].x,points[currentPoint?.minus(1)!!].y, points[x - 1].x,points[x - 1].y)
                }!!
                tour.add(currentPoint!!)
                currentPoint = nearestNeighbor
            }
            tour.add(currentPoint!!)
            return tour
        }

        //function to calculate the total tour length
        fun calculateTourLength(tour: List<Int>, points: List<Point>): Double {
            var totalDistance = 0.0
            val numPoints = points.size

            for (i in 0 until tour.size) {
                val currentVertex = tour[i]
                val nextVertex = tour[(i + 1) % numPoints]
                totalDistance += calculateLength(points[currentVertex - 1].x,points[currentVertex - 1].y,points[nextVertex - 1].x,points[nextVertex - 1].y)
            }
            return totalDistance
        }

        val placesArray = intent.getParcelableArrayListExtra<Place>("placesArray")
        var points= arrayListOf<Point>()
        for (i in placesArray?.indices!!){
            points.add(Point(placesArray[i].lat,placesArray[i].long))
        }
        val tour = nearestNeighborAlgorithm(points)
        val tourLength = calculateTourLength(tour, points)

        Log.d("Tour (in order of traversal)","$tour")
        Log.d("Tour length","$tourLength")
        Toast.makeText(this,"Tour length : $tourLength kms",Toast.LENGTH_SHORT).show()

        //LatLng is a predefined dataclass under GoogleMap
        var locations = arrayListOf<LatLng>()
        for (i in tour){
            //adding the points in the traversal order into the locations array
            locations.add(LatLng(points[i-1].x,points[i-1].y))
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 5f))

        //adding markers to the selected places - titled according to the order of visits of the places
        for (i in locations.indices) {
            googleMap.addMarker(MarkerOptions().position(locations[i]).title("Place ${i+1}"))
        }

        //joining the markers and forming the shortest possible path - solution of TSP
        val lines = PolylineOptions()
        lines.color(Color.BLUE)
        for (i in 0 until locations.size) {
            lines.add(locations[i])
        }
        googleMap.addPolyline(lines)

    }
}


