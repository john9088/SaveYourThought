package com.example.gridlayouttut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cards.view.*

class MainAdapter(private val userList: MutableMap<Int, String>): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {
    var dbKeys = userList.keys
    var mListner:OnItemClickListner? = null
    private var mainActivity:MainActivity = MainActivity()

    interface OnItemClickListner{
        fun onItemClick(position: Int)
        fun deleteItem(position: Int)
    }
    fun setOnItemClickListner(listner:OnItemClickListner?){
        mListner = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cards = layoutInflater.inflate(R.layout.cards,parent,false)
        return CustomViewHolder(cards, mListner)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val title =  userList[dbKeys.elementAt(position)]

        holder.view.apply {
            tv_topic1.text = title
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class CustomViewHolder(val view: View, private val mListner: OnItemClickListner?): RecyclerView.ViewHolder(view) {
        init{
            view.setOnClickListener(View.OnClickListener {
                if(mListner != null){
                    val ptr:Int = adapterPosition
                    if(ptr != RecyclerView.NO_POSITION){
                            mListner.onItemClick(ptr)
                    }
                }
            })
            val deleteButton = view.findViewById<Button>(R.id.btn_deleteThoughts)
            deleteButton.setOnClickListener{
                if(mListner != null){
                    val ptr:Int = adapterPosition
                    if(ptr != RecyclerView.NO_POSITION){
                        mListner.deleteItem(ptr)
                    }
                }
            }
        }
    }
}

