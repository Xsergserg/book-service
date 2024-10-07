package com.innovatrics.dot.interview.helper

import org.springframework.cache.CacheManager

fun CacheManager.clearAll() {
    cacheNames.forEach { cacheName -> getCache(cacheName)?.clear() }
}
