package com.dyusov.news.data.mapper

import com.dyusov.news.domain.entity.RefreshConfig
import com.dyusov.news.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig = RefreshConfig(language, interval, wifiOnly)