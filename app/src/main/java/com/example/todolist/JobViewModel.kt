package com.example.todolist

import DatabaseHelper
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking


class JobViewModel(context : Context) : ViewModel() {

    var jobList = mutableStateListOf<Job>()
    var db = DatabaseHelper(context)

    init {
        getJobs()
    }

    fun getJobs() {


        var cursor = db.readAllData()
        if (cursor != null) {
            if(cursor.count != 0){
                jobList.clear()

                while(cursor.moveToNext()){
                    val job = Job(cursor.getInt(0),cursor.getString(1),cursor.getInt(2))
                    jobList.add(job)
                }

            }
        }
    }



    fun addJob(job_text : String){
        if(job_text.trim().isNotEmpty()){
            val index = jobList.size + 1
            val lower_text = job_text.trim().replaceFirstChar { c: Char -> c.uppercase() }

            db.addJob(index,lower_text,0)
            getJobs()

        }

    }

    fun changeIsDone(id : Int,is_done : Int){

        db.update_is_done(id,is_done)
        getJobs()
    }

    fun clearJob(){
        runBlocking {
            db.deleteAllJob()
        }
        getJobs()
        //jobList.clear()

    }
}

