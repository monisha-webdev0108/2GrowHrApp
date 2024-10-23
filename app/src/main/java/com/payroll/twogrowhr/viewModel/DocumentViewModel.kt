package com.payroll.twogrowhr.viewModel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.DegreeDetailsData
import com.payroll.twogrowhr.Model.ResponseModel.DocumentDetailsData
import com.payroll.twogrowhr.Model.ResponseModel.DocumentListData
import com.payroll.twogrowhr.Model.ResponseModel.EducationalDocumentData
import com.payroll.twogrowhr.Model.ResponseModel.ExperienceDocumentData
import com.payroll.twogrowhr.Model.ResponseModel.OtherDocumentData
import com.payroll.twogrowhr.Model.View.fileName
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class DocumentViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    val empID = userViewModel.getSFCode()



    sealed class PostDocumentResult {
        object Success :  PostDocumentResult()
        data class Failure(val message: String) : PostDocumentResult()
    }


//------------------------------------------GET DOCUMENT COUNT DETAILS--------------------------------------------------//


    // LIST OF DOCUMENT DETAILS


    val documentList = savedStateHandle.getStateFlow("documentList", emptyList<DocumentListData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getDocumentList(navController: NavController, context: Context, empID : String) = viewModelScope.launch {

//        val empId = userViewModel.getSFCode()

        Log.d("DocumentViewModel", "qwerty : Inside getDocumentList, empID : $empID")


        when (val response = repository.getDocumentListDetailsResponse(context = context, empID = empID)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {

                loadingStatus = false


                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["documentList"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getDocumentList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["documentList"] = emptyList<DocumentListData>()
                        Log.d("DocumentViewModel", "qwerty : getDocumentList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        Log.d("DocumentViewModel", "qwerty : getDocumentList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["documentList"] = emptyList<DocumentListData>()
                        Log.d("DocumentViewModel", "qwerty : getDocumentList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

    }

//------------------------------------------GET DOCUMENT DETAILS--------------------------------------------------//


    val documentDetails = savedStateHandle.getStateFlow("documentDetails", emptyList<DocumentDetailsData>())

    var loadingStatus1 = savedStateHandle.get<Boolean>("loadingStatus1") ?: false

    var flag1 = savedStateHandle.get<Int>("flag1") ?: 0

    fun getDocumentDataDetails(navController: NavController, context: Context, empID : String, type : String) = viewModelScope.launch {

//        val empId = userViewModel.getSFCode()

        Log.d("DocumentViewModel", "qwerty : Inside getDocumentList, empID : $empID")


        when (val response = repository.getDocumentDetailsResponse(context = context, empID = empID, type = type)) {
            is Resource.Loading -> {
                loadingStatus1 = true
                flag1 = 0
            }

            is Resource.Success -> {

                loadingStatus1 = false

                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        savedStateHandle["documentDetails"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getDocumentDataDetails API call successful/true. ${data.data} ")
                        flag1 = 1
                    } else {
                        savedStateHandle["documentDetails"] = emptyList<DocumentDetailsData>()
                        Log.d("DocumentViewModel", "qwerty : getDocumentDataDetails API call successful/false. ${data?.data} ")
                        flag1 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus1 = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("My_document")

                        }
                        Log.d("DocumentViewModel", "qwerty : getDocumentDataDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["documentDetails"] = emptyList<DocumentDetailsData>()
                        Log.d("DocumentViewModel", "qwerty : getDocumentDataDetails API call successful/ Data Not Found ")
                    }
                }
                flag1 = 2
                return@launch
            }
        }

    }



//------------------------------------------UPLOAD FILE--------------------------------------------------//



    // Function to save the selected file with a common path and retain its original file name
     private suspend fun saveFileWithOriginalName(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            var savedFile: File? = null

            try {
                val resolver: ContentResolver = context.contentResolver
                val cursorOut = resolver.query(uri, null, null, null, null)
                cursorOut?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val originalFileName = cursor.getString(nameIndex)

                    // Generate new filename with date and time
                    val date = SimpleDateFormat("yyyy-dd-M--HH-mm-ss_", Locale.getDefault()).format(Date()) //yyyy-dd-M--HH-mm-ss_
                    fileName.value = "$date$originalFileName"

                    val outputPath = File(context.filesDir, fileName.value)
                    inputStream = resolver.openInputStream(uri)
                    outputStream = FileOutputStream(outputPath)
                    inputStream?.copyTo(outputStream!!)
                    savedFile = outputPath
                }
            } catch (e: Exception) {
                Log.e("SaveFileException", "Error saving file: ${e.message}", e)
            } finally {
                inputStream?.close()
                outputStream?.close()
            }

            Log.e("SaveFile", "Saving file name: $savedFile" )

            savedFile

        }
    }



    var flag2 = savedStateHandle.get<Boolean>("flag2") ?: false



    fun uploadFile(context: Context, fileUri: Uri, navController: NavController, accountNumber: String, name: String, fatherName: String, dob: String, address: String, expiresOn: String, dateOfIssue: String, placeOfIssue: String, placeOfBirth: String, session: String, org: String, filePath: String, type: String, empId: String) = viewModelScope.launch {

        try {

            Log.d("DocumentViewModel... ", "inside uploadImage : $fileUri")

            val imageUri = saveFileWithOriginalName(context, fileUri)

            Log.e("DocumentViewModel", "Saving file name: $imageUri" )

            when (val response = repository.postDocumentUploadResponse(context = context, docs = imageUri.toString())) {

                is Resource.Loading -> {
                    statusLoading.value = true
                }

                is Resource.Success -> {
                    statusLoading.value = false

                    val data = response.data

                    val message = data?.message
                    if (message == "File uploaded successfully")
                    {
                        statusLoading.value = false
                        flag2 = true

                        Log.e("DocumentViewModel", "Saving file name: File uploaded successfully" )


                        postDocumentDetails(navController, context, accountNumber, name, fatherName, dob, address, expiresOn, dateOfIssue, placeOfIssue, placeOfBirth, session, org, fileName.value, type, empId).let { result ->

                            when (result) {
                                is PostDocumentResult.Success -> {
                                    Constant.showToast(context, "Submitted Successfully")
                                    navController.navigate("My_document")
                                }

                                is PostDocumentResult.Failure -> {
                                    Constant.showToast(context, "Please try again later.....")
                                    navController.navigate("My_document")
                                }
                            }
                        }

//                        postDocumentDetails(navController, context, accountNumber, name, fatherName, dob, address, expiresOn, dateOfIssue, placeOfIssue, placeOfBirth, session, org, fileName.value, type, empId)
                    }
                    else
                    {
                        Log.e("DocumentViewModel", "Saving file name: File is not uploaded successfully" )


                        filePathClearance1()
                        flag2 = false
                        if (message != null)
                        {
                            Constant.showToast(context, message)
                        }
                    }
                }

                is Resource.Error -> {
                    flag2 = false
                    statusLoading.value = false
                    filePathClearance1()
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            filePathClearance1()
            flag2 = false
            e.printStackTrace()
            Log.d("DocumentViewModel", "catch : uploadFile API call was not successful.")
            Log.d("DocumentViewModel... ", "uploadFile... : $e")
            statusLoading.value = false
            Constant.showToast(context, "Something went wrong....")
        }
    }






//------------------------------------------POST DOCUMENT DETAILS--------------------------------------------------//




    suspend fun postDocumentDetails( navController: NavController, context: Context, accountNumber: String, name: String, fatherName: String, dob: String, address: String, expiresOn: String, dateOfIssue: String, placeOfIssue: String, placeOfBirth: String, session: String, org: String, filePath: String, type: String, empId: String): PostDocumentResult
    {
        try {
            Log.d("DocumentViewModel", "accountNumber : $accountNumber, name : $name, fatherName : $fatherName, dob : $dob, address : $address, expiresOn : $expiresOn, dateOfIssue : $dateOfIssue, placeOfIssue : $placeOfIssue, placeOfBirth : $placeOfBirth, session : $session, org : $org, filePath : $filePath, type : $type, empId : $empId")

            when(val response = repository.postDocumentUploadDetailsResponse(context = context, accountNumber = accountNumber, name = name, fatherName = fatherName, dob = dob, address = address, expiresOn = expiresOn, dateOfIssue = dateOfIssue, placeOfIssue = placeOfIssue, placeOfBirth = placeOfBirth, session = session, org = org, filePath = filePath, type = type, empId = empId)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/true : ${data.success}")
                            return PostDocumentResult.Success
                        }
                        else
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/false : ${data?.success}")
                            return PostDocumentResult.Failure("Failed to post  Work From Home  details")
                        }
                    }

                }
                is Resource.Error->{
                    statusLoading.value = false

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/My_document")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was not successful")
                    return PostDocumentResult.Failure("Null response")
                }
            }

            return PostDocumentResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostDocumentResult.Failure("An error occurred: ${e.message}")
        }
    }


