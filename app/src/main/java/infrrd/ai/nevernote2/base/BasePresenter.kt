package infrrd.ai.nevernote2.base

import infrrd.ai.nevernote2.objects.AppPreferences


class BasePresenter(private val baseView: BaseView, val baseInteractor: BaseInteractor ) {

    fun settingsClicked() {
        baseInteractor.openSettingsActivity(baseView.baseActivityContext)
    }

    fun mapViewClicked() {
        baseInteractor.openMapViewActivity(baseView.baseActivityContext)
    }

    fun trashClicked() {
        baseInteractor.openTrashActivity(baseView.baseActivityContext)
    }

    fun signOutClicked() {
        AppPreferences.loginValidity = false
        baseInteractor.openLoginActivity(baseView.baseActivityContext)
    }
}