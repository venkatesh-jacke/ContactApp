package com.example.contactapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private var contactList: ArrayList<Contact> = ArrayList() // ArrayList to hold Contact objects
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchContactsInBackground() // Fetch contacts in the background
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access contacts.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        checkAndRequestPermission()
    }

    private fun init() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        fab = findViewById(R.id.fab)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactAdapter = ContactAdapter(this, contactList) // Pass the contactList to the adapter
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
            contactAdapter.notifyDataSetChanged()
        }
        dialog.show()
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchContactsInBackground()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                Toast.makeText(this, "Contacts permission is required.", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }


    private fun fetchContactsInBackground() {
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = getContacts()
            withContext(Dispatchers.Main) {
                contactList.clear()  // Clear existing data
                contactList.addAll(contacts)  // Add the new contacts
                contactAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
                progressBar.visibility =
                    ProgressBar.GONE  // Hide the progress bar once data is loaded
            }
        }
    }


    @SuppressLint("Range")
    private fun getContacts(): List<Contact> {
        val resolver: ContentResolver = contentResolver
        val contacts = ArrayList<Contact>()
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        ?: "Unknown"
                val phoneNumbers = getPhoneNumbers(resolver, id)
                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                val contactPhotoUri = if (photoUri.isNullOrEmpty()) {
                    Uri.parse("android.resource://${packageName}/drawable/default_contact_photo")
                } else {
                    Uri.parse(photoUri)
                }

                // Only include contacts with phone numbers
                if (phoneNumbers.isNotEmpty()) {
                    // Create a Contact object and add it to the list
                    contacts.add(Contact(name, phoneNumbers[0], contactPhotoUri.toString()))
                }
            }
        }
        return contacts
    }

    @SuppressLint("Range")
    private fun getPhoneNumbers(resolver: ContentResolver, contactId: String): List<String> {
        val phoneNumbers = ArrayList<String>()
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}=?",
            arrayOf(contactId),
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val phoneNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?: ""
                if (phoneNumber.isNotEmpty()) {
                    phoneNumbers.add(phoneNumber)
                }
            }
        }
        return phoneNumbers
    }

}

