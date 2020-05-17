package com.github.salihbasicm.sedexlives.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.salihbasicm.sedexlives.LivesUser;

import java.util.concurrent.TimeUnit;

public class LivesUserCache {

    private LoadingCache<LivesUser, Integer> livesCache;

    public LivesUserCache() {

        livesCache = Caffeine.newBuilder()
                    .maximumSize(50L)
                    .expireAfterWrite(10, TimeUnit.MINUTES)
                    .build(LivesUser::getLives);

    }

    public LoadingCache<LivesUser, Integer> getLivesCache() {
        return livesCache;
    }

}
