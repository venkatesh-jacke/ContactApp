package com.example.contactapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var fab: FloatingActionButton
    private var contactList: ArrayList<Contact> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactList=Utils.getContactsFromAssets(this,"contacts_response.json")
        contactAdapter = ContactAdapter(this, contactList)
        recyclerView.adapter = contactAdapter
        fab.setOnClickListener {
            addContact()
        }
    }

    private fun addContact() {
        val dialog = MyCustomDialog(this) { name, phone ->
            val contact = Contact(
                name,
                phone,
                "android.resource://${packageName}/drawable/default_contact_photo"
            )
            contactList.add(contact)
            val jsonContacts = Gson().toJson(contactList)
            Utils.saveContactsToFile(this, "contacts_response.json", jsonContacts)
            contactAdapter.notifyDataSetChanged()
        }
        dialog.show()
    }
}

