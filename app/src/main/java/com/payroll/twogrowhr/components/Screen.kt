package com.payroll.twogrowhr.components

import com.payroll.twogrowhr.R

sealed class Screen(val route: String, val label: String,val icon: Int) {

    object PrivacyPolicy : Screen("PrivacyPolicy", "PrivacyPolicy",icon = R.drawable.ic_launcher_foreground)

    object Network : Screen("Network/{url}", "Network",icon = R.drawable.ic_launcher_foreground)

    object Login : Screen("Login", "Login",icon = R.drawable.ic_launcher_foreground)

    object HomeScreen : Screen("HomeScreen", "Home", icon = R.drawable.home)

    object OnDutyScreen : Screen("OnDutyScreen", "OnDutyScreen", icon = R.drawable.ic_launcher_foreground)

    object RemoteWorkRequest : Screen("RemoteWorkRequest", "RemoteWorkRequest", icon = R.drawable.home)

    object HomeScreenTwo : Screen("HomeScreenTwo", "Home", icon = R.drawable.home)

    object CameraScreen : Screen("CameraScreen", "CameraScreen", icon = R.drawable.ic_launcher_foreground)

    object Attendance : Screen("attendance", "Attendance",icon = R.drawable.attendance)

    object AttendanceRegularized : Screen("AttendanceRegularized", "AttendanceRegularized",icon = R.drawable.attendance)

    object Reports : Screen("reports", "Reports",icon = R.drawable.reports)

    object Finance : Screen("finance", "Finance",icon = R.drawable.finance)

    object Birthday : Screen("birthday", "Birthday",icon = R.drawable.ic_launcher_foreground)

    object Payslip : Screen("payslip", "Payslip",icon = R.drawable.ic_launcher_foreground)

    object FormTds : Screen("formtds", "FormTds",icon = R.drawable.ic_launcher_foreground)

    object Salary : Screen("salary", "Salary",icon = R.drawable.ic_launcher_foreground)

    object ApplyLoan : Screen("ApplyLoan", "ApplyLoan",icon = R.drawable.ic_launcher_foreground)

    object LoanApprovalList : Screen("LoanApprovalList", "LoanApprovalList",icon = R.drawable.ic_launcher_foreground)

    object LoanApprovalDetail : Screen("LoanApprovalDetail", "LoanApprovalDetail",icon = R.drawable.ic_launcher_foreground)

    object EditReporteeLoan : Screen("EditReporteeLoan", "EditReporteeLoan",icon = R.drawable.ic_launcher_foreground)

    object LoanHistoryList : Screen("LoanHistoryList", "LoanHistoryList",icon = R.drawable.ic_launcher_foreground)

    object LoanApprovalStages : Screen("LoanApprovalStages", "LoanApprovalStages",icon = R.drawable.ic_launcher_foreground)

    object LoanApply : Screen("LoanApply", "LoanApply",icon = R.drawable.ic_launcher_foreground)

    object LoanInstalmentDetails : Screen("LoanInstalmentDetails/{loanName}/{nameInPayslip}/{loanType}/{deductionType}/{numberOfInstalments}/{startMonth}/{loanAmount}/{interestRate}/{interestRateType}/{salaryAmount}/{selectedDeductionType}/{repayableAmount}/{deductionMonth}", "LoanInstalmentDetails",icon = R.drawable.ic_launcher_foreground)

    object Loan : Screen("loan", "Loan",icon = R.drawable.ic_launcher_foreground)

    object LoanView : Screen("loan_view", "Loan_view",icon = R.drawable.ic_launcher_foreground)

    object Profile : Screen("profile", "Profile",icon = R.drawable.ic_launcher_foreground)

    object Updates : Screen("updates", "Updates",icon = R.drawable.ic_launcher_foreground)
    object ViewPayslip : Screen("viewPayslip", "ViewPayslip",icon = R.drawable.ic_launcher_foreground)

    object FakeGPS : Screen("fakeGPS", "fakeGPS",icon = R.drawable.ic_launcher_foreground)

    object FakeTime : Screen("fakeTime", "fakeTime",icon = R.drawable.ic_launcher_foreground)

    object MyDocument : Screen("my_document", "My_document",icon = R.drawable.ic_launcher_foreground)
    object Asset : Screen("my_assets", "My_asset",icon = R.drawable.ic_launcher_foreground)

    object PanCard : Screen("PanCard/{type}", "PanCard",icon = R.drawable.ic_launcher_foreground)

    object PassportCard : Screen("PassportCard/{type}", "PassportCard",icon = R.drawable.ic_launcher_foreground)

    object AadhaarCard : Screen("AadhaarCard/{type}", "AadhaarCard",icon = R.drawable.ic_launcher_foreground)///{type}

    object VoterIDDoc : Screen("VoterIDDoc/{type}", "VoterIDDoc",icon = R.drawable.ic_launcher_foreground)

