package com.example.busticketactivity.firebase

import com.example.busticketactivity.regist.RegistAddImageActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FireBaseRepo {

    private val firebaseFirestore =FirebaseFirestore.getInstance()
    fun getPost():Task<QuerySnapshot>{
        return firebaseFirestore
            .collection("Bus")
            .get()
    }
    fun getPosition(namDoc:String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Bus").document(namDoc)
            .get()
    }
    fun createUser(dataUpdate:RegistAddImageActivity.Data){
        val docData= hashMapOf<String,Any>(
            "username" to dataUpdate.username,
            "email" to dataUpdate.email,
            "nama" to dataUpdate.nama,
            "lastname" to dataUpdate.bio
        )
        firebaseFirestore.collection("User").document(dataUpdate.email)
            .set(docData)
    }
    fun getUser(email:String):Task<DocumentSnapshot>{
        return firebaseFirestore.collection("User").document(email)
            .get()
    }
}