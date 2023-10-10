package com.example.cgt_assignment_tsp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val next:Button=findViewById(R.id.graphViewer)
        val places:TextView=findViewById(R.id.places)

        //functionality of the next button
        next.setOnClickListener {
            val intent= Intent(this,SelectionActivity::class.java)
            //no of places value is passed onto the next activity
            intent.putExtra("places",places.text.toString())
            startActivity(intent)
        }
    }
}