package com.example.gridlayouttut

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
    private lateinit var mAdapter:UserThoughtsAdapter
    private lateinit var rs: Cursor
    private lateinit var db: SQLiteDatabase
    private var thoughtID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        thoughtID = intent.getIntExtra("EXTRA_THOUGHT_ID",0);

        var helper = ModelThoughts(applicationContext)
        db = helper.readableDatabase

        rv_userList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

        addDataInUserListThoughts()
        populateUserList(findViewById(R.id.img_addThoughts))

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
                var thoughts = view.et_addThoughts?.text.toString()
                println("ERROR***:$thoughts")
                if(thoughts ==  "null" || thoughts.isEmpty())
                    Toast.makeText(this, "Enter your thought", Toast.LENGTH_LONG).show()
                else{
                    thoughts = "$thoughts,$thoughtID"
                    helper.insertData(db,"THOUGHTS_LIST","THOUGHTS_DATA,THOUGHT_ID",thoughts)
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
                    val thoughtID:Int = view.tv_thoughtID.text.toString().toInt()
                    mAdapter.alterThoughts(thoughts,thoughtID)
                    mAdapter.notifyItemRemoved(thoughtID)
                    alertBox.dismiss()
                }
            }
        }
    }


    private fun addDataInUserListThoughts(){
        rs = db.rawQuery("SELECT * FROM THOUGHTS_LIST WHERE THOUGHT_ID = $thoughtID", null)
        while (rs.moveToNext()){
            if(!userListThoughts.contains(rs.getString(1)))
                userListThoughts.add(rs.getString(1))
        }
    }

    private fun populateUserList(view: View) {
        addDataInUserListThoughts()
        mAdapter = UserThoughtsAdapter(userListThoughts)
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