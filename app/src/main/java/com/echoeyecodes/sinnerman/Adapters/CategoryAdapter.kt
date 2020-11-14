package com.echoeyecodes.sinnerman.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.R

class CategoryAdapter(private val  context:Context, private val selectedPosition: Int, private val mainActivityContext: MainActivityContext) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_bottom_sheet_list_dialog_item, parent, false)
        return CategoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        if(position == 0){
            holder.textView.text = "GAMING"
            Glide.with(context).load(ResourcesCompat.getDrawable(context.resources, R.drawable.octane, null)).into(holder.imageView);
        }else{
            holder.textView.text = "MOVIES"
            Glide.with(context).load(ResourcesCompat.getDrawable(context.resources, R.drawable.sheldon, null)).into(holder.imageView);
        }

        holder.radioButton.isChecked = selectedPosition == position

        holder.linearLayout.setOnClickListener { mainActivityContext.onCategorySelected(position) }
    }

    override fun getItemCount(): Int {
        return 2
    }

    inner class CategoryViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val linearLayout : LinearLayout = view.findViewById(R.id.category_item)
        val textView : TextView= view.findViewById(R.id.category_item_text)
        val imageView : ImageView= view.findViewById(R.id.category_item_image)
        val radioButton : RadioButton = view.findViewById(R.id.category_item_checkbox)
    }

}
