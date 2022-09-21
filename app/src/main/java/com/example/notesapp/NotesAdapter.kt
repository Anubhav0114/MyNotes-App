package com.example.notesapp

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.entities.Notes
import kotlin.math.log


private const val TAG  = "NotesAdapter"
class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listener : OnItemClickListener ? = null
    var arrList  =  ArrayList<Notes>()

    fun setData( arrNotesList  : List<Notes> ){
        arrList = arrNotesList as ArrayList<Notes>
    }

    fun setOnClickListener(listener1: OnItemClickListener){

        listener = listener1
    }

    inner class NotesViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvDesc  = itemView.findViewById<TextView>(R.id.tv_desc)
        val tvDateTime = itemView.findViewById<TextView>(R.id.tv_DateTime)!!
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val imgNote = itemView.findViewById<ImageView>(R.id.imgNote)
        val webUrl = itemView.findViewById<TextView>(R.id.tv_weblink)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_items_notes, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.tvTitle.text = arrList[position].title
        holder.tvDesc.text = arrList[position].noteText
        holder.tvDateTime.text = arrList[position].dateTime


        if (arrList[position].color != null){
            Log.i(TAG , "the color is " + arrList[position].color.toString())
            var colour : String ?  = arrList[position].color
            Log.i(TAG , "the colour is in variable $colour")
            holder.cardView.setBackgroundColor((Color.parseColor(colour)))

            Log.i(TAG , "Setting the background with $colour")

        }
        else{
            holder.cardView.setBackgroundColor((Color.parseColor("#FF202734")))
        }
        if(arrList[position].imgPath !=null){
            holder.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgPath))
            holder.imgNote.visibility = View.VISIBLE
        }
        else{
            holder.imgNote.visibility = View.GONE
        }
        if(arrList[position].webLink !=null){
            holder.webUrl.text = arrList[position].webLink
            holder.webUrl.visibility = View.VISIBLE
        }
        else{
            holder.imgNote.visibility = View.GONE
        }

        holder.cardView.setOnClickListener{
            listener?.onItemClick(arrList[position].id!!)
            Log.i(TAG , "Adapter onClickListener")
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

   interface OnItemClickListener{
        fun onItemClick(notesId : Int)
   }

}