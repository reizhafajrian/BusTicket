package com.example.busticketactivity.firebase


import android.util.Log
import com.example.busticketactivity.DriverDataClass
import com.example.busticketactivity.TicketPostDataClass
import com.example.busticketactivity.dataclass.ManagerGetData
import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.regist.RegistAddImageActivity
import com.example.busticketactivity.tiketmenu.InfoTiket
import com.example.busticketactivity.tiketmenu.ItemDataTiket

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase


class FireBaseRepo {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    fun getPost(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Bus")
            .get()
    }

    fun getPosition(namDoc: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Bus").document(namDoc)
            .get()
    }

    fun CheckFull(): Task<QuerySnapshot> {
        return getPost()
    }

    fun getPostCopy() {
        firebaseFirestore.collection("Bus").document("DPK-KLATEN")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result!!.toObject(DataItemPickup::class.java)

                    if (data != null) {
                        firebaseFirestore.collection("Bus").document("DPK-WONOSOBO")
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
    fun createDriver(dataUpdate: DriverDataClass){
         auth.createUserWithEmailAndPassword(dataUpdate.email,dataUpdate.pass).addOnCompleteListener {
            firebaseFirestore.collection("User").document(dataUpdate.email)
                .set(dataUpdate)
        }.addOnFailureListener {
            if(auth.currentUser!=null){
                auth.currentUser!!.delete()
                firebaseFirestore.collection("User").document(dataUpdate.email).delete()
            }

        }


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
    fun createDataPay(email :String): Task<Void> {
        val data= mapOf<String,Any>(
            "data" to mutableListOf<Any>()
        )
        return firebaseFirestore.collection("Buy").document(email).set(
            data
        )
    }

    fun postCancel(
        email: String,
        data: InfoTiket
    ) {
//        val docData = hashMapOf<String, Any>(
//            "email" to email,
//            "namaBus" to data.nama,
//            "nomorKursi" to nomor,
//            "harga" to data.harga,
//            "terminal" to data.terminal,
//            "type" to data.type,
//            "pergi" to data.pergi
//        )
        val docudata = InfoTiket(
            email = data.email,
            namaBus = data.nama,
            nomorKursi = data.nomorKursi,
            harga = data.harga,
            terminal = data.terminal,
            type = data.type,
            tanggal = data.tanggal,
            pergi = data.pergi
        )
//        val data = hashMapOf<String, Any>(
//            "data" to mutableListOf(docudata)
//        )
        getCancelTicket(email).addOnCompleteListener {
            if (it.isSuccessful) {
                val hasil = it.result!!.toObject(ManagerGetData::class.java)
                hasil?.data?.add(docudata)
                if (hasil != null) {
                    firebaseFirestore.collection("Cancel").document(email).set(hasil)
                }
            }
        }

    }
    fun getCheckTiket(namaBus: String, nomor: MutableList<String>, isKosong: CheckTiket) {
        getPosition(namaBus).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(DataItemPickup::class.java)
                val list = data?.position
                val listData= mutableListOf<Boolean>()
                for (i in nomor){
                    if ((list?.get(i.toInt() - 1)?.isKosong) == true) {
                        listData.add(true)
                    } else {
                        listData.add(false)
                        isKosong.Gettiket(false,i)
                    }
                }
                val hasil=listData.filter {
                    it==true
                }
                if(listData.size==hasil.size){
                    isKosong.Gettiket(true,"")
                }

            }
        }
    }

    fun postPaymentTiket(
        nomor: String,
        email: String,
        data: DataItemPickup
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
            tanggal = data.tanggal,
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
    fun getCancelTicket(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Cancel").document(email).get()
    }
    fun createCancel(email: String) {
        val data= hashMapOf<String,Any>(
            "data" to mutableListOf<Any>()
        )
        firebaseFirestore.collection("Cancel").document(email).set(data)
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

    fun PostTicket(data:TicketPostDataClass) {
        firebaseFirestore.collection("Bus").document(data.id).set(data)
    }
}
