package com.rabbitframework.jade.cache.decorators;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.rabbitframework.jade.cache.Cache;

public class SoftCache implements Cache {
    private final Cache delegate;
    private final LinkedList<Object> hardLinksToAvoidGarbageCollection;
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;
    private int numberOfHardLinks;

    public SoftCache(Cache delegate) {
        this.delegate = delegate;
        hardLinksToAvoidGarbageCollection = new LinkedList<Object>();
        queueOfGarbageCollectedEntries = new ReferenceQueue<Object>();
        numberOfHardLinks = 256;

    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        removeGarbageCollectedItems();
        return delegate.getSize();
    }

    public void setSize(int size) {
        numberOfHardLinks = size;
    }

    @Override
    public void putObject(Object key, Object value) {
        removeGarbageCollectedItems();
        delegate.putObject(key, new SoftEntry(key, value, queueOfGarbageCollectedEntries));
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        WeakReference<Object> weakReference = (WeakReference<Object>) delegate.getObject(key);
        if (weakReference != null) {
            result = weakReference.get();
            if (result == null) {
                delegate.removeObject(key);
            } else {
                synchronized (hardLinksToAvoidGarbageCollection) {
                    hardLinksToAvoidGarbageCollection.addFirst(key);
                    if (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
                        hardLinksToAvoidGarbageCollection.removeLast();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object removeObject(Object key) {
        removeGarbageCollectedItems();
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        synchronized (hardLinksToAvoidGarbageCollection) {
            hardLinksToAvoidGarbageCollection.clear();
        }
        removeGarbageCollectedItems();
        delegate.clear();
    }

//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return null;
//    }

    private void removeGarbageCollectedItems() {
        SoftEntry sv;
        while ((sv = (SoftEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            delegate.removeObject(sv.key);
        }
    }

    private static class SoftEntry extends SoftReference<Object> {
        private final Object key;

        public SoftEntry(Object key, Object value, ReferenceQueue<? super Object> garbageQueue) {
            super(value, garbageQueue);
            this.key = key;
        }
    }
}
