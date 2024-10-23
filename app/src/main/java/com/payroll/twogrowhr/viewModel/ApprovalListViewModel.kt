package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AppliedLoanInstalmentData
import com.payroll.twogrowhr.Model.ResponseModel.ApprovalList
import com.payroll.twogrowhr.Model.ResponseModel.EmployeeProfileDetail
import com.payroll.twogrowhr.Model.ResponseModel.LoanApprovalStagesData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveCompoOffData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLoanData
import com.payroll.twogrowhr.Model.View.LoanInstalmentItems
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject


@Parcelize
data class LoanApprovalDataDetails(
    var employeeID: String,
    var employeeCode: String,
    var employeeName: String,
    var loanTypeSlNo: String,
    var loanApplySlNo: String,
    var loanRuleName: String,
    var loanStartDate: String,
    var loanRequestAmount: String,
    var loanPeriod: String,
    var remarks: String,
    var instalmentAmount: String,
    var nameInPayslip: String,
    var employeeRequestAmount: String,
    var deductionType: String,
    var deductionStartMonth: String,
    var numberOfMonths: String,
    var loanSanctionDate: String,
    var interestRate: String,
    var interestRateType: String

) :  Parcelable

fun parseLoanDetailsFromString(input: String): LoanApprovalDataDetails {
    val regex = """(\w+)=(\w+)""".toRegex()
    val map = regex.findAll(input)
        .map { it.groupValues[1] to it.groupValues[2] }
        .toMap()

    return LoanApprovalDataDetails(
        employeeID = map["employeeID"] ?: "",
        employeeCode = map["employeeCode"] ?: "",
        employeeName = map["employeeName"] ?: "",
        loanTypeSlNo = map["loanTypeSlNo"] ?: "",
        loanApplySlNo = map["loanApplySlNo"] ?: "",
        loanRuleName = map["loanRuleName"] ?: "",
        loanStartDate = map["loanStartDate"] ?: "",
        loanRequestAmount = map["loanRequestAmount"] ?:  "",
        loanPeriod = map["loanPeriod"] ?: "",
        remarks = map["remarks"] ?: "",
        instalmentAmount = map["instalmentAmount"] ?:  "",
        nameInPayslip = map["nameInPayslip"] ?: "",
        employeeRequestAmount = map["employeeRequestAmount"] ?:  "",
        deductionType = map["deductionType"] ?:  "",
        deductionStartMonth = map["deductionStartMonth"] ?: "",
        numberOfMonths = map["numberOfMonths"] ?:  "",
        loanSanctionDate = map["loanSanctionDate"] ?: "",
        interestRate = map["interestRate"] ?:  "",
        interestRateType = map["interestRateType"] ?: ""
    )
}


class ApprovalListViewModel (private var repository: Repository,
                        private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val empID = userViewModel.getSFCode()

    val approvalList = savedStateHandle.getStateFlow("approvalList", emptyList<ApprovalList>())

    var loadingStatus3 = savedStateHandle.get<Boolean>("loadingStatus3") ?: false

    var flag3 = savedStateHandle.get<Int>("flag3") ?: 0

    fun getApprovalDetails(
        navController: NavController,
        context: Context,
        empId: String,
        org: Int,
    ) = viewModelScope.launch {


        when(val response = repository.getApprovalListResponse(context = context, sfCode = empId, org = org))
        {
            is Resource.Loading->{
                flag3 = 0
                loadingStatus3 = true
            }
            is Resource.Success->{

                loadingStatus3 = false

                response.let {

                    val data = response.data

                    if(data?.success == true)
                    {
                        Log.d("ApprovalsViewModel", "qwerty : getApprovalsDetails API call was successful/true : ${data.Head}")
                        savedStateHandle["approvalList"] = data.Head
                        flag3 = 1

                    }
                    else
                    {
                        savedStateHandle["approvalList"] = emptyList<ApprovalList>()
                        Log.d("ApprovalsViewModel", "qwerty : getApprovalsDetails API call was successful/false : ${data?.Head}")
                        flag3 = 2

                    }

                }

            }
            is Resource.Error->{

                loadingStatus3 = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/HomeScreen")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        savedStateHandle["approvalList"] = emptyList<ApprovalList>()
                        Log.d("ApprovalsViewModel", "qwerty : getApprovalsDetails API call was successful/Data Not Found")

                    }
                }

                Log.d("ApprovalsViewModel", "qwerty : getApprovalsDetails API call was not successful")

                savedStateHandle["approvalList"] = emptyList<ApprovalList>()

                flag3 = 2

                return@launch
            }
        }

        loadingStatus3 = false

    }


