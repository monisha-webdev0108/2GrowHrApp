package com.payroll.twogrowhr.repository
import android.content.Context
import android.util.Log
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceCICOListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRBHideResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRegularizeCountResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRegularizeRulesResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRegularizedResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendenceResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.ChangePasswordResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.CheckInOutResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.EmployeeShiftDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.GeoResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.HolidayResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveApprovalDetailListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveApprovalSubmitResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveFormValidateResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveSubmitResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveTypeDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoansubDetailsListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoginResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OnDutySaveDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.PayslipAPIResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.RegularizeApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.RegularizeApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.SalaryDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.SaveCheckInDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.SaveCheckOutDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.ShiftResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UploadImageResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.WFHApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.WFHApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.WFHSaveDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.WishesResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.WorkFromHomeDetailResponseModel
import com.payroll.twogrowhr.retrofit.APIInterface
import com.google.gson.JsonObject
import com.payroll.twogrowhr.Model.ResponseModel.AppliedLoanInstalmentDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.ApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.AssetListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.CompoOffApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.CompoOffApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.DegreeDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.DocumentDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.DocumentListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.DocumentUploadSubmitResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.EducationDetailSaveResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.EducationalDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.EmployeeProfileResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.ExperienceDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.ExperienceDetailSaveResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveDateListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanApprovalStagesResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanEditApproveResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanEligibleResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanEnableResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanExistingPaidResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanInstallmentResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanMappedDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanSaveResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LoanTypeResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocShowCountResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OtherDocumentDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OtherDocumentDetailSaveResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OverTimeApprovalListResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.OverTimeApprovalResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipDeductionDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipEarningDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipHeadDetailsResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipOtherDeductionResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.TdsFormResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.TodayAttendanceLogResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UnApprovedLoanDetailResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UploadDocumentResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UploadLeaveDocsResponseModel
import com.payroll.twogrowhr.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject
class Repository
@Inject
constructor(private val mAPIInterface: APIInterface) : BaseRepo() {
    companion object {
        var instance: Repository? = null
        private lateinit var mBaseURL: String
        private lateinit var mQryParam: Map<String, String>
        private lateinit var mJsonObject: JsonObject
        private lateinit var mMultipart: MultipartBody.Part

        fun getInstance(
            repository: Repository,
            baseURL: String,
            qryParam: Map<String, String>,
            jsonObject: JsonObject,
            multipart: MultipartBody.Part?
        ): Repository {
            if (instance == null) {
                instance = repository
            }
            mBaseURL = baseURL
            mQryParam = qryParam
            mJsonObject = jsonObject
            if (multipart != null) {
                mMultipart = multipart
            }
            return instance as Repository
        }
    }

    suspend fun initialRequest1(
        axn: String,
        context: Context,
        userName: String,
        password: String
    ): Resource<LoginResponseModel> {
        Log.d("Repository", "Inside InitialRequest")
        return safeApiCall { mAPIInterface.getUserData(axn, userName, password) }
    }

    suspend fun initialRequest(
        axn: String,
        context: Context,
        userName: String,
        password: String
    ): LoginResponseModel? {
        Log.d("Repository", "Inside InitialRequest")
        val response = mAPIInterface.getUserData(axn, userName, password)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        } else {
            //TODO error handlin
            return null
        }
        return null
    }

//------------------------------------------GET EMPLOYEE DETAILS FOR PROFILE--------------------------------------------------//

    suspend fun getEmployeeDetailResponse(context: Context, sfCode: String): Resource<JsonObject> {

        return safeApiCall { mAPIInterface.getEmployeeDetails(action = Constant.EmpPersonalDetails, empId = sfCode) }

    }

//------------------------------------------UPLOAD IMAGE--------------------------------------------------//

    suspend fun postCapturedImageResponse(context: Context, image : String ) : Resource<UploadImageResponseModel>
    {

        val file = File(image)


        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return safeApiCall { mAPIInterface.postCapturedImage(body,action = Constant.uploadImage) }
        
    }

//------------------------------------------GET HOLIDAY DETAILS--------------------------------------------------//

    suspend fun getHolidayResponse(context: Context, sfCode: String, year : String) : Resource<HolidayResponseModel>
    { return safeApiCall { mAPIInterface.getHolidayData(action = Constant.GetHolidayList, sfCode = sfCode, year = year) } }