//------------------------------------------GET EDUCATION DOCUMENT DETAILS--------------------------------------------------//



    val educationDocumentDetails = savedStateHandle.getStateFlow("educationDocumentDetails", emptyList<EducationalDocumentData>())

    var loadingStatus3 = savedStateHandle.get<Boolean>("loadingStatus3") ?: false

    var flag3 = savedStateHandle.get<Int>("flag3") ?: 0

    fun getEducationDocumentDataDetails(navController: NavController, context: Context, empID : String, type : String) = viewModelScope.launch {


        Log.d("DocumentViewModel", "qwerty : Inside getEducationDocumentDataDetails, empID : $empID")


        when (val response = repository.getEducationDocumentDetailsResponse(context = context, empID = empID, type = type)) {
            is Resource.Loading -> {
                loadingStatus3 = true
                flag3 = 0
            }

            is Resource.Success -> {

                loadingStatus3 = false

                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        savedStateHandle["educationDocumentDetails"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getEducationDocumentDataDetails API call successful/true. ${data.data} ")
                        flag3 = 1
                    } else {
                        savedStateHandle["educationDocumentDetails"] = emptyList<EducationalDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getEducationDocumentDataDetails API call successful/false. ${data?.data} ")
                        flag3 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus3 = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("My_document")

                        }
                        Log.d("DocumentViewModel", "qwerty : getEducationDocumentDataDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["documentDetails"] = emptyList<EducationalDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getEducationDocumentDataDetails API call successful/ Data Not Found ")
                    }
                }
                flag3 = 2
                return@launch
            }
        }

    }



