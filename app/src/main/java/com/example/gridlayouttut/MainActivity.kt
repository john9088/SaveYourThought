package com.example.gridlayouttut

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pop_up.view.*


class MainActivity : AppCompatActivity(){
    var userList = mutableListOf<String>()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Thoughts")
    private val thoughtID = dbRef.push().key
    val thoughts = thoughtID?.let { it1 -> ModelThoughts(it1,userList) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword("jasonbritto8085@gmail.com","helloworld12")
            .addOnCompleteListener{
            if(!it.isSuccessful) return@addOnCompleteListener
            else
                Log.d("MAIN","Successfully Created user with ID:${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("MAIN","Failed to create User:${it.message}")
            }

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
                    userList.add(title)
                    thoughts?.title = userList

                    if (thoughtID != null) {
                        dbRef.child(thoughtID).setValue(thoughts).addOnCanceledListener {
                            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
                        }
                    }

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

