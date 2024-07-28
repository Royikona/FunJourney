package com.zenicore.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VoucherAdapter(private val vouchers: List<Voucher>) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    class VoucherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.voucherType)
        val amountTextView: TextView = itemView.findViewById(R.id.voucherAmount)
        val claimButton: Button = itemView.findViewById(R.id.btnClaim)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voucher, parent, false)
        return VoucherViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        val voucher = vouchers[position]
        holder.typeTextView.text = voucher.type
        holder.amountTextView.text = voucher.amount
        holder.claimButton.setOnClickListener {
            // Handle claim button click
        }
    }

    override fun getItemCount() = vouchers.size
}