//------------------------------------------GET EXPERIENCE DOCUMENT DETAILS--------------------------------------------------//



    val experienceDocumentDetails = savedStateHandle.getStateFlow("experienceDocumentDetails", emptyList<ExperienceDocumentData>())

    var loadingStatus4 = savedStateHandle.get<Boolean>("loadingStatus4") ?: false

    var flag4 = savedStateHandle.get<Int>("flag4") ?: 0

    fun getExperienceDocumentDataDetails(navController: NavController, context: Context, empID : String, type : String) = viewModelScope.launch {


        Log.d("DocumentViewModel", "qwerty : Inside getExperienceDocumentDataDetails, empID : $empID")


        when (val response = repository.getExperienceDocumentDetailsResponse(context = context, empID = empID, type = type)) {
            is Resource.Loading -> {
                loadingStatus4 = true
                flag4 = 0
            }

            is Resource.Success -> {

                loadingStatus4 = false

                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        savedStateHandle["experienceDocumentDetails"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getExperienceDocumentDataDetails API call successful/true. ${data.data} ")
                        flag4 = 1
                    } else {
                        savedStateHandle["experienceDocumentDetails"] = emptyList<ExperienceDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getExperienceDocumentDataDetails API call successful/false. ${data?.data} ")
                        flag4 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus4 = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("My_document")

                        }
                        Log.d("DocumentViewModel", "qwerty : getExperienceDocumentDataDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["experienceDocumentDetails"] = emptyList<ExperienceDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getExperienceDocumentDataDetails API call successful/ Data Not Found ")
                    }
                }
                flag4 = 2
                return@launch
            }
        }

    }


