package com.com.dtag.livia.utility

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.goldencouponz.utility.GoldenSharedPreference
import java.util.*


class Local {
    companion object {
        fun updateResources(context: Context) {
            val language: String? = GoldenSharedPreference.getSelectedLanguageValue(context)
            val locale = Locale(language!!)
            Locale.setDefault(locale)
            val res: Resources = context.resources
            val config = Configuration(res.configuration)
            config.setLocale(locale)
//            context.createConfigurationContext(config)
         res.updateConfiguration(config, res.displayMetrics)
        }
    }
}