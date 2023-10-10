package com.example.cgt_assignment_tsp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SolutionActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solution)
        //places array value is received from the previous activity
        val placesArray = intent.getParcelableArrayListExtra<Place>("placesArray")
        val layoutManager= LinearLayoutManager(this)
        val recyclerView:RecyclerView=findViewById(R.id.recyclerView)
        //no of places value is received from the previous activity
        val noOfPlaces=intent.getStringExtra("places")

        //setting up the recycler view to display the list of places
        recyclerView.layoutManager=layoutManager
        val adapter= SolutionAdapter(placesArray as ArrayList<Place>)
        recyclerView.adapter=adapter
        val back: Button =findViewById(R.id.backView)

        //functionality of the back button
        back.setOnClickListener {
            val intent= Intent(this,ViewActivity::class.java)
            //places array is passed backed
            intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
            //no of places value is passed backed
            intent.putExtra("places",noOfPlaces)
            startActivity(intent)
        }
        Toast.makeText(this," Clicked on the Place where You wish to Start", Toast.LENGTH_SHORT).show()

        //functionality -  when click a place , the TSP is solved by taking the clicked place as the starting point
        adapter.setOnItemClickListener(listener = object: SolutionAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@SolutionActivity,"Place ${position+1} selected as initial point for TSP",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SolutionActivity,SolutionViewActivity::class.java)
                intent.putExtra("start","${position+1}")
                //places array is passed onto the next activity
                intent.putParcelableArrayListExtra("placesArray",placesArray as java.util.ArrayList<out Parcelable?>)
                //no of places value is passed onto the next activity
                intent.putExtra("places",noOfPlaces)
                startActivity(intent)
            }


    })}
}

