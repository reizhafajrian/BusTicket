package com.example.busticketactivity.firebase


import com.example.busticketactivity.dataclass.*
import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.pickticket.DataItemPickup
import com.example.busticketactivity.regist.RegistAddImageActivity

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FireBaseRepo {

    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    fun getPost(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Keberangkatan")
            .get()
    }

    fun getPosition(namDoc: String, tanggal: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Keberangkatan").document(namDoc).collection("posisi")
            .document(tanggal)
            .get()
    }

    fun getPositionPosisi(namDoc: String, tanggal: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Keberangkatan").document(namDoc).collection("posisi")
            .document(tanggal)
            .get()
    }

    fun CheckFull(): Task<QuerySnapshot> {
        return getPost()
    }

    fun posisiByTanggal() {
        val data = mutableListOf<DataClassIsKosong>()
        data.removeAll(data)
        for (i in 0 until 40) {
            val nomor = i + 1
            data.add(i, DataClassIsKosong(nomor = nomor.toString(), isKosong = true))
        }
        val map = hashMapOf<String, Any>(
            "position" to data
        )
        firebaseFirestore.collection("Bus").document("1234").collection("posisibytanggal")
            .document("01-02-2021").set(
                map
            )
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

    fun getCancel(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Cancel").get()
    }


    fun createUser(dataUpdate: RegistAddImageActivity.Data): Task<Void> {
        val docData = hashMapOf<String, Any>(
            "email" to dataUpdate.email.toString(),
            "nama" to dataUpdate.nama.toString(),
            "telepon" to dataUpdate.telepon,
            "imageUrl" to dataUpdate.imageLink,
            "role" to dataUpdate.role
        )
        firebaseFirestore.collection("Role").document(dataUpdate.email.toString())
            .set(docData)

        return firebaseFirestore.collection("User").document(dataUpdate.email.toString())
            .set(docData)
    }

    fun createDriver(dataUpdate: DriverDataClass) {
        auth.createUserWithEmailAndPassword(dataUpdate.email, dataUpdate.pass)
            .addOnCompleteListener {
                firebaseFirestore.collection(dataUpdate.role).document(dataUpdate.email)
                    .set(dataUpdate).addOnCompleteListener {
                        createRole(dataUpdate)
                    }
            }.addOnFailureListener {
                if (auth.currentUser != null) {
                    auth.currentUser!!.delete()
                    firebaseFirestore.collection(dataUpdate.role).document(dataUpdate.email)
                        .delete()
                }
            }
    }

    fun createRole(dataUpdate: DriverDataClass) {
        firebaseFirestore.collection("Role").document(dataUpdate.email).set(dataUpdate)
    }

    fun getUser(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("User").document(email)
            .get()
    }

    fun postPosisi(namaBus: String, nomor: String, tanggal: String) {
        getPositionPosisi(namaBus, tanggal).addOnCompleteListener {
            val nomorInt = nomor.toInt()
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()
                if (list != null) {
                    val data = list.posisi
                    val change = DataClassIsKosong(isKosong = false, nomor = (nomor))
                    lis.addAll(data)
                    lis.set(nomorInt - 1, change)
                    val docData = mapOf<String, MutableList<DataClassIsKosong?>>(
                        "posisi" to lis
                    )
                    firebaseFirestore.collection("Keberangkatan").document(namaBus)
                        .collection("posisi").document(tanggal)
                        .update(
                            docData
                        )
                }
            }
        }
    }

    fun createDataPay(email: String): Task<Void> {
        val data = mapOf<String, Any>(
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
            id = data.id,
            nama = data.nama,
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
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                firebaseFirestore.collection("Histori").document("Transaksi")
                                    .collection("Cancel").document(email).set(hasil)

                            }
                        }
                }
            }
        }

    }

    fun getCheckTiket(
        namaBus: String,
        tanggal: String,
        nomor: MutableList<String>,
        isKosong: CheckTiket
    ) {
        getPositionPosisi(namaBus, tanggal).addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result!!.toObject(DataItemPickup::class.java)
                val list = data?.posisi
                val listData = mutableListOf<Boolean>()
                for (i in nomor) {
                    if ((list?.get(i.toInt() - 1)?.isKosong) == true) {
                        listData.add(true)
                    } else {
                        listData.add(false)
                        isKosong.Gettiket(false, i)
                    }
                }
                val hasil = listData.filter {
                    it == true
                }
                if (listData.size == hasil.size) {
                    isKosong.Gettiket(true, "")
                }

            }
        }
    }

    fun
            postPaymentTiket(
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
            id = data.id,
            nama = data.nama,
            nomorKursi = nomor,
            harga = data.harga,
            terminal = data.terminal,
            tanggalBeli = data.tanggalBeli,
            type = data.type,
            noplat = data.noplat,
            tanggal = data.tanggal,
            pergi = data.pergi
        )
        val data = hashMapOf<String, Any>(
            "data" to mutableListOf(data)
        )
        getPaymentTiket(email).addOnCompleteListener {
            if (it.isSuccessful) {
                val hasil = it.result!!.toObject(ManagerGetData::class.java)
                hasil?.data?.add(docudata)
                if (hasil != null) {
                    firebaseFirestore.collection("Buy").document(email).set(hasil)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                firebaseFirestore.collection("Histori").document("Transaksi")
                                    .collection("Buy").document(email).set(hasil)
                            }
                        }
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
        val data = hashMapOf<String, Any>(
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
        firebaseFirestore.collection("Buy").document(email).set(repost).addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseFirestore.collection("Histori").document("Transaksi").collection("Buy")
                    .document(email).set(repost)

            }
        }
    }

    fun canceltiket(namaBus: String, tanggal: String, nomor: String) {
        getPosition(namaBus, tanggal).addOnCompleteListener {
            val nomorInt = nomor.toInt()
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()
                if (list != null) {
                    val data = list.posisi
                    val change = DataClassIsKosong(isKosong = true, nomor = (nomor))
                    lis.addAll(data)
                    lis.set(nomorInt - 1, change)
                    val docData = mapOf<String, MutableList<DataClassIsKosong?>>(
                        "posisi" to lis
                    )
                    firebaseFirestore.collection("Keberangkatan").document(namaBus)
                        .collection("posisi").document(tanggal)
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

    fun resetTiket(namaBus: String, tanggal: String) {
        getPosition(namaBus, tanggal).addOnCompleteListener {
            if (it.isSuccessful) {
                val list = it.result!!.toObject(DataItemPickup::class.java)
                val lis = mutableListOf<DataClassIsKosong?>()
                if (list != null) {
                    val data = list.posisi
//                    val change = DataClassIsKosong(isKosong = false, nomor = (nomor))
                    lis.addAll(data)

                    for (i in 1..lis.size) {

                        lis[i - 1] = DataClassIsKosong(isKosong = true, nomor = i.toString())
                    }
                    val docData = mapOf<String, MutableList<DataClassIsKosong?>>(
                        "posisi" to lis
                    )
                    firebaseFirestore.collection("Bus").document(namaBus)
                        .update(
                            docData
                        )

                }
            }
        }
    }

    fun PostTicket(data: TicketPostDataClass) {
        val list = TicketPostDataClassNoTanggal()
        list.driver = data.driver
        list.harga = data.harga
        list.id = data.id
        list.nama = data.nama
        list.pergi = data.pergi
        list.terminal = data.terminal
        list.noplat = data.noplat
        list.type = data.type
        firebaseFirestore.collection("Keberangkatan").document(data.id).set(list)


    }


    fun cancelDetail(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Cancel").document(email).get()
    }

    fun BuyDetail(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Buy").document(email).get()
    }

    fun getUserRole(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("User").document(email).get().addOnCompleteListener {

        }

    }

    fun getUserRoleAll(email: String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Role").document(email).get().addOnCompleteListener {

        }

    }

    fun GetCancelTicketUser(email: String?): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Cancel").document(email!!).get()
    }

    fun CheckTicket(data: InfoTiket): Task<QuerySnapshot> {
        val hasil = mapOf(
            "email" to data.email,
            "id" to data.id,
            "nama" to data.nama,
            "nomorKursi" to data.nomorKursi,
            "noplat" to data.noplat,
            "harga" to data.harga,
            "pergi" to data.pergi,
            "tanggal" to data.tanggal,
            "tanggalBeli" to data.tanggalBeli,
            "terminal" to data.terminal,
            "type" to data.type
        )
        return firebaseFirestore.collection("Buy").whereArrayContains("data", hasil).get()
    }

    fun PostBus(data: TicketPostDataClass) {
        val dataBus = hashMapOf(
            "plat" to data.noplat,
            "id" to data.id,
            "driver" to data.driver,
            "jurusan" to data.nama
        )
        firebaseFirestore.collection("Bus").document(data.id).set(
            dataBus
        )
    }

    fun getDriver(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Driver").get()
    }

    fun postPosisiTanggal(data: TicketPostDataClass) {
        val hasil = hashMapOf<String, Any>(
            "posisi" to data.position
        )
        for (i in data.tanggal) GlobalScope.launch {
            firebaseFirestore.collection("Keberangkatan").document(data.id).collection("posisi")
                .document(i).set(hasil)
            delay(700)
        }


    }

    fun getIdBus(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Bus").get()
    } fun getDataBus(id:String): Task<DocumentSnapshot> {
        return firebaseFirestore.collection("Bus").document(id).get()
    }

    fun DetailTiket(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Buy").get()
    }

    fun getUserNama(email: String): Task<QuerySnapshot> {
        return firebaseFirestore.collection("User").whereEqualTo("email", email).get()
    }
}
