package id.myeco.myeco.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.myeco.myeco.databinding.ItemWifiBinding

class ListWifiAdapter(private val wifiList: List<ListWifiModel>, private val listener: OnWifiItemClickListener) :
    RecyclerView.Adapter<ListWifiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWifiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = wifiList[position]
        holder.bind(model, listener)
    }

    override fun getItemCount(): Int = wifiList.size

    inner class ViewHolder(val binding: ItemWifiBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: ListWifiModel, listener: OnWifiItemClickListener){
            binding.tvWifiName.text = model.name
            binding.tvWifiIp.text = model.address
            itemView.setOnClickListener {
                listener.onItemClick(model)
            }
        }
    }

    interface OnWifiItemClickListener{
        fun onItemClick(model: ListWifiModel)
    }
}