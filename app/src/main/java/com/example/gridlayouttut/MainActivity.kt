package com.example.gridlayouttut

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pop_up.*
import kotlinx.android.synthetic.main.pop_up.view.*


class MainActivity : AppCompatActivity() {
    var userList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_main.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        img_main.setOnClickListener{
            val dialogue = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.pop_up, null)

            dialogue.setView(view)

            val alertBox = dialogue.create()
            alertBox.show()

            val buttonPopup = view.findViewById<Button>(R.id.buttonCancel)
            val buttonAllow = view.findViewById<Button>(R.id.buttonSubmit)

            // Set a click listener for popup's button widget
            buttonPopup.setOnClickListener{
                // Dismiss the popup window
                alertBox.dismiss()
            }

            buttonAllow.setOnClickListener{
                val title = view.edt_comment?.text.toString()
                Toast.makeText(this,"Enter Title is $title",Toast.LENGTH_SHORT).show()
                if(title ==  "null")
                    Toast.makeText(this,"Enter Title",Toast.LENGTH_SHORT).show()
                else{
                    userList.add(title)
                    rv_main.adapter = MainAdapter(this,userList)
                    alertBox.dismiss()
                }
            }
        }
    }
}

