package com.example.notes
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import org.xmlpull.v1.XmlPullParser
import java.text.FieldPosition


class MainActivity : AppCompatActivity() {

    private val db = NotesDBHelper(this, null)

    companion object{
        var editFlag: Boolean = false
        var position = -1
        var editText: String = ""
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val writeButton: Button = findViewById(R.id.writeButton)
        val adapter: RecyclerViewAdapter = RecyclerViewAdapter(getNotes(db),this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        val search: androidx.appcompat.widget.SearchView = findViewById(R.id.search)

        search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.getFilter().filter(newText)
                return false
            }

        })


        writeButton.setOnClickListener {
            val inputTextArea: EditText = findViewById(R.id.inputTextArea)
            val inputText = inputTextArea.text.toString();

            if(inputText.isNotEmpty()){

                if(editFlag){
                    var text = adapter.notes[position]
                    db.editNote(text,inputText)
                    adapter.notes[position]=inputText
                    adapter.notifyItemChanged(position)
                    editFlag = false
                    inputTextArea.text.clear()
                }else{
                    db.addNote(inputText)
                    adapter.addNote(inputText)
                    inputTextArea.text.clear()
                }
                val inputTextArea: EditText = findViewById(R.id.inputTextArea)
                val editButton: Button = findViewById(R.id.buttonEdit)
                editButton.setOnClickListener{
                    inputTextArea.setText(adapter.notes[position])
                }

            }
            //inputTextArea.text.clear()
            // addNote(inputText,db, id)

        }

    }


    @SuppressLint("Range")
    fun getNotes(db: NotesDBHelper):MutableList<String>{
        val cursor = db.getNotes()
        val data = mutableListOf<String>()
        if(cursor!!.moveToFirst()) {
            //var id = cursor.getString(cursor.getColumnIndex(NotesDBHelper.ID_COL)).toLong()
            data.add(cursor.getString(cursor.getColumnIndex(NotesDBHelper.TEXT_COL)))

            while (cursor.moveToNext()) {
                //id = cursor.getString(cursor.getColumnIndex(NotesDBHelper.ID_COL)).toLong()
                data.add(cursor.getString(cursor.getColumnIndex(NotesDBHelper.TEXT_COL)))
            }
        }
        return data
    }

//    fun addNote(inputText: String, db: NotesDBHelper){

//        val note: EditText = EditText(this);
//        var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//        params.addRule(RelativeLayout.LEFT_OF, counterForSaveButtons)
//        //params.addRule(RelativeLayout.ALIGN_BOTTOM,counterForDeleteButtons)
//        if (counterForNotes != 1000) {
//            params.addRule(RelativeLayout.BELOW, counterForDeleteButtons - 1)
//        }
//        note.layoutParams = params
//        note.setText(inputText)
//        note.id = counterForNotes
//
//
//        val delButton: Button = Button(this)
//        var params2: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//        if (counterForDeleteButtons != 1000) {
//            params2.addRule(RelativeLayout.BELOW, counterForDeleteButtons - 1)
//        }
//        delButton.layoutParams = params2
//        delButton.id = counterForDeleteButtons
//        delButton.setText(delButton.id.toString())//"Delete")
//
//
//        val saveButton: Button = Button(this)
//        var params3: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        //params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//        params3.addRule(RelativeLayout.LEFT_OF, counterForDeleteButtons)
//        if (counterForSaveButtons != 1000) {
//            params3.addRule(RelativeLayout.BELOW, counterForDeleteButtons - 1)
//        }
//        saveButton.layoutParams = params3
//        saveButton.id = counterForSaveButtons
//        saveButton.setText("Save")
//
//
//        val relativeLayout: RelativeLayout = findViewById(R.id.mainLayout)
//        relativeLayout.addView(note)
//        relativeLayout.addView(delButton)
//        relativeLayout.addView(saveButton)
//
//        delButton.setOnClickListener {
//            db.deleteNote(delButton.id-2000)
//            relativeLayout.removeAllViewsInLayout()
//            setContentView(R.layout.activity_main)
//            getNotes(db)
//        }
//
//        saveButton.setOnClickListener {
//
//        }

//    }
}