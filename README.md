Contact App

Overview

The Contact App is an Android application designed to manage and display contacts in a user-friendly interface. The app utilizes modern Android development techniques, with a focus on:

RecyclerView for displaying a list of contacts.

Content Resolver for accessing device contacts.

Custom Dialog Box for adding and editing contacts.

Slide-in Animation for a smooth user experience.

Glide library for efficiently loading contact photos.

Features

1. RecyclerView for Contact List

Displays contacts in a clean and organized list.

Includes a custom layout for each contact item, showing:

Name

Phone number

Profile photo (circular image).

2. Content Resolver

Fetches contacts directly from the device’s contacts storage.

Ensures the app displays real-time data from the user's device.

3. Custom Dialog Box

Provides a custom-styled dialog for:

Adding new contacts.

Editing existing contacts.

Validates user input for name and phone number fields.

4. Slide-in Animation

Implements smooth entry animations for contact items in the RecyclerView.

Enhances the overall user experience with fluid transitions.

5. Glide for Image Loading

Uses the Glide library to load and display contact photos efficiently.

Applies a circular crop transformation for profile images.

Displays a placeholder image for contacts without photos.

Technical Implementation

RecyclerView

Adapter Pattern: The app uses a custom adapter to bind data to the RecyclerView.

ViewHolder: Each contact item is represented by a ViewHolder for efficient view recycling.

Content Resolver

The ContentResolver API is used to query the device’s contacts database.

Permissions: Requires runtime permission for reading contacts (READ_CONTACTS).

Custom Dialog

Layout: The dialog box uses a custom XML layout with input fields for name and phone number.

Validation: Ensures no empty fields are allowed before saving the contact.

Callback: The dialog uses a callback mechanism to return user input to the main activity.

Animations

Slide-in Animation: Each contact item uses slide_in_left animation from the Android framework to enter the list.

Implementation: Applied within the onBindViewHolder method of the RecyclerView adapter.

Glide

Loading Images: Glide is used to load contact images from URIs.

Circular Crop: Applied using CircleCrop transformation.

Placeholder: Displays a default placeholder image if no photo is available.

Screenshots

Contact List

Displays a list of contacts with names, phone numbers, and profile photos.

Custom Dialog

Example of the dialog for adding/editing a contact.

Permissions

The app requires the following permissions:

READ_CONTACTS: To fetch the device’s contacts.

INTERNET: For Glide to fetch images if needed.
