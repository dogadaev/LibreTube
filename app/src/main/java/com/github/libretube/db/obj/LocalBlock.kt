package com.github.libretube.db.obj

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.github.libretube.ui.dialogs.ShareDialog
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "localBlock")
data class LocalBlock(
    @PrimaryKey val channelId: String,
    val name: String = "",
    @Ignore val url: String = "",
) {
    constructor(
        channelId: String,
        name: String,
    ) : this(channelId, name, "${ShareDialog.YOUTUBE_FRONTEND_URL}/channel/$channelId")
}
