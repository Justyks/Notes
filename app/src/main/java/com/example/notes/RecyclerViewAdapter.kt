package com.example.notes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import android.widget.TextView
import com.example.notes.MainActivity
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(public val notes: MutableList<String>,context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(){

    private val db = NotesDBHelper(context, null)
    var notesFilterList = mutableListOf<String>()
    init {
        notesFilterList = notes
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val delButton: Button = itemView.findViewById(R.id.buttonDel)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notesFilterList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.largeTextView.text = notesFilterList[position]
        holder.delButton.setOnClickListener{
            db.deleteNote(holder.largeTextView.text.toString())
            notes.removeAt(notesFilterList.indexOf(holder.largeTextView.text))
            notifyItemRemoved(notesFilterList.indexOf(holder.largeTextView.text))
        }
        holder.editButton.setOnClickListener{
            MainActivity.editFlag = true
            MainActivity.position = notesFilterList.indexOf(holder.largeTextView.text)

        }

    }

    fun addNote(note: String){
        notes.add(note)
        notifyItemInserted(notes.size-1)
    }

    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    notesFilterList = notes
                } else {
                    val resultList = ArrayList<String>()
                    for (row in notes) {
                        if (row.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    notesFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = notesFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notesFilterList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }

        }
    }
}