package com.example.busticketactivity.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Telephony.BaseMmsColumns.TRANSACTION_ID
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.R
import com.example.busticketactivity.dataclass.UserData
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.*
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.*
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_payment.*
import java.util.*


class PaymentActivity : AppCompatActivity(),TransactionFinishedCallback, View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        pay()
        showMidtrans()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.actionButton->showMidtrans()
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
        val userdata: UserData =UserData(nama = "reizha",email = "reizha77@gmail.com",nomorTelfon ="085155208046",UserId = "reizha88")
        val data=com.midtrans.sdk.corekit.models.CustomerDetails()
        data.firstName=userdata.nama
        data.lastName=userdata.nama
        data.email=userdata.email
        data.phone=userdata.nomorTelfon
        return data
    }
    @SuppressLint("WrongConstant")
    private fun TransactionReq():TransactionRequest{
        val id="ticekt"
        val price=20000.0
        val quantity=1
        val itemName="harga"
        val req=TransactionRequest("${System.currentTimeMillis()} ",20000.0)
        req.customerDetails=data1()
        val details=ItemDetails(id,price,quantity,itemName)
        val arrayDetail:ArrayList<ItemDetails> = arrayListOf<ItemDetails>()
        arrayDetail.add(details)
        req.itemDetails=arrayDetail
        val credit=CreditCard()
        credit.isSaveCard=false
        credit.authentication=CreditCard.AUTHENTICATION_TYPE_RBA
        credit.bank=BankType.BCA
        req.creditCard=credit
        return req
    }
//    private fun dataobject(){
//        var userDetail = LocalDataHandler.readObject(
//            "user_details",
//            UserDetail::class.java
//        )
//        if (userDetail == null) {
//            userDetail = UserDetail()
//            userDetail.userFullName = "Budi Utomo"
//            userDetail.email = "budi@utomo.com"
//            userDetail.phoneNumber = "08123456789"
//            // set user ID as identifier of saved card (can be anything as long as unique),
//            // randomly generated by SDK if not supplied
//            userDetail.userId = "budi-6789"
//            val userAddresses = ArrayList<UserAddress>()
//            val userAddress = UserAddress()
//            userAddress.address = "Jalan Andalas Gang Sebelah No. 1"
//            userAddress.city = "Jakarta"
//            userAddress.addressType = Constants.ADDRESS_TYPE_BOTH
//            userAddress.zipcode = "12345"
//            userAddress.country = "IDN"
//            userAddresses.add(userAddress)
//            userDetail.userAddresses = userAddresses
//            LocalDataHandler.saveObject("user_details", userDetail)
//        }
//        val transactionRequest =
//            TransactionRequest(TRANSACTION_ID, 10000.0)
//        val itemDetails1 =
//            com.midtrans.sdk.corekit.models.ItemDetails(
//                "reizha",
//                1000.0,
//                1,
//                "tickter"
//            )
//
//
//// Create array list and add above item details in it and then set it to transaction request.
//
//// Create array list and add above item details in it and then set it to transaction request.
//        val itemDetailsList: ArrayList<com.midtrans.sdk.corekit.models.ItemDetails> =
//            ArrayList()
//        itemDetailsList.add(itemDetails1)
//
//// Set item details into the transaction request.
//
//// Set item details into the transaction request.
//        transactionRequest.itemDetails=itemDetailsList
//    val billInfoModel = BillInfoModel("BILL_INFO_KEY", "10000")
//// Set the bill info on transaction details
//// Set the bill info on transaction details
//    transactionRequest.billInfoModel = billInfoModel
//    MidtransSDK.getInstance().transactionRequest=transactionRequest
//    val creditCardOptions = CreditCard()
//// Set to true if you want to save card to Snap
//// Set to true if you want to save card to Snap
//    creditCardOptions.isSaveCard = false
//// Set to true to save card token as `one click` token
//// Set to true to save card token as `one click` token
//    creditCardOptions.isSecure
//// Set bank name when using MIGS channel
//// Set bank name when using MIGS channel
//    creditCardOptions.bank = BankType.BCA
//// Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
//// Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
//    creditCardOptions.channel = CreditCard.MIGS
//// Set Credit Card Options
//// Set Credit Card Options
//    transactionRequest.creditCard = creditCardOptions
//// Set transaction request into SDK instance
//// Set transaction request into SDK instance
//    MidtransSDK.getInstance().transactionRequest = transactionRequest
//    MidtransSDK.getInstance().startPaymentUiFlow(this);
//    }
}