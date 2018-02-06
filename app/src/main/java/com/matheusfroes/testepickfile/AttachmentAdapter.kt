package com.matheusfroes.testepickfile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.attachment_view.view.*
import java.util.*

class AttachmentAdapter(
        private val context: Context,
        private val attachments: MutableList<AttachmentPreview>) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    lateinit var listener: OnAttachmentPreviewClickListener

    fun setOnAttachmentPreviewClickListener(listener: OnAttachmentPreviewClickListener) {
        this.listener = listener
    }

    fun removerItem(position: Int) {
        attachments.removeAt(position)
        notifyItemRangeRemoved(position, 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attachment_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = attachments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attachment = attachments[position]

        holder.itemView.tvDisplayName.text = attachment.displayName

        val attachmentSize = "${String.format(Locale("pt", "BR"), "%.2f", attachment.size)}MB"
        holder.itemView.tvSize.text = attachmentSize
    }

    interface OnAttachmentPreviewClickListener {
        fun onClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.ivRemoverAnexo.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}