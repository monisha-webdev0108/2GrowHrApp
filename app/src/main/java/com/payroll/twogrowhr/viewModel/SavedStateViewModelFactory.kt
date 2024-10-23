package com.payroll.twogrowhr.viewModel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.payroll.twogrowhr.Model.ResponseModel.UserData
import com.payroll.twogrowhr.repository.Repository


@Suppress("UNCHECKED_CAST")
class SavedStateViewModelFactory(
    private val repository: Repository? = null,
    private val userData: UserData? = null,
    defaultArgs: Bundle? = null,
    savedStateRegistryOwner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(
    savedStateRegistryOwner,
    defaultArgs
) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return when {

            modelClass.isAssignableFrom(UserDataViewModel::class.java) -> {
                UserDataViewModel(userData = userData!!) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(CheckInViewModel::class.java) -> {
                CheckInViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(HolidayViewModel::class.java) -> {
                HolidayViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(LoanDetailViewModel::class.java) -> {
                LoanDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(LoanSubDetailsListViewModel::class.java) -> {
                LoanSubDetailsListViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(DocumentViewModel::class.java) -> {
                DocumentViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(PaySlipViewModel::class.java) -> {
                PaySlipViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(TdsFormViewModel::class.java) -> {
                TdsFormViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(LeaveViewModel::class.java) -> {
                LeaveViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(RegularizedApprovalViewModel::class.java) -> {
                RegularizedApprovalViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(ApprovalListViewModel::class.java) -> {
                ApprovalListViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OnDutyViewModel::class.java) -> {
                OnDutyViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(WorkFromHomeViewModel::class.java) -> {
                WorkFromHomeViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(SalaryDetailViewModel::class.java) -> {
                SalaryDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(BirthdayDetailViewModel::class.java) -> {
                BirthdayDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(WeddingDetailViewModel::class.java) -> {
                WeddingDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(WorkDetailViewModel::class.java) -> {
                WorkDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(HomeWishesViewModel::class.java) -> {
                HomeWishesViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OnLeaveViewModel::class.java) -> {
                OnLeaveViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OnDutyListViewModel::class.java) -> {
                OnDutyListViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OverTimeViewModel::class.java) -> {
                OverTimeViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OnRWViewModel::class.java) -> {
                OnRWViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(AttendanceRegularizedViewModel::class.java) -> {
                AttendanceRegularizedViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(ChangePasswordView::class.java) -> {
                ChangePasswordView(repository = repository!!) as T
            }

            modelClass.isAssignableFrom(OrgDocListViewModel::class.java) -> {
                OrgDocListViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(OrgDocDetailViewModel::class.java) -> {
                OrgDocDetailViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(AssetsListViewModel::class.java) -> {
                AssetsListViewModel(repository = repository!!, savedStateHandle = handle) as T
            }

            modelClass.isAssignableFrom(PaySlipHeadDetailsViewModel::class.java) -> {
                PaySlipHeadDetailsViewModel(repository = repository!!, savedStateHandle = handle) as T
            }



            else -> throw IllegalArgumentException("wrong ViewModel")
        }
    }
}