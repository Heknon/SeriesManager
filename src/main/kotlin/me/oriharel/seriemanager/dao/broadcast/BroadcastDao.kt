package me.oriharel.seriemanager.dao.broadcast

import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import java.util.*

interface BroadcastDao {
    fun <T> getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<T>>
    fun <T> getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<T>
    fun <T : Broadcast> findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<T?>
}