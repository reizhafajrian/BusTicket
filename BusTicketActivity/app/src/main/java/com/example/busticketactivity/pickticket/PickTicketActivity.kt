package com.example.busticketactivity.pickticket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Half.toFloat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.bottomsheets.BottomSheet
import com.example.busticketactivity.bottomsheets.BottomSheetItemListener
import com.example.busticketactivity.firebase.GetStatus
import com.example.busticketactivity.firebase.Response
import com.example.busticketactivity.firebase.Retro
import com.example.busticketactivity.home.HomeActivity
import com.example.busticketactivity.signin.UserObject
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BankType
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
    val TAG="PickTicketActivity"
    private val btnAlert by lazy { BottomSheet(this) }
    private lateinit var rvPickTicket: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_ticket)
        intitateUI()
        Loader()
    }

    private fun DataTiket(): ItemDataTiket? {
        val getDataTicket=intent.getStringExtra("dataTicket")
        val gson=Gson()
        val newData=gson.fromJson(getDataTicket,ItemDataTiket::class.java)
        return newData
    }
    private fun intitateUI() {
//        val list = mutableListOf<DataItemPickup?>()
        rvPickTicket = findViewById(R.id.rv_pick_ticket)
        val newData=DataTiket()
        if (newData!=null) {
            tv_title_bus.text = newData.nama
            tv_type_bus.text = newData.type
            tv_terminal.text = newData.terminal
            tv_berangkat.text = newData.pergi
        }
    }

    private fun Loader() {
        val titleDoc=intent.getStringExtra("title")
        val firebaseFirestore=FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Bus").document(titleDoc)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val list = it.result!!.toObject(DataItemPickup::class.java)
                    Log.d(TAG,"ini list $list")
                    if (list != null) {
                        rvPickTicket.layoutManager = GridLayoutManager(this, 5)
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
                val Nomor=getSharedPreferences("nomorKursi", Context.MODE_PRIVATE).edit()
                Nomor.clear()
                Nomor.putString("nomorKursi",nomor).commit()
                ShowBottomSheets()
            }
        }
    }

    private fun ShowBottomSheets() {
        btnAlert.show(supportFragmentManager, "Alert")
    }

    override fun getUserChoice(Choice: Boolean) {
        super.getUserChoice(Choice)
        if (Choice) {
            pay()
            showMidtrans()
        }
    }
    private fun pay(){
        SdkUIFlowBuilder.init()
            .setContext(this)  // context is mandatory
            .setClientKey("SB-Mid-client-xc9c1l3oJpz9qaOZ") // client_key is mandatory
            .setTransactionFinishedCallback (this) // set transaction finish callback (sdk callback)
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
    override fun onTransactionFinished(result: TransactionResult?) {
        Toast.makeText(this, "${result?.status}", Toast.LENGTH_SHORT).show()
        if (result?.response!=null){
            getStatus(TransactionReq().orderId.toString())
        }else{
            Toast.makeText(this, "Transaksi di batalkan", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getStatus(id:String?){
        val retro=Retro().getRetroClientInstance("https://api.sandbox.midtrans.com/v2/").create(GetStatus::class.java)
        retro.getTransaksi(id,"U0ItTWlkLXNlcnZlci1EbHpZeU85Mlp6YjNDa0k1QTlkMUhmdGs=").enqueue(
            object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    val res = response.body()
                    if (res?.transaction_status.equals("settlement")) {

                    } else {

                    }
                }
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.e("failed", t.message.toString())
                }
            }
        )
    }

    private fun showMidtrans(){
        val setting = MidtransSDK.getInstance().uiKitCustomSetting
        MidtransSDK.getInstance().uiKitCustomSetting = setting
        MidtransSDK.getInstance().transactionRequest=TransactionReq()
        MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER_BCA)
    }
//    private fun data1():com.midtrans.sdk.corekit.models.CustomerDetails{
//        val getUser=getSharedPreferences("dataUser", Context.MODE_PRIVATE)
//        val NewGson=Gson()
//        val json=getUser.getString("dataUser","")
//        val dataUser=NewGson.fromJson(json,UserObject::class.java)
////        val userdata: UserData =
////            UserData(nama = "reizha",email = "reizha77@gmail.com",nomorTelfon ="085155208046",UserId = "reizha88")
//        val data=com.midtrans.sdk.corekit.models.CustomerDetails()
//        data.firstName=dataUser.nama
//        data.lastName=dataUser.nama
//        data.email=dataUser.email
//        data.phone="085155208046"
//        return data
//    }
    @SuppressLint("WrongConstant")
    private fun TransactionReq(): TransactionRequest {
        val dataTiket=DataTiket()
        val id=dataTiket?.nama
        val price=(dataTiket?.harga)!!.toDouble()
        val quantity=1
        val itemName=dataTiket?.type
        val req= TransactionRequest("${System.currentTimeMillis()} ",price)
        val details= ItemDetails(id,price,quantity,itemName)
        val arrayDetail: ArrayList<ItemDetails> = arrayListOf<ItemDetails>()
        val credit= CreditCard()
        arrayDetail.add(details)
        req.itemDetails=arrayDetail
        credit.isSaveCard=false
        credit.authentication= CreditCard.AUTHENTICATION_TYPE_RBA
        credit.bank= BankType.BCA
        req.creditCard=credit
        return req
    }
}
