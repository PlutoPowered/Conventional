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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This interface defines methods used by {@link HashChain} to handle edge cases
 *
 * @see HashChain
 */
public interface Hashable {

    /**
     * A method that acts as the {@link Object#hashCode()} method, while passing on the {@code seen} map to the {@link
     * HashChain} used to generate the hashcode for {@code this}. Returns the hashcode for {@code this}
     *
     * @param seen The seen map used by the {@link HashChain}
     *
     * @return The hashcode of {@code this}
     *
     * @see Hashable#hashCode()
     * @see Object#hashCode()
     */
    int hashCode(Map<Object, AtomicInteger> seen);

    /**
     * The hashCode method. This method should delegate to {@link Hashable#hashCode(Map)} by creating a new {@link
     * IdentityHashMap} and passing it on
     *
     * @return {@code this.hashCode(new IdentityHashMap<>())}
     *
     * @see Hashable#hashCode(Map)
     * @see Object#hashCode()
     */
    @Override
    int hashCode();

}
