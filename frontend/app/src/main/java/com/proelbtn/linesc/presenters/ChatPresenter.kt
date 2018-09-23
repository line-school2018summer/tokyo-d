package com.proelbtn.linesc.presenters

import android.util.Base64
import android.util.Log
import com.proelbtn.linesc.managers.StoredDataManager
import com.proelbtn.linesc.models.Message
import com.proelbtn.linesc.models.containers.ChatAdapterDataContainer
import com.proelbtn.linesc.models.datainterface.UserMessages
import com.proelbtn.linesc.models.requests.MessageRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChatPresenter(val view: View) {
    val data = ChatAdapterDataContainer()
    var uid: String? = null
    var flag = false
    var lastmid: Message? = null

    fun onCreate(uid: String) {
        this.uid = uid

        view.attachData(data)

        startPolling()
    }

    fun onStart() {
        startPolling()
    }

    fun onPause() {
        stopPolling()
    }

    fun onDestroy() {
        stopPolling()
    }

    fun sendMessage(content: String) {
        val content = Base64.encodeToString(content.toByteArray(), Base64.DEFAULT)
        UserMessages.create()
                .sendMessage(MessageRequest(StoredDataManager.getId()!!, uid!!, content))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                        },
                        {
                        }
                )
        view.clearTextView()
    }

    fun startPolling() {
        if (!flag) {
            flag = true
            Observable.interval(500, TimeUnit.MILLISECONDS)
                    .flatMap {
                        if (lastmid == null) UserMessages.create().getLatestMessages(uid!!, count = 1)
                        else UserMessages.create().getAfterMessages(uid!!, lastmid!!.id, count = 1)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                if (!flag) throw Error()
                                if (it.size != 0) {
                                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                                    if (lastmid == null ||
                                            parser.parse(lastmid!!.created_at) <= parser.parse(it.first().created_at)) {
                                        lastmid = it.last()
                                        it.forEach {
                                            data.addLast(it)
                                            view.notifyItemInserted(data.getItemCount())
                                        }
                                    }
                                }
                            },
                            {
                                Log.e("test", it.toString())
                            }
                    )

        }
    }

    fun stopPolling() {
        flag = false
    }

    interface View {
        fun attachData(data: ChatAdapterDataContainer)
        fun notifyItemInserted(pos: Int)
        fun clearTextView()
    }
}