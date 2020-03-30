package com.paras.contactsmvvm.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paras.contactsmvvm.models.BaseModel
import com.paras.contactsmvvm.repositories.ContentProviderRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainActivityViewModel : ViewModel(), IMainActivityViewModel {

    private val contentProviderRepository: ContentProviderRepository = ContentProviderRepository()
    private val compositeDisposable = CompositeDisposable()
    private val contactLiveData: MutableLiveData<BaseModel> = MutableLiveData()

    /**
     * @param contextWeakReference, weak reference of context, so that we can retrieve the context only when it is not null
     *
     * this method will fetch Contacts related information via ContactRepository in another thread and use live data for data passing.
     */
    override fun fetchContacts(contextWeakReference: WeakReference<Context>) {
        contactLiveData.postValue(BaseModel(true, null, null))
        contextWeakReference.get()?.let { context ->
            Observable.just(listOf<Int>())
                .map {
                    contentProviderRepository.getContacts(context)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    contactLiveData.postValue(BaseModel(false, null, it))
                }, {
                    contactLiveData.postValue(BaseModel(false, it, null))
                }, {})
        }
    }

    /**
     * @return the live data so that view can observe the live data and can get updated data instantly.
     */
    override fun getContactLiveData(): LiveData<BaseModel> = contactLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}