package com.matheusfroes.testepickfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIntent.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val uris = mutableListOf<Uri>()
        if (data.data != null) {
            uris.add(data.data)
        } else {
            val clipData = data.clipData
            if (clipData != null) {
                val listUris = (0 until clipData.itemCount)
                        .map { clipData.getItemAt(it) }
                        .map { it.uri }
                uris.addAll(listUris)
            }
        }
        val metaData = getFilesMetaData(uris)
        rvAttachments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = AttachmentAdapter(this, metaData)
        rvAttachments.adapter = adapter

        adapter.setOnAttachmentPreviewClickListener(object : AttachmentAdapter.OnAttachmentPreviewClickListener {
            override fun onClick(position: Int) {
                adapter.removerItem(position)
            }
        })
    }

    private fun getFilesMetaData(uris: List<Uri>): MutableList<AttachmentPreview> {
        val attachments = mutableListOf<AttachmentPreview>()

        uris.forEach { uri ->
            val cursor = this.contentResolver
                    .query(uri, null, null, null, null, null)

            cursor.use {
                if (cursor != null && cursor.moveToFirst()) {
                    val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                    val size = if (!cursor.isNull(sizeIndex)) {
                        (cursor.getString(sizeIndex).toDouble() / 1024 / 1024)
                    } else {
                        0.0
                    }

                    attachments.add(AttachmentPreview(displayName, size))
                }
            }
        }

        return attachments
    }


}
