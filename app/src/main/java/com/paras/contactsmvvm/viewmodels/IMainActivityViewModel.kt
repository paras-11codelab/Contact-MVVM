package com.paras.contactsmvvm.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import com.paras.contactsmvvm.models.BaseModel
import java.lang.ref.WeakReference

interface IMainActivityViewModel {

    fun fetchContacts(contextWeakReference: WeakReference<Context>)
    fun getContactLiveData(): LiveData<BaseModel>

}