//------------------------------------------GET PAYSLIP DETAILS--------------------------------------------------//

    suspend fun getPayslipResponse(context: Context, sfCode: String, year : String) : Resource<PayslipAPIResponseModel>
    {

        return safeApiCall { mAPIInterface.getPayslipData(action = Constant.getPayslipList, sfCode = sfCode, year = year) }

    }

    suspend fun getPayslipHeadResponse(context: Context, Emp_Id: String,Month:String, Year : String,) : Resource<PaySlipHeadDetailsResponseModel>
    {

        return safeApiCall { mAPIInterface.getPayslipHeadData(action = Constant.getPaySlipHeadDetail, sfCode = Emp_Id, Month=Month, Year = Year,) }

    }

    suspend fun getPayslipEarningResponse(context: Context, Emp_Id: String,Month:String, Year : String) : Resource<PaySlipEarningDetailsResponseModel>
    {

        return safeApiCall { mAPIInterface.getPayslipEarningData(action = Constant.getPaySlipEarningDetail, sfCode = Emp_Id, Month=Month, Year = Year) }

    }

    suspend fun getPayslipDeductionResponse(context: Context, Emp_Id: String,Month:String, Year : String) : Resource<PaySlipDeductionDetailsResponseModel>
    {

        return safeApiCall { mAPIInterface.getPayslipDeductionData(action = Constant.getPaySlipDeductionDetail, sfCode = Emp_Id, Month=Month, Year = Year) }

    }
    suspend fun getPayslipOtherDeductionResponse(context: Context, Emp_Id: String,Month:String, Year : String) : Resource<PaySlipOtherDeductionResponseModel>
    {

        return safeApiCall { mAPIInterface.getPayslipOtherDeductionData(action = Constant.getPaySlipOtherDeductionDetail, sfCode = Emp_Id, Month=Month, Year = Year) }

    }


    //------------------------------------------GET TDS DETAILS--------------------------------------------------//

    suspend fun getTdsFormResponse(context: Context, sfCode: String, year : String) : Resource<TdsFormResponseModel>
    {

        return safeApiCall { mAPIInterface.getTdsFormData(action = Constant.getTdsFormList, sfCode = sfCode, year = year) }

    }

//------------------------------------------GET EMPLOYEE SHIFT DETAILS--------------------------------------------------//

    suspend fun getEmpShiftResponse(context: Context, sfCode: String): Resource<EmployeeShiftDetailsResponseModel>
    {
        return safeApiCall { mAPIInterface.getEmpShiftData(action = Constant.EmpShiftdetails, sfCode = sfCode) }

    }

//------------------------------------------GET ATTENDANCE LOG DETAILS--------------------------------------------------//

    suspend fun getTodaysLogResponse(context: Context, sfCode: String, divisionCode:String): Resource<TodayAttendanceLogResponseModel>
    {
        return safeApiCall { mAPIInterface.getTodaysLogData(action = Constant.todaysLog, sfCode = sfCode, divcode = divisionCode) }
    }

//------------------------------------------GET SHIFT DETAILS--------------------------------------------------//

    suspend fun getShiftResponse(context: Context, sfCode: String, divisionCode:String, stateCode:String): Resource<ShiftResponseModel>
    {
        return safeApiCall { mAPIInterface.getShiftData(action = Constant.shiftDetails, divcode = divisionCode, sfCode = sfCode, id = sfCode, stateCode = stateCode) }
    }

//------------------------------------------GET GEO DETAILS--------------------------------------------------//

    suspend fun getGeoResponse(context: Context, sfCode: String) : Resource<GeoResponseModel>
    {

        return safeApiCall {mAPIInterface.getGeoData(action = Constant.GeoDetails, sfCode = sfCode)}

    }

//------------------------------------------SAVE CHECKIN DETAILS--------------------------------------------------//

    suspend fun postCheckInResponse(context: Context, checkInData: String) : Resource<SaveCheckInDetailsResponseModel>
    {

        return safeApiCall {mAPIInterface.postCheckInData(action = Constant.saveCheckIn1, checkInData = checkInData)}

    }

//------------------------------------------SAVE CHECKIN DETAILS--------------------------------------------------//

    suspend fun postCheckOutResponse(context: Context, empId: String, checkOutData: String) : Resource<SaveCheckOutDetailsResponseModel>
    {

        return safeApiCall {mAPIInterface.postCheckOutData(action = Constant.saveCheckOut, sfCode = empId, checkOutData = checkOutData)}

    }

//------------------------------------------GET LEAVE DETAILS--------------------------------------------------//

    suspend fun getLeaveResponse(context: Context, sfCode: String): Resource<LeaveTypeDetailResponseModel>
    {
        Log.d("Repository", "Inside getLeaveResponse API Call")

        return safeApiCall { mAPIInterface.getLeaveData(action = Constant.LeaveType, sfCode = sfCode) }
    }

//------------------------------------------GET LEAVE DATE DETAILS--------------------------------------------------//

    suspend fun getLeaveDateListResponse(context: Context, sfCode: String, fromDate: String, toDate: String, slno: String): Resource<LeaveDateListResponseModel>
    {
        Log.d("Repository", "Inside getLeaveResponse API Call")

        return safeApiCall { mAPIInterface.getLeaveDatesList(action = Constant.LeaveDateList, sfCode = sfCode, fromDate = fromDate, toDate = toDate, slno = slno) }
    }


//------------------------------------------UPLOAD LEAVE DOCUMENT--------------------------------------------------//

    suspend fun postLeaveFileResponse(context: Context, docs : String ) : Resource<UploadLeaveDocsResponseModel>
    {

        val file = File(docs)

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val body: MultipartBody.Part = MultipartBody.Part.createFormData("document", file.name, requestFile)

        return safeApiCall { mAPIInterface.postLeaveDocument(body,action = Constant.uploadLeaveDocs) }

    }


