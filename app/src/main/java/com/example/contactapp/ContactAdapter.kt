package com.example.contactapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class ContactAdapter(private val context: Context, private val contactList: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.contactPhoto)
        val nameTextView: TextView = itemView.findViewById(R.id.contactName)
        val phoneTextView: TextView = itemView.findViewById(R.id.contactPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_contact, parent,
            false
        )
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList.get(position)
        holder.nameTextView.text=contact.name
        holder.phoneTextView.text=contact.phoneNumber
        // Load photo using Glide
        Glide.with(context)
            .load(contact.photoUri)
            .transform(CircleCrop())
            .placeholder(R.drawable.default_contact_photo) // Placeholder image
            .into(holder.photoImageView)

    }
}