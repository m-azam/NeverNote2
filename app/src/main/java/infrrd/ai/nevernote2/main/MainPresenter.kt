package infrrd.ai.nevernote2.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import infrrd.ai.nevernote2.BuildConfig
import infrrd.ai.nevernote2.R
import infrrd.ai.nevernote2.R.id.*
import infrrd.ai.nevernote2.adapters.NotesAdapter
import infrrd.ai.nevernote2.contextactionbar.ActionBarCallBack
import infrrd.ai.nevernote2.itemdecorators.RecyclerSectionItemDecoration
import infrrd.ai.nevernote2.models.Note
import java.io.File

class MainPresenter(private val mainView: MainView, val mainInteractor: MainInteractor): NotesAdapter.ActionBarCallback
        , ActionBarCallBack.OnDeleteSelectionListener {

    fun initializer() {
        mainInteractor.viewManager = LinearLayoutManager(mainView.mainActivityContext)
        mainInteractor.viewAdapter = NotesAdapter(editNote, this, mainView.mainActivityContext
                , mainInteractor.notesDataset)
        mainInteractor.recyclerView = mainView.mainActivity.findViewById<RecyclerView>(note_recycler).apply {
            layoutManager = mainInteractor.viewManager
            adapter = mainInteractor.viewAdapter
        }

        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(mainInteractor.notesDataset))
        mainInteractor.recyclerView.addItemDecoration(sectionItemDecoration)
    }

    override fun startActionBar() {
        actionMode = mainView.mainActivity.startActionMode(ActionBarCallBack(mainInteractor.viewAdapter,this))
        mainView.noteActionVisibilityToggle(false)
    }

    override fun finishActionBar() {
        actionMode?.finish()
        mainView.noteActionVisibilityToggle(true)
    }

    override fun onDeleteSelection() {
        var deleteIds:MutableList<Int> = ArrayList()
        for(index in mainInteractor.viewAdapter.selectedArray) {
            deleteIds.add(mainInteractor.viewAdapter.filteredNotes[index].id)
        }
        deleteNotes(deleteIds)
    }

    val editNote = { position:Int, note:Note ->
        mainInteractor.openNewNoteforEdit(mainView.mainActivity, note, position)
    }

    fun textButtonClicked() {
        mainInteractor.openNewNoteActivity(mainView.mainActivity)
    }

    fun audioButtonClicked() {
        mainInteractor.openAudioRecorderActivity(mainView.mainActivityContext)
    }

    fun cameraClicked() {
        takePicture()
    }

    private fun deleteNotes(deleteList:List<Int>) {
        mainInteractor.serviceLayer.moveToTrash({
            Toast.makeText(mainView.mainActivityContext, "Items moved to Trash", Toast.LENGTH_LONG).show()
            loadNotes()
            finishActionBar()
        },deleteList.toString())
    }

    private fun loadNotes() {
        mainInteractor.serviceLayer.loadNotes {
            mainInteractor.notesDataset.clear()
            mainInteractor.notesDataset.addAll(it)
            mainInteractor.viewAdapter.notifyDataSetChanged()
        }
    }

    private fun takePicture() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(mainView.mainActivity, mainInteractor.permissions[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(mainView.mainActivity, mainInteractor.permissions[1])) {
            val uri = Uri.fromParts("package", mainView.mainActivityContext.packageName, null)
            mainInteractor.openSettings(mainView.mainActivityContext, mainView.mainActivity, uri)
        }
        else {
            if (checkPermissions()) {
                val photo = File(Environment.getExternalStorageDirectory(), "Pic.jpg")
                mainInteractor.openCamera(mainView.mainActivityContext, mainView.mainActivity, photo)
                mainInteractor.imageUri = FileProvider.getUriForFile(mainView.mainActivityContext,
                        BuildConfig.APPLICATION_ID + ".provider", photo)
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

    private fun getSectionCallback(notes: List<Note>): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {

            override fun isSection(position: Int): Boolean {
                return position == 0 || getHeader(position) != getHeader(position - 1)
            }

            override fun getSectionHeader(position: Int): CharSequence {
                return getHeader(position)
            }

            fun getHeader(position:Int): String {
                val header: String = notes.get(position).created.subSequence(3,7).toString()+ " "+  notes.get(position).created.subSequence(30,34)
                return header
            }
        }
    }

    fun onResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK ) {
                    mainView.mainActivityContext.contentResolver.notifyChange(mainInteractor.imageUri, null)
                }
            }
            2 -> {

                if (resultCode == Activity.RESULT_OK) {
                    var gson = Gson()
                    var new_note: Note = gson.fromJson(data?.getStringExtra("result"),
                            object : TypeToken<Note>(){}.type)
                    var position:Int? = data?.getIntExtra("Position",-1)

                    if(position == -1) {
                        mainInteractor.notesDataset.add(0,new_note)
                        mainInteractor.viewAdapter.notifyItemInserted(0)

                    }
                    else {
                        position?.let {
                            mainInteractor.notesDataset.removeAt(position)
                            mainInteractor.notesDataset.add(position,new_note)
                            mainInteractor.viewAdapter.notifyItemChanged(position)
                        }
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                }
            }
        }
    }

}