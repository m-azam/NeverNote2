package infrrd.ai.nevernote2.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import infrrd.ai.nevernote2.BuildConfig
import infrrd.ai.nevernote2.adapters.NotesAdapter
import infrrd.ai.nevernote2.audiorecorder.AudioRecorderActivity
import infrrd.ai.nevernote2.models.Note
import infrrd.ai.nevernote2.newnote.NewNoteActivity
import infrrd.ai.nevernote2.services.NotesServiceLayer
import java.io.File

class MainInteractor {

    lateinit var imageUri: Uri
    val permissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")
    var notesDataset: MutableList<Note> = ArrayList()
    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: NotesAdapter
    var serviceLayer: NotesServiceLayer = NotesServiceLayer()

    fun openNewNoteActivity(activity: Activity) {
        val intent = Intent(activity, NewNoteActivity::class.java)
        activity.startActivityForResult( intent, 2)
    }

    fun openNewNoteforEdit(activity: Activity, note: Note, position: Int) {
        val intent = Intent(activity, NewNoteActivity::class.java)
        intent.putExtra("Title",note.title)
        intent.putExtra("Body",note.body)
        intent.putExtra("Position",position)
        startActivityForResult(activity, intent, 2, null)
    }

    fun openAudioRecorderActivity(context: Context) {
        val intent = Intent(context, AudioRecorderActivity::class.java)
        context.startActivity(intent)
    }

    fun openSettings(context: Context, activity: Activity, uri: Uri) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = uri
        startActivityForResult(activity, intent, 1, null)
    }

    fun openCamera(context: Context,activity: Activity, photo: File) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider", photo))
        activity.startActivityForResult(intent, 1)
    }

}