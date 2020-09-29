package me.oriharel.seriemanager.service

import me.oriharel.seriemanager.dao.broadcast.BroadcastDao
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class BroadcastService @Autowired constructor(@Qualifier("broadcastDao") private val broadcastDao: BroadcastDao) {
    fun getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<JSONObject>> {
        return broadcastDao.getDetailedBroadcasts(*serializedBroadcast)
    }

    fun getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<JSONObject> {
        return broadcastDao.getDetailedBroadcast(serializedBroadcast)
    }

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): JSONObject {
        return broadcastDao.findBroadcasts(searchType, query, page, adult)
    }
}