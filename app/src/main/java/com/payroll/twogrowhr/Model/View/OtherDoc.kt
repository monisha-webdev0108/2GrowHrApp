package com.payroll.twogrowhr.Model.View

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.data.loadImages
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.MandatoryTextField
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.containsOnlySpecialCharacters
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.getFileName1
import com.payroll.twogrowhr.components.isFileSizeValid1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.components.viewSelectedFile
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.DocumentViewModel
import com.payroll.twogrowhr.viewModel.statusLoading

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtherDoc(navController: NavController, documentViewModel: DocumentViewModel, type : String) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Other Document",
            "${Screen.OtherDocList.route}/${type}"
        ) },
        drawerContent = {},
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { Other_Screen(navController = navController, documentViewModel = documentViewModel, type = type)}
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Other_Screen(navController: NavController, documentViewModel: DocumentViewModel, type : String) {



    var documentName by remember { mutableStateOf("") }

    var uploadFileName by remember { mutableStateOf("") }


    val context = androidx.compose.ui.platform.LocalContext.current

/*

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapResults ->
        mapResults.forEach {
            Log.d("Permissions", "Permission: ${it.key} Status: ${it.value}")
        }
        if (mapResults.values.all { it })
        {
            Constant.showToast(context, "Permission Granted")
        }
        else
        {
            Constant.showToast(context, "Permission Denied, Please give the permission to access..!")
        }
    }

*/

    var showDialog by remember { mutableStateOf(false) }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapResults ->
        mapResults.forEach {
            Log.d("Permissions", "Permission: ${it.key} Status: ${it.value}")
        }

        Log.d("Permissions", "Permissionssss: ${mapResults.values}")

        // check if any of the requested permissions is granted or not
        when
        {
            mapResults.values.all { it } -> { Constant.showToast(context, "Permission Granted") }
            mapResults.values.none { it } -> { Constant.showToast(context, "All Permissions Denied, Please give the permission to access..!") }
            else -> { Constant.showToast(context, "Permission Granted") }

        }

        showDialog = false

    }

    val empId = userViewModel.getSFCode()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
    {


        //FOR RECEIVED LIST RESPONSE

        if(statusLoading.value)
        {
            circularProgression1(statusLoading.value)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .weight(0.85f)
        ) {


            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)) {

                //DOCUMENT NAME

                MandatoryTextField(label = "Document Name")

                Box(modifier = Modifier.padding(bottom = 10.dp)) {
                    val containerColor = colorResource(id = R.color.white)
                    TextField(

                        value = documentName,
                        onValueChange = {newValue ->
                            val filteredValue = newValue.replace("\\s+".toRegex(), " ")
                            documentName = filteredValue
                        },

                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.divider), // Change this to your desired border color
                                shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                            )
                    )

                }
                


                val contentResolver = androidx.compose.ui.platform.LocalContext.current.contentResolver             // Obtain the ContentResolver using LocalContext


                val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    if (uri != null) {

                        val mimeType = contentResolver.getType(uri)
                        if (mimeType != null && ((mimeType.startsWith("image/jpeg") || mimeType == "image/jpeg") ||  mimeType == "image/jpg" || mimeType == "application/pdf") || mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || mimeType == "application/msword")
                        {

                            // Check if the selected file size is valid
                            if (isFileSizeValid1(context, uri))
                            {

                                fileUri.value = uri // Handle the returned URI
                                fileName.value = getFileName1(uri,context)
                                filePath.value = fileName.value
                                uploadFileName = getFileName1(uri,context)

                                Log.d("Other Document Details","File size is valid. Proceed with processing.")
                            }
                            else
                            {
                                Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                Log.d("Other Document Details","File size exceeds the maximum allowed size.")
                            }

                        }
                        else
                        {
                            Constant.showToast(context,"Please select an jpeg, jpg, pdf or word file")
                        }

                    }

                }


                @RequiresApi(34)
                @RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
                @Composable
                fun MediaStoreQueryDialog( onDismiss: () -> Unit, onConfirm: (Uri, String) -> Unit) {

                    val files by loadImages(context.contentResolver)

                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = { Text("Select File") },

                        text = {

                            Column {
                                // Display the number of images found

                                Card(modifier = Modifier.fillMaxWidth(1f),
                                    shape = RoundedCornerShape(8.dp))
                                {
                                    Row(modifier = Modifier.fillMaxWidth())
                                    {
                                        Column(modifier = Modifier.weight(0.75f).padding(start = 5.dp))
                                        {
                                            Text(
                                                text = "you've given access to a select number of photos",
                                                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                                style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(400)),
                                                color = colorResource(id = R.color.black)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(0.25f).padding(start = 2.dp, end = 5.dp).align(Alignment.CenterVertically))
                                        {
                                            Card(modifier = Modifier.padding(1.dp).align(Alignment.CenterHorizontally).clickable {
                                                showDialog = false
                                                launcherMultiplePermissions.launch(arrayOf(READ_MEDIA_IMAGES,READ_MEDIA_VISUAL_USER_SELECTED)) },
                                                shape = RoundedCornerShape(8.dp))
                                            {
                                                TextFieldNameSizeValidation(value = "Manage", size = 13, color = colorResource(id = R.color.themeColor), weight = 500, maxLength = 10)
                                            }
                                        }

                                    }
                                }

                                if(files.isNotEmpty())
                                {
                                    Text(
                                        text = "${files.size} images found",
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .fillMaxWidth(),
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }

                                // Display the images in a grid with 3 columns
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 300.dp) // Set a maximum height for the grid
                                ) {
                                    items(files.size) { index ->
                                        val file = files[index]
                                        Column(
                                            modifier = Modifier
                                                .padding(3.dp)
                                                .clickable {
                                                    val mimeType = contentResolver.getType(file.uri)
                                                    if (mimeType != null && ((mimeType.startsWith("image/jpeg") || mimeType == "image/jpeg") ||  mimeType == "image/jpg" || mimeType == "application/pdf") || mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || mimeType == "application/msword")
                                                    {


                                                        if (isFileSizeValid1(context, file.uri))
                                                        {
                                                            onConfirm(file.uri, getFileName(file.uri, context))
                                                            Log.d("Other Doc","File size is valid. Proceed with processing.")
                                                        }
                                                        else
                                                        {
                                                            Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                                            Log.d("Other Doc","File size exceeds the maximum allowed size.")
                                                        }

                                                    }
                                                    else
                                                    {
                                                        Constant.showToast(context,"Please select an jpeg, jpg, pdf or word file")
                                                    }
                                                }
                                        ) {
                                            AsyncImage(
                                                model = file.uri,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(128.dp)
                                                    .aspectRatio(1f)
                                            )
                                            Text(
                                                text = file.name,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )

                                            Text(
                                                text = Formatter.formatShortFileSize(context, file.size),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                        },
                        confirmButton = {

                            Row()
                            {
                                TextButton(onClick = onDismiss) {
                                    Text("Close")
                                }
                            }

                        }
                    )

                }


                if(showDialog)
                {

                    MediaStoreQueryDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { uri, fileName1 ->

                            fileUri.value = uri // Handle the returned URI
                            fileName.value = fileName1
                            filePath.value = fileName.value
                            uploadFileName = fileName1

                            showDialog = false
                        }
                    )
                }

                /*Upload document*/

                if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                {
                    Divider(modifier = Modifier.padding( top = 10.dp,bottom = 10.dp),color = colorResource(id = R.color.lightshade))

                    Row(verticalAlignment = Alignment.CenterVertically){

                        if (uploadFileName.length > 20) {
                            PlainTooltipBox(
                                tooltip = {
                                    Text(
                                        text = uploadFileName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }
                            ) {
                                Text(
                                    text = uploadFileName.take(20) + "..." ,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    ),
                                    modifier = Modifier
                                        .tooltipTrigger()
                                        .clickable { viewSelectedFile(context, fileUri.value.toString())  }
                                )
                            }
                        }
                        else
                        {
                            Text(
                                text = uploadFileName,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),
                                modifier = Modifier.clickable { viewSelectedFile(context, fileUri.value.toString()) }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))


                        Icon(painterResource(id = R.drawable.fi_rs_trash)  , contentDescription ="trash" ,tint = colorResource(id = R.color.red),
                            modifier = Modifier.clickable { filePathClearance1() }
                        )
                    }
                }
                else
                {
                    Button(
                        onClick = {

                            filePathClearance1()

                            val granted = checkAndRequestStoragePermissions(
                                context = context,
                                launcher = launcherMultiplePermissions
                            )

                            Log.d("granted value", "$granted")

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE  )
                            {
                                if(ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
                                {
                                    getContent.launch("*/*")
                                }
                                else showDialog = ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED
                            }
                            else
                            {
                                getContent.launch("*/*")
                            }

                        },
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_green)),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.green))
                    )
                    {
                        Row {
                            Icon(
                                painterResource(id = R.drawable.singleplus),
                                contentDescription = "calender",
                                tint = colorResource(id = R.color.green),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 5.dp, end = 5.dp)
                            )
                            Text(
                                text = "Upload document",
                                color = colorResource(id = R.color.green),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                    }


                    Row(modifier = Modifier.padding(top = 10.dp))
                    {

                        Column()
                        {
                            PlainTooltipBox(
                                tooltip = {
                                    Text(
                                        text = "Max File size not exceeding 2MB",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }
                            ) {

                                Image(
                                    painter = painterResource(id = R.drawable.information),
                                    contentDescription = "Information",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .tooltipTrigger(),
                                    contentScale = ContentScale.Crop
                                )

                            }
                        }

                        Column(modifier = Modifier.padding(start = 10.dp))
                        {
                            Text(
                                text = "File Should be in jpeg, jpg, pdf, word Format.",
                                color = colorResource(id = R.color.black),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }

                    }


                }

            }
        }

        Box(modifier = Modifier
            .padding(top = 5.dp)
            .weight(0.13f)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                shape = RoundedCornerShape(0),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                    Button(
                        onClick = {

                            Log.d("Other Document Details", "Other Document Details submitted : Document Name / FileName :  $documentName / ${fileName.value}")

                            if(documentName.isEmpty() || documentName.isBlank())
                            {
                                Constant.showToast(context, "Please enter the Document Name")
                            }
                            else if(containsOnlySpecialCharacters(documentName))
                            {
                                documentName = ""
                                Constant.showToast(context, "Please enter the valid Document Name")
                            }
                            else if(fileName.value.isEmpty())
                            {
                                Constant.showToast(context, "Please select the document to upload")
                            }
                            else {
                                Log.d("Other Document Details", "Other Document Details submitted : Document Name / FileName : $documentName / ${fileName.value}")

                                statusLoading.value = true

                                documentViewModel.uploadFile1(context = context, fileUri = fileUri.value!!, navController = navController, degree_CompanyName = "", experienceYears = "", qualification_DesignationName = "", location = "", doj = "", doc_dor = "", universityName = "", documentName = documentName, filePath = fileName.value, type = type, empId = empId)

                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(vertical = 15.dp)
                    ) {
                        Text(
                            text = "Save",
                            color = colorResource(id = R.color.white),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiOtherDocumentPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Other Document", "url") },
        drawerContent = {},
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {

        var documentName by remember { mutableStateOf("") }

        val uploadFileName by remember { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
        {


            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(0.85f)
            ) {


                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)) {

                    //DOCUMENT NAME

                    MandatoryTextField(label = "Document Name")

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = documentName,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.replace("\\s+".toRegex(), " ")
                                documentName = filteredValue
                            },

                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.divider), // Change this to your desired border color
                                    shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                )
                        )

                    }

                    /*Upload document*/

                    if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                    {
                        Divider(modifier = Modifier.padding( top = 10.dp,bottom = 10.dp),color = colorResource(id = R.color.lightshade))

                        Row(verticalAlignment = Alignment.CenterVertically){

                            if (uploadFileName.length > 20) {
                                PlainTooltipBox(
                                    tooltip = {
                                        Text(
                                            text = uploadFileName,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight(500),
                                            ),
                                        )
                                    }
                                ) {
                                    Text(
                                        text = uploadFileName.take(20) + "..." ,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)
                                        ),
                                        modifier = Modifier.tooltipTrigger()
                                    )
                                }
                            }
                            else
                            {
                                Text(
                                    text = uploadFileName,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    ),
                                    modifier = Modifier
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            Icon(painterResource(id = R.drawable.fi_rs_trash)  , contentDescription ="trash" ,tint = colorResource(id = R.color.red),
                                modifier = Modifier
                            )
                        }
                    }
                    else
                    {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_green)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                            border = BorderStroke(1.dp, colorResource(id = R.color.green))
                        )
                        {
                            Row {
                                Icon(
                                    painterResource(id = R.drawable.singleplus),
                                    contentDescription = "calender",
                                    tint = colorResource(id = R.color.green),
                                    modifier = Modifier.size(16.dp).padding(top = 5.dp, end = 5.dp)
                                )
                                Text(
                                    text = "Upload document",
                                    color = colorResource(id = R.color.green),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                        }


                        Row(modifier = Modifier.padding(top = 10.dp))
                        {

                            Column()
                            {
                                PlainTooltipBox(
                                    tooltip = {
                                        Text(
                                            text = "Max File size not exceeding 2MB",
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                            ),
                                        )
                                    }
                                ) {

                                    Image(
                                        painter = painterResource(id = R.drawable.information),
                                        contentDescription = "Information",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .tooltipTrigger(),
                                        contentScale = ContentScale.Crop
                                    )

                                }
                            }

                            Column(modifier = Modifier.padding(start = 10.dp))
                            {
                                Text(
                                    text = "File Should be in jpeg, jpg, pdf, word Format.",
                                    color = colorResource(id = R.color.black),
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier
                .padding(top = 5.dp)
                .weight(0.13f)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(0),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = "Save",
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}