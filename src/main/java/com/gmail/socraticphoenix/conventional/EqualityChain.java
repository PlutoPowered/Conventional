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

import com.gmail.socraticphoenix.collect.coupling.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * This class provides a convenient builder-syntax for checking the equality of two objects and their fields. This class
 * uses a {@code seen} map to track equality checks already made, and accesses methods from the {@link Equalable}
 * interface to propagate the map. The map ensures that cyclic references are handled properly in arrays and {@link
 * Equalable} values. The chain works by holding a single boolean result, and AND'ing the results of equality
 * comparisons to it. When all comparisons are finished, the result is returned through the {@link
 * EqualityChain#finish()} method. Since endlessly comparing cyclic references would fail, a definition for the equality
 * of such references must be made: two or more references which are cyclic are considered equal iff all their other
 * fields are equal. For example, suppose class A implements {@link Equalable} and uses this class for equality testing.
 * Also suppose class A has public variables a-b, initialized to null. The following will occur:
 * <pre>
 *     A a = new A();
 *     A b = new A();
 *     a.equals(b); //true
 *     a.a = b;
 *     b.a = a;
 *     a.equals(b); //true
 *     a.b = "hello world";
 *     b.b = "good day world";
 *     a.equals(b); //false
 * </pre>
 * This extends to any length of cycle, so the following will occur:
 * <pre>
 *     A a = new A();
 *     A b = new A();
 *     A c = new A();
 *     a.a = b;
 *     b.a = c;
 *     c.a = a;
 *     a.equals(b); //true
 *     b.equals(c); //true
 *     a.equals(c); //true
 * </pre>
 */
public class EqualityChain {
    private Map<Object, Pair<Object, AtomicReference<Boolean>>> seen;

    private Object a;
    private Object b;

    private boolean looseCollections;
    private boolean result;
    private boolean skip;

    /**
     * Creates a new EqualityChain
     */
    public EqualityChain() {
        this(new IdentityHashMap<>());
    }

    /**
     * Creates a new EqualityChain with the given {@code seen} map
     *
     * @param seen The {@code seen} map
     */
    public EqualityChain(Map<Object, Pair<Object, AtomicReference<Boolean>>> seen) {
        this(seen, true);
    }

    /**
     * Creates a new EqualityChain with the given {@code seen} map, and the given {@code looseCollections} option. If
     * {@code looseCollections} is true, instances of the {@link Collection} or {@link Map} class will be converted to
     * arrays and then compared.
     *
     * @param seen             The {@code seen} map
     * @param looseCollections Whether or not to treat collections and maps loosely
     */
    public EqualityChain(Map<Object, Pair<Object, AtomicReference<Boolean>>> seen, boolean looseCollections) {
        this.result = true;
        this.skip = false;
        this.seen = seen;
        this.looseCollections = looseCollections;
    }

    /**
     * Starts the equality chain by comparing the two root arguments; the objects being compared. {@code a} is the
     * object handling the {@link Object#equals(Object)} method, and {@code b} is the argument of that method. This
     * method performs direct equality (==) checks, null checks, and instanceof checks on the given objects. If any of
     * the checks are false, the overall chain result becomes false, and no further computation will be done in any
     * {@code next} methods.
     *
     * @param a The object handling the {@code equals} method
     * @param b The argument of the {@code equals} method
     *
     * @return this
     */
    public EqualityChain start(Object a, Object b) {
        this.a = a;
        this.b = b;
        if (a == b) {
            this.result = true;
            this.skip = true;
        } else if (!a.getClass().isInstance(b)) {
            this.result = false;
            this.skip = true;
        }

        return this;
    }

    /**
     * Executes the consumer on {@code this} iff the current chain result is true, and computations are enabled. This
     * method is meant to be used directly after the {@link EqualityChain#start(Object, Object)} method as it allows for
     * direct casting in the consumer.
     *
     * @param action The consumer to execute
     *
     * @return true if the equality comparison succeeded, false otherwise
     */
    public boolean safeCompare(Consumer<EqualityChain> action) {
        if (this.result && !this.skip) {
            action.accept(this);
        }
        return this.finish();
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(int a, int b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(char a, char b) {
        if (this.result && !this.skip && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(short a, short b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(byte a, byte b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(long a, long b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(boolean a, boolean b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(float a, float b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@code a == b}
     *
     * @param a The left-hand value
     * @param b The right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(double a, double b) {
        if (this.result && !this.skip) {
            return this.include(a == b);
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(int[], int[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(int[] a, int[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(char[], char[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(char[] a, char[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(short[], short[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(short[] a, short[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(byte[], byte[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(byte[] a, byte[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(long[], long[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(long[] a, long[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(boolean[], boolean[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(boolean[] a, boolean[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(float[], float[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(float[] a, float[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes {@link Arrays#equals(double[], double[]) Arrays.equals(a, b)}
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(double[] a, double[] b) {
        if (this.result && !this.skip) {
            return this.include(Arrays.equals(a, b));
        } else {
            return this;
        }
    }

    /**
     * Includes the equality comparison between a and b. The comparison maintains a {@code seen} map which prevents
     * endless looping. Cyclic reference of any complexity can be handled by this method
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(Object[] a, Object[] b) {
        if (!this.result || this.skip || (this.seen.containsKey(a) && this.seen.get(a).getA() == b) || (this.seen.containsKey(b) && this.seen.get(b).getA() == a)) {
            return this;
        } else {
            AtomicReference<Boolean> result = new AtomicReference<>();
            this.seen.put(a, Pair.of(b, result));
            result.set(this.deepEquals(a, b));
            return this;
        }
    }

    /**
     * Includes the equality comparison between a and b. If {@code a == b}, true will be used, if (@code a == null} or
     * {@code b == null}, false will be used. If a and b are instances of the same type of primitive array, the will be
     * compared using the appropriate {@link Arrays#equals(int[], int[]) Arrays.equals()}. Uf a abd b are both
     * collections or both maps, and {@code looseCollections} is true, they will be compared as arrays. If a and b are
     * both {@code Object[]}s, they will be deeply compared (see {@link EqualityChain#next(Object[], Object[])}). If a
     * and b are both {@link Equalable}, the {@link Equalable#equals(Object, Map)} method will be invoked. Otherwise,
     * the {@link Object#equals(Object)} method will be used.
     *
     * @param a the left-hand value
     * @param b the right-hand value
     *
     * @return this
     *
     * @see EqualityChain#include(boolean)
     */
    public EqualityChain next(Object a, Object b) {
        if (!this.result || this.skip || (this.seen.containsKey(a) && this.seen.get(a).getA() == b) || (this.seen.containsKey(b) && this.seen.get(b).getA() == a)) {
            return this;
        } else {
            if (a == b) {
                return this.include(true);
            } else if (a == null || b == null) {
                return this.include(false);
            } else if (a instanceof int[] && b instanceof int[]) {
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
            } else if (a instanceof Equalable && b instanceof Equalable) {
                return this.next((Equalable) a, (Equalable) b);
            } else {
                return this.include(a.equals(b));
            }
        }
    }

    private boolean deepEquals(Object[] a, Object[] b) {
        if (a.length != b.length) {
            return false;
        }

        EqualityChain sub = new EqualityChain(this.seen, this.looseCollections);
        for (int i = 0; i < a.length; i++) {
            sub.next(a[i], b[i]);
        }
        return sub.finish();
    }

    private EqualityChain next(Equalable a, Equalable b) {
        if ((this.seen.containsKey(a) && this.seen.get(a).getA() == b) || (this.seen.containsKey(b) && this.seen.get(b).getA() == a)) {
            return this;
        } else {
            AtomicReference<Boolean> result = new AtomicReference<>();
            this.seen.put(a, Pair.of(b, result));
            result.set(a.equals(b, this.seen));
            return this;
        }
    }

    /**
     * @return The final value of this chain
     */
    public boolean finish() {
        this.seen.values().stream().map(Pair::getB).filter(ref -> ref.get() != null).forEach(ref -> this.include(ref.get()));
        return this.result;
    }

    /**
     * Includes the equality comparison result in this chain. {@code val} will be AND'd with the current result to
     * produce the new result
     *
     * @param val The comparison result to include
     *
     * @return this
     */
    public EqualityChain include(boolean val) {
        this.result = this.result && val;
        return this;
    }

    /**
     * The EqualityChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @return the current result
     */
    public int hashCode() {
        return this.result ? 0 : 1;
    }

    /**
     * The EqualityChain is considered a finite state machine for the purposes of hashCode and equals, although
     * contextual data is held in the class, only the currently calculated value is used to determine the result of
     * hashCode and equals
     *
     * @param other The object to compare against
     *
     * @return true if {@code this.result = other.result}, false otherwise
     */
    public boolean equals(Object other) {
        return other != null && other instanceof EqualityChain && other.hashCode() == this.hashCode();
    }

}
