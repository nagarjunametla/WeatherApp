package com.android.weatherapp.city.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.city.model.City
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(val ctx: Context, val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CityAdapter.CityHolder>() {
    private val citiesList: ArrayList<City> = ArrayList()

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.tvCityName.text = citiesList[position].cityName
            itemView.setOnClickListener { onItemClickListener.onItemClick(citiesList[position]) }
            itemView.ivClose.setOnClickListener { onItemClickListener.onRemoveClick(citiesList[position]) }
        }


    }

    fun updateData(cities: List<City>) {
        citiesList.clear()
        citiesList.addAll(cities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        return CityHolder(LayoutInflater.from(ctx).inflate(R.layout.item_city, parent, false))
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(position)
    }

    interface OnItemClickListener {
        fun onItemClick(city: City)
        fun onRemoveClick(city: City)
    }
}