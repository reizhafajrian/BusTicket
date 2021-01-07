package com.example.busticketactivity.firebase


import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.regist.RegistAddImageActivity
import com.example.busticketactivity.tiketmenu.ItemDataTiket

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class FireBaseRepo {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    fun getPost(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Bus")
            .get()
    }

    fun getPosition(namDoc: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Bus").document(namDoc)
            .get()
    }

    fun getPostCopy() {
        firebaseFirestore.collection("Bus").document("DPK-KLATEN")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result!!.toObject(DataItemPickup::class.java)

                    if (data != null) {
                        firebaseFirestore.collection("Bus").document("DPK-YGY")
                            .set(
                                data
                            )
                    }
                }
            }
    }

    fun createUser(dataUpdate: RegistAddImageActivity.Data): Task<Void> {
        val docData = hashMapOf<String, Any>(
            "username" to dataUpdate.username.toString(),
            "email" to dataUpdate.email.toString(),
            "nama" to dataUpdate.nama.toString(),
            "bio" to dataUpdate.bio,
            "imageUrl" to dataUpdate.imageLink
        )
        return firebaseFirestore.collection("User").document(dataUpdate.email.toString())
            .set(docData)
    }

    fun getUser(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("User").document(email)
            .get()
    }

    fun postPosisi(namaBus: String, nomor: String) {
        getPosition(namaBus).addOnCompleteListener {
            val nomorInt = nomor.toInt()
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()

                if (list != null) {
                    val data = list.position
                    val change = DataClassIsKosong(isKosong = false, nomor = (nomor))
                    lis.addAll(data)
                    lis.set(nomorInt - 1, change)
                    val docData = mapOf<String, MutableList<DataClassIsKosong?>>(
                        "position" to lis
                    )
                    firebaseFirestore.collection("Bus").document(namaBus)
                        .update(
                            docData
                        )
                }
            }
        }


    }

    fun getCheckTiket(namaBus: String, nomor: String, isKosong: CheckTiket) {
        getPosition(namaBus).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(DataItemPickup::class.java)
                val list = data?.position
                val nomorInt = nomor.toInt()
                if ((list?.get(nomorInt - 1)?.isKosong) == true) {
                    isKosong.Gettiket(true)
                } else {
                    isKosong.Gettiket(false)
                }

            }
        }
    }

    fun postPaymentTiket(nomor: String, email: String, data: ItemDataTiket) {
        val docData = hashMapOf<String, Any>(
            "email" to email,
            "namaBus" to data.nama,
            "nomorKursi" to nomor,
            "harga" to data.harga,
            "terminal" to data.terminal,
            "type" to data.type,
            "pergi" to data.pergi
        )
        firebaseFirestore.collection("Buy").document("Payment").collection("Buy").document(email)
            .set(docData)
    }

    fun getPaymentTiket(email: String): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Buy").document(email).collection("Buy").get()
    }

    fun getPaymentManager(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Buy").document("Payment").collection("Buy").get()
    }
}