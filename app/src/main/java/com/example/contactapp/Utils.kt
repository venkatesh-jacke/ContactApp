package com.example.contactapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object Utils {

    fun getContactsFromAssets(context: Context, fileName: String): ArrayList<Contact> {
        val contacts = ArrayList<Contact>()
        try {
            // Open the file from the assets folder
            val inputStream = context.assets.open(fileName)

            // Read the content as a string
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Deserialize JSON to a list of Contact objects
            val type = object : TypeToken<List<Contact>>() {}.type
            val contactList: List<Contact> = Gson().fromJson(jsonString, type)

            contacts.addAll(contactList) // Add all contacts to the ArrayList
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return contacts
    }

    fun saveContactsToFile(context: Context, fileName: String,jsonContacts: String) {
        try {
            // Define the file location in internal storage
            val file = File(context.filesDir, fileName)

            // Write the JSON string to the file
            file.writeText(jsonContacts)

            // Show a confirmation message on the UI thread
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Contacts saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the error on the UI thread
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Error saving contacts", Toast.LENGTH_SHORT).show()
            }
        }
    }

}