//------------------------------------------FOR COMPO OFF APPROVAL--------------------------------------------------//


    sealed class PostCompoOffApprovalResult {
        object Success : PostCompoOffApprovalResult()
        data class Failure(val message: String) : PostCompoOffApprovalResult()
    }

//------------------------------------------FOR LOAN APPROVAL--------------------------------------------------//


    sealed class PostLoanApprovalResult {
        object Success : PostLoanApprovalResult()
        data class Failure(val message: String) : PostLoanApprovalResult()
    }

//------------------------------------------LIST OF APPROVALS FOR COMPO OFF--------------------------------------------------//


    val compoOffApprovalList = savedStateHandle.getStateFlow("compoOffApprovalList", emptyList<UnApproveCompoOffData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getCompoOffApprovalDetails(navController: NavController, context: Context,empId: String, org: String) = viewModelScope.launch {

        when(val response = repository.getCompoOffApprovalListResponse(context = context, sfCode = empId, org = org)){
            is Resource.Loading->
            {
                flag = 0
                loadingStatus = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    loadingStatus = false

                    if (data?.success == true)
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getCompoOffApprovalDetails API call was successful/true  : ${data.head}")

                        savedStateHandle["compoOffApprovalList"] = data.head

                        flag = 1
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getCompoOffApprovalDetails API call was successful/false  : ${data?.head}")

                        savedStateHandle["compoOffApprovalList"] = emptyList<UnApproveCompoOffData>()

                        flag = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/CompoOffApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getCompoOffApprovalDetails API call was successful/Data Not Found")

                        savedStateHandle["compoOffApprovalList"] = emptyList<UnApproveCompoOffData>()
                    }
                }

                Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getCompoOffApprovalDetails API call was not successful")

                savedStateHandle["compoOffApprovalList"] = emptyList<UnApproveCompoOffData>()

                flag = 2
            }
        }

        loadingStatus = false

    }


    fun fetchAndUpdateCompoOffData(navController: NavController, context: Context, empId: String, org: String)
    {
        getCompoOffApprovalDetails(navController, context, empId, org)
    }



