package com.echoeyecodes.sinnerman.Fragments

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.echoeyecodes.sinnerman.Adapters.CategoryAdapter
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.R

class CategoryBottomSheet(private val position: Int) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivityContext: MainActivityContext
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_category_bottom_sheet_list_dialog, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityContext = context as MainActivityContext
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.category_list)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        val adapter = CategoryAdapter(requireContext(), position, mainActivityContext)
        recyclerView.adapter = adapter

    }


    companion object {

        fun newInstance(position:Int): CategoryBottomSheet = CategoryBottomSheet(position)

    }
}