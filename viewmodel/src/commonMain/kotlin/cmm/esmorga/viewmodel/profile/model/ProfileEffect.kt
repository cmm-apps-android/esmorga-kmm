package cmm.esmorga.viewmodel.profile.model

sealed class ProfileEffect {
    data object NavigateToChangePassword : ProfileEffect()
}