    object DrivingLicenseDoc : Screen("DrivingLicenseDoc/{type}", "DrivingLicenseDoc",icon = R.drawable.ic_launcher_foreground)

    object EducationDocList : Screen("EducationDocList/{type}", "EducationDocList",icon = R.drawable.ic_launcher_foreground)

    object EducationDoc : Screen("EducationDoc/{type}", "EducationDoc",icon = R.drawable.ic_launcher_foreground)

    object ExperienceDocumentList : Screen("ExperienceDocumentList/{type}", "ExperienceDocumentList",icon = R.drawable.ic_launcher_foreground)

    object ExperienceDoc : Screen("ExperienceDoc/{type}", "ExperienceDoc",icon = R.drawable.ic_launcher_foreground)

    object OtherDoc : Screen("OtherDoc/{type}", "OtherDoc",icon = R.drawable.ic_launcher_foreground)

    object OtherDocList : Screen("OtherDocList/{type}", "OtherDocList",icon = R.drawable.ic_launcher_foreground)

    object OrgDocList : Screen("OrgDocList", "OrgDocList",icon = R.drawable.ic_launcher_foreground)

    object OrgDoc : Screen("OrgDoc", "OrgDoc",icon = R.drawable.ic_launcher_foreground)

    object Wedding : Screen("wedding", "Wedding",icon = R.drawable.ic_launcher_foreground)

    object Work : Screen("work", "Work",icon = R.drawable.ic_launcher_foreground)

    object OnLeave : Screen("OnLeave", "OnLeave",icon = R.drawable.ic_launcher_foreground)

    object OnDutyList : Screen("OnDutyList", "OnDutyList",icon = R.drawable.ic_launcher_foreground)

    object OnRW : Screen("OnRW", "OnRW",icon = R.drawable.ic_launcher_foreground)

    object Holiday : Screen("holiday", "Holiday",icon = R.drawable.ic_launcher_foreground)

    object Approval : Screen("approval", "Approval",icon = R.drawable.ic_launcher_foreground)

    object ApprovalList : Screen("approvallist", "ApprovalList",icon = R.drawable.ic_launcher_foreground)

    object Notify : Screen("notify", "Notify",icon = R.drawable.ic_launcher_foreground)

    object LeaveApprovals : Screen("leave_approvals/{empName}/{slNo}/{empId}", "Leave_approvals",icon = R.drawable.ic_launcher_foreground)

    object ChangePassword : Screen("change_password", "Change_password",icon = R.drawable.ic_launcher_foreground)

    object Leave : Screen("leave", "Leave",icon = R.drawable.ic_launcher_foreground)

    object LeaveHistory : Screen("leaveHistory", "leaveHistory",icon = R.drawable.ic_launcher_foreground)

    object LeaveHistoryDetail : Screen("leaveHistoryDetail/{slNo}/{empId}/{balance}", "leaveHistoryDetail",icon = R.drawable.ic_launcher_foreground)

    object LeaveApply : Screen("leaveApply", "LeaveApply",icon = R.drawable.ic_launcher_foreground)

    object ApplyLeaveNew : Screen("applyLeave", "applyLeave",icon = R.drawable.ic_launcher_foreground)

    object TeamAttendanceDetailView : Screen("teamAttendance_DetailView", "TeamAttendance_DetailView",icon = R.drawable.ic_launcher_foreground)

    object TeamAttendance : Screen("teamAttendance", "TeamAttendance",icon = R.drawable.ic_launcher_foreground)

    object RegularizedApproval : Screen("regularizedApproval", "RegularizedApproval",icon = R.drawable.ic_launcher_foreground)

    object LeaveListApproval : Screen("leaveListApproval", "LeaveListApproval",icon = R.drawable.ic_launcher_foreground)

    object ODApproval : Screen("odApproval", "ODApproval",icon = R.drawable.ic_launcher_foreground)

    object OTApproval : Screen("otApproval", "OTApproval",icon = R.drawable.ic_launcher_foreground)

    object CompoOffApproval : Screen("CompoOffApproval", "CompoOffApproval",icon = R.drawable.ic_launcher_foreground)

    object CompoOffApprovalDetail : Screen("CompoOffApprovalDetail/{date}/{empId}/{empName}", "CompoOffApprovalDetail",icon = R.drawable.ic_launcher_foreground)

    object WFHApproval : Screen("wfhApproval", "WFHApproval",icon = R.drawable.ic_launcher_foreground)

    object Currentlocation : Screen("Currentlocation", "Currentlocation",icon = R.drawable.ic_launcher_foreground)

    object CheckInLocation : Screen("CheckInLocation", "CheckInLocation",icon = R.drawable.ic_launcher_foreground)

    object ShiftSelection : Screen("ShiftSelection", "ShiftSelection",icon = R.drawable.ic_launcher_foreground)

    object HomeTwo : Screen("Home_two", "Home_two",icon = R.drawable.ic_launcher_foreground)

}