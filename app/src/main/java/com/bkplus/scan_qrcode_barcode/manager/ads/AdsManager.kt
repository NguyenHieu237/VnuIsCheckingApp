package com.bkplus.scan_qrcode_barcode.manager.ads

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AdsManager {
    companion object {
        val instance: AdsManager = AdsManager()
    }

    private var _nativeWifiSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeWebsiteSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeTextSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeContactSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativePhoneSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeEmailSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeQRCodeResultSubject = BehaviorSubject.create<ApNativeAdContainer>()
    private var _nativeHistoryListSubject = BehaviorSubject.create<ApNativeAdContainer>()

    fun setupFirstValue() {
        setNativeWifi(adContainer = ApNativeAdContainer())
        setNativeWebsite(adContainer = ApNativeAdContainer())
        setNativeText(adContainer = ApNativeAdContainer())
        setNativeContact(adContainer = ApNativeAdContainer())
        setNativePhone(adContainer = ApNativeAdContainer())
        setNativeEmail(adContainer = ApNativeAdContainer())
        setNativeQRCodeResult(adContainer = ApNativeAdContainer())
        setNativeHistoryList(adContainer = ApNativeAdContainer())
    }

    /// Wifi
    fun setNativeWifi(adContainer: ApNativeAdContainer) {
        if (_nativeWifiSubject.value?.adsNative != null) { return }

        _nativeWifiSubject.onNext(adContainer)
    }

    fun getNativeWifiObservable(): Observable<ApNativeAdContainer> {
        return _nativeWifiSubject
    }

    /// Website
    fun setNativeWebsite(adContainer: ApNativeAdContainer) {
        if (_nativeWebsiteSubject.value?.adsNative != null) { return }

        _nativeWebsiteSubject.onNext(adContainer)
    }

    fun getNativeWebsiteObservable(): Observable<ApNativeAdContainer> {
        return _nativeWebsiteSubject
    }

    /// Text
    fun setNativeText(adContainer: ApNativeAdContainer) {
        if (_nativeTextSubject.value?.adsNative != null) { return }

        _nativeTextSubject.onNext(adContainer)
    }

    fun getNativeTextObservable(): Observable<ApNativeAdContainer> {
        return _nativeTextSubject
    }

    /// Contact
    fun setNativeContact(adContainer: ApNativeAdContainer) {
        if (_nativeContactSubject.value?.adsNative != null) { return }

        _nativeContactSubject.onNext(adContainer)
    }

    fun getNativeContactObservable(): Observable<ApNativeAdContainer> {
        return _nativeContactSubject
    }

    /// Phone
    fun setNativePhone(adContainer: ApNativeAdContainer) {
        if (_nativePhoneSubject.value?.adsNative != null) { return }

        _nativePhoneSubject.onNext(adContainer)
    }

    fun getNativePhoneObservable(): Observable<ApNativeAdContainer> {
        return _nativePhoneSubject
    }

    /// Email
    fun setNativeEmail(adContainer: ApNativeAdContainer) {
        if (_nativeEmailSubject.value?.adsNative != null) { return }

        _nativeEmailSubject.onNext(adContainer)
    }

    fun getNativeEmailObservable(): Observable<ApNativeAdContainer> {
        return _nativeEmailSubject
    }

    /// QR Code result
    fun setNativeQRCodeResult(adContainer: ApNativeAdContainer) {
        if (_nativeQRCodeResultSubject.value?.adsNative != null) { return }

        _nativeQRCodeResultSubject.onNext(adContainer)
    }

    fun getNativeQRCodeResultObservable(): Observable<ApNativeAdContainer> {
        return _nativeQRCodeResultSubject
    }

    /// History list
    fun setNativeHistoryList(adContainer: ApNativeAdContainer) {
        if (_nativeHistoryListSubject.value?.adsNative != null) { return }

        _nativeHistoryListSubject.onNext(adContainer)
    }

    fun getNativeHistoryListObservable(): Observable<ApNativeAdContainer> {
        return _nativeHistoryListSubject
    }
}