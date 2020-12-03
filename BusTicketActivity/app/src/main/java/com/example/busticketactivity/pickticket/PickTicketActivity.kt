package com.example.busticketactivity.pickticket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R

import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.bottomsheets.BottomSheet
import com.example.busticketactivity.bottomsheets.BottomSheetItemListener
import com.example.busticketactivity.dataclass.UserData
import com.example.busticketactivity.firebase.FireBaseRepo
import com.example.busticketactivity.payment.PaymentActivity
import com.example.busticketactivity.tiketmenu.ItemDataTiket
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BankType
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_pick_ticket.*
import java.util.ArrayList


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

    private fun intitateUI() {
//        val list = mutableListOf<DataItemPickup?>()
        rvPickTicket = findViewById(R.id.rv_pick_ticket)
        val getDataTicket=intent.getStringExtra("dataTicket")
        val gson=Gson()
        val newData=gson.fromJson(getDataTicket,ItemDataTiket::class.java)
        tv_title_bus.text=newData.nama
        tv_type_bus.text=newData.type
        tv_terminal.text=newData.terminal
        tv_berangkat.text=newData.pergi
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
            ) // set theme. it will replace theme on snap theme on MAP ( optional)
            .buildSDK()
    }
    override fun onTransactionFinished(result: TransactionResult?) {
        if(result?.response!=null){
            when(result.status){
                TransactionResult.STATUS_SUCCESS->{
                    Toast.makeText(this, "transaksi berhasil", Toast.LENGTH_SHORT).show()
                }
                TransactionResult.STATUS_FAILED->{
                    Toast.makeText(this, "transaksi gagal", Toast.LENGTH_SHORT).show()
                }
            }
            result?.response?.validationMessages
        }else{
            Toast.makeText(this, "transaksi di batalkan", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showMidtrans(){
        MidtransSDK.getInstance().transactionRequest=TransactionReq()
        MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER_BCA)
    }
    private fun data1():com.midtrans.sdk.corekit.models.CustomerDetails{

        val userdata: UserData =
            UserData(nama = "reizha",email = "reizha77@gmail.com",nomorTelfon ="085155208046",UserId = "reizha88")
        val data=com.midtrans.sdk.corekit.models.CustomerDetails()
        data.firstName=userdata.nama
        data.lastName=userdata.nama
        data.email=userdata.email
        data.phone=userdata.nomorTelfon
        return data
    }
    @SuppressLint("WrongConstant")
    private fun TransactionReq(): TransactionRequest {
        val id="ticekt"
        val price=20000.0
        val quantity=1
        val itemName="harga"
        val req= TransactionRequest("${System.currentTimeMillis()} ",20000.0)
        req.customerDetails=data1()
        val details= ItemDetails(id,price,quantity,itemName)
        val arrayDetail: ArrayList<ItemDetails> = arrayListOf<ItemDetails>()
        arrayDetail.add(details)
        req.itemDetails=arrayDetail
        val credit= CreditCard()
        credit.isSaveCard=false
        credit.authentication= CreditCard.AUTHENTICATION_TYPE_RBA
        credit.bank= BankType.BCA
        req.creditCard=credit
        return req
    }
}
