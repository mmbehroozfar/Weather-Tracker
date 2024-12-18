package com.mmb.ui_home

import androidx.annotation.StringRes

sealed interface HomeSideEffect {
    data class ShowError(@StringRes val messageRes: Int) : HomeSideEffect
}