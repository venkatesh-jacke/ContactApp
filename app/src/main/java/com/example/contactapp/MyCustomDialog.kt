package com.example.contactapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MyCustomDialog(
    context: Context,
    val title:String,
    val name:String="",
    val phone:String="",
    private val onAddContact: (String, String) -> Unit
) : Dialog(context) {

    lateinit var nameEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the custom layout
        val view = LayoutInflater.from(context).inflate(R.layout.add_contact_layout, null)
        setContentView(view)

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        // Initialize UI elements
        tvTitle = view.findViewById(R.id.dialogTitle)
        nameEditText = view.findViewById(R.id.nameEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        val doneButton: Button = view.findViewById(R.id.doneButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        tvTitle.text=title
        nameEditText.setText(name)
        phoneEditText.setText(phone)

        // Handle Done button click
        doneButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                onAddContact(name, phone) // Trigger callback with input data
                dismiss() // Close the dialog
            } else {
                // Show an error or toast for empty fields
                nameEditText.error = if (name.isEmpty()) "Name cannot be empty" else null
                phoneEditText.error = if (phone.isEmpty()) "Phone number cannot be empty" else null
            }
        }

        // Handle Cancel button click
        cancelButton.setOnClickListener {
            dismiss() // Close the dialog without any action
        }
    }


}