//------------------------------------------LEAVE FORM VALIDATION DETAILS--------------------------------------------------//

    suspend fun postLeaveFormValidateResponse(context: Context, sfCode: String, data : String): Resource<LeaveFormValidateResponseModel> {

        return safeApiCall { mAPIInterface.postLeaveFormValidate(action = Constant.LeaveFormValidate, sfCode = sfCode, data = data) }

    }

//------------------------------------------POST LEAVE DETAILS--------------------------------------------------//

    suspend fun postLeaveResponse(context: Context, sfCode: String, data: String ): Resource<LeaveSubmitResponseModel>
    {
        Log.d("Repository", "Inside postLeaveResponse API Call")

        return safeApiCall { mAPIInterface.postLeaveDetails(action = Constant.LeaveForm, sfCode = sfCode, data =  data) }

    }

//------------------------------------------GET LEAVE HISTORY DETAILS--------------------------------------------------//

    suspend fun getLeaveHistoryResponse(context: Context, sfCode: String): Resource<LeaveHistoryResponseModel>
    {
        Log.d("Repository", "Inside getLeaveHistoryResponse API Call")

        return safeApiCall {mAPIInterface.getLeaveHistoryData(action = Constant.leaveHistory, sfCode = sfCode)}

    }

//------------------------------------------GET LEAVE HISTORY DETAILS--------------------------------------------------//

    suspend fun getLeaveHistoryDetailResponse(context: Context, sfCode: String, slNo: String): Resource<LeaveHistoryDetailResponseModel>
    {
        Log.d("Repository", "Inside getLeaveHistoryDetailResponse API Call")

        return safeApiCall { mAPIInterface.getLeaveHistoryDetailData(action = Constant.leaveHistoryDetail, sfCode = sfCode, sl_no = slNo) }

    }

//------------------------------------------GET LEAVE APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun getLeaveApprovalListResponse(context: Context, sfCode: String): Resource<LeaveApprovalListResponseModel> {

        Log.d("Repository...", " Inside getLeaveApprovalListResponse ")

        return safeApiCall { mAPIInterface.getLeaveListApproval(action = Constant.unApproveLeave, sfCode = sfCode) }

    }

//------------------------------------------GET LEAVE DETAIL APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun getLeaveDetailListResponse(context: Context, sfCode: String, ruleId: String): Resource<LeaveApprovalDetailListResponseModel> {

        return safeApiCall { mAPIInterface.getLeaveDetailListApproval(action = Constant.unApproveLeaveDetail, sfCode = sfCode, ruleId = ruleId) }

  }

//------------------------------------------POST LEAVE APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun postLeaveApprovalResponse(context: Context, empId: String, slNo : String, data: String): Resource<LeaveApprovalSubmitResponseModel> {

        return safeApiCall { mAPIInterface.postLeaveListApprovalDetails(action = Constant.leaveApprove, sfCode = empId, slNo = slNo, data = data) }

    }

//------------------------------------------GET ATTENDANCE REGULARIZED APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun getRegularizedListResponse(context: Context, sfCode: String): Resource<RegularizeApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getAttendanceRegularizeApproval(action = Constant.approveAttendanceReglarizeAPI, sfCode = sfCode) }

 }

//------------------------------------------POST ATTENDANCE REGULARIZE APPROVAL LIST DETAILS--------------------------------------------------//
    suspend fun postRegularizeApprovalResponse(context: Context, slNo: String,status : String): Resource<RegularizeApprovalResponseModel> {

        return safeApiCall {mAPIInterface.postRegularizeApproval(action = Constant.approveForRegularizeAPI, sl_no = slNo, App = status)}

    }

//------------------------------------------GET COMPO OFF APPROVAL LIST DETAILS--------------------------------------------------//
    suspend fun getCompoOffApprovalListResponse(context: Context, sfCode: String, org: String): Resource<CompoOffApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getCompoOffApproval(action = Constant.unApproveCompoOff, sfCode = sfCode, org = org) }

    }

