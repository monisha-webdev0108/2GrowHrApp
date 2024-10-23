package com.payroll.twogrowhr.components

import android.annotation.SuppressLint
import java.net.URL
import java.net.URLConnection
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.Model.View.fileName
import com.payroll.twogrowhr.Model.View.filePath
import com.payroll.twogrowhr.Model.View.fileUri
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.ui.theme.poppins_font
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.Locale

@Composable
fun MandatoryTextField(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label,
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(bottom = 10.dp))
        Text(
            text = "*",
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.red),
            modifier = Modifier.padding(bottom = 10.dp)
//            modifier = Modifier.padding(start = 4.dp) // Add some spacing between the label and the asterisk
        )
    }
}

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileNameSizeValidation(fileName: String, fileUrl: String) {

    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (fileName.length > 20) {
            PlainTooltipBox(
                tooltip = {
                    Text(
                        text = fileName,
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 13.sp,
                            fontWeight = FontWeight(500),
                        ),
                    )
                }
            ) {
                Text(
                    text = fileName.take(20) + "..." ,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 13.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.themeColor)
                    ),
                    modifier = Modifier
                        .tooltipTrigger()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl))
                            context.startActivity(intent)
                        }
                )
            }
        }
        else
        {
            Text(
                text = fileName,
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 13.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl))
                        context.startActivity(intent)
                    }
            )
        }
    }
}


@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldNameSizeValidation(value: String, size: Int, color: Color, weight: Int, maxLength: Int) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (value.length > maxLength) {
            PlainTooltipBox(
                tooltip = {
                    Text(
                        text = value,
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 13.sp,
                            fontWeight = FontWeight(400),
                        ),
                    )
                }
            ) {
                Text(
                    text = value.take(maxLength) + "..." ,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = size.sp,
                        fontWeight = FontWeight(weight),
                        color = color
                    ),
                    modifier = Modifier.tooltipTrigger())
            }
        }
        else
        {
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = size.sp,
                    fontWeight = FontWeight(weight),
                    color = color
                ),
            )
        }
    }
}



// Function to validate the selected file size
fun isFileSizeValid1(context: Context, uri: Uri): Boolean {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    inputStream?.use { stream ->
        val fileSize = stream.available().toLong()
        return fileSize <= Constant.MAX_FILE_SIZE_BYTES
    }
    return false
}


fun getFileName1(uri: Uri, context: Context): String {

    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(displayNameIndex)
            }
        }
        "Unknown File"
    } catch (e: Exception) {
        "Unknown File"
    }
}


//To View the selected file

