package me.oriharel.seriemanager.service

import me.oriharel.seriemanager.dao.broadcast.BroadcastDao
import me.oriharel.seriemanager.dao.broadcast.SearchType
import me.oriharel.seriemanager.model.content.Broadcast
import me.oriharel.seriemanager.model.content.SerializedBroadcast
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class BroadcastService @Autowired constructor(@Qualifier("broadcastDao") private val broadcastDao: BroadcastDao) {
    fun <T> getDetailedBroadcasts(vararg serializedBroadcast: SerializedBroadcast): List<Optional<T>> {
        return broadcastDao.getDetailedBroadcasts(*serializedBroadcast)
    }

    fun <T> getDetailedBroadcast(serializedBroadcast: SerializedBroadcast): Optional<T> {
        return broadcastDao.getDetailedBroadcast(serializedBroadcast)
    }

    fun findBroadcasts(searchType: SearchType, query: String, page: Int, adult: Boolean): List<Broadcast?> {
        return broadcastDao.findBroadcasts(searchType, query, page, adult)
    }
}