package com.safi.weather.network.core

import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class NetworkBoundResource @Inject constructor() {

    suspend fun<ResultType> downloadData(api : suspend () -> Response<ResultType>) : Flow<ApiResponse<ResultType>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(ApiResponse.Progress(true))
                val response:Response<ResultType> = api()
                emit(ApiResponse.Progress(false))
                try {
                    if (response.isSuccessful){
                        response.body()?.let {
                            emit(ApiResponse.Success(data = it))
                        }?: emit(ApiResponse.Failure(message = "Unknown error occurred", code = 0))
                    }else{
                        emit(ApiResponse.Failure(message = parserErrorBody(response.errorBody()), code = response.code()))
                    }
                }catch (e:Exception){
                    emit(ApiResponse.Failure(message = message(e), code = code(e)))
                }
            }
        }
    }

    private fun code(throwable: Throwable?):Int{
        return if (throwable is HttpException) (throwable).code()
        else  0
    }

    private fun parserErrorBody(response: ResponseBody?):String{
        return response?.let {
            val errorMessage = JsonParser.parseString(it.string()).asJsonObject["message"].asString
            errorMessage.ifEmpty { "Whoops! Something went wrong" }
            errorMessage
        }?:"Unknown error occur, please try again"
    }

    private fun message(throwable: Throwable?): String {
        when (throwable) {
            is SocketTimeoutException -> return "Whoops! connection time out, try again!"
            is IOException -> return "No internet connection, try again!"
            is HttpException -> return try {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                val errorMessage =
                    JsonParser.parseString(errorJsonString).asJsonObject["message"].asString
                errorMessage.ifEmpty { "Whoops! Something went wrong" }
            } catch (e: Exception) {
                "Unknown error occur, please try again!"
            }
        }
        return "Unknown error occur, please try again!"
    }
}