package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.getLoginDetails
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Profile(navController: NavController, profileViewModel: ProfileViewModel) {

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Profile Overview",
            "HomeScreen"
        ) },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { Profile_Screen(profileViewModel = profileViewModel) }
}
@Composable
fun Profile_Screen(profileViewModel: ProfileViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 5.dp)

    ) {

        Column {
                Profile_details(profileViewModel)
        }

        Column {
            TabContent_holder()
        }


     /*   LazyColumn {
            item {
                Profile_details()
            }
            item {
                TabContent_holder()
            }
        }*/
    }
}

@Composable
fun Profile_details(profileViewModel: ProfileViewModel) {

    val employeeDetails = profileViewModel.employeeDetails.collectAsState()
    Log.d("Profile  ", "Employee Data List : ${employeeDetails.value}")

    Column(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        val jsonObject = com.payroll.twogrowhr.getEmployeeDetails()
        val jsonObject1 = getLoginDetails()

        var imgUrl = ""
        var empName= ""
        var designation = ""

        if (jsonObject != null) {
            val firstName = jsonObject.getString("Name")
            val lastName = jsonObject.getString("LName")
            empName = "$firstName $lastName"
            designation = jsonObject.getString("Designation_Name")
            imgUrl = jsonObject.getString("FilePath")
        }

        if (jsonObject1 != null)
        {
            val profile = jsonObject1.getString("Profile")
            imgUrl = profile
            Log.d("ImgUrl", imgUrl)
        }


        val link = Constant.IMAGE_URL + "Images/EmpUpload/"
        val img = imgUrl // Assuming `wedding.Profile` contains the relative image path
        val profile = link + img
        Log.d("Profile...", profile)
        val processedImgUrl = profile.ifEmpty { link }


        if (processedImgUrl != link)
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(processedImgUrl).crossfade(true).transformations(
                    CircleCropTransformation()
                ).allowHardware(true).build(),
                placeholder = painterResource(R.drawable.capa),
                contentDescription = "icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(120.dp)
            )
        }
        else
        {
            // If profile is empty or null, display a default image
            Image(
                painter = painterResource(id = R.drawable.nonimgicon),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        Text(text = empName,
            style = MaterialTheme.typography.titleLarge,
            color = colorResource(id = R.color.black),
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(text = designation,
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
@Composable
fun TabContent_holder() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column {


        Constant.AppTheme {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                containerColor = Color.White,
                ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    modifier = Modifier.padding(10.dp),
                    selectedContentColor = colorResource(id = R.color.themeColor),
                    unselectedContentColor = colorResource(id = R.color.paraColor)
                ) {
                    Text(text = "Basic Info", style = MaterialTheme.typography.titleMedium)
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    modifier = Modifier.padding(10.dp),
                    selectedContentColor = colorResource(id = R.color.themeColor),
                    unselectedContentColor = colorResource(id = R.color.paraColor)
                ) {
                    Text(text = "Work Info", style = MaterialTheme.typography.titleMedium)
                }

            }
        }


        LazyColumn {
            item {
                when (selectedTabIndex)
                {
                    0 -> TabContent()
                    1 -> TabContent_2()
                }
            }
        }

    }
}
@Composable
fun TabContent() {
    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxWidth(1f)
        .padding(22.dp)
    ) {
        val jsonObject = com.payroll.twogrowhr.getEmployeeDetails()
        var fatherName = ""
        var empPerNo = ""
        var personalEmail = ""
        var dob = ""
        var dow = ""
        var gender = ""
        var bloodGrp = ""
        val address1: String
        val address2: String
        var address=""
        var aadhaar = ""
        var bankAccNumber = ""
        var bankName = ""
        var ifscCode = ""
        var panNo = ""
        var pincode = ""

        if (jsonObject != null)
        {
            //BASIC INFO
            fatherName = jsonObject.getString("FatherName")
            empPerNo = jsonObject.getString("mobile")
            personalEmail = jsonObject.getString("Email")
            dob = jsonObject.getString("DOB")
            dow = jsonObject.getString("DOW")
            gender = jsonObject.getString("gender")
            bloodGrp = jsonObject.getString("BloodGrp")
            address1 = jsonObject.getString("Address")
            address2 = jsonObject.getString("Address1")
            address = address1+address2
            aadhaar = jsonObject.getString("Adhr")
            bankAccNumber = jsonObject.getString("BankAc")
            bankName = jsonObject.getString("Bank")
            ifscCode = jsonObject.getString("ifc_code")
            panNo = jsonObject.getString("Pan")
            pincode = jsonObject.getString("Pincode")

        }



        Text(text = "Father’s Name", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = fatherName,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Personal No",style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empPerNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Email", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = personalEmail,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Gender", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = gender, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Birth",style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = dob, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Wedding", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = dow, style = MaterialTheme.typography.titleMedium, color = colorResource(
            id = R.color.black
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Blood Group", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bloodGrp,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Address", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = address,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Pin Code", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = pincode,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Aadhaar Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = aadhaar,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Bank Account Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bankAccNumber,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Bank Name", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bankName,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "IFSC CODE", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = ifscCode,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "PAN Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = panNo,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )
    }

}
@Composable
fun TabContent_2() {
    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxWidth(1f)
        .padding(22.dp)
    ) {
        val jsonObject = com.payroll.twogrowhr.getEmployeeDetails()

        var empID = ""
        var empOffNo = ""
        var empEmergencyNo = ""
        var department = ""
        var doj = ""
        var workLocation = ""
//        var offMail = ""
        var uanNo = ""
        var epfNo = ""
        var esiNo = ""
        var healthInsurance = ""
        var state = ""


        if (jsonObject != null)
        {
            //WORK INFO

            empID = jsonObject.getString("Emp_Code")
            empOffNo = jsonObject.getString("OfficeNo")
            empEmergencyNo = jsonObject.getString("EmergencyNo")
            department = jsonObject.getString("Dep_Name")
            doj = jsonObject.getString("Doj")
            state = jsonObject.getString("State_Name")
            workLocation = jsonObject.getString("WorkLoc")
//            offMail = jsonObject.getString("Email")
            uanNo = jsonObject.getString("UAN")
            epfNo = jsonObject.getString("EPFN")
            esiNo = jsonObject.getString("ESIN")
            healthInsurance = jsonObject.getString("Health")

        }


        Text(text = "Employee ID",
            style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empID,  style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Office No",  style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empOffNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Emergency No", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empEmergencyNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Department", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = department, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Joining", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = doj, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "State", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = state,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Work Location", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = workLocation, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "UAN Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))
        Text(text = uanNo , style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "EPF Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))
        Text(text = epfNo,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "ESI Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = esiNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Health Insurance Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = healthInsurance, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )
    }

}




@Composable
@Preview
fun Profile_detailsPreview() {

    Column(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {


        val empName= "Matheesha Pathirana"
        val designation = "Software Engineer"


        val link = Constant.IMAGE_URL + "Images/EmpUpload/"
        val processedImgUrl = Constant.IMAGE_URL + "Images/EmpUpload/"


        if (processedImgUrl != link)
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(processedImgUrl).crossfade(true).transformations(
                    CircleCropTransformation()
                ).allowHardware(true).build(),
                placeholder = painterResource(R.drawable.capa),
                contentDescription = "icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(120.dp)
            )
        }
        else
        {
            // If profile is empty or null, display a default image
            Image(
                painter = painterResource(id = R.drawable.nonimgicon),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        Text(text = empName,
            style = MaterialTheme.typography.titleLarge,
            color = colorResource(id = R.color.black),
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(text = designation,
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
@Preview
fun TabContent_holderPreview() {

    val selectedTabIndex = 0

    Column {


        Constant.AppTheme {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                containerColor = Color.White,
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {  },
                    modifier = Modifier.padding(10.dp),
                    selectedContentColor = colorResource(id = R.color.themeColor),
                    unselectedContentColor = colorResource(id = R.color.paraColor)
                ) {
                    Text(text = "Basic Info", style = MaterialTheme.typography.titleMedium)
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {  },
                    modifier = Modifier.padding(10.dp),
                    selectedContentColor = colorResource(id = R.color.themeColor),
                    unselectedContentColor = colorResource(id = R.color.paraColor)
                ) {
                    Text(text = "Work Info", style = MaterialTheme.typography.titleMedium)
                }

            }
        }


        LazyColumn {
            item {
                when (selectedTabIndex)
                {
                    0 -> TabContentPreview()
                    1 -> TabContentPreview_2()
                }
            }
        }

    }
}
@Composable
@Preview
fun TabContentPreview() {
    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxWidth(1f)
        .padding(22.dp)
    ) {

        var fatherName = "Kumar"
        var empPerNo = "9865063214"
        var personalEmail = "pathirana@gmail.com"
        var dob = "26 Mar 1998"
        var dow = "03 Sep 2021"
        var gender = "Male"
        var bloodGrp = "B +ve"
        var address = "No 123, ABC Road, Chennai - 91"
        var aadhaar = "1234 5678 9102"
        var bankAccNumber = "1234567891235"
        var bankName = "State Bank Of India"
        var ifscCode = "SBIN000002584"
        var panNo = "QWERT1234Y"
        var pincode = "600091"



        Text(text = "Father’s Name", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = fatherName,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Personal No",style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empPerNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Email", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = personalEmail,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Gender", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = gender, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Birth",style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = dob, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Wedding", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = dow, style = MaterialTheme.typography.titleMedium, color = colorResource(
            id = R.color.black
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Blood Group", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bloodGrp,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Address", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = address,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Pin Code", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = pincode,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Aadhaar Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = aadhaar,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Bank Account Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bankAccNumber,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Bank Name", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = bankName,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "IFSC CODE", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = ifscCode,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "PAN Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = panNo,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )
    }

}
@Composable
@Preview
fun TabContentPreview_2() {
    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxWidth(1f)
        .padding(22.dp)
    ) {

        var empID = "EMP123"
        var empOffNo = "8569321475"
        var empEmergencyNo = "9632587412"
        var department = "Payroll"
        var doj = "22 Feb 2022"
        var workLocation = "Chennai"
        var uanNo = "1031999745821"
        var epfNo = "TN25413698745"
        var esiNo = "ESI546546154+"
        var healthInsurance = "687846546"
        var state = "TamilNadu"



        Text(text = "Employee ID",
            style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 12.sp, fontWeight = FontWeight(500),
                color = colorResource(id = R.color.paraColor)
            ))

        Text(text = empID,  style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Office No",  style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empOffNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Emergency No", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = empEmergencyNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Department", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = department, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Date of Joining", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = doj, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "State", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = state,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Work Location", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = workLocation, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "UAN Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))
        Text(text = uanNo , style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "EPF Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))
        Text(text = epfNo,style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "ESI Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = esiNo, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )

        Text(text = "Health Insurance Number", style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 12.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.paraColor)
        ))

        Text(text = healthInsurance, style = TextStyle(
            fontFamily = poppins_font,
            fontSize = 14.sp, fontWeight = FontWeight(500),
            color = colorResource(id = R.color.black)
        ))

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = colorResource(id = R.color.divider)
        )
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiProfilePreview() {

    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Profile Overview", "HomeScreen") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 5.dp)
        ) {

            Column {
                Profile_detailsPreview()
            }

            Column {
                TabContent_holderPreview()
            }
        }
    }
}