//------------------------------------------APPROVALS - COMPO OFF DETAILS FOR SELECTED EMPLOYEE--------------------------------------------------//


    var loading1 = mutableStateOf(false)


    private suspend fun postCompoOffApprovalDetails(navController: NavController, context: Context, empId: String, date: String, compoOffType: String,compoOffValue: String, checkIn: String, checkOut: String, CRate: Float,  divId: Int, holidayType: String, hourPerDayCR: Float, ratePerHourCR: Float,ratePerHourWeekOffCR: Float, ratePerHourFestiveCR: Float, ratePerHourNationalCR: Float,  salaryCalculation: Int, salaryCount: Float,ratePerHourNationalOT: Float,adjustSalary: Int, maxHourForFestiveHolidayOT: Float, maxHourForWeekOffOT: Float,org: Int,approvalAction: Int, rejectReason: String): PostCompoOffApprovalResult {



        Log.d("postCompoOffApprovalDetails", "date:$date\n compoOffType : $compoOffType\n compoffValue :$compoOffValue\n checkIn: $checkIn\n checkOut: $checkOut\n CRate: $CRate\n  divId: $divId\n holidayType: $holidayType\n hourPerDayCR: $hourPerDayCR\n ratePerHourCR:$ratePerHourCR\n ratePerHourWeekOffCR: $ratePerHourWeekOffCR\n ratePerHourFestiveCR: $ratePerHourFestiveCR\n ratePerHourNationalCR: $ratePerHourNationalCR\n  salaryCalculation: $salaryCalculation\n salaryCount: $salaryCount\n ratePerHourNationalOT: $ratePerHourNationalOT \n" +
                "adjustSalary: $adjustSalary\n maxHourForFestiveHolidayOT: $maxHourForFestiveHolidayOT\n maxHourForWeekOffOT: $maxHourForWeekOffOT\n org: $org\n approvalAction: $approvalAction\n rejectReason: $rejectReason")


        try {

            when(val response = repository.postCompoOffApprovalResponse(context = context, empId, date, compoOffType, compoOffValue, checkIn, checkOut, CRate, divId, holidayType, hourPerDayCR, ratePerHourCR, ratePerHourWeekOffCR, ratePerHourFestiveCR, ratePerHourNationalCR, salaryCalculation, salaryCount, ratePerHourNationalOT, adjustSalary, maxHourForFestiveHolidayOT, maxHourForWeekOffOT, org, approvalAction, rejectReason)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {


                        Log.d("postCompoOffApprovalDetails" , "response : $response")

                        val data = response.data

                        Log.d("postCompoOffApprovalDetails" , "response Data: ${response.data}")



                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postCompoOffApprovalDetails API call was successful/true : ${data.success}")
                            return PostCompoOffApprovalResult.Success
                        }
                        else
                        {
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postCompoOffApprovalDetails API call was successful/false : ${data?.success}")
                            return PostCompoOffApprovalResult.Failure("Failed to post Compo Off approval")
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
                                navController.navigate("${Screen.Network.route}/CompoOffApproval")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postCompoOffApprovalDetails API call was not successful")
                    return PostCompoOffApprovalResult.Failure("Null response")
                }
            }

            return PostCompoOffApprovalResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostCompoOffApprovalResult.Failure("An error occurred: ${e.message}")
        }

    }



    fun postCompoOffStatusUpdate(navController: NavController, context : Context, empId: String, date: String, compoOffType: String,compoOffValue: String, checkIn: String, checkOut: String, CRate: Float,  divId: Int, holidayType: String, hourPerDayCR: Float, ratePerHourCR: Float,ratePerHourWeekOffCR: Float, ratePerHourFestiveCR: Float, ratePerHourNationalCR: Float,  salaryCalculation: Int, salaryCount: Float,ratePerHourNationalOT: Float,adjustSalary: Int, maxHourForFestiveHolidayOT: Float, maxHourForWeekOffOT: Float,org: Int,approvalAction: Int, rejectReason: String)
    {
        viewModelScope.launch {
            try
            {
                loading1.value = true

                Log.d("APPROVAL LIST VIEW MODEL...","Inside postCompoOffStatusUpdate : Before when")
                // Perform the approval and rejection logic

                when (val result = postCompoOffApprovalDetails(navController, context, empId, date, compoOffType, compoOffValue, checkIn, checkOut, CRate, divId, holidayType, hourPerDayCR, ratePerHourCR, ratePerHourWeekOffCR, ratePerHourFestiveCR, ratePerHourNationalCR, salaryCalculation, salaryCount, ratePerHourNationalOT, adjustSalary, maxHourForFestiveHolidayOT, maxHourForWeekOffOT, org, approvalAction, rejectReason )) {
                    is PostCompoOffApprovalResult.Success ->
                    {

                        Log.d("APPROVAL LIST VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (approvalAction == 1) "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateCompoOffData(navController, context, empID, org.toString())
                        navController.navigate("CompoOffApproval")
                    }
                    is PostCompoOffApprovalResult.Failure ->
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (approvalAction == 1) "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateCompoOffData(navController, context, empID, org.toString())
                        navController.navigate("CompoOffApproval")
                    }
                }

                Log.d("APPROVAL LIST VIEW MODEL...","Inside postCompoOffStatusUpdate : After when.. fetchAndUpdateOnDutyData ")
                fetchAndUpdateCompoOffData(navController, context, empID, org.toString())
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("APPROVAL LIST VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
                navController.navigate("CompoOffApproval")
            }
            finally
            {
                Log.d("APPROVAL LIST VIEW MODEL...","Inside finally")
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateCompoOffData(navController, context, empID, org.toString())
                navController.navigate("CompoOffApproval")
            }
        }
    }




//------------------------------------------LIST OF APPROVALS FOR LOAN--------------------------------------------------//


    val loanApprovalList = savedStateHandle.getStateFlow("loanApprovalList", emptyList<UnApproveLoanData>())

    var loadingStatus1 = savedStateHandle.get<Boolean>("loadingStatus1") ?: false

    var flag1 = savedStateHandle.get<Int>("flag1") ?: 0


    fun getLoanApprovalDetails(navController: NavController, context: Context,empId: String, org: String) = viewModelScope.launch {

        when(val response = repository.getUnApproveLoanDetailsResponse(context = context, empID = empId, orgId = org)){
            is Resource.Loading->
            {
                flag1 = 0
                loadingStatus1 = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    loadingStatus1 = false

                    if (data?.success == true)
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalDetails API call was successful/true  : ${data.data}")

                        savedStateHandle["loanApprovalList"] = data.data

                        flag1 = 1
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalDetails API call was successful/false  : ${data?.data}")

                        savedStateHandle["loanApprovalList"] = emptyList<UnApproveLoanData>()

                        flag1 = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus1 = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/LoanApprovalList")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalDetails API call was successful/Data Not Found")

                        savedStateHandle["loanApprovalList"] = emptyList<UnApproveLoanData>()
                    }
                }

                savedStateHandle["loanApprovalList"] = emptyList<UnApproveLoanData>()

                flag1 = 2
            }
        }

        loadingStatus1 = false

    }


    fun fetchAndUpdateLoanData(navController: NavController, context: Context, empId: String, org: String)
    {
        getLoanApprovalDetails(navController, context, empId, org)
    }


