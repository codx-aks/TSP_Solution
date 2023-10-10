package com.example.cgt_assignment_tsp
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class GraphActivity : AppCompatActivity() {

    private lateinit var graphContainer: FrameLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        graphContainer = findViewById(R.id.graphContainer)
        Toast.makeText(this," The Complete Graph that represents the Different Paths ", Toast.LENGTH_SHORT).show()
        Toast.makeText(this," Each edge's width is directly proportional to path length", Toast.LENGTH_SHORT).show()
        Toast.makeText(this," Weight of edges is equal to path length", Toast.LENGTH_SHORT).show()
        //places array value is received from the previous activity
        val placesArray = intent.getParcelableArrayListExtra<Place>("placesArray")
        Log.d("list of graph vertices","$placesArray")
        //no of places value is received from the previous activity
        val noOfPlaces=intent.getStringExtra("places")
            Log.d("vertices","$noOfPlaces")
            val numVertices= noOfPlaces?.toInt()
            if (numVertices != null && numVertices >= 2) {
                graphContainer.removeAllViews()
                val drawView = DrawCompleteGraphView(this, numVertices)
                graphContainer.addView(drawView)
            }

        val back:Button=findViewById(R.id.backGraph)
        //functionality of the back button
        back.setOnClickListener {
            val intent= Intent(this,ViewActivity::class.java)
            //places array is passed backed
            intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
            //no of places value is passed backed
            intent.putExtra("places",noOfPlaces)
            startActivity(intent)
        }
        }

    //android canvas class where the graph is drawn
    private inner class DrawCompleteGraphView(
        context: AppCompatActivity,
        private val numVertices: Int
    ) : View(context) {
        private val latitudes = mutableListOf<Double>()
        private val longitudes = mutableListOf<Double>()
        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            val placesArray = intent.getParcelableArrayListExtra<Place>("placesArray")
            for (i in placesArray?.indices!!){
                latitudes.add(placesArray[i].lat)
                longitudes.add(placesArray[i].long)
            }
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = minOf(centerX, centerY) - 20
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.RED
            paint.textSize = 60f
            val angle = (2 * Math.PI / numVertices).toFloat()
            for (i in 0 until numVertices) {
                val x = centerX + (radius * cos(i * angle))
                val y = centerY + (radius * sin(i * angle))
                val paintCircle = Paint()
                paintCircle.style = Paint.Style.FILL_AND_STROKE
                paintCircle.color = Color.BLUE

                //drawing the point/circle that denotes each vertex
                canvas?.drawCircle(x, y, 20f, paintCircle)
                val label = (i + 1).toString()
                val textWidth = paint.measureText(label)

                //writing down the place number
                canvas?.drawText(label, x - textWidth / 2 , y + 90, paint)

                for (j in i + 1 until numVertices) {
                    val x2 = centerX + (radius * cos(j * angle))
                    val y2 = centerY + (radius * sin(j * angle))

                    val length = calculateEdgeLength(latitudes[i], longitudes[i], latitudes[j], longitudes[j])
                    val edgePaint = Paint()
                    edgePaint.style = Paint.Style.STROKE
                    edgePaint.color = Color.BLUE
                    edgePaint.alpha= 60

                    //making the width of the edges proportional to the path length
                    edgePaint.strokeWidth = length.toFloat() / 50

                    //drawing the edges that connect every pair of vertices
                    canvas?.drawLine(x, y, x2, y2, edgePaint)

                    val paintLength = Paint()
                    paintLength.style = Paint.Style.FILL
                    paintLength.color = Color.BLACK
                    paintLength.alpha= 100
                    paintLength.textSize = 30f
                    val distanceLabel = String.format("%.2f km", length)
                    val labelX = (x + x2) / 2
                    val labelY = (y + y2) / 2

                    //writing down the edge weights (path length) above the edges
                    canvas?.drawText(distanceLabel, labelX, labelY, paintLength)
                }
            }
        }
        //function to calculate edge length - distance between two points when latitude and longitude of two points are given
        private fun calculateEdgeLength(
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
            //Haversine Formula - to calculate distance between two places when we have its lat and long
            val a = sin(dlat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dlon / 2).pow(2)
            val c = 2 * Math.asin(Math.sqrt(a))
            return R * c
        }
    }
}
