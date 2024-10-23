package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.ExistingLoanPaidData
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.LoanEligibilityData
import com.payroll.twogrowhr.Model.ResponseModel.LoanEnableData
import com.payroll.twogrowhr.Model.ResponseModel.LoanInstallmentData
import com.payroll.twogrowhr.Model.ResponseModel.LoanMappedData
import com.payroll.twogrowhr.Model.ResponseModel.LoanTypeData
import com.payroll.twogrowhr.Model.View.LoanInstalmentItems
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoanDetailViewModel
@Inject
constructor(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


//------------------------------------------LOAN HISTORY DETAILS--------------------------------------------------//



    val loanList = savedStateHandle.getStateFlow("loanList", emptyList<LoanData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0

    // MODE 1 = EMPLOYEE, 2 = REPORTEE

    fun getLoanDetails1(navController:NavController, context: Context, empId: String, loanData: LoanApprovalDataDetails, mode: String) = viewModelScope.launch {

/*
        val loanApprovalData = LoanApprovalDataDetails(employeeID = loanData.employeeID, employeeCode = loanData.employeeCode, employeeName = loanData.employeeName,loanTypeSlNo = loanData.loanTypeSlNo,
            loanApplySlNo = loanData.loanApplySlNo, loanRuleName = loanData.loanRuleName, loanStartDate = loanData.loanStartDate
            , loanRequestAmount = loanData.loanRequestAmount, loanPeriod = loanData.loanPeriod, remarks = loanData.remarks, instalmentAmount = loanData.instalmentAmount, nameInPayslip = loanData.nameInPayslip, employeeRequestAmount = loanData.loanRequestAmount,
            deductionType = loanData.deductionType, deductionStartMonth = loanData.deductionStartMonth, numberOfMonths = loanData.numberOfMonths, loanSanctionDate = loanData.loanSanctionDate, interestRate = loanData.interestRate, interestRateType = loanData.interestRateType)
*/


        val url = if(mode == "1"){ "Finance" }
                  else{ "LoanApprovalList" }


        when(val response = repository.getLoanResponse(context = context, sfCode = empId))
        {
            is Resource.Loading->{
                flag = 0
                loadingStatus = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    loadingStatus = false

                    if(data?.success == true)
                    {
                        Log.d("LoanDetailViewModel", "qwerty : getLoanDetails1 API call successful true. Message: ${data.Head}")
                        savedStateHandle["loanList"] = data.Head
                        flag = 1
                    }
                    else
                    {
                        savedStateHandle["loanList"] = emptyList<LoanData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanDetails1 API call successful false. Message: ${data?.Head}")
                        flag = 2
                    }

                }

            }
            is Resource.Error->{
                Log.d("Loan List", "qwerty : getLoanDetails1 API call not successful. ")

               loadingStatus = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/$url")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("Loan List", "qwerty : getLoanDetails1 API call was successful/Data Not Found")
                        savedStateHandle["loanList"] = emptyList<LoanData>()
                    }
                }

                savedStateHandle["loanList"] = emptyList<LoanData>()

                flag = 2
            }
        }

        loadingStatus = false
    }



