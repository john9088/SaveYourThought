package com.jasonbritto.simplyinfopad

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.input_thoughts_pop_up.view.*

class UserList : AppCompatActivity() {
    var userListThoughts = mutableMapOf<Int,String>()
    private lateinit var mAdapter:UserThoughtsAdapter
    private lateinit var rs: Cursor
    private lateinit var db: SQLiteDatabase
    private var thoughtID: Int = 0
    private var thoughtTitle:String = ""
    var dbKeys = userListThoughts.keys
    lateinit var alertBox:AlertDialog
    private lateinit var helper:ModelThoughts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        thoughtID = intent.getIntExtra("EXTRA_THOUGHT_ID",0);
        thoughtTitle = intent.getStringExtra("EXTRA_THOUGHT_NAME").toString();

        helper = ModelThoughts(applicationContext)
        db = helper.readableDatabase

        rv_userList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        tv_thought_title.text = "\"" +thoughtTitle + "\""

        populateUserList()

        rv_userList_add.setOnClickListener{
            val view:View = initializeAlertBox()
            implementButtonFunctions(view,false)
        }
    }

    private fun implementButtonFunctions(view:View,isEditOn:Boolean){
        val buttonDropPopup = view.findViewById<Button>(R.id.btn_cancleThoughts)
        val buttonAllow = view.findViewById<Button>(R.id.btn_submitThoughts)
        val buttonEdit = view.findViewById<Button>(R.id.btn_editThoughts)

        if(isEditOn){
            buttonEdit.visibility = View.VISIBLE
            buttonAllow.visibility = View.GONE
        }

        else{
            buttonEdit.visibility = View.GONE
            buttonAllow.visibility = View.VISIBLE
        }

        alertBox.show()

        //To remove alert box
        buttonDropPopup.setOnClickListener{
            alertBox.dismiss()
        }

        //To Add data from alert box to recyclerview
        buttonAllow.setOnClickListener{
            var thoughts = view.et_addThoughts?.text.toString()
            if(thoughts ==  "null" || thoughts.isEmpty())
                Toast.makeText(this, "Enter your thought", Toast.LENGTH_LONG).show()
            else{
                val thoughts = arrayOf<String>(thoughts,"$thoughtID")
                helper.insertData(db,"THOUGHTS_LIST","THOUGHTS_DATA,THOUGHT_ID",thoughts)
                populateUserList()
                alertBox.dismiss()
            }
        }

        //To Edit data from alert box to recyclerview
        buttonEdit.setOnClickListener {
            val updatedthought = view.et_addThoughts?.text.toString()

            if(updatedthought ==  "null" || updatedthought.isEmpty())
                Toast.makeText(this, "Enter your thought", Toast.LENGTH_LONG).show()
            else{
                val thoughtID:Int = view.tv_thoughtID.text.toString().toInt()
                val key = dbKeys.elementAt(thoughtID)
                mAdapter.alterThoughts(updatedthought,key)
                helper.updateData(db,"THOUGHTS_LIST","THOUGHTS_DATA",updatedthought,"THOUGHTS_LIST_ID","$key")
                mAdapter.notifyItemChanged(thoughtID)
                alertBox.dismiss()
            }

        }
    }

    private fun addDataInUserListThoughts(){

        rs = db.rawQuery("SELECT * FROM THOUGHTS_LIST WHERE THOUGHT_ID = $thoughtID", null)
        while (rs.moveToNext()){
            if(!userListThoughts.contains(rs.getString(1))){
                userListThoughts[rs.getInt(0)] = rs.getString(1)
            }
        }
    }

    private fun initializeAlertBox():View{
        val view = layoutInflater.inflate(R.layout.input_thoughts_pop_up, null)
        val dialogue = AlertDialog.Builder(this@UserList)
            .setView(view)

        alertBox = dialogue.create()
        alertBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    private fun populateUserList() {
        var viewToEdit:View
        addDataInUserListThoughts()
        mAdapter = UserThoughtsAdapter(userListThoughts)
        rv_userList.adapter = mAdapter

        mAdapter.setOnItemClickListner(object : UserThoughtsAdapter.OnItemClickListner {
            override fun onItemClick(ptr: Int) {
                var thoughts: String? = userListThoughts[dbKeys.elementAt(ptr)]
                viewToEdit = initializeAlertBox()
                viewToEdit.et_addThoughts.setText(thoughts)
                implementButtonFunctions(viewToEdit, true)
                alertBox.show()
                viewToEdit.tv_thoughtID.setText("$ptr")
            }

            override fun deleteItem(position: Int) {
                val key: Int = dbKeys.elementAt(position)
                userListThoughts.remove(key)
                if (helper.deleteData(db, "THOUGHTS_LIST", "THOUGHTS_LIST_ID", key)) {
                    Log.d("USER", "Delete Successful")
                    Toast.makeText(applicationContext, "Delete Successful", Toast.LENGTH_SHORT).show()
                } else{
                    Log.d("USER", "Delete Unsuccessful")
                    Toast.makeText(applicationContext, "Delete Successful", Toast.LENGTH_SHORT).show()
                }
                mAdapter.notifyItemRemoved(position)
            }
        })
    }
}