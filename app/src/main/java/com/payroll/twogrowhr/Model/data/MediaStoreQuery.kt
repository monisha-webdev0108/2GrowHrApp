package com.payroll.twogrowhr.Model.data

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun loadImages(
    contentResolver: ContentResolver,
): State<List<FileEntry>> = produceState(initialValue = emptyList()) {
    value = getImages(contentResolver)
}

/**
 * Query [MediaStore] through [ContentResolver] to get all images sorted by added date by targeting
 * the [Images] collection
 */
suspend fun getImages(contentResolver: ContentResolver): List<FileEntry> {
    return withContext(Dispatchers.IO) {
        // List of columns we want to fetch
        val projection = arrayOf(
            Images.Media._ID,
            Images.Media.DISPLAY_NAME,
            Images.Media.SIZE,
            Images.Media.MIME_TYPE,
            Images.Media.DATE_ADDED,
        )

        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // This allows us to query all the device storage volumes instead of the primary only
            Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Images.Media.EXTERNAL_CONTENT_URI
        }

        val images = mutableListOf<FileEntry>()

        val state = Environment.getExternalStorageState()
        if (state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY) {
            contentResolver.query(
                collectionUri, // Queried collection
                projection, // List of columns we want to fetch
                null, // Filtering parameters (in this case none)
                null, // Filtering values (in this case none)
                "${Images.Media.DATE_ADDED} DESC", // Sorting order (recent -> older files)
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(Images.Media.SIZE)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                    val name = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)

                    images.add(FileEntry(uri, name, size, mimeType, dateAdded))
                }
            }
            return@withContext images

        } else {
            // Handle the case where external storage is not available or not readable
            Log.e("queryContentResolver", "External storage is not mounted or readable: $state")
            return@withContext emptyList<FileEntry>()
        }


    }
}