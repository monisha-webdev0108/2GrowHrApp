package com.payroll.twogrowhr.Model

import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.R

sealed class MainCardModel(
    open val data: List<WishesData>,
    open val title: String = "",
    open val bgColor: Int = R.color.white,
    open val iconColor: Int = R.color.pink,
    open val onClick: () -> Unit,
    open val image: Int = R.drawable.birthday_icon,
    open val contentDescription: String = "birthday",
    open val defaultText: String = "No Birthday"
) {
    data class RemoteWorkModel(
        override val data: List<WishesData>,
        override val title: String = "On Remote work",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.red,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.remote_work_icon,
        override val contentDescription: String = "ring",
        override val defaultText: String = "No one is on Remote work"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )
    data class OnDutyModel(
        override val data: List<WishesData>,
        override val title: String = "On Duty",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.red,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.on_duty_icon,
        override val contentDescription: String = "ring",
        override val defaultText: String = "No one is on duty"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )
    data class LeaveModel(
        override val data: List<WishesData>,
        override val title: String = "On Leave",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.green,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.leave_icon,
        override val contentDescription: String = "ring",
        override val defaultText: String = "No one is on leave"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )
    data class WeddingModel(
        override val data: List<WishesData>,
        override val title: String = "Wedding Anniversary",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.green,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.wedding_icon,
        override val contentDescription: String = "ring",
        override val defaultText: String = "No Wedding Anniversary"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )

    data class BirthdayModel(
        override val data: List<WishesData>,
        override val title: String = "Birthdays",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.pink,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.birthday_icon,
        override val contentDescription: String = "birthday",
        override val defaultText: String = "No Birthday"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )

    data class WorkAnniversaryModel(
        override val data: List<WishesData>,
        override val title: String = "Work Anniversary",
        override val bgColor: Int = R.color.white,
        override val iconColor: Int = R.color.yellow,
        override val onClick: () -> Unit,
        override val image: Int = R.drawable.work_icon,
        override val contentDescription: String = "Anniversary",
        override val defaultText: String = "No Work Anniversary"
    ) : MainCardModel(
        data = data,
        title = title,
        bgColor = bgColor,
        iconColor = iconColor,
        onClick = onClick,
        image = image,
        contentDescription = contentDescription,
        defaultText = defaultText
    )
    
}