//------------------------------------------GET LOAN INSTALMENT DETAILS--------------------------------------------------//


    val loanInstalmentDetailList = savedStateHandle.getStateFlow("loanInstalmentDetailList", emptyList<AppliedLoanInstalmentData>())

    var loadingStatus2 = savedStateHandle.get<Boolean>("loadingStatus2") ?: false

    var flag2 = savedStateHandle.get<Int>("flag2") ?: 0


    fun getAppliedLoanInstalmentDetails(navController: NavController, context: Context, empId: String, loanApplySlNo: String) = viewModelScope.launch {

        when(val response = repository.getAppliedLoanInstalmentDetailsResponse(context = context, empID = empId, loanApplySlNo = loanApplySlNo)){
            is Resource.Loading->
            {
                flag2 = 0
                loadingStatus2 = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    loadingStatus2 = false

                    if (data?.success == true)
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getAppliedLoanInstalmentDetails API call was successful/true  : ${data.data}")

                        savedStateHandle["loanInstalmentDetailList"] = data.data

                        flag2 = 1
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getAppliedLoanInstalmentDetails API call was successful/false  : ${data?.data}")

                        savedStateHandle["loanInstalmentDetailList"] = emptyList<AppliedLoanInstalmentData>()

                        flag2 = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus2 = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/LoanApprovalList")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getAppliedLoanInstalmentDetails API call was successful/Data Not Found")

                        savedStateHandle["loanInstalmentDetailList"] = emptyList<AppliedLoanInstalmentData>()
                    }
                }
                else
                {
                    Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getAppliedLoanInstalmentDetails API call was not successful")
                }


                savedStateHandle["loanInstalmentDetailList"] = emptyList<AppliedLoanInstalmentData>()

                flag2 = 2
            }
        }

        loadingStatus2 = false

    }



//------------------------------------------GET LOAN APPROVAL STAGES DETAILS--------------------------------------------------//


    val loanApprovalStagesDetailList = savedStateHandle.getStateFlow("loanApprovalStagesDetailList", emptyList<LoanApprovalStagesData>())

    var loadingStatus4 = savedStateHandle.get<Boolean>("loadingStatus4") ?: false

    var flag4 = savedStateHandle.get<Int>("flag4") ?: 0


    fun getLoanApprovalStagesDetails(navController: NavController, context: Context, empId: String, loanApplySlNo: String, orgId: String) = viewModelScope.launch {

        when(val response = repository.getLoanApprovalStagesDetailsResponse(context = context, empID = empId, loanApplySlNo = loanApplySlNo, orgId = orgId)){
            is Resource.Loading->
            {
                flag4 = 0
                loadingStatus4 = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    loadingStatus4 = false

                    if (data?.success == true)
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalStagesDetails API call was successful/true  : ${data.data}")

                        savedStateHandle["loanApprovalStagesDetailList"] = data.data

                        flag4 = 1
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalStagesDetails API call was successful/false  : ${data?.data}")

                        savedStateHandle["loanApprovalStagesDetailList"] = emptyList<LoanApprovalStagesData>()

                        flag4 = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus4 = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/LoanApprovalList")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalStagesDetails API call was successful/Data Not Found")

                        savedStateHandle["loanApprovalStagesDetailList"] = emptyList<LoanApprovalStagesData>()
                    }
                }

                Log.d("APPROVAL LIST VIEW MODEL...", "qwerty : getLoanApprovalStagesDetails API call was not successful")

                savedStateHandle["loanApprovalStagesDetailList"] = emptyList<LoanApprovalStagesData>()

                flag4 = 2
            }
        }

        loadingStatus4 = false

    }