fun viewSelectedFile(context: Context, fileUri: String) {

    try
    {

        val uri = Uri.parse(fileUri)
        val mimeType = context.contentResolver.getType(uri)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure that the viewer opens in a new task


        // Determine the MIME type and open the file accordingly
//        when (mimeType)
//        {
//            "application/pdf" -> {
//                intent.setDataAndType(uri, "application/pdf")
//            }
//            else -> {
//                intent.setDataAndType(uri, "image/*")
//            }
//        }


        when (mimeType) {
            "application/pdf" -> {
                intent.setDataAndType(uri, "application/pdf")
            }

            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword" -> {
                intent.setDataAndType(uri, "application/msword") // DOCX // DOC
            }

            "image/jpeg", "image/jpg" -> {
                intent.setDataAndType(uri, "image/*") // Image files
            }

            else -> {
                intent.setDataAndType(uri, "*/*") // If MIME type is not recognized, open with generic type
            }
        }


        Log.d("aadhaar", "aadhaar mimeType : $mimeType")

        context.startActivity(intent)
    }
    catch (e: ActivityNotFoundException)
    {

        Log.d("aadhaar", "aadhaar exception: ${e.message}")
        Constant.showToast(context, "No application found to open the file") // Handle if no application is available to view the file
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun downloadImageWithProgression(navController : NavController, imageLink: String, title: String, navigatePath : String, showLoading : MutableState<Boolean>, context: Context) {

    Log.d("Download", "imageLink : $imageLink")
    Log.d("Download", "title : $title")


    if (!Constant.isNetworkAvailable(context))
    {
        Constant.showToast(context, "Please check your network connection")
        navController.navigate("${Screen.Network.route}/$navigatePath")
    }
    else
    {
        showLoading.value = true


        try
        {
            val request = DownloadManager.Request(Uri.parse(imageLink))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(title)
            request.setDescription("Downloading...")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Show download progress in notification
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title) // File destination
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            val mimeType = downloadManager.getMimeTypeForDownloadedFile(downloadId)


            Log.d("Download", "Mime Type : $mimeType")


            if (mimeType.equals("application/pdf", ignoreCase = true))
            {
                val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title)
                val pdfUri = FileProvider.getUriForFile(context, "com.payroll.twogrowhr.fileprovider", pdfFile)

                val pdfIntent = Intent(Intent.ACTION_VIEW)
                pdfIntent.setDataAndType(pdfUri, "application/pdf")
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(pdfIntent)
            }
            else
            {

                val onComplete = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (id == downloadId)
                        {
                            showLoading.value = false
                            Constant.showToast(context, "Download completed")// Download completed, show toast
                            context.unregisterReceiver(this) // Unregister the BroadcastReceiver to avoid memory leaks
                        }
                    }
                }

                context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED)


            }

            // Register a BroadcastReceiver to listen for the completion of the download
        }
        catch(e: Exception)
        {
            Constant.showToast(context, e.message.toString())
        }

    }


}


@RequiresApi(Build.VERSION_CODES.O)
fun downloadImage(navController : NavController, imageLink: String, title: String, navigatePath : String, context: Context) {

    Log.d("Download", "imageLink : $imageLink")
    Log.d("Download", "title : $title")


    if (!Constant.isNetworkAvailable(context))
    {
        Constant.showToast(context, "Please check your network connection")
        navController.navigate("${Screen.Network.route}/$navigatePath")
    }
    else
    {

        val request = DownloadManager.Request(Uri.parse(imageLink))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(title)
        request.setDescription("Downloading...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Show download progress in notification
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title) // File destination
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        val mimeType = downloadManager.getMimeTypeForDownloadedFile(downloadId)


        Log.d("Download", "Mime Type : $mimeType")


        if (mimeType.equals("application/pdf", ignoreCase = true))
        {
            val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), title)
            val pdfUri = FileProvider.getUriForFile(context, "com.payroll.twogrowhr.fileprovider", pdfFile)

            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(pdfUri, "application/pdf")
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(pdfIntent)
        }
        else
        {

            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId)
                    {
                        Constant.showToast(context, "Download completed")// Download completed, show toast
                        context.unregisterReceiver(this) // Unregister the BroadcastReceiver to avoid memory leaks
                    }
                }
            }

            context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED)


        }

        // Register a BroadcastReceiver to listen for the completion of the download

    }


}

fun filePathClearance1()
{
    fileUri = mutableStateOf(null)
    fileName.value = ""
    filePath.value = ""
}

fun fileNameFormatCheck(filename: String): String {
    val filenamePattern = Regex("""^\d{4}-\d{2}-\d--\d{2}-\d{2}-\d{2}_""")// yyyy-dd-M--HH-mm-ss_
    return filename.replaceFirst(filenamePattern, "")
}


fun orgFileNameFormatCheck(filename: String): String {
    val filenamePattern = Regex("^[0-9]{8}-[0-9]+")
    return filename.replaceFirst(filenamePattern, "").trimStart('-')
}

fun containsOnlySpecialCharacters(str: String): Boolean {
    val specialCharacterRegex = "[^A-Za-z0-9 ]".toRegex()
    return str.all { it.toString().matches(specialCharacterRegex) }
}