package com.echoeyecodes.sinnerman.Fragments

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.R
import com.google.android.material.button.MaterialButton

class PromotionBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivityContext: MainActivityContext
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_promotion_bottom_sheet, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityContext = context as MainActivityContext
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button : MaterialButton = view.findViewById(R.id.reach_out_btn)
        button.setOnClickListener{
            mainActivityContext.openExternalLink("https://wa.link/4kgo0h")
            dismiss()
        }
    }


    companion object {

        fun newInstance(): PromotionBottomSheet = PromotionBottomSheet()

    }
}