package com.udemy.projemanag.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "Users"
    const val BOARDS: String = "boards"

    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val TASK_LIST: String = "taskList"
    // (Step 1: Add constant for passing the board details through intent.)
    const val BOARD_DETAIL: String = "board_detail"
    // (Step 3: Add field name as a constant which we will be using for getting the list of user details from the database.)
    const val ID: String = "id"
    const val EMAIL: String = "email"

    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"
    const val BOARD_MEMBERS_LIST: String = "board_members_list"

    const val SELECT: String = "Select"
    const val UN_SELECT: String = "UnSelect"

    // (Step 1: Add a SharedPreferences name and key names.)
    const val PROGEMANAG_PREFERENCES: String = "ProjemanagPrefs"

    const val FCM_TOKEN:String = "fcmToken"
    const val FCM_TOKEN_UPDATED:String = "fcmTokenUpdated"
    // END

    //  (Step 1: Add the base url  and key params for sending firebase notification.)
    // START
    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAAwUQRnBA:APA91bFlOjN4A55gFmKPoossXC8GBo5GXqLok6f7vsPS1Vd22vS4sTQwCoLBf16Z4lt5hVKNpO8H6NvnFg7juE8u6k3ejv-99KQ1fL25joAQzOfEo29kBfF2HALHOZY-WZUFMd6W_c9Y"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"
    // END

    /**
     * show image chooser
     */
    fun showImageChooser(activity: Activity){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * get file extension based on URI
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}