//------------------------------------------LOAN ENABLE DETAILS--------------------------------------------------//


    val loanMappedData = savedStateHandle.getStateFlow("loanMappedData", emptyList<LoanMappedData>())

    var loadingStatus2 = savedStateHandle.get<Boolean>("loadingStatus2") ?: false

    var flag2 = savedStateHandle.get<Int>("flag2") ?: 0

    fun getLoanMappedDetails(navController:NavController, context: Context, empId: String, orgId: String, callback: (String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {


        Log.d("LoanDetailViewModel", "qwerty : Inside getLoanMappedDetails, empID : $empId")


        when (val response = repository.getLoanMappedDetailsResponse(context = context, empId = empId, orgId = orgId)) {
            is Resource.Loading -> {
                loadingStatus2 = true
                statusLoading.value = true
                flag2 = 0
            }

            is Resource.Success -> {

                loadingStatus2 = false
                statusLoading.value = false


                response.let {
                    val responseData = response.data

                    Log.d("LoanDetailViewModel", "qwerty : getLoanMappedDetails API call successful1/true. $responseData ")


                    if (responseData?.success == true) {
                        savedStateHandle["loanMappedData"] = responseData.data
                        Log.d("LoanDetailViewModel", "qwerty : getLoanMappedDetails API call successful/true. ${responseData.data} ")
                        flag2 = 1
                        callback(responseData.data[0].flag)
                    } else {
                        savedStateHandle["loanMappedData"] = emptyList<LoanMappedData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanMappedDetails API call successful/false. ${responseData?.data} ")
                        flag2 = 2
                        callback("")
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus2 = false

                statusLoading.value = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Finance")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("Finance")

                        }
                        Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["loanMappedData"] = emptyList<LoanMappedData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanMappedDetails API call successful/ Data Not Found ")
                    }
                }
                flag2 = 2
                callback("")
                return@launch
            }
        }

    }

//------------------------------------------LOAN MAPPING DETAILS--------------------------------------------------//


    val loanEnableData = savedStateHandle.getStateFlow("loanEnableData", emptyList<LoanEnableData>())

    var loadingStatus6 = savedStateHandle.get<Boolean>("loadingStatus6") ?: false

    var flag6 = savedStateHandle.get<Int>("flag6") ?: 0

    fun getLoanEnableDetails(navController:NavController, context: Context, empId: String, orgId: String) = viewModelScope.launch  {


        Log.d("LoanDetailViewModel", "qwerty : Inside getLoanEnableDetails, empID : $empId")


        when (val response = repository.getLoanEnableDetailsResponse(context = context, empId = empId, orgId = orgId)) {
            is Resource.Loading -> {
                loadingStatus6 = true
                statusLoading.value = true
                flag6 = 0
            }

            is Resource.Success -> {

                loadingStatus6 = false
                statusLoading.value = false


                response.let {
                    val responseData = response.data

                    Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call successful1/true. $responseData ")


                    if (responseData?.success == true)
                    {
                        savedStateHandle["loanEnableData"] = responseData.data
                        Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call successful/true. ${responseData.data} ")
                        flag6 = 1
                    }
                    else
                    {
                        savedStateHandle["loanEnableData"] = emptyList<LoanEnableData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call successful/false. ${responseData?.data} ")
                        flag6 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus6 = false

                statusLoading.value = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Finance")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("Finance")
                        }
                        Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["loanEnableData"] = emptyList<LoanEnableData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanEnableDetails API call successful/ Data Not Found ")
                    }
                }
                flag6 = 2
                return@launch
            }
        }

    }


//------------------------------------------LOAN TYPE DETAILS--------------------------------------------------//



    val loanTypeList = savedStateHandle.getStateFlow("loanTypeList", emptyList<LoanTypeData>())

    var loadingStatus1 = savedStateHandle.get<Boolean>("loadingStatus1") ?: false

    var flag1 = savedStateHandle.get<Int>("flag1") ?: 0

    fun getLoanTypeDetails(navController:NavController, context: Context, empId: String, orgId: String ) = viewModelScope.launch {


        Log.d("LoanDetailViewModel", "qwerty : Inside getLoanTypeDetails, empID : $empId")


        when (val response = repository.getLoanTypeDetailsResponse(context = context, empId = empId, orgId = orgId)) {
            is Resource.Loading -> {
                loadingStatus1 = true
                flag1 = 0
            }

            is Resource.Success -> {

                loadingStatus1 = false

                response.let {
                    val data = response.data

                    Log.d("LoanDetailViewModel", "qwerty : getLoanTypeDetails API call successful1/true. $data ")


                    if (data?.success == true) {
                        savedStateHandle["loanTypeList"] = data.data
                        Log.d("LoanDetailViewModel", "qwerty : getLoanTypeDetails API call successful/true. ${data.data} ")
                        flag1 = 1
                    } else {
                        savedStateHandle["loanTypeList"] = emptyList<LoanTypeData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanTypeDetails API call successful/false. ${data?.data} ")
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
                            navController.navigate("${Screen.Network.route}/ApplyLoan")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("ApplyLoan")

                        }
                        Log.d("LoanDetailViewModel", "qwerty : getLoanTypeDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["loanTypeList"] = emptyList<LoanTypeData>()
                        Log.d("LoanDetailViewModel", "qwerty : getLoanTypeDetails API call successful/ Data Not Found ")
                    }
                }
                flag1 = 2
                return@launch
            }
        }

    }