//------------------------------------------GET OTHER DOCUMENT DETAILS--------------------------------------------------//



    val otherDocumentDetails = savedStateHandle.getStateFlow("otherDocumentDetails", emptyList<OtherDocumentData>())

    var loadingStatus5 = savedStateHandle.get<Boolean>("loadingStatus5") ?: false

    var flag5 = savedStateHandle.get<Int>("flag5") ?: 0

    fun getOtherDocumentDataDetails(navController: NavController, context: Context, empID : String, type : String) = viewModelScope.launch {


        Log.d("DocumentViewModel", "qwerty : Inside getOtherDocumentDataDetails, empID : $empID")


        when (val response = repository.getOtherDocumentDetailsResponse(context = context, empID = empID, type = type)) {
            is Resource.Loading -> {
                loadingStatus5 = true
                flag5 = 0
            }

            is Resource.Success -> {

                loadingStatus5 = false

                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        savedStateHandle["otherDocumentDetails"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getOtherDocumentDataDetails API call successful/true. ${data.data} ")
                        flag5 = 1
                    } else {
                        savedStateHandle["otherDocumentDetails"] = emptyList<OtherDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getOtherDocumentDataDetails API call successful/false. ${data?.data} ")
                        flag5 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus5 = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("My_document")

                        }
                        Log.d("DocumentViewModel", "qwerty : getOtherDocumentDataDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["otherDocumentDetails"] = emptyList<OtherDocumentData>()
                        Log.d("DocumentViewModel", "qwerty : getOtherDocumentDataDetails API call successful/ Data Not Found ")
                    }
                }
                flag5 = 2
                return@launch
            }
        }

    }


