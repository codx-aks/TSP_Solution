package com.example.cgt_assignment_tsp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button

class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        val graph: Button =findViewById(R.id.graphViewer)
        //places array value is received from the previous activity
        val placesArray = intent.getParcelableArrayListExtra<Place>("placesArray")
        //no of places value is received from the previous activity
        val noOfPlaces=intent.getStringExtra("places")

        //functionality of view complete graph texted button - moves to GraphActivity when clicked
        graph.setOnClickListener {
            val intent= Intent(this,GraphActivity::class.java)
            //places array is passed onto the next activity
            intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
            //no of places value is passed onto the next activity
            intent.putExtra("places",noOfPlaces)
            startActivity(intent)
        }
        val solution: Button =findViewById(R.id.solution)

        //functionality of view Solution texted button - moves to SolutionActivity when clicked
        solution.setOnClickListener {
            val intent= Intent(this,SolutionActivity::class.java)
            //places array is passed onto the next activity
            intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
            //no of places value is passed onto the next activity
            intent.putExtra("places",noOfPlaces)
            startActivity(intent)
        }
    }
}