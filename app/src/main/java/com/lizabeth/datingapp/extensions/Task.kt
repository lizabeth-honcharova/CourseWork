package com.lizabeth.datingapp.extensions

import android.util.Log
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <TResult> Task<TResult>.getTaskResult(): TResult {
    return suspendCancellableCoroutine { continuation ->
        this.addOnCompleteListener { task ->
            if(task.isSuccessful){
                continuation.resume(task.result)
            } else {
                task.exception!!.message?.let { Log.e("LOGIN ERROR TASK", it) }
                continuation.resumeWithException(task.exception!!)
            }
        }
    }
}

