package com.payroll.twogrowhr.viewModel

import androidx.lifecycle.ViewModel
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceCurrentDateData

class AttendanceViewModel(val data: List<AttendanceCurrentDateData> = emptyList()) : ViewModel() {
    fun getCheckinTimeList() = data.map { it.checkin_time }

    fun getCheckoutTimeList() = data.map { it.checkout_time }

    fun getCheckinLocList() = with(data) {
        ifEmpty { return listOf("No logged entries") }
        map { it.checkinLoc.ifEmpty { "No logged entries" } }

    }

    fun getCheckoutLocList() = with(data) {
        ifEmpty { return listOf("No logged entries") }
        map { it.checoutLoc.ifEmpty { "No logged entries" } }
    }
}