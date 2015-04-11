package com.coscon.shipsuite.common.util.generic;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;

public class WeakHashSet extends HashSet {
    
    private static final long serialVersionUID = -6936461186898411657L;
    
    ReferenceQueue queue = new ReferenceQueue();

    public Iterator iterator() {
	processQueue();

	final Iterator i = super.iterator();

	return new Iterator() {
	    public boolean hasNext() {
		return i.hasNext();
	    }

	    public Object next() {
		return WeakHashSet.this.getReferenceObject((WeakReference) i
			.next());
	    }

	    public void remove() {
		i.remove();
	    }
	};
    }

    public boolean contains(Object o) {
	return super.contains(WeakElement.create(o));
    }

    public boolean add(Object o) {
	processQueue();
	return super.add(WeakElement.create(o, this.queue));
    }

    public boolean remove(Object o) {
	boolean ret = super.remove(WeakElement.create(o));
	processQueue();
	return ret;
    }

    private final Object getReferenceObject(WeakReference ref) {
	return ref == null ? null : ref.get();
    }

    private final void processQueue() {
	WeakElement wv = null;
	while ((wv = (WeakElement) this.queue.poll()) != null) {
	    super.remove(wv);
	}
    }

    private static class WeakElement extends WeakReference {
	private int hash;

	private WeakElement(Object o) {
	    super(o);
	    this.hash = o.hashCode();
	}

	private WeakElement(Object o, ReferenceQueue q) {
	    super(q);
	    this.hash = o.hashCode();
	}

	private static WeakElement create(Object o) {
	    return o == null ? null : new WeakElement(o);
	}

	private static WeakElement create(Object o, ReferenceQueue q) {
	    return o == null ? null : new WeakElement(o, q);
	}

	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (!(o instanceof WeakElement)) {
		return false;
	    }
	    Object t = get();
	    Object u = ((WeakElement) o).get();
	    if (t == u) {
		return true;
	    }
	    if ((t == null) || (u == null)) {
		return false;
	    }
	    return t.equals(u);
	}

	public int hashCode() {
	    return this.hash;
	}
    }
}
