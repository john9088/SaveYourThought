package com.example.gridlayouttut

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cards.view.*

class MainAdapter(private val mainView: MainActivity, private val userList: MutableList<String>): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cards = layoutInflater.inflate(R.layout.cards,parent,false)
        return CustomViewHolder(cards, mainView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val title =  userList[position]
        holder.view.apply {
            tv_topic1.text = title
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class CustomViewHolder(val view: View, private val mainView:MainActivity): RecyclerView.ViewHolder(view), View.OnClickListener {
        init{
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val intent =  Intent(mainView,UserList::class.java)
            mainView.startActivity(intent)
        }
    }
}

