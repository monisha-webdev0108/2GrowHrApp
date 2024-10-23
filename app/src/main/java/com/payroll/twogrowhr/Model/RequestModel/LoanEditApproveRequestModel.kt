package com.payroll.twogrowhr.Model.RequestModel


data class LoanEditApproveRequestModel
(
    var sfCode: String,
    var deductionType: String,
    var loanAmount: String,
    var month: String,
    var year: String,
    var numberOfInstalment: String,
    var divId: String,
    var orgId: String,
    var fromDate: String,
    var toDate: String,
    var finalAmount: String,
    var InterestRate: String,
    var InterestRateType: String,
    var nameInPayslip: String,
    var loanInterestAmount: String,
    var Remarks: String,
    var loanTypeSlNo: String,
    var loanApplySlNo: String,
    var approverEmpId: String,
    var loanSanctionDate: String,
    var instalmentInterestAmount: String,
    var loanData: String

)