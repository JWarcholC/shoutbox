package com.example.chat

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.chat.com.example.chat.JSON.Post

class MessageAdapter(val messages : ArrayList<Post>, private val context: Context?) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MessageViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.message_item,
                p0, false)

        val mvh = MessageViewHolder(v)
        return mvh
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, p1: Int) {
        val currentItem = messages[p1]

        holder.login!!.text = currentItem.login
        holder.date!!.text = trimDate(currentItem.date)
        holder.content!!.text = currentItem.content

        holder.itemView.setOnClickListener {
            removeItem(p1)
        }

        // TODO: put
        holder.itemView.setOnLongClickListener{
            editItem(p1)
            true
        }
    }

    private fun trimDate( oldDate : String) : String {
        var newDate : String?

        newDate = oldDate.substring(0, 10) +  "   " + oldDate.substring(11, 19)
        return newDate
    }


    private fun removeItem(pos : Int) {
        val element = messages[pos]
        messages.removeAt(pos)
        notifyItemChanged(pos)
        notifyDataSetChanged()
        MainActivity.toDeleteList.add(element.id)
    }

    private fun editItem(pos : Int) {
        //TODO: edit

    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var login : TextView? = null
        var date : TextView? = null
        var content : TextView? = null

        init {
            login = itemView.findViewById(R.id.login)
            date = itemView.findViewById(R.id.date)
            content = itemView.findViewById(R.id.content)
        }
    }

}