//------------------------------------------LOAN ELIGIBILITY DETAILS--------------------------------------------------//


    val loanEligibilityDetailData = savedStateHandle.getStateFlow("loanEligibilityDetailData", emptyList<LoanEligibilityData>())

    var flag3 = savedStateHandle.get<Int>("flag3") ?: 0

    fun getLoanEligibilityDetails(navController:NavController, context: Context, empId: String, ruleID: String,callback: (String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {

            Log.d("LoanDetailViewModel", "qwerty : Inside getLoanEligibilityDetails, empID : $empId")



            when (val response = repository.getLoanEligibilityDetailsResponse(context = context, empId = empId, ruleID = ruleID)) {
                is Resource.Loading -> {
                    statusLoading.value = true
                    flag3 = 0
                }

                is Resource.Success -> {

                    statusLoading.value = false

                    response.let {
                        val data = response.data

                        Log.d("LoanDetailViewModel", "qwerty : getLoanEligibilityDetails API call successful1/true. $data ")


                        if (data?.success == true)
                        {
                            savedStateHandle["loanEligibilityDetailData"] = data.data
                            Log.d("LoanDetailViewModel", "qwerty : getLoanEligibilityDetails API call successful/true. ${data.data} ")
                            flag3 = 1
                            callback(response.data!!.data[0].count)
                        }
                        else
                        {
                            savedStateHandle["loanEligibilityDetailData"] = emptyList<LoanEligibilityData>()
                            Log.d("LoanDetailViewModel", "qwerty : getLoanEligibilityDetails API call successful/false. ${data?.data} ")
                            flag3 = 2
                            callback("")
                        }
                    }

                }

                is Resource.Error -> {

                    statusLoading.value = false

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/ApplyLoan")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                                navController.navigate("ApplyLoan")

                            }
                            Log.d("LoanDetailViewModel", "qwerty : getLoanEligibilityDetails API call not successful.")
                        }
                        else
                        {
                            savedStateHandle["loanEligibilityDetailData"] = emptyList<LoanEligibilityData>()
                            Log.d("LoanDetailViewModel", "qwerty : getLoanEligibilityDetails API call successful/ Data Not Found ")
                        }
                    }
                    flag3 = 2
                    callback("")
                    return@launch
                }
            }



    }









//------------------------------------------LOAN ELIGIBILITY DETAILS--------------------------------------------------//


    val installmentDetails = savedStateHandle.getStateFlow("installmentDetails", emptyList<LoanInstallmentData>())

    var loadingStatus4 = savedStateHandle.get<Boolean>("loadingStatus4") ?: false

    var flag4 = savedStateHandle.get<Int>("flag4") ?: 0

    fun getInstallmentDetails(navController:NavController, context: Context, deductionType: String, numberOfInstalments: String, startMonth: String, loanAmount: String, interestRate: String, interestType: String ) = viewModelScope.launch {


        Log.d("LoanDetailViewModel", "qwerty : Inside getInstallmentDetails, deductionType: $deductionType, numberOfInstallments: $numberOfInstalments, startMonth: $startMonth, loanAmount: $loanAmount, interestRate: $interestRate, interestType: $interestType")


        when (val response = repository.getLoanInstallmentDetailsResponse(context = context, deductionType = deductionType, numberOfInstallments = numberOfInstalments, startMonth = startMonth, loanAmount = loanAmount, interestRate = interestRate, interestType = interestType)) {
            is Resource.Loading -> {
                loadingStatus4 = true
                statusLoading.value = true
                flag4 = 0
            }

            is Resource.Success -> {

                loadingStatus4 = false
                statusLoading.value = false

                response.let {
                    val data = response.data

                    Log.d("LoanDetailViewModel", "qwerty : getInstallmentDetails API call successful1/true. $data ")


                    if (data?.success == true) {
                        savedStateHandle["installmentDetails"] = data.data
                        Log.d("LoanDetailViewModel", "qwerty : getInstallmentDetails API call successful/true. ${data.data} ")
                        flag4 = 1
                    } else {
                        savedStateHandle["installmentDetails"] = emptyList<LoanInstallmentData>()
                        Log.d("LoanDetailViewModel", "qwerty : getInstallmentDetails API call successful/false. ${data?.data} ")
                        flag4 = 2
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus4 = false
                statusLoading.value = false


                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/ApplyLoan")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("ApplyLoan")

                        }
                        Log.d("LoanDetailViewModel", "qwerty : getInstallmentDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["installmentDetails"] = emptyList<LoanInstallmentData>()
                        Log.d("LoanDetailViewModel", "qwerty : getInstallmentDetails API call successful/ Data Not Found ")
                    }
                }
                flag4 = 2
                return@launch
            }
        }

    }




