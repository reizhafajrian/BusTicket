package com.example.busticketactivity.pickticket

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.bottomsheets.BottomSheet
import com.example.busticketactivity.listener.BottomSheetItemListener
import com.example.busticketactivity.firebase.*
import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.listener.GetStatus
import com.example.busticketactivity.listener.ListenerPickTicket
import com.example.busticketactivity.signin.UserObject
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BankType
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_pick_ticket.*
import retrofit2.Call
import retrofit2.Callback
import java.util.*


class PickTicketActivity : AppCompatActivity(), ListenerPickTicket, BottomSheetItemListener,
    TransactionFinishedCallback {
    val TAG = "PickTicketActivity"
    var orderId = ""
    private val gson = Gson()
    private val btnAlert by lazy { BottomSheet(this) }
    private lateinit var rvPickTicket: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_ticket)
        intitateUI()
        Loader()
    }

    private fun DataTiket(): ItemDataTiket? {
        val getDataTicket = intent.getStringExtra("dataTicket")
        val gson = Gson()
        val newData = gson.fromJson(getDataTicket, ItemDataTiket::class.java)
        return newData
    }

    private fun intitateUI() {
        rvPickTicket = findViewById(R.id.rv_pick_ticket)
        val newData = DataTiket()
        if (newData != null) {
            tv_title_bus.text = newData.nama
            tv_type_bus.text = newData.type
            tv_terminal.text = newData.terminal
            tv_berangkat.text = newData.pergi
        }
    }

    private fun Loader() {
        val titleDoc = intent.getStringExtra("title")
        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Bus").document(titleDoc.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val list = it.result!!.toObject(DataItemPickup::class.java)
                    Log.d(TAG, "ini list $list")
                    if (list != null) {
                        rvPickTicket.layoutManager = GridLayoutManager(this, 5)
                        Log.d(TAG, "ini list ${list.position}")
                        val listItemAdapter = PickTicketAdapter(list.position, this)
                        listItemAdapter.notifyDataSetChanged()
                        rvPickTicket.adapter = listItemAdapter
                    }
                }
            }
    }

    override fun onClick(nomor: String) {
        when (nomor) {
            nomor -> {
                val Nomor = getSharedPreferences("nomorKursi", Context.MODE_PRIVATE)

                val posisi = Nomor.edit()
                posisi.clear()
                posisi.putString("nomorKursi", nomor).apply()
                ShowBottomSheets()
            }
        }
    }

    private fun ShowBottomSheets() {
        btnAlert.show(supportFragmentManager, "Alert")
    }

    override fun getUserChoice(Choice: Boolean) {
        val dataTiket=DataTiket()
        if (Choice) {
            val Nomor = getSharedPreferences("nomorKursi", Context.MODE_PRIVATE)
            val data=Nomor.getString("nomorKursi", "").toString()
                FireBaseRepo().getCheckTiket(
                    dataTiket!!.nama,
                    data,
                    object : CheckTiket {
                        override fun Gettiket(isKosong: Boolean) {
                            Log.d(TAG,"ini boolean ${isKosong}")
                            if (isKosong==true) {
                                pay()
                                showMidtrans()
                            } else {
                                Toast.makeText(this@PickTicketActivity, "maaf tiket sudah di pesan oleh orang lain", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )


        }
    }

    private fun pay() {
        SdkUIFlowBuilder.init()
            .setContext(this)  // context is mandatory
            .setClientKey("SB-Mid-client-xc9c1l3oJpz9qaOZ") // client_key is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("https://pythonmidtrans.herokuapp.com/") //set merchant url (required)
            .enableLog(true) // enable sdk log (optional)
            .setColorTheme(
                CustomColorTheme(
                    "#FFE51255",
                    "#B61548",
                    "#FFE51255"
                )
            )
            .buildSDK()
    }
//    private fun getCheckTiket(){
//        FireBaseRepo().getPost().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val data = it.result!!.toObjects(ItemDataTiket::class.java)
//                rvTiket.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
//                listItemAdapter.notifyDataSetChanged()
//                rvTiket.adapter = listItemAdapter
//            } else {
//                Log.d(TAG,"error")
//            }
//        }
//    }
    override fun onTransactionFinished(result: TransactionResult?) {
        if (result?.response != null) {
            getStatus()
        } else {
            getStatus()
        }
    }

    private fun getStatus() {
        val retro = Retro().getRetroClientInstance("https://api.sandbox.midtrans.com/v2/")
            .create(GetStatus::class.java)
        retro.getTransaksi("U0ItTWlkLXNlcnZlci1EbHpZeU85Mlp6YjNDa0k1QTlkMUhmdGs=", otp = orderId)
            .enqueue(
                object : Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        val res = response.body()
                        if (res?.transaction_status.equals("settlement")) {
                            val Nomor = getSharedPreferences("nomorKursi", Context.MODE_PRIVATE)
                            val data = getDataUser()
                            val tiket = DataTiket()
                            val posisi = Nomor.getString("nomorKursi", "").toString()
                            if (tiket != null) {
                                FireBaseRepo().postPosisi(namaBus = tiket.nama, nomor = posisi)
                                FireBaseRepo().postPaymentTiket(posisi, data.email, tiket)
                            }
                        }
                        Log.d(TAG, "ini response ${res?.transaction_status}")
                    }
                    override fun onFailure(call: Call<Response>, t: Throwable) {

                    }
                }
            )
    }

    private fun getDataUser(): UserObject {
        val prefs2 = getSharedPreferences("dataUser", Context.MODE_PRIVATE)
        val output = prefs2.getString("dataUser", "")
        val obj = gson.fromJson(output, UserObject::class.java)
        return obj
    }


    private fun showMidtrans() {
        val setting = MidtransSDK.getInstance().uiKitCustomSetting
        setting.isSkipCustomerDetailsPages = true
        MidtransSDK.getInstance().uiKitCustomSetting = setting
        MidtransSDK.getInstance().transactionRequest = TransactionReq()
        MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER_BCA)
    }

    //        private fun data1():com.midtrans.sdk.corekit.models.CustomerDetails{
//        val getUser=getSharedPreferences("dataUser", Context.MODE_PRIVATE)
//        val NewGson=Gson()
//        val json=getUser.getString("dataUser","")
//        val dataUser=NewGson.fromJson(json,UserObject::class.java)
//        val data=com.midtrans.sdk.corekit.models.CustomerDetails()
//        data.firstName=dataUser.nama
//        data.lastName=dataUser.nama
//        data.email=dataUser.email
//        data.phone="085155208046"
//        return data
//    }
    private fun TransactionReq(): TransactionRequest {
        val dataTiket = DataTiket()
        val dataUser = getDataUser()
        orderId = System.currentTimeMillis().toString()
        val req =
            TransactionRequest(orderId, (dataTiket?.harga)!!.toDouble())
        val details =
            ItemDetails(dataTiket.nama, (dataTiket.harga).toDouble(), 1, dataTiket.type)
        val arrayDetail: ArrayList<ItemDetails> = arrayListOf()

        arrayDetail.add(details)
        val credit = CreditCard()
        val customerData = CustomerDetails()
        customerData.apply {
            email = dataUser.email
            firstName = dataUser.email
            phone = ("0989217182")
            lastName = dataUser.lastname
        }

        req.apply {
            customerDetails = customerData
            itemDetails = arrayDetail
        }
        credit.apply {
            isSaveCard = false
            authentication = CreditCard.AUTHENTICATION_TYPE_RBA
            bank = BankType.BCA
        }

        return req
    }
}
