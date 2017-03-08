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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This interface defines methods used by the {@link ComparisonChain} to handle edge cases
 *
 * @param <T> The type of object this class is comparable to
 *
 * @see ComparisonChain
 */
public interface Comparable<T> extends java.lang.Comparable<T> {

    /**
     * A method that acts as the {@link java.lang.Comparable#compareTo(Object)} method, while passing on the {@code
     * seen} map to the {@link ComparisonChain} used to compare {@code this} and {@code other}. Returns a positive
     * integer if <code>this &#62; other</code>, a negative integer if <code>this &#60; other</code>, or zero if {@code this = other}. It
     * should be noted that the {@link ComparisonChain} considers un-comparable objects non-impactful (the same as equal
     * objects), so this method may break the general recommendation that {@code (x.compareTo(y)==0) == (x.equals(y))}
     *
     * @param other The object to compare against
     * @param seen  The seen map used by the {@link ComparisonChain}
     *
     * @return a positive integer if <code>this &#62; other</code>, a negative integer if <code>this &#60; other</code>, or zero if {@code
     * this = other}
     *
     * @see Comparable#compareTo(Object)
     * @see java.lang.Comparable#compareTo(Object)
     */
    int compareTo(T other, Map<Object, Pair<Object, AtomicReference<Integer>>> seen);


    /**
     * The compareTo method. This method should delegate to {@link Comparable#compareTo(Object)} (Object, Map)} by
     * creating a new {@link java.util.IdentityHashMap} and passing on {@code other} and the new map
     *
     * @param other The object to compare against
     *
     * @return {@code this.compareTo(other, new IdentityHashMap<>())}
     *
     * @see Comparable#compareTo(Object, Map)
     * @see java.lang.Comparable#compareTo(Object)
     */
    @Override
    default int compareTo(T other) {
        return this.compareTo(other, new IdentityHashMap<>());
    }
}
