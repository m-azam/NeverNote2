package infrrd.ai.nevernote2.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import java.io.File

class MainPresenter(private val mainView: MainView, val mainInteractor: MainInteractor) {

    fun textButtonClicked() {
        mainInteractor.openNewNoteActivity(mainView.mainActivityContext as Activity)
    }

    fun audioButtonClicked() {
        mainInteractor.openAudioRecorderActivity(mainView.mainActivityContext)
    }

    fun cameraClicked() {
        takePicture()
    }

    private fun takePicture() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
            val uri = Uri.fromParts("package", this.packageName, null)
            mainInteractor.openSettings(mainView.mainActivityContext, uri)
        }
        else {
            if (checkPermissions()) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photo = File(Environment.getExternalStorageDirectory(), "Pic.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(this,
                                BuildConfig.APPLICATION_ID + ".provider", photo))
                imageUri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", photo)
                startActivityForResult(intent, 1)
            } else {
                getPermission()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        for (permission in mainInteractor.permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}