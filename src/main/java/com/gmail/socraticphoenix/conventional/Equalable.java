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

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.IdentityHashMap;


/**
 * This interface defines methods used by the {@link EqualityChain} to handle edge cases
 *
 * @see EqualityChain
 */
public interface Equalable {

    /**
     * A method that acts as the {@link Object#equals(Object)} method, while passing on the {@code seen} map to the
     * {@link EqualityChain} used to compare {@code this} and {@code other}. Returns true if {@code this = other}, and
     * false otherwise
     *
     * @param other The object to compare against
     * @param seen  The seen map used by the {@link EqualityChain}
     *
     * @return true if {@code this = other}, false otherwise
     *
     * @see Equalable#equals(Object)
     * @see Object#equals(Object)
     */
    boolean equals(Object other, Map<Object, Pair<Object, AtomicReference<Boolean>>> seen);

    /**
     * This method invokes {@code super.equals(other, seen)} if this class's superclass implements {@link Equalable},
     * and {@code super.equals(other)} if it is not.
     *
     * @param other The object to compare against
     * @param seen The seen map used by the {@link EqualityChain}
     *
     * @return if {@code super} equals {@code other}
     */
    boolean superEquals(Object other, Map<Object, Pair<Object, AtomicReference<Boolean>>> seen);

    /**
     * The equals method. This method should delegate to {@link Equalable#equals(Object, Map)} by creating a new {@link
     * IdentityHashMap} and passing on {@code other} and the new map
     *
     * @param other The object to compare against
     *
     * @return {@code this.equals(other, new IdentityHashMap<>())}
     *
     * @see Equalable#equals(Object, Map)
     * @see Object#equals(Object)
     */
    @Override
    boolean equals(Object other);

}
