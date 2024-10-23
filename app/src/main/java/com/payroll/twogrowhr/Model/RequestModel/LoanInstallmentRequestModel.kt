package com.payroll.twogrowhr.Model.RequestModel



data class LoanInstallmentRequestModel(
    var startMonth: String,
    var numberOfInstallments: String,
    var loanAmount: String,
    var interestRate: String,
    var interestType: String,
    var deductionType: String
)

