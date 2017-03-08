/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.conventional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class provides a convenient builder-syntax for constructing a unique hash code for an object. This class uses a
 * {@code seen} map to check already calculated values, and accesses the methods from the {@link Hashable} interface to
 * propagate the map. The map ensures cyclic references are handled properly in arrays and {@link Hashable} values. The
 * chain works by holding a single integer resulting, and apply new values to that result. The formula for applying a
 * new value is (a,i) = 31a + i, where a is the current result, and b is the new result to apply. When all values
 * are calculated, the result is returned through the {@link HashChain#finish()} method
 */
public class HashChain {
    private int hash;
    private Map<Object, AtomicInteger> seen;
    private List<AtomicInteger> additional;
    private boolean looseCollections;

    /**
     * Creates a new HashChain
     */
    public HashChain() {
        this(new IdentityHashMap<>());
    }

    /**
     * Creates a new HashChain with the given {@code seen} map
     *
     * @param seen The {@code seen} map
     */
    public HashChain(Map<Object, AtomicInteger> seen) {
        this(seen, true);
    }

    /**
     * Creates a new HashChain with the given {@code seen} map, and the given {@code looseCollections option}. If {@code
     * looseCollections} is true, instances of the {@link Collection} or {@link Map} class will be converted to arrays
     * and then compared
     *
     * @param seen             The {@code seen} map
     * @param looseCollections Whether or not to treat collections and maps loosely
     */
    public HashChain(Map<Object, AtomicInteger> seen, boolean looseCollections) {
        this.hash = 1;
        this.seen = seen;
        this.additional = new ArrayList<>();
        this.looseCollections = looseCollections;
    }

    /**
     * Includes {@code a.superHashCode()}
     *
     * @param a The object handling the {@code hashCode} method
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain $super(Hashable a) {
        return this.include(a.superHashCode(this.seen));
    }

    /**
     * Includes 0 if {@code val} is true, 1 otherwise
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(boolean val) {
        return this.include(val ? 0 : 1);
    }

    /**
     * Includes {@code val}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(int val) {
        return this.include(val);
    }

    /**
     * Includes {@code (int) val}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(char val) {
        return this.include(val);
    }

    /**
     * Includes {@code (int) val}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(byte val) {
        return this.include(val);
    }

    /**
     * Includes {@code (int) val}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(short val) {
        return this.include(val);
    }

    /**
     * Includes {@code (int) (val ^ (val >>> 32))}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(long val) {
        return this.include((int) (val ^ (val >>> 32)));
    }

    /**
     * Includes {@link Float#floatToIntBits(float) Float.floatToIntBits(val)}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(float val) {
        return this.include(Float.floatToIntBits(val));
    }

    /**
     * Converts {@code val} to a long with {@link Double#doubleToLongBits(double)}, then includes {@code (int) (val ^
     * (val >>> 32))}
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(double val) {
        return this.next(Double.doubleToLongBits(val));
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(double)
     * @see HashChain#include(int)
     */
    public HashChain next(double[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (double v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(float)
     * @see HashChain#include(int)
     */
    public HashChain next(float[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (float v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(long)
     * @see HashChain#include(int)
     */
    public HashChain next(long[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (long v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(short)
     * @see HashChain#include(int)
     */
    public HashChain next(short[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (short v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(byte)
     * @see HashChain#include(int)
     */
    public HashChain next(byte[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (byte v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(char)
     * @see HashChain#include(int)
     */
    public HashChain next(char[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (char v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(boolean)
     * @see HashChain#include(int)
     */
    public HashChain next(boolean[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (boolean v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(int)
     * @see HashChain#include(int)
     */
    public HashChain next(int[] val) {
        if (val == null) {
            return this.include(0);
        }
        for (int v : val) {
            this.next(v);
        }
        return this;
    }

    /**
     * Includes every element from {@code val}
     *
     * @param val The values to include
     *
     * @return this
     *
     * @see HashChain#next(Object)
     * @see HashChain#include(int)
     */
    public HashChain next(Object[] val) {
        if (val == null) {
            return this.include(0);
        }
        if (!this.seen.containsKey(val)) {
            AtomicInteger integer = new AtomicInteger();
            this.seen.put(val, integer);
            integer.set(this.deepHashCode(val));
            return this;
        } else {
            this.additional.add(this.seen.get(val));
            return this;
        }
    }

    /**
     * Includes {@code val}. If val is primitive, or a primitive array, the appropriate {@code next} method will be
     * used. If val is null, 0 will be used. Otherwise, the hashcode of val will be calculated and included
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see HashChain#include(int)
     */
    public HashChain next(Object val) {
        if (val == null) {
            return this.include(0);
        } else if (val instanceof boolean[]) {
            return this.next((boolean[]) val);
        } else if (val instanceof int[]) {
            return this.next((int[]) val);
        } else if (val instanceof char[]) {
            return this.next((char[]) val);
        } else if (val instanceof byte[]) {
            return this.next((byte[]) val);
        } else if (val instanceof short[]) {
            return this.next((short[]) val);
        } else if (val instanceof long[]) {
            return this.next((long[]) val);
        } else if (val instanceof float[]) {
            return this.next((float[]) val);
        } else if (val instanceof double[]) {
            return this.next((double[]) val);
        } else if (val instanceof Object[]) {
            return this.next((Object[]) val);
        } else if (val instanceof Collection && this.looseCollections) {
            return this.next(((Collection) val).toArray());
        } else if (val instanceof Map && this.looseCollections) {
            return this.next((Map) val);
        } else if (val instanceof Hashable) {
            return this.next((Hashable) val);
        } else {
            return this.include(val.hashCode());
        }
    }

    private int deepHashCode(Map val) {
        HashChain sub = new HashChain(this.seen);
        Set<Map.Entry> set = val.entrySet();
        set.forEach(e -> sub.next(e.getKey()).next(e.getValue()));
        return sub.finish();
    }

    private HashChain next(Map val) {
        if (!this.seen.containsKey(val)) {
            if (val == null) {
                return this.include(0);
            }
            AtomicInteger integer = new AtomicInteger();
            this.seen.put(val, integer);
            integer.set(this.deepHashCode(val));
            return this;
        } else {
            this.additional.add(this.seen.get(val));
            return this;
        }
    }

    private int deepHashCode(Object[] val) {
        HashChain sub = new HashChain(this.seen, this.looseCollections);
        for (Object v : val) {
            sub.next(v);
        }
        return sub.finish();
    }

    private HashChain next(Hashable val) {
        if (val == null) {
            return this.include(0);
        }
        if (!this.seen.containsKey(val)) {
            AtomicInteger integer = new AtomicInteger();
            this.seen.put(val, integer);
            integer.set(val.hashCode(this.seen));
            return this;
        } else {
            this.additional.add(this.seen.get(val));
            return this;
        }
    }

    /**
     * Includes the value in this chain. The current value will be redefined with the formula f(a,i) = 31a + i, where a
     * is the current value and i is the new value to include
     *
     * @param i The new value to include
     *
     * @return this
     */
    public HashChain include(int i) {
        this.hash = (31 * this.hash) + i;
        return this;
    }

    /**
     * @return The final result of this chain
     */
    public int finish() {
        this.seen.values().stream().map(AtomicInteger::get).forEach(this::include);
        this.additional.stream().map(AtomicInteger::get).forEach(this::include);
        return this.hashCode();
    }

    /**
     * The HashChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @return the current result
     */
    public int hashCode() {
        return this.hash;
    }

    /**
     * The HashChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @param other The object to compare against
     *
     * @return true if {@code this.result == other.result}, false otherwise
     */
    public boolean equals(Object other) {
        return other != null && other instanceof HashChain && other.hashCode() == this.hashCode();
    }

}
