package com.example.mesagingapp.adapter

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mesagingapp.R
import com.example.mesagingapp.data.MessageData
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerAdapter :RecyclerView.Adapter<ChatRecyclerAdapter.ChatHolder>() {
    private val SENT_MESSAGE=1
    private val RECEIVED_MESSAGE=2
    class ChatHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
    private val diffUtil=object :DiffUtil.ItemCallback<MessageData>(){
        override fun areItemsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem==newItem
            }

    }
    private val recyclerListDiffer=AsyncListDiffer(this,diffUtil)

    var chats:List<MessageData>
        get() =recyclerListDiffer.currentList
            set(value)=recyclerListDiffer.submitList(value)


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        val chat=chats.get(position)

        if(chat.sender==FirebaseAuth.getInstance().currentUser!!.email.toString()){
            return SENT_MESSAGE
        }else {
            return RECEIVED_MESSAGE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        if(viewType==RECEIVED_MESSAGE){
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left,parent,false)
            return ChatHolder(view)
        }else{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right,parent,false)
            return ChatHolder(view)
        }


    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
         val textView=holder.itemView.findViewById<TextView>(R.id.txt_msg)
        textView.text=recyclerListDiffer.currentList.get(position).message
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}