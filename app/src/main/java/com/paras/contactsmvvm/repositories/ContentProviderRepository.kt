package com.paras.contactsmvvm.repositories

import android.content.Context
import android.provider.ContactsContract
import com.paras.contactsmvvm.models.Contact

class ContentProviderRepository {

    /**
     * @param context, to get contentProvider object
     *
     * this method will get the contact related information from content provider and serialize it to the list of contact model
     */
    fun getContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contactUri = ContactsContract.Contacts.CONTENT_URI
        val cursor = context.contentResolver.query(
            contactUri,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contact().apply {
                    id = cursor.getString(cursor.getColumnIndex("_ID"))
                    val dataUri = ContactsContract.Data.CONTENT_URI
                    val dataCursor = context.contentResolver.query(
                        dataUri,
                        null,
                        "${ContactsContract.Data.CONTACT_ID} = $id",
                        null,
                        null
                    )
                    if (dataCursor?.moveToFirst() == true) {
                        contactName =
                            dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                        do {
                            if (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2")) == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                                contactNumber =
                                    dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1))
                                break;
                            }
                        } while (dataCursor.moveToNext())
                    }
                    contacts.add(this)
                    dataCursor?.close()
                }
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return contacts
    }

}