//------------------------------------------POST COMPO OFF APPROVAL DETAILS--------------------------------------------------//
    suspend fun postCompoOffApprovalResponse(context: Context, empId: String, date: String, compoOffType: String,compoOffValue: String, checkIn: String, checkOut: String, CRate: Float,  divId: Int, holidayType: String, hourPerDayCR: Float, ratePerHourCR: Float,ratePerHourWeekOffCR: Float, ratePerHourFestiveCR: Float, ratePerHourNationalCR: Float,  salaryCalculation: Int, salaryCount: Float,ratePerHourNationalOT: Float,adjustSalary: Int, maxHourForFestiveHolidayOT: Float, maxHourForWeekOffOT: Float,org: Int,approvalAction: Int, rejectReason: String): Resource<CompoOffApprovalResponseModel> {

        return safeApiCall {mAPIInterface.postCompoOffApproval(action = Constant.compoOffApprove, empId, date, compoOffType, compoOffValue, checkIn, checkOut, CRate, divId, holidayType, hourPerDayCR, ratePerHourCR, ratePerHourWeekOffCR, ratePerHourFestiveCR, ratePerHourNationalCR, salaryCalculation, salaryCount, ratePerHourNationalOT, adjustSalary, maxHourForFestiveHolidayOT, maxHourForWeekOffOT, org, approvalAction, rejectReason)}

    }

    //------------------------------------------POST OVER TIME APPROVAL DETAILS--------------------------------------------------//
    suspend fun postOverTimeApprovalResponse(context: Context, empId: String, org: Int, hours: Float, ratePerHour:Float, flag: Int, checkin: String, checkout: String, weekStart: String,  weekEnd: String, month: Int, year: Int, OtHoli_rate_per_hr_National: Float,OtMax_hr_holi_Fastival: Float, OtWkend_per_hr_Weekly: Float, Extra_Hrs: Float,  OtIsMaximumHoursAllowedReq: Int, OtMaxHoursLimit: Int,OtAdjustduration: Int,IsBalanceFlowReq: Int, BalanceLaps: Int, IsApprovalFlowReq: Int,Approval: Int,Is_HoursExceeds: Int, HoursExceeds: Int,DurationOff:String,GoToApproval:String,Reject_Reason:String): Resource<OverTimeApprovalResponseModel> {

        return safeApiCall {mAPIInterface.postOverTimeApproval(action = Constant.overTimeApprove, empId, org, hours, ratePerHour, flag, checkin, checkout, weekStart, weekEnd, month, year, OtHoli_rate_per_hr_National, OtMax_hr_holi_Fastival, OtWkend_per_hr_Weekly, Extra_Hrs, OtIsMaximumHoursAllowedReq, OtMaxHoursLimit, OtAdjustduration, IsBalanceFlowReq, BalanceLaps,IsApprovalFlowReq, Approval,Is_HoursExceeds,HoursExceeds,DurationOff,GoToApproval,Reject_Reason)}

    }


    //------------------------------------------GET ATTENDANCE ON DUTY APPROVAL LIST DETAILS--------------------------------------------------//
    suspend fun getOnDutyApprovalListResponse(context: Context, sfCode: String, org: String): Resource<OnDutyApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getOnDutyApproval(action = Constant.unApproveOnDuty, sfCode = sfCode, org = org) }

    }

//------------------------------------------POST ATTENDANCE ON DUTY APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun postOnDutyApprovalResponse(context: Context, empId: String, ruleID: String, odDate: String, org: String, status: String, reason :String): Resource<OnDutyApprovalResponseModel> {

        return safeApiCall {mAPIInterface.postOnDutyApproval(action = Constant.onDutyApprove, sfCode = empId, ruleId = ruleID, odDate =odDate, org =org, flag = status, reason = reason)}

    }

    //------------------------------------------GET ATTENDANCE WORK FROM HOME APPROVAL LIST DETAILS--------------------------------------------------//
    suspend fun getWFHApprovalListResponse(context: Context, sfCode: String, org: String): Resource<WFHApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getWFHApproval(action = Constant.unApproveWFH, sfCode = sfCode, org = org) }

    }

//------------------------------------------POST ATTENDANCE ON DUTY APPROVAL LIST DETAILS--------------------------------------------------//

    suspend fun postWFHApprovalResponse(context: Context, empId: String, ruleID: String, wfhDate: String, org: String, status: String, reason: String): Resource<WFHApprovalResponseModel> {

        return safeApiCall {mAPIInterface.postWFHApproval(action = Constant.wfhApprove, sfCode = empId, ruleId = ruleID, wfhDate =wfhDate, org =org, flag = status, reason = reason) }

    }

//------------------------------------------GET ATTENDANCE ON DUTY LIST DETAILS--------------------------------------------------//

    suspend fun getOnDutyListResponse(context: Context, sfCode: String, date : String, org : String): Resource<OnDutyDetailResponseModel> {

        return safeApiCall { mAPIInterface.getOnDutyList(action = Constant.onDutyEmployeeDetail, sfCode = sfCode, setDate = date, org = org) }

    }

//------------------------------------------POST ATTENDANCE ON DUTY LIST DETAILS--------------------------------------------------//

    suspend fun postOnDutyDetailResponse(context: Context, sfCode: String, ruleID: String, date: String, odType: String, odHalfDayType: String, checkIn: String, checkOut: String, remarks: String, org: String): Resource<OnDutySaveDetailResponseModel> {

        return safeApiCall { mAPIInterface.postOnDutySaveDetails(action = Constant.onDutyDetailSave, sfCode = sfCode, odRuleId = ruleID, odFHDate = date, odFulHalfDayType = odType, odHalfDayType = odHalfDayType, odCheckIn = checkIn, odCheckOut = checkOut, odRemarks = remarks, org = org) }

    }

    //------------------------------------------ GET ATTENDANCE OVER TIME LIST DETAILS--------------------------------------------------//

    suspend fun getOverTimeListResponse(context: Context, sfCode: String, org : String): Resource<OverTimeApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getOverTimeList(action = Constant.unApproveOverTime, sfCode = sfCode, org = org) }

    }

//------------------------------------------ GET ATTENDANCE WFH LIST DETAILS--------------------------------------------------//
    suspend fun getWFHListResponse(context: Context, sfCode: String, date : String, org : String): Resource<WorkFromHomeDetailResponseModel> {
        return safeApiCall { mAPIInterface.getWFHList(action = Constant.WorkFromHomeGetEmp, sfCode = sfCode, setDate = date, org = org) }
    }

