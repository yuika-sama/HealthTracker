package com.yuika.healthtracker.utils

import kotlinx.coroutines.flow.Flow

fun <T> CollectEffect(
    effect: Flow<T>,
    onEffect
)