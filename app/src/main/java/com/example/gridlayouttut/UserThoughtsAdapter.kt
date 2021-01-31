package com.example.gridlayouttut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_thoughts.view.*

class UserThoughtsAdapter(private val userListThoughts: MutableMap<Int, String>) :RecyclerView.Adapter<UserThoughtsAdapter.UserThoughtsHolder>() {
    var dbKeys = userListThoughts.keys
    class UserThoughtsHolder(val view: View, mListner: OnItemClickListner?, userListThoughts: MutableMap<Int, String>): RecyclerView.ViewHolder(view){

        init{
            view.setOnClickListener {
                val ptr: Int = adapterPosition
                if (ptr != RecyclerView.NO_POSITION){
                    mListner?.onItemClick(ptr)
                }
            }
            view.btn_deleteThoughtsFromList.setOnClickListener{
                val ptr: Int = adapterPosition
                if (ptr != RecyclerView.NO_POSITION){
                    mListner?.deleteItem(ptr)
                }
            }
        }
    }
    var mListner:OnItemClickListner?=null

    interface OnItemClickListner{
        fun onItemClick(position: Int)
        fun deleteItem(position: Int)
    }

    fun alterThoughts(data: String,position: Int){
        userListThoughts[position] = data
    }

    fun setOnItemClickListner(listner:OnItemClickListner){
        mListner = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserThoughtsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cards = layoutInflater.inflate(R.layout.user_thoughts,parent,false)
        return UserThoughtsHolder(cards,mListner,userListThoughts)
    }

    override fun onBindViewHolder(holder: UserThoughtsHolder, position: Int) {
        var thoughts: String? =  userListThoughts[dbKeys.elementAt(position)]
        holder.view.apply {
            if(thoughts!!.length > 100)
                thoughts = thoughts?.substring(0,100)
            et_userThoughts.text = "$thoughts..."
        }

    }
    override fun getItemCount(): Int {
        return userListThoughts.size
    }
}