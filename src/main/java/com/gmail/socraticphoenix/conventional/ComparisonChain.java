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

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.collect.coupling.Pair;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class provides a convenient builder-syntax for comparing two objects and their fields. The class uses a {@code
 * seen} map to track comparisons already made, and accesses methods from the {@link Comparable} interface to propagate the
 * map. The map ensures that cyclic references are handled properly in arrays and {@link Comparable} values. The chain
 * works by holding a single integer result, and adding the results of comparisons to it. When all comparisons are
 * finished, the result is returned through the {@link ComparisonChain#finish()} method
 */
public class ComparisonChain {
    private Map<Object, Pair<Object, AtomicReference<Integer>>> seen;

    private boolean looseCollections;
    private boolean normalize;
    private int result;

    /**
     * Creates a new ComparisonChain with the given {@code seen} map, and the given {@code looseCollections} and {@code
     * normalize} options. If {@code looseCollections} is true, instances of the {@link Collection} or {@link Map} class
     * will be converted to arrays and then compared. If {@code normalize} is true, each comparison will be reduced to a
     * 1, 0, or -1 if the result was a positive integer, zero, or a negative integer respectively.
     *
     * @param seen             The {@code seen} map
     * @param looseCollections Whether or not to treat collections and maps loosely
     * @param normalize        Whether or not to normalize comparisons
     */
    public ComparisonChain(Map<Object, Pair<Object, AtomicReference<Integer>>> seen, boolean looseCollections, boolean normalize) {
        this.seen = seen;
        this.looseCollections = looseCollections;
        this.normalize = normalize;
        this.result = 0;
    }

    /**
     * Creates a new ComparisonChain with the given {@code seen} map
     *
     * @param seen The {@code seen} map
     */
    public ComparisonChain(Map<Object, Pair<Object, AtomicReference<Integer>>> seen) {
        this(seen, true, false);
    }

    /**
     * Creates a new ComparisonChain
     */
    public ComparisonChain() {
        this(new IdentityHashMap<>());
    }

    /**
     * Includes {@link Integer#compare(int, int) Integer.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(int a, int b) {
        return this.include(Integer.compare(a, b));
    }

    /**
     * Includes {@link Character#compare(char, char) Character.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(char a, char b) {
        return this.include(Character.compare(a, b));
    }

    /**
     * Includes {@link Short#compare(short, short) Short.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(short a, short b) {
        return this.include(Short.compare(a, b));
    }

    /**
     * Includes {@link Byte#compare(byte, byte) Byte.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(byte a, byte b) {
        return this.include(Byte.compare(a, b));
    }

    /**
     * Includes {@link Long#compare(long, long) Long.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(long a, long b) {
        return this.include(Long.compare(a, b));
    }

    /**
     * Includes {@link Boolean#compare(boolean, boolean) Boolean.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(boolean a, boolean b) {
        return this.include(Boolean.compare(a, b));
    }

    /**
     * Includes {@link Float#compare(float, float) Float.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(float a, float b) {
        return this.include(Float.compare(a, b));
    }

    /**
     * Includes {@link Double#compare(double, double) Double.compare(a, b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(double a, double b) {
        return this.include(Double.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(int[], int[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(int[] a, int[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(char[], char[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(char[] a, char[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(short[], short[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(short[] a, short[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(byte[], byte[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(byte[] a, byte[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(long[], long[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(long[] a, long[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(boolean[], boolean[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(boolean[] a, boolean[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(float[], float[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(float[] a, float[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes {@link Items#compare(double[], double[]) Items.compare(a, b)}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(double[] a, double[] b) {
        return this.include(Items.compare(a, b));
    }

    /**
     * Includes the comparison between a and b. The comparison maintains a {@code seen} map which prevents endless
     * looping. Cyclic references of any complexity can be handled by this method
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(Object[] a, Object[] b) {
        if (a == b || a == null || b == null) {
            return this.include(a == b ? 0 : a == null ? -1 : 1);
        }

        if (this.seen.containsKey(a) && this.seen.get(a).getA() == b) {
            return this;
        } else {
            AtomicReference<Integer> result = new AtomicReference<>();
            this.seen.put(a, Pair.of(b, result));
            result.set(this.deepCompare(a, b));
            return this;
        }
    }

    /**
     * Includes the comparison between a and b. If {@code a == b}, zero is used, and if {@code a == null} or {@code b ==
     * null}, the null value will be considered less than the nonull value. If a and b are instances of the same type of
     * primitive array, they will be compared using the appropriate {@link Items#compare(int[], int[]) Items.compare()}
     * method. If a and b are both collections or both maps, and {@code looseCollections} is true, they will be compared as
     * arrays. If a and b are both {@code Object[]}s, they will be deeply compared (see {@link
     * ComparisonChain#next(Object[], Object[])}). If a and b are both {@link Comparable}, the {@link
     * Comparable#compareTo(Object, Map)} method will be invoked. If a and b are both {@link java.lang.Comparable}, the
     * {@link java.lang.Comparable#compareTo(Object)} method will be invoked. If the objects are not comparable, they
     * will have no impact on the comparison (as if they were equal).
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return {@code this}
     *
     * @see ComparisonChain#include(int)
     */
    public ComparisonChain next(Object a, Object b) {
        if (a == b || a == null || b == null) {
            return this.include(a == b ? 0 : a == null ? -1 : 1);
        }

        if (this.seen.containsKey(a) && this.seen.get(a).getA() == b) {
            return this;
        } else {
            if (a instanceof int[] && b instanceof int[]) {
                return this.next((int[]) a, (int[]) b);
            } else if (a instanceof short[] && b instanceof short[]) {
                return this.next((short[]) a, (short[]) b);
            } else if (a instanceof byte[] && b instanceof byte[]) {
                return this.next((byte[]) a, (byte[]) b);
            } else if (a instanceof char[] && b instanceof char[]) {
                return this.next((char[]) a, (char[]) b);
            } else if (a instanceof boolean[] && b instanceof boolean[]) {
                return this.next((boolean[]) a, (boolean[]) b);
            } else if (a instanceof long[] && b instanceof long[]) {
                return this.next((long[]) a, (long[]) b);
            } else if (a instanceof float[] && b instanceof float[]) {
                return this.next((float[]) a, (float[]) b);
            } else if (a instanceof double[] && b instanceof double[]) {
                return this.next((double[]) a, (double[]) b);
            } else if (a instanceof Object[] && b instanceof Object[]) {
                return this.next((Object[]) a, (Object[]) b);
            } else if (a instanceof Collection && b instanceof Collection && this.looseCollections) {
                return this.next(((Collection) a).toArray(), ((Collection) b).toArray());
            } else if (a instanceof Map && b instanceof Map && this.looseCollections) {
                Set<Map.Entry> setA = ((Map) a).entrySet();
                Object[] keysA = setA.stream().map(Map.Entry::getKey).toArray();
                Object[] valsA = setA.stream().map(Map.Entry::getValue).toArray();

                Set<Map.Entry> setB = ((Map) b).entrySet();
                Object[] keysB = setB.stream().map(Map.Entry::getKey).toArray();
                Object[] valsB = setB.stream().map(Map.Entry::getValue).toArray();

                this.next(keysA, keysB);
                this.next(valsA, valsB);
                return this;
            } else if (a instanceof Comparable && b instanceof Comparable) {
                return this.next((Comparable) a, (Comparable) b);
            } else if (a instanceof java.lang.Comparable && b instanceof java.lang.Comparable) {
                return this.next((java.lang.Comparable) a, (java.lang.Comparable) b);
            } else {
                return this.include(0);
            }
        }
    }

    private ComparisonChain next(java.lang.Comparable a, java.lang.Comparable b) {
        if (a == b || a == null || b == null) {
            return this.include(a == b ? 0 : a == null ? -1 : 1);
        }
        try {
            return this.include(a.compareTo(b));
        } catch (ClassCastException e) {
            return this.include(0);
        }
    }

    private ComparisonChain next(Comparable a, Comparable b) {
        if (a == b || a == null || b == null) {
            return this.include(a == b ? 0 : a == null ? -1 : 1);
        }
        if (this.seen.containsKey(a) && this.seen.get(a).getA() == b) {
            return this;
        } else {
            AtomicReference<Integer> result = new AtomicReference<>();
            this.seen.put(a, Pair.of(b, result));
            try {
                result.set(a.compareTo(b, this.seen));
            } catch (ClassCastException e) {
                result.set(0);
            }
            return this;
        }


    }

    private int deepCompare(Object[] a, Object[] b) {
        ComparisonChain chain = new ComparisonChain(this.seen, this.looseCollections, this.normalize);
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            chain.next(a[i], b[i]);
        }
        chain.include(a.length - b.length);
        return chain.finish();
    }

    /**
     * @return The final value of this chain
     */
    public int finish() {
        this.seen.values().stream().map(p -> p.getB().get()).filter(i -> i != null).forEach(this::include);
        return this.result;
    }

    /**
     * The ComparisonChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @return the current result
     */
    public int hashCode() {
        return this.result;
    }

    /**
     * The ComparisonChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @param other The object to compare against
     *
     * @return true if {@code this.result = other.result}, false otherwise
     */
    public boolean equals(Object other) {
        return other != null && other instanceof HashChain && other.hashCode() == this.hashCode();
    }

    /**
     * Includes the comparison result in this chain. If {@code normalize} is true, the value will be converted to -1, 0
     * or 1 if it is a negative integer, zero, or a positive integer, respectively. The value will then be added to the
     * current result
     *
     * @param val The comparison result to include
     *
     * @return this
     */
    public ComparisonChain include(int val) {
        if (this.normalize) {
            if (val != 0) {
                this.result += val < 0 ? -1 : 1;
            }
        } else {
            this.result += val;
        }
        return this;
    }

}
