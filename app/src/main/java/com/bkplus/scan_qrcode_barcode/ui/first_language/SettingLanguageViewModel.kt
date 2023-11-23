package com.bkplus.scan_qrcode_barcode.ui.first_language

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.Language
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor() : ViewModel() {

    val onLanguageListReady = MutableLiveData<List<Language>>()

    fun fetchLanguageList() {
        viewModelScope.launch {
            val listAllLanguage = makeListAllLanguage()
            for (language in listAllLanguage) {
                if (language.codeLanguage == QRCodePreferences.getPrefsInstance().locale) {
                    language.isSelected = true
                    break
                }
            }

            onLanguageListReady.postValue(listAllLanguage)
        }
    }

    fun fetchFirstLanguageList() {
        viewModelScope.launch {
            val listAllLanguage = makeListAllLanguage()

            // Hard code list
            val firstLanguageList = mutableListOf(
                Language("en", R.string.setting_en, R.mipmap.ic_english),
                Language("hi", R.string.setting_hindi, R.mipmap.ic_hindi),
                Language("es", R.string.setting_spain, R.mipmap.ic_spain),
                Language("pt", R.string.setting_portuguese, R.mipmap.ic_portugal),
                Language("de", R.string.setting_german, R.mipmap.ic_germany),
                Language("fr", R.string.setting_france, R.mipmap.ic_france),
                Language("zh", R.string.setting_chinese, R.mipmap.ic_china),
            )

            val currentDeviceLanguage = Resources.getSystem().configuration.locales.get(0)

            // find language in all language list
            val deviceLanguage = try {
                listAllLanguage.first { language: Language ->
                    currentDeviceLanguage.language.equals(Locale(language.codeLanguage).language)
                }
            } catch (e: NoSuchElementException) {
                listAllLanguage[0] // english if no language match device language
            }

            // check if language is in first language list and swap to top if true
            for (i in 0 until firstLanguageList.size) {
                if (firstLanguageList[i].codeLanguage == deviceLanguage.codeLanguage) {
                    firstLanguageList.removeAt(i)
                    firstLanguageList.add(0, deviceLanguage)
                    break
                }
            }

            // if first language list does not have device language
            if (firstLanguageList[0].codeLanguage != deviceLanguage.codeLanguage) {
                firstLanguageList.add(0, deviceLanguage)
            }

            firstLanguageList[0].isSelected = true
            onLanguageListReady.postValue(firstLanguageList)
        }
    }

    private fun makeListAllLanguage(): MutableList<Language> {
        // Hard code list
        return mutableListOf(
            Language("en", R.string.setting_en, R.mipmap.ic_english),
            Language("hi", R.string.setting_hindi, R.mipmap.ic_hindi),
            Language("es", R.string.setting_spain, R.mipmap.ic_spain),
            Language("pt", R.string.setting_portuguese, R.mipmap.ic_portugal),
            Language("de", R.string.setting_german, R.mipmap.ic_germany),
            Language("fr", R.string.setting_france, R.mipmap.ic_france),
            Language("zh", R.string.setting_chinese, R.mipmap.ic_china),
            //            Language("bn", R.string.setting_bengal, R.mipmap.ic_bengal),
//            Language("ru", R.string.setting_russia, R.mipmap.ic_russian),
//            Language("ja", R.string.setting_japanese, R.mipmap.ic_japanese),
//            Language("mr", R.string.setting_marathi, R.mipmap.ic_marathi),
//            Language("te", R.string.setting_tegulu, R.mipmap.ic_telugu),
//            Language("tr", R.string.setting_turkrish, R.mipmap.ic_turkrish),
//            Language("ta", R.string.setting_tamil, R.mipmap.ic_tamil),
//            Language("ko", R.string.setting_korean, R.mipmap.ic_korean),
//            Language("vi", R.string.setting_vietnamese, R.mipmap.ic_vietnamese),
//            Language("it", R.string.setting_italian, R.mipmap.ic_italian),
//            Language("th", R.string.setting_thailand, R.mipmap.ic_thailand),
        )
    }
}
