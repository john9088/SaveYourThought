package com.example.gridlayouttut

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pop_up.view.*


class MainActivity : AppCompatActivity(){
    var userList = mutableMapOf<Int,String>()

    private lateinit var rs: Cursor
    private lateinit var db:SQLiteDatabase
    private lateinit var helper:ModelThoughts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helper = ModelThoughts(applicationContext)
        db = helper.readableDatabase

        rv_main.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        //addDataInUserList()
        populateRecyclerView()

        img_main.setOnClickListener{

            val dialogue = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.pop_up, null)

            dialogue.setView(view)

            val alertBox = dialogue.create()
            alertBox.show()

            val buttonPopup = view.findViewById<Button>(R.id.buttonCancel)
            val buttonAllow = view.findViewById<Button>(R.id.buttonSubmit)

            buttonPopup.setOnClickListener{
                alertBox.dismiss()
            }

            buttonAllow.setOnClickListener{
                val title = view.edt_comment?.text.toString()

                if(title ==  "null" || title.isEmpty())
                    Toast.makeText(this, "Enter Title", Toast.LENGTH_LONG).show()
                else{
                    helper.insertData(db,"THOUGHTS","THOUGHTS_NAME",title)
                    populateRecyclerView()
                    alertBox.dismiss()
                }
            }
        }
    }

    private fun addDataInUserList(){
        rs = db.rawQuery("SELECT * FROM THOUGHTS", null)
        while (rs.moveToNext()){
            if(!userList.contains(rs.getString(1)))
                userList[rs.getInt(0)] = rs.getString(1)
        }
    }

    //To populate RecyclerView
    private fun populateRecyclerView(){
        addDataInUserList()
        val mAdapter = MainAdapter(userList)
        rv_main.adapter = mAdapter

        mAdapter.setOnItemClickListner(object : MainAdapter.OnItemClickListner {
            override fun onItemClick(position: Int) {
                val intent =  Intent(this@MainActivity,UserList::class.java)
                intent.putExtra("EXTRA_SESSION_ID", position+1);
                startActivity(intent)
            }
            override fun deleteItem(position: Int) {
                val dbKeys = userList.keys

                val key = dbKeys.elementAt(position)
                userList.remove(key)

                if (helper.deleteData(db,"THOUGHTS","THOUGHT_ID",key))
                    Log.d("SUCCESS:","Delete Successfull")
                else
                    Log.d("Error:","Delete Unsuccessfull")
                mAdapter.notifyItemRemoved(position)
            }
        })
    }
}

