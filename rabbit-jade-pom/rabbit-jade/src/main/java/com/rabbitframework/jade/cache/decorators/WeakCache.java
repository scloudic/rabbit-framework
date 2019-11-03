package com.rabbitframework.jade.cache.decorators;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import com.rabbitframework.jade.cache.Cache;

public class WeakCache implements Cache {
    private final Cache delegate;
    private final LinkedList<Object> hardLinksToAvoidGarbageCollection;
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;
    private int numberOfHardLinks;
    public WeakCache(Cache delegate) {
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
        delegate.putObject(key,new WeakEntry(key,value,queueOfGarbageCollectedEntries));
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        WeakReference<Object> weakReference = (WeakReference<Object>) delegate.getObject(key);
        if(weakReference != null) {
            result = weakReference.get();
            if(result == null) {
                delegate.removeObject(key);
            } else {
                hardLinksToAvoidGarbageCollection.addFirst(key);
                if(hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
                    hardLinksToAvoidGarbageCollection.removeLast();
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
        hardLinksToAvoidGarbageCollection.clear();
        removeGarbageCollectedItems();
        delegate.clear();
    }

//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return null;
//    }

    private void removeGarbageCollectedItems() {
        WeakEntry sv;
        while ((sv = (WeakEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            delegate.removeObject(sv.key);
        }
    }

    private static class WeakEntry extends WeakReference<Object> {
        private final Object key;
        public WeakEntry(Object key,Object value, ReferenceQueue<? super Object> garbageQueue) {
            super(value, garbageQueue);
            this.key = key;
        }
    }
}
