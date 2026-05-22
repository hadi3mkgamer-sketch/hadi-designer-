package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.first

class PortfolioRepository(private val portfolioDao: PortfolioDao) {

    val allPortfolioItems: Flow<List<PortfolioItem>> = portfolioDao.getAllPortfolioItems()
    val allSettings: Flow<List<AppSetting>> = portfolioDao.getAllSettings()

    suspend fun insertPortfolioItem(item: PortfolioItem) {
        portfolioDao.insertPortfolioItem(item)
    }

    suspend fun deletePortfolioItem(id: Int) {
        portfolioDao.deletePortfolioItemById(id)
    }

    suspend fun getSetting(key: String): String? {
        return portfolioDao.getSettingByKey(key)?.value
    }

    suspend fun saveSetting(key: String, value: String) {
        portfolioDao.insertSetting(AppSetting(key, value))
    }

    suspend fun prepopulateDefaultsIfEmpty() {
        // Query to check if settings or items are empty
        val currentItems = portfolioDao.getAllPortfolioItems().first()
        if (currentItems.isEmpty()) {
            val defaultItems = listOf(
                PortfolioItem(
                    title = "Avant-Garde Brand Identity",
                    description = "Sleek, modernist geometric logo system and typography hierarchy designed for a leading AI startup. Features stark typographic details and high-contrast composition.",
                    imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
                    category = "Branding"
                ),
                PortfolioItem(
                    title = "Noir Cinematic Poster",
                    description = "Asymmetrical typography study blending industrial silhouettes with grainy, raw print textures. Award-winning layout design for an independent art film.",
                    imageUrl = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800&auto=format&fit=crop&q=80",
                    category = "Poster Art"
                ),
                PortfolioItem(
                    title = "Premium Cosmetics Vessel",
                    description = "Tactile earthy ceramic container packaging, embossed vector insignias, and custom matte finish templates for a luxurious organic skincare studio.",
                    imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=800&auto=format&fit=crop&q=80",
                    category = "Packaging"
                ),
                PortfolioItem(
                    title = "Spaceflight HUD Interface",
                    description = "Dynamic dashboard experience designed for responsive aerospace controls. Highlights precise technical HUD widgets and deep negative space structure.",
                    imageUrl = "https://images.unsplash.com/photo-1508921912186-1d1a45ebb3c1?w=800&auto=format&fit=crop&q=80",
                    category = "UI/UX"
                )
            )
            for (item in defaultItems) {
                portfolioDao.insertPortfolioItem(item)
            }
        }

        // Prepopulate default settings if they are not defined
        val defaultSettings = mapOf(
            "inner_logo_preset" to "brush",
            "inner_logo_type" to "preset",
            "inner_logo_custom_text" to "HD",
            "inner_logo_custom_url" to "",
            "contact_phone" to "0998638910",
            "contact_whatsapp" to "https://wa.me/0998638910",
            "contact_instagram" to "https://instagram.com/hadi.designer",
            "contact_behance" to "https://behance.net/hadi_designer",
            "contact_email" to "hadi3mkgamer@gmail.com"
        )

        for ((key, value) in defaultSettings) {
            if (portfolioDao.getSettingByKey(key) == null) {
                portfolioDao.insertSetting(AppSetting(key, value))
            }
        }
    }
}
