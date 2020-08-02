package com.github.salihbasicm.ouroboros.storage;

public interface AbstractStorageFactory<T> {

    T getStorage(StorageType storageType);

}
