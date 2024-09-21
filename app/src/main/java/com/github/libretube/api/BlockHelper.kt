package com.github.libretube.api


import com.github.libretube.db.DatabaseHolder.Database
import com.github.libretube.db.obj.LocalBlock

object BlockHelper {

    suspend fun block(channelId: String, name: String) {
        Database.localBlockDao().insert(LocalBlock(channelId, name))
    }

    suspend fun unblock(channelId: String) {
        Database.localBlockDao().delete(channelId)
    }

    suspend fun isBlocked(channelId: String): Boolean {
        return Database.localBlockDao().includes(channelId)
    }
}
