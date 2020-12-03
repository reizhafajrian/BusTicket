package com.example.busticketactivity.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.busticketactivity.R


import android.content.Context
import android.graphics.Typeface

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*


class BottomSheet(private val listener: BottomSheetItemListener) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container)
      }
//
//    private var title = ""
//    private var subTitle = ""
//    private var subText = ""
//    private var color = 0
//    private var textYesBtn = ""
//    private var textNoBtn = ""
//
//    fun setTitle(text: String) {
//        title = text
//    }
//
//    fun setSubTitle(text: String, subText: String = "") {
//        subTitle = text
//        this.subText = subText
//    }
//
//    fun setSubTitleTextColor(color: Int) {
//        this.color = color
//    }
//
//    fun setTextYesBtn(text: String) {
//        textYesBtn = text
//    }
//
//    fun setTextNoBtn(text: String) {
//        textNoBtn = text
//    }

//    private fun setSubtitleTextColor(
//        tv: TextView,
//        fulltext: String,
//        subText: String,
//        color: Int,
//        context: Context?
//    ) {
//        tv.setText(fulltext, TextView.BufferType.SPANNABLE)
//        val text = tv.text as Spannable
//        val index = fulltext.indexOf(subText)
//        if (subText.isNotEmpty().and(color == 0)) {
//            text.setSpan(
//                ForegroundColorSpan(context?.let {
//                    ContextCompat.getColor(
//                        it,
//                        R.color.colorPrimary
//                    )
//                } ?: 0),
//                index,
//                index + subText.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        } else if (subText.isNotEmpty()) {
//            text.setSpan(
//                StyleSpan(Typeface.BOLD),
//                index,
//                index + subText.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_no.setOnClickListener {
            dismiss()
        }
        btn_yes.setOnClickListener {
            listener.getUserChoice(true)
            dismiss()
        }
    }

}
