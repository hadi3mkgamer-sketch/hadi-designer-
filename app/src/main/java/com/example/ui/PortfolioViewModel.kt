package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PortfolioItem
import com.example.data.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PortfolioRepository

    val portfolioItems: StateFlow<List<PortfolioItem>>
    val settings: StateFlow<Map<String, String>>

    private val _showPasswordDialog = MutableStateFlow(false)
    val showPasswordDialog: StateFlow<Boolean> = _showPasswordDialog.asStateFlow()

    private val _showAdminPanel = MutableStateFlow(false)
    val showAdminPanel: StateFlow<Boolean> = _showAdminPanel.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PortfolioRepository(database.portfolioDao())

        // Fetch data
        portfolioItems = repository.allPortfolioItems
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        settings = repository.allSettings
            .map { list ->
                list.associate { it.key to it.value }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

        // Initialize defaults
        viewModelScope.launch {
            repository.prepopulateDefaultsIfEmpty()
        }
    }

    fun openPasswordDialog() {
        _passwordError.value = null
        _showPasswordDialog.value = true
    }

    fun closePasswordDialog() {
        _showPasswordDialog.value = false
        _passwordError.value = null
    }

    fun verifyPassword(password: String): Boolean {
        return if (password == "0998638910") {
            _showPasswordDialog.value = false
            _showAdminPanel.value = true
            _passwordError.value = null
            true
        } else {
            _passwordError.value = "Incorrect password. Access denied."
            false
        }
    }

    fun logoutAdmin() {
        _showAdminPanel.value = false
    }

    fun updateLogoSettings(type: String, preset: String, customText: String, customUrl: String) {
        viewModelScope.launch {
            repository.saveSetting("inner_logo_type", type)
            repository.saveSetting("inner_logo_preset", preset)
            repository.saveSetting("inner_logo_custom_text", customText)
            repository.saveSetting("inner_logo_custom_url", customUrl)
        }
    }

    fun addPortfolioItem(title: String, description: String, imageUrl: String, category: String) {
        viewModelScope.launch {
            val newItem = PortfolioItem(
                title = title,
                description = description,
                imageUrl = imageUrl,
                category = category
            )
            repository.insertPortfolioItem(newItem)
        }
    }

    fun deletePortfolioItem(id: Int) {
        viewModelScope.launch {
            repository.deletePortfolioItem(id)
        }
    }

    fun updateContactInfo(
        phone: String,
        whatsapp: String,
        instagram: String,
        behance: String,
        email: String
    ) {
        viewModelScope.launch {
            repository.saveSetting("contact_phone", phone)
            repository.saveSetting("contact_whatsapp", whatsapp)
            repository.saveSetting("contact_instagram", instagram)
            repository.saveSetting("contact_behance", behance)
            repository.saveSetting("contact_email", email)
        }
    }
}
