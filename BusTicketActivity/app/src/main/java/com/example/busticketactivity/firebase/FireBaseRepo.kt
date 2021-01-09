package com.example.busticketactivity.firebase


import android.util.Log
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.regist.RegistAddImageActivity
import com.example.busticketactivity.tiketmenu.InfoTiket
import com.example.busticketactivity.tiketmenu.ItemDataTiket

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    fun postPaymentTiket(
        nomor: String,
        email: String,
        data: ItemDataTiket
    ) {
        val docData = hashMapOf<String, Any>(
            "email" to email,
            "namaBus" to data.nama,
            "nomorKursi" to nomor,
            "harga" to data.harga,
            "terminal" to data.terminal,
            "type" to data.type,
            "pergi" to data.pergi
        )
        val docudata = InfoTiket(
            email = email,
            namaBus = data.nama,
            nomorKursi = nomor,
            harga = data.harga,
            terminal = data.terminal,
            type = data.type,
            pergi = data.pergi
        )
        val data = hashMapOf<String, Any>(
            "data" to mutableListOf(docData)
        )
        getPaymentTiket(email).addOnCompleteListener {
            if (it.isSuccessful) {
                val hasil = it.result!!.toObject(ManagerGetData::class.java)
                hasil?.data?.add(docudata)
                if (hasil != null) {
                    firebaseFirestore.collection("Buy").document(email).set(hasil)
                }
            }
        }

    }

    fun getPaymentTiket(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Buy").document(email).get()
    }

    fun getPaymentManager(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Buy").get()
    }

    fun deletetiket(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Buy").document(email).get()
    }

    fun repostTiket(email: String, data: List<InfoTiket>) {
        val repost = hashMapOf<String, Any>(
            "data" to data
        )
        firebaseFirestore.collection("Buy").document(email).set(repost)
    }

    fun canceltiket(namaBus: String, nomor: String) {
        getPosition(namaBus).addOnCompleteListener {
            val nomorInt = nomor.toInt()
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()
                if (list != null) {
                    val data = list.position
                    val change = DataClassIsKosong(isKosong = true, nomor = (nomor))
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

    fun postDeleteTicket(email: String, data: List<InfoTiket>) {
        val repost = hashMapOf<String, Any>(
            "data" to data
        )
        firebaseFirestore.collection("Cancel").document(email).set(repost)
    }

    fun resetTiket(namaBus: String) {
        getPosition(namaBus).addOnCompleteListener {
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()
                if (list != null) {
                    val data = list.position
//                    val change = DataClassIsKosong(isKosong = false, nomor = (nomor))
                    lis.addAll(data)

                    for (i in 1..lis.size) {

                        lis[i - 1] = DataClassIsKosong(isKosong = true, nomor = i.toString())
                    }
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
}
