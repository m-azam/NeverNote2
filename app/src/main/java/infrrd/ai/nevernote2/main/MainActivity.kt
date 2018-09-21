package infrrd.ai.nevernote2.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.ActionMode
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import infrrd.ai.nevernote2.R
import infrrd.ai.nevernote2.adapters.NotesAdapter
import infrrd.ai.nevernote2.base.BaseActivity
import infrrd.ai.nevernote2.contextactionbar.ActionBarCallBack
import infrrd.ai.nevernote2.models.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity(), NotesAdapter.ActionBarCallback, SearchView.OnQueryTextListener,
        ActionBarCallBack.OnDeleteSelectionListener, MainView {

    override val baseActivityContext: Context = baseContext
    private val presenter = MainPresenter(this, MainInteractor())
    override var actionMode: ActionMode? = null
    private lateinit var sampleVariable:String

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var notesDataset: MutableList<Note> = ArrayList()
    private var trashNotes: MutableList<Note> = ArrayList()
    private lateinit var imageUri: Uri
    val editNote = {
        position:Int, note:Note ->
        val intent = Intent(this, NewNote::class.java)
        intent.putExtra("Title",note.title)
        intent.putExtra("Body",note.body)
        intent.putExtra("Position",position)
        startActivityForResult(intent,2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewManager = LinearLayoutManager(this)
        viewAdapter = NotesAdapter(editNote, this, this, notesDataset)
        recyclerView = findViewById<RecyclerView>(R.id.note_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(notesDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)

        text.setOnClickListener {
            note_actions.collapse()
            presenter.textButtonClicked()
        }
        audio.setOnClickListener {
            note_actions.collapse()
            presenter.audioButtonClicked()
        }
        camera.setOnClickListener {
            note_actions.collapse()
            presenter.cameraClicked()
        }
        NotesServiceLayer().getallnotes {
            notesDataset.addAll(it)
            viewAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK ) {
                    val capturedImage = imageUri
                    contentResolver.notifyChange(capturedImage, null)

                    Toast.makeText(this, capturedImage.toString(), Toast.LENGTH_LONG).show()
                }
            }
            2 -> {

                if (resultCode == Activity.RESULT_OK) {
                    var gson = Gson()
                    var new_note: Note = gson.fromJson(data?.getStringExtra("result"),
                            object : TypeToken<Note>(){}.type)
                    var position:Int? = data?.getIntExtra("Position",-1)

                    if(position == -1) {
                        Log.d("inside if","lolol")
                        notesDataset.add(0,new_note)
                        viewAdapter.notifyItemInserted(0)

                    }
                    else {
                        Log.d("inside else","lolol")
                        position?.let {
                            notesDataset.removeAt(position)
                            notesDataset.add(position,new_note)
                            viewAdapter.notifyItemChanged(position)
                        }

                    }

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                }
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

}