package com.github.salihbasicm.ouroboros.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.salihbasicm.ouroboros.OuroborosUser;

import java.util.concurrent.TimeUnit;

public class OuroborosUserCache {

    private final LoadingCache<OuroborosUser, Integer> ouroborosCache;

    public OuroborosUserCache() {

        ouroborosCache = Caffeine.newBuilder()
                    .maximumSize(50L) // TODO: Make configurable
                    .expireAfterWrite(10, TimeUnit.MINUTES)
                    .build(OuroborosUser::getLives);

    }

    public LoadingCache<OuroborosUser, Integer> getOuroborosCache() {
        return ouroborosCache;
    }

}
