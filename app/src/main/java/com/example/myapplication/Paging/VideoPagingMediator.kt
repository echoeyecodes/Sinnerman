package com.example.myapplication.Paging

import android.content.Context
import android.util.Log
import androidx.paging.*
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalPagingApi::class)
class VideoPagingMediator(private val videoDao: VideosDao, private val context: Context, private val initialLoadSize: Int) : RemoteMediator<Int, VideoResponseBody>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, VideoResponseBody>): MediatorResult {
        val position = state.anchorPosition ?: initialLoadSize
        return try {

            val roomDao = PersistenceDatabase.getInstance(context).videoDao()
            val result = videoDao.fetchVideos("5", position.toString())

            val endOfPaginationReached = result.isEmpty()

            withContext(Dispatchers.IO) {
                result.forEach {
                    roomDao.insertVideoAndUsers(it)
                }
            }

            MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached
            )

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}