//------------------------------------------ POST ATTENDANCE WFH LIST DETAILS--------------------------------------------------//

    suspend fun postWFHDetailResponse(context: Context, sfCode: String, ruleID: String, date: String, wfhType: String, wfhHalfDayType: String, checkIn: String, checkOut: String, remarks: String, org: String): Resource<WFHSaveDetailResponseModel> {

        Log.d("Repository", "postWFHDetailResponse : empId: $sfCode, ruleID: $ruleID, date: $date, wfhType: $wfhType, wfhHalfDayType: $wfhHalfDayType, checkIn: $checkIn, checkOut:$checkOut, remarks:$remarks, org:$org ")

        return safeApiCall {mAPIInterface.postWFHSaveDetails(action = Constant.WorkFromHome_Save, sfCode = sfCode, wfhRuleId = ruleID, wfhFHDate = date, wfhFulHalfDayType = wfhType, wfhHalfDayType = wfhHalfDayType, wfhCheckIn = checkIn, wfhCheckOut = checkOut, wfhRemarks = remarks, org = org) }

    }



//------------------------------------------GET SALARY DETAILS--------------------------------------------------//

    suspend fun getSalaryResponse(context: Context, sfCode: String): Resource<SalaryDetailResponseModel> {

        return safeApiCall { mAPIInterface.getSalary(action = Constant.SalaryDatails, sfCode = sfCode) }

    }

//------------------------------------------GET LOAN DETAILS--------------------------------------------------//

    suspend fun getLoanResponse(context: Context, sfCode: String): Resource<LoanListResponseModel> {
        return safeApiCall {  mAPIInterface.getLoan(action = Constant.LoanDatails, sfCode = sfCode)}
    }

    suspend fun getLoanSubDetailResponse(context: Context, Sl_No: Int): Resource<LoansubDetailsListResponseModel> {
        return safeApiCall { mAPIInterface.getLoanSubDetail(action = Constant.LoanSubDatails, Sl_No = Sl_No)}
    }

    //------------------------------------------GET WISHES DETAILS--------------------------------------------------//
    suspend fun getWishResponse(context: Context, sfCode: String, id:Int): Resource<WishesResponseModel> {

        return safeApiCall { mAPIInterface.getWish(action = Constant.Wishes, sfCode = sfCode, id = id) }

    }

    //------------------------------------------GET ASSET DETAILS--------------------------------------------------//
    suspend fun getAssetResponse(context: Context, Emp_ID: String,): Resource<AssetListResponseModel> {
        Log.d("Repository", "getAssetResponse : empId: $Emp_ID,")
        return safeApiCall { mAPIInterface.getAsset(action = Constant.getAssetListDetails, sfCode = Emp_ID) }

    }

    //------------------------------------------GET ORG LIST --------------------------------------------------//
    suspend fun getOrgDocListResponse(context: Context, sfCode: String, id:Int): Resource< OrgDocListResponseModel> {

        return safeApiCall { mAPIInterface.getOrgDocList(action = Constant.OrgDOcList, Emp_ID = sfCode, Org = id) }

    }

    //------------------------------------------GET ORG DETAILS  --------------------------------------------------//
    suspend fun getOrgDocDetailResponse(context: Context, sfCode: String, id:Int,folderId:Int): Resource<OrgDocDetailResponseModel> {

        return safeApiCall { mAPIInterface.getOrgDocDetailList(action = Constant.OrgDocDetail, Emp_ID = sfCode, Org = id, Folder_Id = folderId) }

    }

    //------------------------------------------GET Org Doc Count DETAILS--------------------------------------------------//
    suspend fun getOrgDocShowCountResponse(context: Context, sfCode: String, org : Int,documentId:Int): Resource<OrgDocShowCountResponseModel> {

        return safeApiCall { mAPIInterface.getOrgDocShowCountDetailList(action = Constant.OrgDocCountDetail, Emp_ID = sfCode, Org = org,Document_Id=documentId)}

    }

    //------------------------------------------GET APPROVALS DETAILS--------------------------------------------------//
    suspend fun getApprovalListResponse(context: Context, sfCode: String, org:Int): Resource<ApprovalListResponseModel> {

        return safeApiCall { mAPIInterface.getApprovalList(action = Constant.ApprovalList, sfCode = sfCode, org = org) }

    }

    //------------------------------------------GET Attendance DETAILS--------------------------------------------------//
    suspend fun getAttendanceRegularizedResponse(context: Context, sfCode: String,fDT: String, tDT:String): Resource<AttendenceResponseModel> {
        return safeApiCall { mAPIInterface.getAttendance(action = Constant.Attendance, sfCode = sfCode, fDT = fDT,tDT = tDT)}

    }



    suspend fun getAttendanceRegularizedCICOResponse(context: Context, sfCode: String,DT: String): Resource<AttendanceCICOListResponseModel> {
        return safeApiCall { mAPIInterface.getAttendanceCurrentCICO(action = Constant.AttendanceListLocationAPI, sfCode = sfCode, DT = DT)}

    }
    suspend fun getAttendanceRegularizedRBHResponse(context: Context, sfCode: String,FT: String): Resource<AttendanceRBHideResponseModel> {


        return safeApiCall { mAPIInterface.getAttendanceRBH(action = Constant.MissedDatePunch, sfCode = sfCode, DT = FT)}

    }
    suspend fun getAttendanceRegularizedRulesResponse(context: Context, sfCode: String): Resource<AttendanceRegularizeRulesResponseModel> {

        return safeApiCall { mAPIInterface.getAttendanceRules(action = Constant.AttendanceRegularizeRules, sfCode = sfCode)}

    }

    suspend fun getAttendanceRegularizedCountResponse(context: Context, sfCode: String, mode : String,Fdt : String,Tdt : String): Resource<AttendanceRegularizeCountResponseModel> {
        Log.d("getDashAttendanceRegularizedResponse", "sfCode/Fdt/Tdt: $sfCode/$Fdt/$Tdt")
        return safeApiCall { mAPIInterface.getAttendanceCount(action = Constant.AttendanceRegularizeCount, sfCode = sfCode, id = mode, StartTime = Fdt, EndTime = Tdt )}

    }

    suspend fun getDashAttendanceRegularizedResponse(context: Context, sfCode: String, org : Int,MissedDate : String,StartTime : String,EndTime:String,Remarks:String): Resource<AttendanceRegularizedResponseModel> {

        Log.d("getDashAttendanceRegularizedResponse", "sfCode/org/MissedDate/StartTime/EndTime/Remarks : $sfCode/$org/$StartTime/$EndTime")
        return safeApiCall { mAPIInterface.getAttendanceRegularized(action = Constant.DashboardAttendanceRegularize, sfCode = sfCode, id = org,MissedDate=MissedDate, StartTime=StartTime,EndTime=EndTime,Remarks=Remarks)}

    }

    suspend fun getChangePasswordResponse(context: Context, sfCode: String,id: Int, password:String): Resource<ChangePasswordResponseModel> {

        return safeApiCall { mAPIInterface.getChangePassword(action = Constant.UpdateNewPassword, sfCode = sfCode, id = id, password = password)}

    }
