package com.payroll.twogrowhr.repository


import com.payroll.twogrowhr.di.NoConnectivityException
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
//https://www.geeksforgeeks.org/how-to-handle-api-responses-success-error-in-android/
abstract class BaseRepo {

    // we'll use this function in all
    // repos to handle api errors.
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {

        // Returning api response
        // wrapped in Resource class
        return withContext(Dispatchers.IO) {
            try {

                // Here we are calling api lambda
                // function that will return response
                // wrapped in Retrofit's Response class
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    // In case of success response we
                    // are returning Resource.Success object
                    // by passing our data in it.
                    Resource.Success(data = response.body()!!)
                } else {
                    // parsing api's own custom json error
                    // response in ExampleErrorResponse pojo
//					val errorResponse: ExampleErrorResponse? = convertErrorBody(response.errorBody())
                    // Simply returning api's own failure message
                    Resource.Error(
                        errorMessage = response.message() ?: response.errorBody()?.toString() ?: "Something went wrong"
                    )
                }

            } catch (e: HttpException) {
                // Returning HttpException's message
                // wrapped in Resource.Error
                Resource.Error(errorMessage = e.message ?: "Something went wrong")
            } catch (e: NoConnectivityException) {
                Resource.Error("Please check your network connection")
//            } catch (e: IOException) {
//                // Returning no internet message
//                // wrapped in Resource.Error
//                Resource.Error("Something went wrong")
            } catch (e: Exception) {
                // Returning 'Something went wrong' in case
                // of unknown error wrapped in Resource.Error
                Resource.Error(errorMessage = "Something went wrong ${e.message}")
            }
        }
    }
}
