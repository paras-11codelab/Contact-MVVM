package com.paras.contactsmvvm.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.paras.contactsmvvm.R
import com.paras.contactsmvvm.adapter.ContactAdapter
import com.paras.contactsmvvm.models.BaseModel
import com.paras.contactsmvvm.models.Contact
import com.paras.contactsmvvm.viewmodels.IMainActivityViewModel
import com.paras.contactsmvvm.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    /**
     * view model declaration
     */
    private lateinit var mMainActivityViewModel: IMainActivityViewModel

    /**
     * activity's lifecycle onCreate method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting view model object from ViewModelProviders
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        // Observing liveData
        mMainActivityViewModel.getContactLiveData().observe(
            this,
            Observer {
                setUI(it)
            }
        )

        // setting recyclerView adapter
        rv_contact_list.adapter = ContactAdapter(arrayListOf())
    }

    override fun onResume() {
        super.onResume()
        checkForContactPermission()
    }

    /**
     * Requesting contact permission
     */
    private fun checkForContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // fetching contacts
            mMainActivityViewModel.fetchContacts(WeakReference(this as Context))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            checkForContactPermission()
        }
    }

    /**
     * @param baseModel, observing loading, error or response
     *
     * this method will be observe data and change UI according to the change
     */
    private fun setUI(baseModel: BaseModel) {
        loader_container.visibility = View.VISIBLE.takeIf { baseModel.isLoading } ?: View.GONE
        tv_error.visibility = View.GONE
        baseModel.error?.let {
            tv_error.visibility = View.VISIBLE
            tv_error.text = baseModel.error?.localizedMessage
        }
        rv_contact_list.visibility = View.GONE
        baseModel.response?.let {
            rv_contact_list.visibility = View.VISIBLE
            (rv_contact_list.adapter as? ContactAdapter)?.updateData(
                (baseModel.response as? List<Contact>) ?: arrayListOf()
            )
        }
    }

}