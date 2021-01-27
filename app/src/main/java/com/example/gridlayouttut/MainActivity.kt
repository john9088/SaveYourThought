package com.example.gridlayouttut

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pop_up.view.*


class MainActivity : AppCompatActivity(){
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

            buttonPopup.setOnClickListener{
                alertBox.dismiss()
            }

            buttonAllow.setOnClickListener{
                val title = view.edt_comment?.text.toString()

                if(title ==  "null")
                    Toast.makeText(this, "Enter Title", Toast.LENGTH_LONG).show()
                else{
                    userList.add(title)
                    populateRecyclerView()
                    alertBox.dismiss()
                }
            }
        }
    }


    //To populate RecyclerView
    private fun populateRecyclerView(){
        val mAdapter = MainAdapter(userList)
        rv_main.adapter = mAdapter

        mAdapter.setOnItemClickListner(object : MainAdapter.OnItemClickListner {
            override fun onItemClick() {
                val intent =  Intent(this@MainActivity,UserList::class.java)
                startActivity(intent)
            }
            override fun deleteItem(position: Int) {
                userList.removeAt(position)
                mAdapter.notifyItemRemoved(position)
            }
        })
    }

}

