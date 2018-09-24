package infrrd.ai.nevernote2.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import infrrd.ai.nevernote2.R
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BaseView {

    lateinit var toggle: ActionBarDrawerToggle
    override val baseActivityContext: Context = baseContext
    private val presenter = BasePresenter(this, BaseInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        layoutInflater.inflate(getContentView(), layout_container, true)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

    }

    abstract fun getContentView(): Int

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort -> {
                true
            }
            R.id.settings -> {
                presenter.settingsClicked()
                true
            }
            R.id.action_search -> {
                toggle.isDrawerIndicatorEnabled = false//Fixes visual glitch when expanding search bar
                true
            }
            R.id.notes_map_view -> {
                presenter.mapViewClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.trash -> {
                presenter.trashClicked()
            }
            R.id.completed -> {
            }
            R.id.all_notes -> {
            }
            R.id.sign_out -> {
                presenter.signOutClicked()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}