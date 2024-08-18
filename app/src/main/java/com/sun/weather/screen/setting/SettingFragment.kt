package com.sun.weather.screen.setting

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.sun.weather.R
import com.sun.weather.databinding.FragmentSettingBinding
import com.sun.weather.utils.SharedPrefManager
import com.sun.weather.utils.base.BaseFragment
import java.util.Locale

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }

    override fun initData() {
        // TODO LATER
    }

    override fun initView() {
        val flagResId = SharedPrefManager.getInt(KEY_FLAG_RES_ID, DEFAULT_FLAG_RES_ID)
        viewBinding.flagImageView.setImageResource(flagResId)
        viewBinding.languageButton.setOnClickListener {
            showLanguageSelectionDialog()
        }
    }

    private fun showLanguageSelectionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(TITLE_SELECT_LANGUAGE)
            .setItems(LANGUAGES) { _, which ->
                when (which) {
                    INDEX_VIETNAMESE -> setLocale(LANGUAGE_CODE_VIETNAMESE, FLAG_RES_ID_VIETNAMESE)
                    INDEX_ENGLISH -> setLocale(LANGUAGE_CODE_ENGLISH, FLAG_RES_ID_ENGLISH)
                }
            }
            .show()
    }

    private fun setLocale(
        languageCode: String,
        flagResId: Int,
    ) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
        SharedPrefManager.putInt(KEY_FLAG_RES_ID, flagResId)
        viewBinding.flagImageView.setImageResource(flagResId)
        activity?.recreate()
    }

    companion object {
        const val KEY_FLAG_RES_ID = "flagResId"
        val DEFAULT_FLAG_RES_ID = R.drawable.vn_flag
        const val TITLE_SELECT_LANGUAGE = "Chọn ngôn ngữ"
        const val LANGUAGE_CODE_VIETNAMESE = "vi"
        const val LANGUAGE_CODE_ENGLISH = "en"
        val FLAG_RES_ID_VIETNAMESE = R.drawable.vn_flag
        val FLAG_RES_ID_ENGLISH = R.drawable.en_flag
        const val INDEX_VIETNAMESE = 0
        const val INDEX_ENGLISH = 1
        val LANGUAGES = arrayOf("Tiếng Việt", "English")

        fun newInstance() = SettingFragment()
    }
}
