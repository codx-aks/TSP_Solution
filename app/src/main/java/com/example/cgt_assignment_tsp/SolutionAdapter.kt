package com.example.cgt_assignment_tsp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//adapter that is responsible for the functionality of the recyclerview implemented
class SolutionAdapter(private val List:ArrayList<Place>): RecyclerView.Adapter<SolutionAdapter.MyViewHolder>( ){
    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolutionAdapter.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return SolutionAdapter.MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return List.size
    }
    override fun onBindViewHolder(holder: SolutionAdapter.MyViewHolder, position: Int) {
        val currentItem=List[position]
        holder.no.text= currentItem.Number.toString()
        holder.lat.text= currentItem.lat.toString()
        holder.lon.text=currentItem.long.toString()
    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val no: TextView =itemView.findViewById(R.id.no)
        val lat: TextView =itemView.findViewById(R.id.lat)
        val lon: TextView =itemView.findViewById(R.id.lon)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    }


}