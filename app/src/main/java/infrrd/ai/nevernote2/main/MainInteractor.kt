package infrrd.ai.nevernote2.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat.startActivityForResult

class MainInteractor {

    val permissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")

    fun openNewNoteActivity(activity: Activity) {
        val intent = Intent(activity, NewNote::class.java)
        activity.startActivityForResult( intent, 2)
    }

    fun openAudioRecorderActivity(context: Context) {
        val intent = Intent(context, AudioRecorder::class.java)
        context.startActivity(intent)
    }

    fun openSettings(context: Context, uri: Uri) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = uri
        startActivityForResult(context as Activity, intent, 1, null)
    }

}