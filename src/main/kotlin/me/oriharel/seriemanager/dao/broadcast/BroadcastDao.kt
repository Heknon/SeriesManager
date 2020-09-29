package me.oriharel.seriemanager.dao.broadcast

import me.oriharel.seriemanager.model.content.SerializedBroadcast
import org.json.JSONObject
import java.util.*

interface BroadcastDao {
    fun getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<JSONObject>>
    fun getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<JSONObject>
    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): JSONObject
}