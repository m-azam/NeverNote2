package infrrd.ai.nevernote2.base

import android.content.Context
import android.content.Intent
import infrrd.ai.nevernote2.login.LoginActivity
import infrrd.ai.nevernote2.map.MapsActivity
import infrrd.ai.nevernote2.settings.SettingsActivity
import infrrd.ai.nevernote2.trash.TrashActivity

class BaseInteractor() {
    fun openSettingsActivity(context: Context) {
        val settingsActivityIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsActivityIntent)
    }

    fun openMapViewActivity(context: Context) {
        val mapViewActivityIntent = Intent(context, MapsActivity::class.java)
        context.startActivity(mapViewActivityIntent)
    }

    fun openTrashActivity(context: Context) {
        val trashActivityIntent = Intent(context, TrashActivity::class.java)
        context.startActivity(trashActivityIntent)
    }

    fun openLoginActivity(context: Context) {
        val loginActivityIntent = Intent(context, LoginActivity::class.java)
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(loginActivityIntent)
    }
}