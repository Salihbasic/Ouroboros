package com.github.salihbasicm.ouroboros.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.salihbasicm.ouroboros.OuroborosUser;

import java.util.concurrent.TimeUnit;

public class OuroborosCache {

    private final LoadingCache<OuroborosUser, Integer> ouroborosUserCache;

    public OuroborosCache() {

        ouroborosUserCache = Caffeine.newBuilder()
                    .maximumSize(50L) // TODO: Make configurable
                    .expireAfterWrite(10, TimeUnit.MINUTES)
                    .build(OuroborosUser::getLives);
    }

    public LoadingCache<OuroborosUser, Integer> getUserCache() {
        return ouroborosUserCache;
    }

}
