package com.example.gridlayouttut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.input_thoughts_pop_up.view.*

class UserList : AppCompatActivity() {
    var userListThoughts = mutableListOf<String>()
    lateinit var alertBox:AlertDialog
    var thoughtModify:Boolean = false
    private val mAdapter = UserThoughtsAdapter(userListThoughts)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        rv_userList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        img_addThoughts.setOnClickListener{
            val dialogue = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.input_thoughts_pop_up, null)

            val buttonDropPopup = view.findViewById<Button>(R.id.btn_cancleThoughts)
            val buttonAllow = view.findViewById<Button>(R.id.btn_submitThoughts)
            val buttonEdit = view.findViewById<Button>(R.id.btn_editThoughts)

            buttonAllow.visibility = View.VISIBLE
            buttonEdit.visibility = View.GONE

            dialogue.setView(view)

            alertBox = dialogue.create()
            alertBox.show()



            buttonDropPopup.setOnClickListener{
                alertBox.dismiss()
            }

            buttonAllow.setOnClickListener{
                val thoughts = view.et_addThoughts?.text.toString()
                println("ERROR***:$thoughts")
                if(thoughts ==  "null" || thoughts.isEmpty())
                    Toast.makeText(this, "Enter your thought", Toast.LENGTH_LONG).show()
                else{
                    userListThoughts.add(thoughts)
                    populateUserList(view)
                    alertBox.dismiss()
                }
            }
            buttonEdit.setOnClickListener {
                val thoughts = view.et_addThoughts?.text.toString()
                println("ERROR***:$thoughts")
                if(thoughts ==  "null" || thoughts.isEmpty())
                    Toast.makeText(this, "Enter your thought", Toast.LENGTH_LONG).show()
                else{
                    //println(view.tv_thoughtID.text+"***")
                    val thoughtID:Int = view.tv_thoughtID.text.toString().toInt()
                    mAdapter.alterThoughts(thoughts,thoughtID)
                    mAdapter.notifyItemRemoved(thoughtID)
                    alertBox.dismiss()
                }
            }
        }
    }

    private fun populateUserList(view: View) {

        rv_userList.adapter = mAdapter

        mAdapter.setOnItemClickListner(object : UserThoughtsAdapter.OnItemClickListner{
            override fun onItemClick(data:String,ptr:Int) {
                view.et_addThoughts.setText(data)
                view.btn_submitThoughts.visibility = View.GONE
                view.btn_editThoughts.visibility = View.VISIBLE
                alertBox.show()
                view.tv_thoughtID.setText("$ptr")
            }
        })
    }
}