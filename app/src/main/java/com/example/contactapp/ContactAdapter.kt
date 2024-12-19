package com.example.contactapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class ContactAdapter(private val context: Context, private val contactList: MutableList<Contact>) :
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
        val contact = contactList[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phoneNumber

        // Load photo using Glide
        Glide.with(context)
            .load(contact.photoUri)
            .transform(CircleCrop())
            .placeholder(R.drawable.default_contact_photo)
            .into(holder.photoImageView)

        // Handle long click with AlertDialog
        holder.itemView.setOnLongClickListener {
            deleteContact(position)

            true // Return true to indicate the click was handled
        }

        holder.itemView.setOnClickListener {
            editContact(position)
        }

        setAnimation(holder.itemView, position)
    }


    fun setAnimation(view: View, position: Int) {
        val slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        view.animation = slideIn

    }

    fun editContact(position: Int) {
        val editDialog = MyCustomDialog(
            context,
            "Edit Contact",
            contactList[position].name,
            contactList[position].phoneNumber
        ) { name, phone ->
            contactList[position].name = name
            contactList[position].phoneNumber = phone
            notifyItemChanged(position)
        }

        editDialog.show()
    }


    fun deleteContact(position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Remove Contact")
            .setMessage("Are you sure you want to remove this contact?")
            .setPositiveButton("Yes") { dialog, _ ->
                contactList.removeAt(position) // Remove the item
                notifyItemRemoved(position)    // Notify the adapter
                notifyItemRangeChanged(position, contactList.size) // Update the positions
                dialog.dismiss() // Close the dialog
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Close the dialog without removing
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}