//------------------------------------------UPLOAD DOCUMENT DETAILS - PART 2--------------------------------------------------//




    var flagUploadFile1 = savedStateHandle.get<Boolean>("flagUploadFile1") ?: false



    fun uploadFile1(context: Context, fileUri: Uri, navController: NavController, degree_CompanyName: String, experienceYears : String, qualification_DesignationName: String, location: String, doj: String, doc_dor: String, universityName: String, documentName: String, filePath: String, type: String, empId: String) = viewModelScope.launch {

        try {

            Log.d("DocumentViewModel... ", "inside uploadImage : $fileUri")

            val imageUri = saveFileWithOriginalName(context, fileUri)

            Log.e("DocumentViewModel", "Saving file name: $imageUri" )

            when (val response = repository.postDocumentUploadResponse(context = context, docs = imageUri.toString())) {

                is Resource.Loading -> {
                    statusLoading.value = true
                }

                is Resource.Success -> {
                    statusLoading.value = false

                    val data = response.data

                    val message = data?.message
                    if (message == "File uploaded successfully")
                    {
                        statusLoading.value = false
                        flagUploadFile1 = true

                        Log.e("DocumentViewModel", "Saving file name: File uploaded successfully" )


                        when(type)
                        {
                            "EDU" -> postEducationDocumentDetails(navController, context, empId = empId, degree = degree_CompanyName, branch = qualification_DesignationName, doj = doj, doc = doc_dor, university = universityName, location = location, filePath = fileName.value).let { result ->

                                when (result) {
                                    is PostDocumentResult.Success -> {
                                        Constant.showToast(context, "Submitted Successfully")
                                        filePathClearance1()
                                        navController.navigate("${Screen.EducationDocList.route}/${type}")
                                    }

                                    is PostDocumentResult.Failure -> {
                                        Constant.showToast(context, "Please try again later.....")
                                        filePathClearance1()
                                        navController.navigate("${Screen.EducationDocList.route}/${type}")
                                    }
                                }
                            }

                            "EXP" -> postExperienceDocumentDetails(navController, context, empId = empId, companyName = degree_CompanyName, experienceYears = experienceYears, jobTitle = qualification_DesignationName, doj = doj, dor = doc_dor, location = location, filePath = fileName.value).let { result ->

                                when (result) {
                                    is PostDocumentResult.Success -> {
                                        Constant.showToast(context, "Submitted Successfully")
                                        filePathClearance1()
                                        navController.navigate("${Screen.ExperienceDocumentList.route}/${type}")
                                    }

                                    is PostDocumentResult.Failure -> {
                                        Constant.showToast(context, "Please try again later.....")
                                        filePathClearance1()
                                        navController.navigate("${Screen.ExperienceDocumentList.route}/${type}")
                                    }
                                }
                            }

                            "OD" -> postOtherDocumentDetails(navController, context, empId = empId, documentName = documentName, fileName = fileName.value).let { result ->

                                when (result) {
                                    is PostDocumentResult.Success -> {
                                        Constant.showToast(context, "Submitted Successfully")
                                        filePathClearance1()
                                        navController.navigate("${Screen.OtherDocList.route}/${type}")
                                    }

                                    is PostDocumentResult.Failure -> {
                                        Constant.showToast(context, "Please try again later.....")
                                        filePathClearance1()
                                        navController.navigate("${Screen.OtherDocList.route}/${type}")
                                    }
                                }
                            }

                        }

                    }
                    else
                    {
                        Log.e("DocumentViewModel", "Saving file name: File is not uploaded successfully" )


                        filePathClearance1()
                        flagUploadFile1 = false
                        if (message != null)
                        {
                            Constant.showToast(context, message)
                        }
                    }
                }

                is Resource.Error -> {
                    flagUploadFile1 = false
                    statusLoading.value = false
                    filePathClearance1()
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            filePathClearance1()
            flagUploadFile1 = false
            e.printStackTrace()
            Log.d("DocumentViewModel", "catch : uploadFile1 API call was not successful.")
            Log.d("DocumentViewModel... ", "uploadFile1... : $e")
            statusLoading.value = false
            Constant.showToast(context, "Something went wrong....")
        }
    }

//------------------------------------------POST EDUCATION DOCUMENT DETAILS--------------------------------------------------//




    private suspend fun postEducationDocumentDetails(navController: NavController, context: Context, empId : String, degree: String, branch: String, doj: String, doc: String, university: String, location: String, filePath: String): PostDocumentResult
    {
        try {
            Log.d("DocumentViewModel", "empId : $empId, degree : $degree, branch :$branch, doj :$doj, doc :$doc, university : $university, location :$location, filePath :$filePath")

            when(val response = repository.postEducationDocumentUploadDetailsResponse(context = context, empId = empId, degree = degree, branch = branch, doj = doj, doc = doc, university = university, location = location, filePath = filePath)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("DocumentViewModel", "qwerty : postEducationDocumentDetails API call was successful/true : ${data.success}")
                            return PostDocumentResult.Success
                        }
                        else
                        {
                            Log.d("DocumentViewModel", "qwerty : postEducationDocumentDetails API call was successful/false : ${data?.success}")
                            return PostDocumentResult.Failure("Failed to post  Work From Home  details")
                        }
                    }

                }
                is Resource.Error->{
                    statusLoading.value = false

                    filePathClearance1()

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/My_document")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("DocumentViewModel", "qwerty : postEducationDocumentDetails API call was not successful")
                    return PostDocumentResult.Failure("Null response")
                }
            }

            return PostDocumentResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostDocumentResult.Failure("An error occurred: ${e.message}")
        }
    }

