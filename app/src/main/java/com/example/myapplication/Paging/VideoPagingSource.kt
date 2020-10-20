package com.example.myapplication.Paging

import android.util.Log
import androidx.paging.PagingSource
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Room.Dao.VideoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class VideoPagingSource(private val videoDao: VideoDao) : PagingSource<Int, VideoResponseBody>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoResponseBody> {
        val position = params.key ?: 0

        return try {

            var result: List<VideoResponseBody>

            withContext(Dispatchers.IO) {
                result = videoDao.getVideos(position, 5)
            }

            LoadResult.Page(
                    data = result,
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = if (result.isEmpty()) null else position + result.size
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

}