//------------------------------------------GET CHECKIN VISIBILITY--------------------------------------------------//


    suspend fun getCheckInVisibility(context: Context, sfCode: String): Resource<CheckInOutResponseModel> {

        return safeApiCall { mAPIInterface.getCheckInVisibility(action = Constant.checkInOutVisible, sfCode = sfCode) }

    }

//------------------------------------------GET DOCUMENT DETAILS LIST--------------------------------------------------//

    suspend fun getDocumentListDetailsResponse(context: Context, empID: String): Resource<DocumentListResponseModel> {

        return safeApiCall { mAPIInterface.getDocumentDetails(action = Constant.getDocumentDetails, empID = empID) }

    }

//------------------------------------------GET DOCUMENT DETAILS--------------------------------------------------//


    suspend fun getDocumentDetailsResponse(context: Context, empID: String, type: String): Resource<DocumentDetailResponseModel> {

        return safeApiCall { mAPIInterface.getDocumentUploadDetails(action = Constant.getDocumentUploadDetails, empID = empID, type = type) }

    }

//------------------------------------------UPLOAD DOCUMENT--------------------------------------------------//

    suspend fun postDocumentUploadResponse(context: Context, docs : String ) : Resource<UploadDocumentResponseModel>
    {

        val file = File(docs)

        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val body: MultipartBody.Part = MultipartBody.Part.createFormData("document", file.name, requestFile)

        return safeApiCall { mAPIInterface.postUploadDocument(body,action = Constant.postDocumentUpload) }

    }

//------------------------------------------UPLOAD DOCUMENT DETAILS SAVE--------------------------------------------------//

    suspend fun postDocumentUploadDetailsResponse(context: Context, accountNumber: String, name: String, fatherName: String, dob: String, address: String, expiresOn: String, dateOfIssue: String, placeOfIssue: String, placeOfBirth: String, session: String, org: String, filePath: String, type: String, empId: String): Resource<DocumentUploadSubmitResponseModel>
    {
        return safeApiCall { mAPIInterface.postDocumentUploadDetails(action = Constant.postDocumentDetailsSave, accountNumber = accountNumber, name = name, fatherName = fatherName, dob = dob, address = address, expiresOn = expiresOn, dateOfIssue = dateOfIssue, placeOfIssue = placeOfIssue, placeOfBirth = placeOfBirth, session = session, org = org, filePath = filePath, type = type, empId = empId) }
    }
//------------------------------------------GET EDUCATION DOCUMENT DETAILS--------------------------------------------------//

    suspend fun getEducationDocumentDetailsResponse(context: Context, empID: String, type: String): Resource<EducationalDetailResponseModel> {
        return safeApiCall { mAPIInterface.getEducationalDocumentDetails(action = Constant.getEducationDocumentDetails, empID = empID) }
    }

//------------------------------------------GET EXPERIENCE DOCUMENT DETAILS--------------------------------------------------//

    suspend fun getExperienceDocumentDetailsResponse(context: Context, empID: String, type: String): Resource<ExperienceDetailResponseModel> {
        return safeApiCall { mAPIInterface.getExperienceDocumentDetails(action = Constant.getExperienceDocumentDetails, empID = empID) }
    }