//------------------------------------------EXISTING LOAN PAID DETAILS--------------------------------------------------//


    val loanExistingPaidData = savedStateHandle.getStateFlow("loanExistingPaidData", emptyList<ExistingLoanPaidData>())

    var loadingStatus5 = savedStateHandle.get<Boolean>("loadingStatus5") ?: false

    var flag5 = savedStateHandle.get<Int>("flag5") ?: 0

    fun getExistingLoanAmountDetails(navController:NavController, context: Context, empId: String, callback: (String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {


        Log.d("LoanDetailViewModel", "qwerty : Inside getExistingLoanAmountDetails, empID : $empId")


        when (val response = repository.getExistingLoanDetailsResponse(context = context, empID = empId)) {
            is Resource.Loading -> {
                loadingStatus5 = true
                statusLoading.value = true
                flag5 = 0
            }

            is Resource.Success -> {

                loadingStatus5 = false
                statusLoading.value = false


                response.let {
                    val responseData = response.data

                    Log.d("LoanDetailViewModel", "qwerty : getExistingLoanAmountDetails API call successful1/true. $responseData ")


                    if (responseData?.success == true) {
                        savedStateHandle["loanExistingPaidData"] = responseData.data
                        Log.d("LoanDetailViewModel", "qwerty : getExistingLoanAmountDetails API call successful/true. ${responseData.data} ")
                        flag5 = 1
                        callback(responseData.data[0].existingInstallmentAmount.toString())
                    } else {
                        savedStateHandle["loanExistingPaidData"] = emptyList<ExistingLoanPaidData>()
                        Log.d("LoanDetailViewModel", "qwerty : getExistingLoanAmountDetails API call successful/false. ${responseData?.data} ")
                        flag5 = 2
                        callback("")
                    }
                }

            }

            is Resource.Error -> {

                loadingStatus5 = false

                statusLoading.value = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Finance")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            navController.navigate("Finance")

                        }
                        Log.d("LoanDetailViewModel", "qwerty : getExistingLoanAmountDetails API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["loanExistingPaidData"] = emptyList<ExistingLoanPaidData>()
                        Log.d("LoanDetailViewModel", "qwerty : getExistingLoanAmountDetails API call successful/ Data Not Found ")
                    }
                }
                flag5 = 2
                callback("")
                return@launch
            }
        }

    }






//------------------------------------------POST LOAN DETAILS--------------------------------------------------//



    fun postLoanForm(context: Context, navController: NavController, loanItems: MutableList<LoanInstalmentItems>, empID: String, deductionType: String, loanAmount: String, month: String, year: String, numberOfInstalments: String, divId: String, orgId: String, fromDate: String, toDate: String, finalAmount: String, interestRate: String, interestRateType: String, nameInPayslip: String, loanInterestAmount: String, remarks: String, loanTypeSlNo: String, instalmentInterestType: String) = viewModelScope.launch {

        var formattedDataString: String
        val formattedData = JSONObject()

        try
        {
/*
            // Create XML string

            val xmlStringBuilder = StringBuilder()
            xmlStringBuilder.append("<row>\n")
            for (item in loanItems)
            {
                xmlStringBuilder.append("<Pay mon=\"${item.mon}\" yr=\"${item.yr}\" PAmt=\"${String.format("%.2f", item.PAmt)}\" />\n")
            }
            xmlStringBuilder.append("</row>")

            val loanXMLString = xmlStringBuilder.toString()

            Log.d("LoanViewModel", "createLoanXMLString : formattedDataString : $loanXMLString")

            */

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

            Log.d("LoanViewModel", "createLoanJsonString : formattedDataString : $formattedDataString")

            Log.d("loan viewmodel value", "empID: $empID, deductionType: $deductionType, loanAmount: $loanAmount, month: $month, year: $year, numberOfInstalments: $numberOfInstalments, divId: $divId, orgId: $orgId, fromDate: $fromDate, toDate: $toDate, finalAmount: $finalAmount, interestRate: $interestRate, interestRateType: $interestRateType, nameInPayslip: $nameInPayslip, loanInterestAmount: $loanInterestAmount, remarks: $remarks, loanTypeSlNo: $loanTypeSlNo, instalmentInterestType: $instalmentInterestType, loanData: $formattedDataString" )


            when(val response = repository.postLoanSaveDetailsResponse(context = context, empID = empID, deductionType = deductionType, loanAmount = loanAmount, month = month, year = year, numberOfInstalments = numberOfInstalments, divId = divId, orgId = orgId, fromDate = fromDate, toDate = toDate, finalAmount = finalAmount, interestRate = interestRate, interestRateType = interestRateType, nameInPayslip = nameInPayslip, loanInterestAmount = loanInterestAmount, remarks = remarks, loanTypeSlNo = loanTypeSlNo, instalmentInterestType = instalmentInterestType, loanData = formattedDataString)){
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
                            Constant.showToast(context, "Submitted Successfully....")
                            Log.d("LoanDetailViewModel", "qwerty : postLoanForm API call was successful/true : ${data.success}")
                            navController.navigate("Finance")
                        }
                        else
                        {
                            Constant.showToast(context, "Please try again later....")
                            Log.d("LoanDetailViewModel", "qwerty : postLoanForm API call was successful/false : ${data?.success}")
                            navController.navigate("Finance")
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
                                navController.navigate("${Screen.Network.route}/Finance")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("LoanDetailViewModel", "qwerty : postLoanForm API call was not successful")
                    navController.navigate("Finance")
                    return@launch
                }
            }



        }
        catch (e: Exception)
        {
            Log.e("LoanDetailViewModel", "Error during API call: ${e.message}")
            Constant.showToast(context, "Something went wrong....")
            navController.navigate("Finance")
        }

    }


}

