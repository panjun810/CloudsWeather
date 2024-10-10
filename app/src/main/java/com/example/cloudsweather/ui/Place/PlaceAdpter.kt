package com.example.cloudsweather.ui.Place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudsweather.R
import com.example.cloudsweather.logic.Model.Place
import com.example.cloudsweather.ui.Weather.WeatherActivity
import com.example.cloudsweather.utils.LogUtil
import kotlinx.android.synthetic.main.activity_weather.*

class PlaceAdpter(private val fragment:PlaceFragment,private val placeList:List<Place>) : RecyclerView.Adapter<PlaceAdpter.ViewHolder>() {

    inner class ViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        val placeName:TextView = view.findViewById(R.id.placeName)
        val placeAddress:TextView = view.findViewById(R.id.placeAddress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val vh = ViewHolder(view)
        vh.itemView.setOnClickListener {
            val position = vh.adapterPosition
            val place = placeList[position]

            //当在WeatherActivity时候，仅需关闭滑动菜单，赋值再刷新即可
            val activity = fragment.activity
            if(activity is WeatherActivity){
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()

            }else{
                val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                    putExtra("location_lng",place.location.lng)
                    putExtra("location_lat",place.location.lat)
                    putExtra("place_name",place.name)

                    LogUtil.d("loaction_lng",place.location.lng)
                    LogUtil.d("loaction_lat",place.location.lat)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList.get(position)
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}