//------------------------------------------APPROVALS - POST LOAN APPROVAL DETAILS--------------------------------------------------//


    private var loading5 = mutableStateOf(false)


    private suspend fun postLoanApprovalDetails(navController: NavController, context: Context, orgId: String, loanApplySlNo: String, empId: String, appEmpId: String, status: String, rejectReason: String): PostLoanApprovalResult {


        try {

            when(val response = repository.postLoanApprovalDetailsResponse(context = context, orgId, loanApplySlNo, empId, appEmpId, status, rejectReason)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {


                        Log.d("postLoanApprovalDetails" , "response : $response")

                        val data = response.data

                        Log.d("postLoanApprovalDetails" , "response Data: ${response.data}")



                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanApprovalDetails API call was successful/true : ${data.success}")
                            return PostLoanApprovalResult.Success
                        }
                        else
                        {
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanApprovalDetails API call was successful/false : ${data?.success}")
                            return PostLoanApprovalResult.Failure("Failed to post Loan approval")
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
                                navController.navigate("${Screen.Network.route}/LoanApprovalList")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanApprovalDetails API call was not successful")
                    return PostLoanApprovalResult.Failure("Null response")
                }
            }

            return PostLoanApprovalResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostLoanApprovalResult.Failure("An error occurred: ${e.message}")
        }

    }



    fun postLoanStatusUpdate(navController: NavController, context : Context, orgId: String, loanApplySlNo: String, empId: String, appEmpId: String, status: String, rejectReason: String)
    {
        viewModelScope.launch {
            try
            {
                loading5.value = true

                Log.d("APPROVAL LIST VIEW MODEL...","Inside postLoanStatusUpdate : Before when")
                // Perform the approval and rejection logic

                when (val result = postLoanApprovalDetails(navController, context, orgId, loanApplySlNo, empId, appEmpId, status, rejectReason)) {
                    is PostLoanApprovalResult.Success ->
                    {

                        Log.d("APPROVAL LIST VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (status == "Approve") "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateLoanData(navController, context, empID, orgId)
                        navController.navigate("LoanApprovalList")
                    }
                    is PostLoanApprovalResult.Failure ->
                    {
                        Log.d("APPROVAL LIST VIEW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (status == "Approve") "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateLoanData(navController, context, empID, orgId)
                        navController.navigate("LoanApprovalList")
                    }
                }

                Log.d("APPROVAL LIST VIEW MODEL...","Inside postLoanStatusUpdate : After when.. fetchAndUpdateLoanData ")
                fetchAndUpdateLoanData(navController, context, empID, orgId)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("APPROVAL LIST VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
                navController.navigate("LoanApprovalList")
            }
            finally
            {
                Log.d("APPROVAL LIST VIEW MODEL...","Inside finally")
                loading5.value = false // Set loading state to false after the operation
                fetchAndUpdateLoanData(navController, context, empID, orgId)
                navController.navigate("LoanApprovalList")
            }
        }
    }



//------------------------------------------POST LOAN EDIT AND APPROVE DETAILS--------------------------------------------------//



    fun postLoanEditApproveDetails(context: Context, navController: NavController, loanItems: MutableList<LoanInstalmentItems>,empID: String, deductionType: Int, loanAmount: Float, month: Int, year: Int, numberOfInstalments: Int, divId: Int, orgId: Int, fromDate: String, toDate: String, finalAmount: Float, interestRate: Float, interestRateType: Int, nameInPayslip: String, loanInterestAmount: Float, remarks: String, loanTypeSlNo: Int, loanApplySlNo: Int, approverEmpId: String, loanSanctionDate: String, instalmentInterestType: Float) = viewModelScope.launch {

        val formattedDataString: String
        val formattedData = JSONObject()

        try
        {
            val loanInstalmentArray = JSONArray()

            // Convert each LoanInstalmentItems object to JSON and add to the loanInstalmentArray
            loanItems.forEach { item ->
                val loanInstalmentObject = JSONObject()
                loanInstalmentObject.put("mon", item.mon)
                loanInstalmentObject.put("yr", item.yr)
                loanInstalmentObject.put("PAmt", item.PAmt.toString())
                loanInstalmentArray.put(loanInstalmentObject)
            }

            formattedData.put("loanData", loanInstalmentArray.toString())
            formattedDataString = formattedData.toString() // Convert the formattedData JSON object to a string

            Log.d("APPROVAL LIST VIEW MODEL", "createLoanJsonString : formattedDataString : $formattedDataString")

            Log.d("APPROVAL LIST VIEW MODEL", "empID: $empID, deductionType: $deductionType, loanAmount: $loanAmount, month: $month, year: $year, numberOfInstalments: $numberOfInstalments, divId: $divId, orgId: $orgId, fromDate: $fromDate, toDate: $toDate, finalAmount: $finalAmount, interestRate: $interestRate, interestRateType: $interestRateType, nameInPayslip: $nameInPayslip, loanInterestAmount: $loanInterestAmount, remarks: $remarks, loanTypeSlNo: $loanTypeSlNo,  loanApplySlNo: $loanApplySlNo, approverEmpId: $approverEmpId, loanSanctionDate: $loanSanctionDate, instalmentInterestType: $instalmentInterestType, loanData: $formattedDataString" )


            when(val response = repository.postLoanEditApproveDetailsResponse(context = context, empID = empID, deductionType = deductionType, loanAmount = loanAmount, month = month, year = year, numberOfInstalments = numberOfInstalments, divId = divId, orgId = orgId, fromDate = fromDate, toDate = toDate, finalAmount = finalAmount, interestRate = interestRate, interestRateType = interestRateType, nameInPayslip = nameInPayslip, loanInterestAmount = loanInterestAmount, remarks = remarks, loanTypeSlNo = loanTypeSlNo, loanApplySlNo = loanApplySlNo, approverEmpId = approverEmpId, loanSanctionDate = loanSanctionDate, instalmentInterestType = instalmentInterestType, loanData = formattedDataString)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            //Loan Form Submitted Successfully....
                            Constant.showToast(context, "Approved Successfully....")
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanEditApproveDetails API call was successful/true : ${data.success}")
                            navController.navigate("LoanApprovalList")
                        }
                        else
                        {
                            Constant.showToast(context, "Please try again later....")
                            Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanEditApproveDetails API call was successful/false : ${data?.success}")
                            navController.navigate("LoanApprovalList")
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
                                navController.navigate("${Screen.Network.route}/LoanApprovalList")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("APPROVAL LIST VIEW MODEL", "qwerty : postLoanEditApproveDetails API call was not successful")
                    navController.navigate("LoanApprovalList")
                    return@launch
                }
            }



        }
        catch (e: Exception)
        {
            Log.e("APPROVAL LIST VIEW MODEL", "Error during API call: ${e.message}")
            Constant.showToast(context, "Something went wrong....")
            navController.navigate("LoanApprovalList")
        }

    }



//------------------------------------------EMPLOYEE PROFILE DETAILS--------------------------------------------------//



    val employeeProfileDetailList = savedStateHandle.getStateFlow("employeeProfileDetailList", emptyList<EmployeeProfileDetail>())

    private var loadingStatus5 = savedStateHandle.get<Boolean>("loadingStatus5") ?: false

    private var flag5 = savedStateHandle.get<Int>("flag5") ?: 0

    fun getEmployeeProfileDetails(navController:NavController, context: Context, empId: String, orgId: String ) = viewModelScope.launch {


        Log.d("APPROVAL LIST VIEW MODEL", "qwerty : Inside getEmployeeProfileDetails, empID : $empId")


        when (val response = repository.getEmployeeProfileDetailsResponse(context = context, empId = empId, orgId = orgId)) {
            is Resource.Loading -> {
                loadingStatus5 = true
                flag5 = 0
            }

            is Resource.Success -> {

                loadingStatus5 = false

                response.let {
                    val data = response.data

                    Log.d("APPROVAL LIST VIEW MODEL", "qwerty : getEmployeeProfileDetails API call successful1/true. $data ")


                    if (data?.success == true) {
                        savedStateHandle["employeeProfileDetailList"] = data.data
                        Log.d("APPROVAL LIST VIEW MODEL", "qwerty : getEmployeeProfileDetails API call successful/true. ${data.data} ")
                        flag5 = 1
                    } else {
                        savedStateHandle["employeeProfileDetailList"] = emptyList<EmployeeProfileDetail>()
                        Log.d("APPROVAL LIST VIEW MODEL", "qwerty : getEmployeeProfileDetails API call successful/false. ${data?.data} ")
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
                            navController.navigate("${Screen.Network.route}/LoanApprovalList")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("LoanApprovalList")

                        }
                        Log.d("APPROVAL LIST VIEW MODEL", "qwerty : getEmployeeProfileDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["employeeProfileDetailList"] = emptyList<EmployeeProfileDetail>()
                        Log.d("APPROVAL LIST VIEW MODEL", "qwerty : getEmployeeProfileDetails API call successful/ Data Not Found ")
                    }
                }
                flag5 = 2
                return@launch
            }
        }

    }






}