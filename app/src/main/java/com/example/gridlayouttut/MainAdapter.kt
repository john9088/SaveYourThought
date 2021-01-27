package com.example.gridlayouttut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cards.view.*

class MainAdapter(private val userList: MutableList<String>): RecyclerView.Adapter<MainAdapter.CustomViewHolder>() {
    public var mListner:OnItemClickListner? = null

    public interface OnItemClickListner{
        fun onItemClick()
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
        val title =  userList[position]
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
                            mListner.onItemClick()
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

