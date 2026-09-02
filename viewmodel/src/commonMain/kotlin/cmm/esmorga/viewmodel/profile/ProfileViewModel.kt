package cmm.esmorga.viewmodel.profile

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.profile.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val result = getSavedUserUseCase()
            result.onSuccess { userSuccess ->
                _uiState.value = ProfileUiState(
                    isLoggedIn = true,
                    name = "${userSuccess.data.name} ${userSuccess.data.lastName}".trim(),
                    email = userSuccess.data.email
                )
            }.onFailure {
                _uiState.value = ProfileUiState(isLoggedIn = false)
            }
        }
    }

    fun onChangePasswordClicked() {}

    fun onLogoutClicked() {}
}
