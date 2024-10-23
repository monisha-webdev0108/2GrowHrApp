package com.payroll.twogrowhr.viewModel

import androidx.lifecycle.ViewModel
import com.payroll.twogrowhr.Model.ResponseModel.UserData


class UserDataViewModel(
    private val userData: UserData
) : ViewModel() {

    fun getDivisionName() = userData.Division_Name

    fun getDivisionCode() = userData.Division_Code

    fun isMobileCheckIn() = userData.mobile_check_in == 1

    fun isImageCaptureNeed() = userData.imageCaptureNeed == 1

    fun isCameraFlipNeed() = userData.cameraFlip == 1

    fun hasLocationCheckNeed() = userData.LocBasedCheckNeed == 1

    fun hasGeoFencing() = userData.Geo_Fencing == "1"

    fun hasShiftSelection() = userData.Shift_Selection == "1"

    fun getSFCode() = userData.Sf_code

    fun getOrg() = userData.org

    fun getSFName() = userData.Sf_Name

    fun getProfile() = userData.Profile

}