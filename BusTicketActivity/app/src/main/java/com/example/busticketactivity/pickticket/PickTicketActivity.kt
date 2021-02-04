package com.example.busticketactivity.pickticket

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busticketactivity.R
import com.example.busticketactivity.adapter.PickTicketAdapter
import com.example.busticketactivity.bottomsheets.BottomSheet
import com.example.busticketactivity.listener.BottomSheetItemListener
import com.example.busticketactivity.firebase.*
import com.example.busticketactivity.listener.CheckTiket
import com.example.busticketactivity.listener.GetStatus
import com.example.busticketactivity.listener.ListenerPickTicket
import com.example.busticketactivity.dataclass.UserObject
import com.google.android.material.datepicker.MaterialDatePicker
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class PickTicketActivity : AppCompatActivity(), View.OnClickListener, ListenerPickTicket,
    BottomSheetItemListener,
    TransactionFinishedCallback,TextWatcher {
    val TAG = "PickTicketActivity"
    var tanggal=""
    var orderId = ""

    private var nomor = mutableListOf<String>()
    private val gson = Gson()
    private val btnAlert by lazy { BottomSheet(this) }
    private lateinit var rvPickTicket: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_ticket)
        intitateUI()
        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())
        tanggal=currentDate
        ed_tanggal.text=tanggal
        Loader(tanggal)
        btn_checkout.setOnClickListener(this)
        ed_tanggal.setOnClickListener(this)
        ed_tanggal.addTextChangedListener (this)

    }


    override fun onStart() {
        Loader(tanggal)
        super.onStart()
    }


    private fun DataTiket(): DataItemPickup? {
        val getDataTicket = intent.getStringExtra("dataTicket")
        val gson = Gson()
        val newData = gson.fromJson(getDataTicket, DataItemPickup::class.java)
        return newData
    }

    private fun intitateUI() {
        rvPickTicket = findViewById(R.id.rv_pick_ticket)
        val newData = DataTiket()
        if (newData != null) {
            tv_title_bus.text = newData.nama
            tv_type_bus.text = newData.type
            tv_terminal.text = newData.terminal
            tv_berangkat.text = "${newData.pergi}"
        }

    }

    private fun tanggal() {
        val builder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
        val picker: MaterialDatePicker<*> = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener{
            val dateFormat=SimpleDateFormat("dd MMM yy")
            val hasil=dateFormat.parse(picker.headerText)
            val dateAfterFormat=SimpleDateFormat("dd-M-yyyy").format(hasil)
            simpanTanggal(dateAfterFormat)
            tanggal=dateAfterFormat
        }
    }

    private fun simpanTanggal(picker: String){
        ed_tanggal.text=picker
    }

    private fun Loader(tanggal:String) {
        val titleDoc = intent.getStringExtra("title")
        val firebaseFirestore = FirebaseFirestore.getInstance()
        spinner.visibility = View.VISIBLE
        Log.d(TAG,"ini tanggal${tanggal}")
        Log.d(TAG,"ini tanggal${titleDoc}")
        firebaseFirestore.collection("Keberangkatan").document(titleDoc.toString()).collection("posisi").document(tanggal)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    spinner.visibility = View.GONE
                    val list = it.result!!.toObject(DataItemPickup::class.java)
                    if (list != null) {
                        rvPickTicket.visibility=View.VISIBLE
                        rvPickTicket.layoutManager = GridLayoutManager(this, 5)
                        val listItemAdapter = PickTicketAdapter(list.posisi, this)
                        listItemAdapter.notifyDataSetChanged()
                        rvPickTicket.adapter = listItemAdapter
                    }
                    else{
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

    private fun PayMent(dataTiket: DataItemPickup) {
        FireBaseRepo().getCheckTiket(
            dataTiket!!.id,tanggal,
            nomor,
            object : CheckTiket {
                override fun Gettiket(isKosong: Boolean, nomor: String) {
                    if (isKosong == true) {
                        pay()
                        showMidtrans()
                    } else {
                        Toast.makeText(
                            this@PickTicketActivity,
                            "maaf tiket nomor ${nomor} sudah di pesan oleh orang lain",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    override fun getUserChoice(Choice: Boolean) {
        val dataTiket = DataTiket()
        val Nomor = getSharedPreferences("nomorKursi", Context.MODE_PRIVATE)
        val data = Nomor.getString("nomorKursi", "").toString()
        if (nomor.size == 0) {
            nomor.add(data)
        }
        if (Choice) {
            PayMent(dataTiket!!)
        } else {
            val Nomor = getSharedPreferences("nomorKursi", Context.MODE_PRIVATE)
            val data = Nomor.getString("nomorKursi", "").toString()
            val check = nomor.filter {
                it == data
            }
            Log.d(TAG, "check $check")
            if (check.isEmpty()) {
                nomor.add(data)
            } else {
                Toast.makeText(this, "Nomor Kursi Sudah Anda Pesan", Toast.LENGTH_SHORT).show()
            }
            val adapterAdd = AddTicketAdapter(nomor)
            adapterAdd.notifyDataSetChanged()
            rv_add_ticket.apply {
                layoutManager =
                    LinearLayoutManager(this@PickTicketActivity, RecyclerView.HORIZONTAL, false)
                adapter = adapterAdd
            }


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

                            val data = getDataUser()
                            val tiket = DataTiket()

                            if (tiket != null) {
                                runBlocking {
                                    fungsibaru(nomor, tiket)
                                }
                            }

                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {

                    }
                }
            )
    }

    suspend fun fungsibaru(nomor: MutableList<String>, tiket: DataItemPickup) {
        GlobalScope.launch {
            val data = getDataUser()
            for (i in nomor) {
                tiket.tanggal=tanggal
                FireBaseRepo().postPosisi(namaBus = tiket.id, nomor = i,tanggal = tanggal)
                FireBaseRepo().postPaymentTiket(i, data.email, tiket)
                delay(1000)
            }
        }
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

    private fun TransactionReq(): TransactionRequest {
        val dataTiket = DataTiket()
        val dataUser = getDataUser()
        orderId = System.currentTimeMillis().toString()
        val hargaTotal = (dataTiket?.harga?.toInt())?.times(nomor.size)
        val req =
            TransactionRequest(orderId, (hargaTotal)!!.toDouble())
        val details =
            ItemDetails(dataTiket.nama, (dataTiket.harga).toDouble(), nomor.size, dataTiket.type)
        val arrayDetail: ArrayList<ItemDetails> = arrayListOf()

        arrayDetail.add(details)
        val credit = CreditCard()
        val customerData = CustomerDetails()
        customerData.apply {
            email = dataUser.email
            firstName = dataUser.nama
            lastName = dataUser.nama
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_checkout -> {
                if (nomor.isEmpty()) {
                    Toast.makeText(this, "Anda belum memilih Nomor Kursi", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val data = DataTiket()
                    PayMent(data!!)
                }
            }
            R.id.ed_tanggal -> {
                tanggal()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        tanggal=s.toString()
        nomor.removeAll(nomor)
        Loader(tanggal)
    }


}