//------------------------------------------GET OTHER DOCUMENT DETAILS--------------------------------------------------//

    suspend fun getOtherDocumentDetailsResponse(context: Context, empID: String, type: String): Resource<OtherDocumentDetailResponseModel> {

        return safeApiCall { mAPIInterface.getOtherDocumentDetails(action = Constant.getOtherDocumentDetails, empID = empID) }

    }

//------------------------------------------UPLOAD DOCUMENT DETAILS SAVE--------------------------------------------------//

    suspend fun postEducationDocumentUploadDetailsResponse(context: Context,  empId : String, degree: String, branch: String, doj: String, doc: String, university: String, location: String, filePath: String): Resource<EducationDetailSaveResponseModel>
    {

        return safeApiCall { mAPIInterface.postEducationDocumentUploadDetails(action = Constant.postEduDocumentDetailsSave, empId = empId, degree = degree, branch = branch, doj = doj, doc = doc, university = university, location = location, filePath = filePath) }

    }

//------------------------------------------UPLOAD DOCUMENT DETAILS SAVE--------------------------------------------------//

    suspend fun postExperienceDocumentUploadDetailsResponse(context: Context, empId: String, companyName: String, experienceYears: String, jobTitle: String, doj: String, dor: String, location: String, filePath: String): Resource<ExperienceDetailSaveResponseModel>
    {

        return safeApiCall { mAPIInterface.postExperienceDocumentUploadDetails(action = Constant.postExpDocumentDetailsSave, empId = empId, companyName = companyName, experienceYears = experienceYears, jobTitle = jobTitle, doj = doj, dor = dor, location = location, filePath = filePath) }

    }

//------------------------------------------UPLOAD DOCUMENT DETAILS SAVE--------------------------------------------------//

    suspend fun postOtherDocumentUploadDetailsResponse(context: Context, empId: String, documentName: String, fileName: String): Resource<OtherDocumentDetailSaveResponseModel>
    {
        return safeApiCall { mAPIInterface.postOtherDocumentUploadDetails(action = Constant.postOtherDocumentDetailsSave, empId = empId, documentName = documentName, fileName = fileName) }
    }

//------------------------------------------UPLOAD DOCUMENT DETAILS SAVE--------------------------------------------------//

    suspend fun getDegreeDetailsResponse(context: Context): Resource<DegreeDetailsResponseModel>
    {
        return safeApiCall { mAPIInterface.getDegreeDetails(action = Constant.getDegreeDetails) }
    }

//------------------------------------------GET LOAN TYPE DETAILS--------------------------------------------------//

    suspend fun getLoanEnableDetailsResponse(context: Context, empId: String, orgId: String): Resource<LoanEnableResponseModel>
    {
        return safeApiCall { mAPIInterface.getLoanEnableDetail(action = Constant.isLoanEnable, empId = empId, orgId = orgId) }
    }

//------------------------------------------GET LOAN MAPPED DETAILS--------------------------------------------------//

    suspend fun getLoanMappedDetailsResponse(context: Context, empId: String, orgId: String): Resource<LoanMappedDetailsResponseModel>
    {

        return safeApiCall { mAPIInterface.getLoanMappedDetail(action = Constant.isLoanMapped, empId = empId, orgId = orgId) }

    }

//------------------------------------------GET LOAN TYPE DETAILS--------------------------------------------------//

    suspend fun getLoanTypeDetailsResponse(context: Context, empId: String, orgId: String): Resource<LoanTypeResponseModel>
    {

        return safeApiCall { mAPIInterface.getLoanTypeDetails(action = Constant.getLoanType, empId = empId, orgId = orgId) }

    }

//------------------------------------------GET LOAN ELIGIBILITY DETAILS--------------------------------------------------//

    suspend fun getLoanEligibilityDetailsResponse(context: Context, empId: String, ruleID: String): Resource<LoanEligibleResponseModel>
    {

        return safeApiCall { mAPIInterface.getLoanEligibilityDetails(action = Constant.getLoanEligibility, empId = empId, ruleID = ruleID) }

    }

//------------------------------------------GET LOAN INSTALLMENT DETAILS--------------------------------------------------//

    suspend fun getLoanInstallmentDetailsResponse(context: Context, deductionType: String, numberOfInstallments: String, startMonth: String, loanAmount: String, interestRate: String, interestType: String): Resource<LoanInstallmentResponseModel>
    {
        return safeApiCall { mAPIInterface.getLoanInstallmentDetails(action = Constant.GetInstallmentsMonth, deductionType = deductionType, numberOfInstallments = numberOfInstallments, startMonth = startMonth, loanAmount = loanAmount, interestRate = interestRate, interestType = interestType) }

    }


