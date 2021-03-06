package infrrd.ai.nevernote2.contextactionbar

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import infrrd.ai.nevernote2.R

class ActionBarCallBack(private val onExitSelection: OnExitSelectionListener,
                        private val onDeleteSelection: OnDeleteSelectionListener): ActionMode.Callback {

    interface OnExitSelectionListener {
        fun onExitSelection()
    }

    interface OnDeleteSelectionListener {
        fun onDeleteSelection()
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        onDeleteSelection.onDeleteSelection()
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.context_menu, menu)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        onExitSelection.onExitSelection()
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {

        return false
    }

}