package com.example.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.chat.com.example.chat.JSON.Post

class MessageAdapter(val messages : ArrayList<Post> ) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MessageViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.message_item,
                p0, false)

        val mvh = MessageViewHolder(v)
        return mvh
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(p0: MessageViewHolder, p1: Int) {
        val currentItem = messages[p1]

        p0.idNumber!!.text = currentItem.id.toString()
        p0.login!!.text = currentItem.login
        p0.date!!.text = currentItem.date.toString()
        p0.content!!.text = currentItem.content
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var idNumber : TextView? = null
        var login : TextView? = null
        var date : TextView? = null
        var content : TextView? = null

        init {
            idNumber = itemView.findViewById(R.id.id)
            login = itemView.findViewById(R.id.login)
            date = itemView.findViewById(R.id.date)
            content = itemView.findViewById(R.id.content)
        }
    }


}