//------------------------------------------POST LOAN APPLY SAVE DETAILS--------------------------------------------------//

    suspend fun postLoanSaveDetailsResponse(context: Context, empID: String, deductionType: String, loanAmount: String, month: String, year: String, numberOfInstalments: String, divId: String, orgId: String, fromDate: String, toDate: String, finalAmount: String, interestRate: String, interestRateType: String, nameInPayslip: String, loanInterestAmount: String, remarks: String, loanTypeSlNo: String, instalmentInterestType: String, loanData: String): Resource<LoanSaveResponseModel>
    {
        return safeApiCall { mAPIInterface.postLoanDetails(action = Constant.postLoanData, empID = empID, deductionType = deductionType, loanAmount = loanAmount, month = month, year = year, numberOfInstalments = numberOfInstalments, divId = divId, orgId = orgId, fromDate = fromDate, toDate = toDate, finalAmount = finalAmount, interestRate = interestRate, interestRateType = interestRateType, nameInPayslip = nameInPayslip, loanInterestAmount = loanInterestAmount, remarks = remarks, loanTypeSlNo = loanTypeSlNo, instalmentInterestType = instalmentInterestType, loanData = loanData) }
    }



//------------------------------------------GET EXISTING LOAN INSTALLMENT DETAILS--------------------------------------------------//

    suspend fun getExistingLoanDetailsResponse(context: Context, empID: String): Resource<LoanExistingPaidResponseModel>
    {
        return safeApiCall { mAPIInterface.getExistingLoanDetails(action = Constant.getExistingLoanInstallment, empID = empID) }
    }


//------------------------------------------GET UNAPPROVED LOAN DETAILS--------------------------------------------------//

    suspend fun getUnApproveLoanDetailsResponse(context: Context, empID: String, orgId: String): Resource<UnApprovedLoanDetailResponseModel>
    {
        return safeApiCall { mAPIInterface.getUnApproveLoanDetails(action = Constant.unApprovedLoan, empID = empID, orgId = orgId) }
    }


//------------------------------------------GET EXISTING LOAN INSTALLMENT DETAILS--------------------------------------------------//

    suspend fun getAppliedLoanInstalmentDetailsResponse(context: Context, empID: String, loanApplySlNo: String): Resource<AppliedLoanInstalmentDetailResponseModel>
    {
        return safeApiCall { mAPIInterface.getAppliedLoanInstalmentDetails(action = Constant.appliedLoanInstalment, empID = empID, loanApplySlNo = loanApplySlNo) }
    }

//------------------------------------------GET LOAN APPROVAL STAGES DETAILS--------------------------------------------------//

    suspend fun getLoanApprovalStagesDetailsResponse(context: Context, empID: String, loanApplySlNo: String, orgId: String): Resource<LoanApprovalStagesResponseModel>
    {
        return safeApiCall { mAPIInterface.getLoanApprovalStagesDetails(action = Constant.loanApprovalStages, empID = empID, loanApplySlNo = loanApplySlNo, orgId = orgId) }
    }

//------------------------------------------POST LOAN APPROVAL DETAILS--------------------------------------------------//

    suspend fun postLoanApprovalDetailsResponse(context: Context, orgId: String, loanApplySlNo: String, empId: String, appEmpId: String, status: String, reason: String): Resource<LoanApprovalResponseModel>
    {
        return safeApiCall { mAPIInterface.postLoanApprovalDetails(action = Constant.loanApprovalSave, orgId = orgId.toInt(), loanApplySlNo = loanApplySlNo.toInt(), empID = empId, appEmpId = appEmpId, status = status, reason = reason) }
    }

//------------------------------------------POST LOAN EDIT APPROVAL DETAILS--------------------------------------------------//

    suspend fun postLoanEditApproveDetailsResponse(context: Context, empID: String, deductionType: Int, loanAmount: Float, month: Int, year: Int, numberOfInstalments: Int, divId: Int, orgId: Int, fromDate: String, toDate: String, finalAmount: Float, interestRate: Float, interestRateType: Int, nameInPayslip: String, loanInterestAmount: Float, remarks: String, loanTypeSlNo: Int, loanApplySlNo: Int, approverEmpId: String, loanSanctionDate: String, instalmentInterestType: Float, loanData: String): Resource<LoanEditApproveResponseModel>
    {
        return safeApiCall { mAPIInterface.postLoanEditApproveDetails(action = Constant.loanEditApprovalSave,empID = empID, deductionType = deductionType, loanAmount = loanAmount, month = month, year = year, numberOfInstalments = numberOfInstalments, divId = divId, orgId = orgId, fromDate = fromDate, toDate = toDate, finalAmount = finalAmount, interestRate = interestRate, interestRateType = interestRateType, nameInPayslip = nameInPayslip, loanInterestAmount = loanInterestAmount, remarks = remarks, loanTypeSlNo = loanTypeSlNo, loanApplySlNo = loanApplySlNo, approverEmpId = approverEmpId, loanSanctionDate = loanSanctionDate, instalmentInterestType = instalmentInterestType, loanData = loanData) }
    }

//------------------------------------------GET LOAN TYPE DETAILS--------------------------------------------------//

    suspend fun getEmployeeProfileDetailsResponse(context: Context, empId: String, orgId: String): Resource<EmployeeProfileResponseModel>
    {
        return safeApiCall { mAPIInterface.getEmployeeProfileDetails(action = Constant.employeeProfile, empID = empId, orgId = orgId.toInt()) }
    }


}