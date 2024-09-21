package com.github.libretube.ui.extensions

import android.widget.TextView
import androidx.core.view.isVisible
import com.github.libretube.R
import com.github.libretube.api.BlockHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun TextView.setupBlockButton(
    channelId: String?,
    channelName: String,
    isBlocked: Boolean? = null,
    onIsBlockedChange: (Boolean) -> Unit = {}
) {
    if (channelId == null) return

    var blocked = false

    CoroutineScope(Dispatchers.IO).launch {
        blocked = isBlocked ?: BlockHelper.isBlocked(channelId)

        withContext(Dispatchers.Main) {
            if (blocked) this@setupBlockButton.text = context.getString(R.string.unblock)
            this@setupBlockButton.isVisible = true
        }
    }

    setOnClickListener {
        if (blocked) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    BlockHelper.unblock(channelId)
                }

                text = context.getString(R.string.block)

                blocked = false

            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    BlockHelper.block(channelId, channelName)
                }

                text = context.getString(R.string.unblock)

                blocked = true
            }
        }

        onIsBlockedChange(blocked)
    }
}
