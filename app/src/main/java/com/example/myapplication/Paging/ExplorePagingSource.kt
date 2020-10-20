package com.example.myapplication.Paging;

import android.util.Log
import androidx.paging.PagingSource
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.ExploreResponseBody
import retrofit2.HttpException
import java.io.IOException

data class ExplorePagingSource (private val videoDao:VideosDao) : PagingSource<Int, ExploreResponseBody>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExploreResponseBody> {
        val position = params.key ?: 0

        return try {

            val result = videoDao.fetchExplore("5", position.toString())
            if(result.isNotEmpty()){
                Log.d("CARRR", result.toString())
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
