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
        mainInteractor.openNewNoteActivity(mainView.mainActivity)
    }

    fun audioButtonClicked() {
        mainInteractor.openAudioRecorderActivity(mainView.mainActivityContext)
    }

    fun cameraClicked() {
        takePicture()
    }

    private fun takePicture() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(mainView.mainActivity, mainInteractor.permissions[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(mainView.mainActivity, mainInteractor.permissions[1])) {
            val uri = Uri.fromParts("package", mainView.mainActivityContext.packageName, null)
            mainInteractor.openSettings(mainView.mainActivityContext, uri)
        }
        else {
            if (checkPermissions()) {
                val photo = File(Environment.getExternalStorageDirectory(), "Pic.jpg")
                mainInteractor.openCamera(mainView.mainActivityContext, mainView.mainActivity, photo)
            } else {
                getPermission()
            }
        }
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(mainView.mainActivity, mainInteractor.permissions
                , 1)
    }

    private fun checkPermissions(): Boolean {
        for (permission in mainInteractor.permissions) {
            if (ContextCompat.checkSelfPermission(mainView.mainActivityContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}