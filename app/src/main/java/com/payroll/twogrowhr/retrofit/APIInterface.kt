package com.payroll.twogrowhr.retrofit


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
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface APIInterface {


    @POST("apipayroll")
    suspend fun getUserData(
        @Query("axn") action: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponseModel>

    @POST("apipayroll")
    suspend fun getEmployeeDetails(
        @Query("axn") action: String,
        @Query("Emp_ID") empId: String,
    ):  Response<JsonObject>


    @POST("apipayroll")
    suspend fun postCheckInData(
        @Query("axn") action: String,
        @Query("data") checkInData: String
    ): Response<SaveCheckInDetailsResponseModel>

    @POST("apipayroll")
    suspend fun postCheckOutData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("data") checkOutData: String
    ): Response<SaveCheckOutDetailsResponseModel>


    @Multipart
    @POST("apipayroll")
    suspend fun postCapturedImage(
        @Part file: MultipartBody.Part,
        @Query("axn") action: String
    ): Response<UploadImageResponseModel>


    @POST("apipayroll")
    suspend fun getHolidayData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("year") year: String
    ): Response<HolidayResponseModel>


    @POST("apipayroll")
    suspend fun getPayslipData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("year") year: String
    ): Response<PayslipAPIResponseModel>

    @POST("apipayroll")
    suspend fun getPayslipHeadData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("Month") Month: String,
        @Query("Year") Year: String,

    ): Response<PaySlipHeadDetailsResponseModel>

    @POST("apipayroll")
    suspend fun getPayslipEarningData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("Month") Month: String,
        @Query("Year") Year: String,

    ): Response<PaySlipEarningDetailsResponseModel>

    @POST("apipayroll")
    suspend fun getPayslipDeductionData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("Month") Month: String,
        @Query("Year") Year: String,
    ): Response<PaySlipDeductionDetailsResponseModel>

    @POST("apipayroll")
    suspend fun getPayslipOtherDeductionData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("Month") Month: String,
        @Query("Year") Year: String,
    ): Response<PaySlipOtherDeductionResponseModel>

    @POST("apipayroll")
    suspend fun getTdsFormData(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,
        @Query("year") year: String
    ): Response<TdsFormResponseModel>

    @POST("apipayroll")
    suspend fun getEmpShiftData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
    ): Response<EmployeeShiftDetailsResponseModel>

    @POST("apipayroll")
    suspend fun getGeoData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String
    ): Response<GeoResponseModel>

    @POST("apipayroll")
    suspend fun getShiftData(
        @Query("axn") action: String,
        @Query("divisionCode") divcode: String,
        @Query("sf_Code") sfCode: String,
        @Query("rSF") id: String,
        @Query("State_Code") stateCode: String
    ): Response<ShiftResponseModel>

    @POST("apipayroll")
    suspend fun getTodaysLogData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("Divcode") divcode: String

    ): Response<TodayAttendanceLogResponseModel>


    @POST("apipayroll")
    suspend fun getLeaveData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
    ): Response<LeaveTypeDetailResponseModel>

    @POST("apipayroll")
    suspend fun getLeaveDatesList(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("f_Date") fromDate: String,
        @Query("l_Date") toDate: String,
        @Query("slno") slno: String,

        ): Response<LeaveDateListResponseModel>


    @Multipart
    @POST("apipayroll")
    suspend fun postLeaveDocument(
        @Part file: MultipartBody.Part,
        @Query("axn") action: String
    ): Response<UploadLeaveDocsResponseModel>


    @POST("apipayroll")
    suspend fun postLeaveFormValidate(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("data") data: String,
        ): Response<LeaveFormValidateResponseModel>

    @POST("apipayroll")
    suspend fun postLeaveDetails(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("data") data: String,
    ): Response<LeaveSubmitResponseModel>


    @POST("apipayroll")
    suspend fun getLeaveHistoryData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
    ): Response<LeaveHistoryResponseModel>


    @POST("apipayroll")
    suspend fun getLeaveHistoryDetailData(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("sl") sl_no: String,
        ): Response<LeaveHistoryDetailResponseModel>


    @POST("apipayroll")
    suspend fun getLeaveListApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
    ): Response<LeaveApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun getLeaveDetailListApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("sl") ruleId: String,
    ): Response<LeaveApprovalDetailListResponseModel>

    @POST("apipayroll")
    suspend fun postLeaveListApprovalDetails(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("sl") slNo: String,
        @Query("data") data: String,
    ): Response<LeaveApprovalSubmitResponseModel>


    @POST("apipayroll")
    suspend fun getCompoOffApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("org") org: String
    ): Response<CompoOffApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun postCompoOffApproval(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("Date") date: String,
        @Query("combooff") compoOffType: String,
        @Query("txtval") compoOffValue: String,
        @Query("CCheckin") checkIn: String,
        @Query("CCheckOut") checkOut: String,
        @Query("CRate") CRate: Float,
        @Query("div") divId: Int,
        @Query("CHolidayType") holidayType: String,
        @Query("CRHr_Per_Day") hourPerDayCR: Float,
        @Query("CRRate_Per_Hr") ratePerHourCR: Float,
        @Query("CRWoff_Hr") ratePerHourWeekOffCR: Float,
        @Query("CRFestive_Hr") ratePerHourFestiveCR: Float,
        @Query("CRNat_Holi_Hr") ratePerHourNationalCR: Float,
        @Query("SalaryCalculation") salaryCalculation: Int,
        @Query("SalaryCount") salaryCount: Float,
        @Query("OtHoli_rate_per_hr_National") ratePerHourNationalOT: Float,
        @Query("AdjustSalary") adjustSalary: Int,
        @Query("OtMax_hr_holi_Fastival") maxHourForFestiveHolidayOT: Float,
        @Query("OtWkend_per_hr_Weelky") maxHourForWeekOffOT: Float,
        @Query("org") org: Int,
        @Query("Flag") approvalAction: Int,
        @Query("Reject_Reason") rejectReason: String,
    ): Response<CompoOffApprovalResponseModel>

    @POST("apipayroll")
    suspend fun postOverTimeApproval(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("org") org: Int,
        @Query("Hours") hours: Float,
        @Query("RatePerHour") ratePerHour: Float,
        @Query("Flag") flag: Int,
        @Query("checkin") checkin: String,
        @Query("checkout") checkout: String,
        @Query("WeekStart") weekStart: String,
        @Query("WeekEnd") weekEnd: String,
        @Query("Month") month: Int,
        @Query("Year") year: Int,
        @Query("OtHoli_rate_per_hr_National") otHoli_rate_per_hr_National: Float,
        @Query("OtMax_hr_holi_Fastival") otMax_hr_holi_Fastival: Float,
        @Query("OtWkend_per_hr_Weekly") otWkend_per_hr_Weekly: Float,
        @Query("Extra_Hrs") extra_Hrs: Float,
        @Query("OtIsMaximumHoursAllowedReq") otIsMaximumHoursAllowedReq: Int,
        @Query("OtMaxHoursLimit") otMaxHoursLimit: Int,
        @Query("OtAdjustduration") otAdjustduration: Int,
        @Query("IsBalanceFlowReq") isBalanceFlowReq: Int,
        @Query("BalanceLaps") balanceLaps: Int,
        @Query("IsApprovalFlowReq") isApprovalFlowReq: Int,
        @Query("Approval") approval: Int,
        @Query("Is_HoursExceeds") is_HoursExceeds: Int,
        @Query("HoursExceeds") hoursExceeds: Int,
        @Query("DurationOff") durationOff: String,
        @Query("GoToApproval") goToApproval: String,
        @Query("Reject_Reason") rejectReason: String,
    ): Response<OverTimeApprovalResponseModel>



    @POST("apipayroll")
    suspend fun getAttendanceRegularizeApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String
    ): Response<RegularizeApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun postRegularizeApproval(
        @Query("axn") action: String,
        @Query("sl_no") sl_no: String,
        @Query("App") App: String
    ): Response<RegularizeApprovalResponseModel>



    @POST("apipayroll")
    suspend fun getOnDutyApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("org") org: String
    ): Response<OnDutyApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun postOnDutyApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("ODRuleid") ruleId: String,
        @Query("ODdate") odDate: String,
        @Query("org") org: String,
        @Query("flg") flag: String,
        @Query("reason") reason: String,
        ): Response<OnDutyApprovalResponseModel>

    @POST("apipayroll")
    suspend fun getWFHApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("org") org: String
    ): Response<WFHApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun postWFHApproval(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("WFHRuleid") ruleId: String,
        @Query("WFHdate") wfhDate: String,
        @Query("org") org: String,
        @Query("flg") flag: String,
        @Query("reason") reason: String,
        ): Response<WFHApprovalResponseModel>

    @POST("apipayroll")
    suspend fun getOnDutyList(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("setDate") setDate: String,
        @Query("org") org: String,
    ): Response<OnDutyDetailResponseModel>

    @POST("apipayroll")
    suspend fun getOverTimeList(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("org") org: String,
    ): Response<OverTimeApprovalListResponseModel>


    @POST("apipayroll")
    suspend fun postOnDutySaveDetails(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("ODRuleid") odRuleId: String,
        @Query("ODFHdate") odFHDate: String,
        @Query("ODFulHalfDayType") odFulHalfDayType: String,
        @Query("ODHalfDayType") odHalfDayType: String,
        @Query("ODCheckIn") odCheckIn: String,
        @Query("ODCheckOut") odCheckOut: String,
        @Query("ODRemarks") odRemarks: String,
        @Query("org") org: String
    ) : Response<OnDutySaveDetailResponseModel>


    @POST("apipayroll")
    suspend fun getWFHList(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("setDate") setDate: String,
        @Query("org") org: String,
    ): Response<WorkFromHomeDetailResponseModel>


    @POST("apipayroll")
    suspend fun postWFHSaveDetails(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("WRuleid") wfhRuleId: String,
        @Query("WFHdate") wfhFHDate: String,
        @Query("WFulHalfDayType") wfhFulHalfDayType: String,
        @Query("WHalfDayType") wfhHalfDayType: String,
        @Query("WCheckIn") wfhCheckIn: String,
        @Query("WCheckOut") wfhCheckOut: String,
        @Query("WRemarks") wfhRemarks: String,
        @Query("org") org: String
    ) : Response<WFHSaveDetailResponseModel>


    @POST("apipayroll")
    suspend fun getSalary(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String
    ): Response<SalaryDetailResponseModel>

    @POST("apipayroll")
    suspend fun getLoan(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String
    ): Response<LoanListResponseModel>

    @POST("apipayroll")
    suspend fun getLoanSubDetail(
        @Query("axn") action: String,
        @Query("Sl_No") Sl_No: Int
    ): Response<LoansubDetailsListResponseModel>

    @POST("apipayroll")
    suspend fun getWish(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("id") id: Int
    ): Response<WishesResponseModel>

    @POST("apipayroll")
    suspend fun getAsset(
        @Query("axn") action: String,
        @Query("Emp_ID") sfCode: String,

    ): Response<AssetListResponseModel>

    @POST("apipayroll")
    suspend fun getApprovalList(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("org") org: Int
    ): Response<ApprovalListResponseModel>

    @POST("apipayroll")
    suspend fun getAttendance(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("fDT") fDT: String,
        @Query("tDT") tDT: String
    ): Response<AttendenceResponseModel>

    @POST("apipayroll")
    suspend fun getAttendanceCurrentCICO(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("DT") DT: String
    ): Response<AttendanceCICOListResponseModel>

    @POST("apipayroll")
    suspend fun getAttendanceRBH(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("FT") DT: String
    ): Response<AttendanceRBHideResponseModel>

    @POST("apipayroll")
    suspend fun getOrgDocList(
        @Query("axn") action: String,
        @Query("Emp_ID") Emp_ID: String,
        @Query("Org") Org: Int
    ): Response<OrgDocListResponseModel>

    @POST("apipayroll")
    suspend fun getOrgDocDetailList(
        @Query("axn") action: String,
        @Query("Emp_ID") Emp_ID: String,
        @Query("Org") Org: Int,
        @Query("Folder_Id") Folder_Id: Int
    ): Response<OrgDocDetailResponseModel>

    @POST("apipayroll")
    suspend fun getOrgDocShowCountDetailList(
        @Query("axn") action: String,
        @Query("Emp_ID") Emp_ID: String,
        @Query("Org") Org: Int,
        @Query("Document_Id") Document_Id: Int
    ): Response<OrgDocShowCountResponseModel>

    @POST("apipayroll")
    suspend fun getAttendanceRules(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        ): Response<AttendanceRegularizeRulesResponseModel>

    @POST("apipayroll")
    suspend fun getAttendanceCount(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("mode") id: String,
        @Query("Fdt") StartTime: String,
        @Query("Tdt") EndTime: String
    ): Response<AttendanceRegularizeCountResponseModel>

    @POST("apipayroll")
    suspend fun getAttendanceRegularized(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("id") id: Int,
        @Query("MissedDate") MissedDate: String,
        @Query("StartTime") StartTime: String,
        @Query("EndTime") EndTime: String,
        @Query("Remarks") Remarks: String
    ): Response<AttendanceRegularizedResponseModel>

    @POST("apipayroll")
    suspend fun getChangePassword(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String,
        @Query("id") id: Int,
        @Query("password") password: String
    ): Response<ChangePasswordResponseModel>


    @POST("apipayroll")
    suspend fun getCheckInVisibility(
        @Query("axn") action: String,
        @Query("sfCode") sfCode: String
    ): Response<CheckInOutResponseModel>

    @POST("apipayroll")
    suspend fun getDocumentDetails(
        @Query("axn") action: String,
        @Query("Emp_ID") empID: String
    ): Response<DocumentListResponseModel>

    @POST("apipayroll")
    suspend fun getDocumentUploadDetails(
        @Query("axn") action: String,
        @Query("Emp_ID") empID: String,
        @Query("Type") type: String
    ): Response<DocumentDetailResponseModel>

    @Multipart
    @POST("apipayroll")
    suspend fun postUploadDocument(
        @Part file: MultipartBody.Part,
        @Query("axn") action: String
    ): Response<UploadDocumentResponseModel>

    @POST("apipayroll")
    suspend fun postDocumentUploadDetails(
        @Query("axn") action: String,
        @Query("AccountNo") accountNumber: String,
        @Query("name") name: String,
        @Query("fathername") fatherName: String,
        @Query("dob") dob: String,
        @Query("address") address: String,
        @Query("expireson") expiresOn: String,
        @Query("dateofissue") dateOfIssue: String,
        @Query("placeofissue") placeOfIssue: String,
        @Query("placeofbirth") placeOfBirth: String,
        @Query("session") session: String,
        @Query("org") org: String,
        @Query("filepath") filePath: String,
        @Query("type") type: String,
        @Query("Emp_Id") empId: String

    ): Response<DocumentUploadSubmitResponseModel>

    @POST("apipayroll")
    suspend fun getEducationalDocumentDetails(
        @Query("axn") action: String,
        @Query("empid") empID: String,
    ): Response<EducationalDetailResponseModel>

    @POST("apipayroll")
    suspend fun getExperienceDocumentDetails(
        @Query("axn") action: String,
        @Query("empid") empID: String,
    ): Response<ExperienceDetailResponseModel>

    @POST("apipayroll")
    suspend fun getOtherDocumentDetails(
        @Query("axn") action: String,
        @Query("empid") empID: String,
    ): Response<OtherDocumentDetailResponseModel>



    @POST("apipayroll")
    suspend fun postEducationDocumentUploadDetails(
        @Query("axn") action: String,
        @Query("EmpId") empId: String,
        @Query("Degree") degree: String,
        @Query("Brch_Spcl") branch: String,
        @Query("Date_Of_Joining") doj: String,
        @Query("Date_Of_Completion") doc: String,
        @Query("Uni_clg") university: String,
        @Query("Location") location: String,
        @Query("filepath") filePath: String

    ): Response<EducationDetailSaveResponseModel>


    @POST("apipayroll")
    suspend fun postExperienceDocumentUploadDetails(
        @Query("axn") action: String,
        @Query("EmpId") empId: String,
        @Query("Cmpy_Name") companyName: String,
        @Query("Exp_In_Years") experienceYears: String,
        @Query("Job_Title") jobTitle: String,
        @Query("Date_Of_Joining") doj: String,
        @Query("Date_Of_Relieving") dor: String,
        @Query("Location") location: String,
        @Query("filepath") filePath: String
    ): Response<ExperienceDetailSaveResponseModel>

    @POST("apipayroll")
    suspend fun postOtherDocumentUploadDetails(
        @Query("axn") action: String,
        @Query("EmpId") empId: String,
        @Query("Fname") documentName: String,
        @Query("Fil") fileName: String,

    ): Response<OtherDocumentDetailSaveResponseModel>


    @POST("apipayroll")
    suspend fun getDegreeDetails(
        @Query("axn") action: String,
        ): Response<DegreeDetailsResponseModel>


    @POST("apipayroll")
    suspend fun getLoanEnableDetail(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("orgid") orgId: String,

        ): Response<LoanEnableResponseModel>

    @POST("apipayroll")
    suspend fun getLoanMappedDetail(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("org") orgId: String,

        ): Response<LoanMappedDetailsResponseModel>

    @POST("apipayroll")
    suspend fun getLoanTypeDetails(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("orgId") orgId: String,

        ): Response<LoanTypeResponseModel>


    @POST("apipayroll")
    suspend fun getLoanEligibilityDetails(
        @Query("axn") action: String,
        @Query("sfCode") empId: String,
        @Query("ruleId") ruleID: String,

        ): Response<LoanEligibleResponseModel>


    @POST("apipayroll")
    suspend fun getLoanInstallmentDetails(
        @Query("axn") action: String,
        @Query("dedType") deductionType: String,
        @Query("nofmonth") numberOfInstallments: String,
        @Query("startmon") startMonth: String,
        @Query("amount") loanAmount: String,
        @Query("interest") interestRate: String,
        @Query("interestRateType") interestType: String,

        ): Response<LoanInstallmentResponseModel>


    @POST("apipayroll")
    suspend fun postLoanDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("Deduction_Type") deductionType: String,
        @Query("Loan_Amt") loanAmount: String,
        @Query("Month") month: String,
        @Query("Year") year: String,
        @Query("Nmonth") numberOfInstalments: String,
        @Query("Div") divId: String,
        @Query("Org") orgId: String,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("final_amt") finalAmount: String,
        @Query("Interest_rate") interestRate: String,
        @Query("interestRateType") interestRateType: String,
        @Query("Nameinpay") nameInPayslip: String,
        @Query("Loan_Intrest_Amount") loanInterestAmount: String,
        @Query("Remarks") remarks: String,
        @Query("loanTypeSlNo") loanTypeSlNo: String,
        @Query("InstalIntrest_Amt") instalmentInterestType: String,
        @Query("Lxml") loanData: String,

        ): Response<LoanSaveResponseModel>

    @POST("apipayroll")
    suspend fun getExistingLoanDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String

        ): Response<LoanExistingPaidResponseModel>

    @POST("apipayroll")
    suspend fun getUnApproveLoanDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("orgid") orgId: String

    ): Response<UnApprovedLoanDetailResponseModel>

    @POST("apipayroll")
    suspend fun getAppliedLoanInstalmentDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("LoanApplySl_No") loanApplySlNo: String

    ): Response<AppliedLoanInstalmentDetailResponseModel>

    @POST("apipayroll")
    suspend fun getLoanApprovalStagesDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("loanApplySlNo") loanApplySlNo: String,
        @Query("orgId") orgId: String

    ): Response<LoanApprovalStagesResponseModel>

    @POST("apipayroll")
    suspend fun postLoanApprovalDetails(
        @Query("axn") action: String,
        @Query("orgId") orgId: Int,
        @Query("loanApplySlNo") loanApplySlNo: Int,
        @Query("sfCode") empID: String,
        @Query("appEmpId") appEmpId: String,
        @Query("status") status: String,
        @Query("reason") reason: String

    ): Response<LoanApprovalResponseModel>


    @POST("apipayroll")
    suspend fun postLoanEditApproveDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("deductionType") deductionType: Int,
        @Query("loanAmount") loanAmount: Float,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("numberOfMonth") numberOfInstalments: Int,
        @Query("div") divId: Int,
        @Query("org") orgId: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("finalAmount") finalAmount: Float,
        @Query("interestRate") interestRate: Float,
        @Query("interestRateType") interestRateType: Int,
        @Query("nameInPayslip") nameInPayslip: String,
        @Query("loanIntrestAmount") loanInterestAmount: Float,
        @Query("remarks") remarks: String,
        @Query("loanTypeSlNo") loanTypeSlNo: Int,
        @Query("loanApplySlNo") loanApplySlNo: Int,
        @Query("approverEmpId") approverEmpId: String,
        @Query("loanSanctionDate") loanSanctionDate: String,
        @Query("instalInterestAmount") instalmentInterestType: Float,
        @Query("Lxml") loanData: String,

        ): Response<LoanEditApproveResponseModel>


    @POST("apipayroll")
    suspend fun getEmployeeProfileDetails(
        @Query("axn") action: String,
        @Query("sfCode") empID: String,
        @Query("orgId") orgId: Int

    ): Response<EmployeeProfileResponseModel>

    /*
        @POST("apipayroll")
        fun getTeamAttendanceAsJsonObject(
            @Query("axn") action: String,
            @Query("sfCode") sfCode: String,
            @Query("Date") year: String
        ): Call<JsonObject>

        @POST("apipayroll")
        fun getAttendanceCurrentDateAsJsonObject(
            @Query("axn") action: String,
            @Query("sfCode") sfCode: String
        ): Call<JsonObject>

        @POST("apipayroll")
        fun getAttendanceRegularizeApprovalAsJsonObject(
            @Query("axn") action: String,
            @Query("sfCode") sfCode: String
        ): Call<JsonObject>

        @POST("apipayroll")
        fun getTeamAttendanceDetailAsJsonObject(
            @Query("axn") action: String,
            @Query("sfCode") sfCode: String,
            @Query("DT") id: String
        ): Call<JsonObject>

        @POST("apipayroll")
        fun postRegularizeApprovalAsJsonObject(
            @Query("axn") action: String,
            @Query("sl_no") sl_no: String,
            @Query("App") App: String
        ): Call<JsonObject>

    */

}