//------------------------------------------POST EXPERIENCE DOCUMENT DETAILS--------------------------------------------------//


    suspend fun postExperienceDocumentDetails( navController: NavController, context: Context, empId: String, companyName: String, experienceYears: String, jobTitle: String, doj: String, dor: String, location: String, filePath: String): PostDocumentResult
    {
        try {
            Log.d("DocumentViewModel", "empId : $empId, companyName : $companyName, experienceYears : $experienceYears, jobTitle : $jobTitle, doj : $doj, dor :$dor, location : $location, filePath : $filePath")

            when(val response = repository.postExperienceDocumentUploadDetailsResponse(context = context, empId = empId, companyName = companyName, experienceYears = experienceYears, jobTitle = jobTitle, doj = doj, dor = dor, location = location, filePath = filePath)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/true : ${data.success}")
                            return PostDocumentResult.Success
                        }
                        else
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/false : ${data?.success}")
                            return PostDocumentResult.Failure("Failed to post  Work From Home  details")
                        }
                    }

                }
                is Resource.Error->{
                    statusLoading.value = false

                    filePathClearance1()

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/My_document")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was not successful")
                    return PostDocumentResult.Failure("Null response")
                }
            }

            return PostDocumentResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostDocumentResult.Failure("An error occurred: ${e.message}")
        }
    }

//------------------------------------------POST OTHER DOCUMENT DETAILS--------------------------------------------------//


    suspend fun postOtherDocumentDetails( navController: NavController, context: Context,  empId: String, documentName: String, fileName: String): PostDocumentResult
    {
        try {
            Log.d("DocumentViewModel", "empId : $empId, documentName : $documentName, fileName : $fileName")

            when(val response = repository.postOtherDocumentUploadDetailsResponse(context = context, empId = empId, documentName = documentName, fileName = fileName)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/true : ${data.success}")
                            return PostDocumentResult.Success
                        }
                        else
                        {
                            Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was successful/false : ${data?.success}")
                            return PostDocumentResult.Failure("Failed to post  Work From Home  details")
                        }
                    }

                }
                is Resource.Error->{
                    statusLoading.value = false

                    filePathClearance1()

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/My_document")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("DocumentViewModel", "qwerty : postDocumentDetails API call was not successful")
                    return PostDocumentResult.Failure("Null response")
                }
            }

            return PostDocumentResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostDocumentResult.Failure("An error occurred: ${e.message}")
        }
    }


//------------------------------------------GET OTHER DOCUMENT DETAILS--------------------------------------------------//



    val degreeDetails = savedStateHandle.getStateFlow("degreeDetails", emptyList<DegreeDetailsData>())

    var loadingStatus6 = savedStateHandle.get<Boolean>("loadingStatus6") ?: false

    var flag6 = savedStateHandle.get<Int>("flag6") ?: 0

    fun getDegreeDetails(navController: NavController, context: Context, empID : String, type : String) = viewModelScope.launch {


        Log.d("DocumentViewModel", "qwerty : Inside getDegreeDetails, empID : $empID")


        when (val response = repository.getDegreeDetailsResponse(context = context)) {
            is Resource.Loading -> {
                loadingStatus6 = true
                flag6 = 0
            }

            is Resource.Success -> {

                loadingStatus6 = false

                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        savedStateHandle["degreeDetails"] = data.data
                        Log.d("DocumentViewModel", "qwerty : getDegreeDetails API call successful/true. ${data.data} ")
                        flag6 = 1
                    } else {
                        savedStateHandle["degreeDetails"] = emptyList<DegreeDetailsData>()
                        Log.d("DocumentViewModel", "qwerty : getDegreeDetails API call successful/false. ${data?.data} ")
                        flag6 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus6 = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/My_document")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("My_document")

                        }
                        Log.d("DocumentViewModel", "qwerty : getDegreeDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["degreeDetails"] = emptyList<DegreeDetailsData>()
                        Log.d("DocumentViewModel", "qwerty : getDegreeDetails API call successful/ Data Not Found ")
                    }
                }
                flag6 = 2
                return@launch
            }
        }

    }

}