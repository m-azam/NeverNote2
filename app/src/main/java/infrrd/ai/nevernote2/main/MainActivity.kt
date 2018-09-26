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
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import infrrd.ai.nevernote2.R
import infrrd.ai.nevernote2.adapters.NotesAdapter
import infrrd.ai.nevernote2.base.BaseActivity
import infrrd.ai.nevernote2.contextactionbar.ActionBarCallBack
import infrrd.ai.nevernote2.models.Note
import infrrd.ai.nevernote2.newnote.NewNoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity(), NotesAdapter.ActionBarCallback, SearchView.OnQueryTextListener,
        ActionBarCallBack.OnDeleteSelectionListener, MainView {

    override val baseActivityContext: Context = baseContext
    override val mainActivity: Activity = this
    private val presenter = MainPresenter(this, MainInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        presenter.initializer()
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResultReceived(requestCode, resultCode, data)
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun noteActionVisibilityToggle(setVisibility: Boolean) {
        if(setVisibility) note_actions.visibility = View.VISIBLE  else  note_actions.visibility = View.GONE
    }

}