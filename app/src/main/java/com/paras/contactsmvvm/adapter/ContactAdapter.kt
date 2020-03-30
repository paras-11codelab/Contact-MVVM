package com.paras.contactsmvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paras.contactsmvvm.R
import com.paras.contactsmvvm.models.Contact
import kotlinx.android.synthetic.main.contact_item_view.view.*

class ContactAdapter(private var mContacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return mContacts.size
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.setData()
    }

    /**
     * update the recyclerView's data and the notify the recyclerView to change according to the data
     */
    fun updateData(list: List<Contact>) {
        mContacts = list
        notifyDataSetChanged()
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData() {
            itemView.tv_contact_person_name.text = mContacts[adapterPosition].contactName
            itemView.tv_contact_person_mobile_number.text = mContacts[adapterPosition].contactNumber
        }

    }

}