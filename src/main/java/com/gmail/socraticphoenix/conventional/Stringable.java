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

import com.gmail.socraticphoenix.collect.IdentityList;

import java.util.List;

/**
 * This interface defines methods used by the {@link StringChain} to handle edge cases
 *
 * @see StringChain
 */
public interface Stringable {

    /**
     * A method that acts as the {@link Object#toString()} method, while passing on the {@code seen} list and the
     * current {@code indent} to the {@link StringChain} used to convert {@code this} to a string. Returns a string
     * representing this object
     *
     * @param indent The current indent level used by the {@link StringChain}
     * @param seen   The {@code seen} list used by the {@link StringChain}
     *
     * @return A string representation of this object
     *
     * @see Stringable#toString(int)
     * @see Stringable#toString()
     * @see Object#toString()
     */
    String toString(int indent, List seen);

    /**
     * This method invokes {@code super.toString()} if {@link Stringable#superIsStringable()} is false, and {@code
     * super.toString(indent)}} if it is true
     *
     * @param indent The current indent level used by the {@link StringChain} (if applicable)
     * @param seen The {@code seen} list used by the {@link StringChain} (if applicable)
     *
     * @return A string representation of {@code super}
     *
     * @see Stringable#superIsStringable()
     */
    String superToString(int indent, List seen);

    /**
     * @return True if this class's superclass implements {@link Stringable}, false otherwise
     */
    boolean superIsStringable();

    /**
     * The toString method. This method should delegate to {@link Stringable#toString(int)} by passing in a 0
     *
     * @return {@code this.toString(0)}
     *
     * @see Stringable#toString(int)
     * @see Stringable#toString(int, List)
     * @see Object#toString()
     */
    @Override
    String toString();

    /**
     * This method delegates to {@link Stringable#toString(int, List)} by creating a new {@link IdentityList} and
     * passing {@code indent} and the new list
     *
     * @param indent The current indent level used by the {@link StringChain} (if applicable)
     *
     * @return {@code this.toString(indent, new IdentityList())}
     *
     * @see Stringable#toString(int, List)
     * @see Stringable#toString()
     * @see Object#toString()
     */
    default String toString(int indent) {
        return this.toString(indent, new IdentityList());
    }

    /**
     * This method returns the human readable form of this object. By default, this method delegates to {@link
     * Stringable#toString()}, but implementors are encouraged to override this method and provide a human readable
     * representation for their objects. For example, the {@code toString} method of a {@code Fraction} class
     * implementing this interface will display the identity hashcode and individual fields of the class, whereas it may
     * be desirable to display the {@code Fraction} in the form x/y
     *
     * @return A human readable representation of this object
     */
    default String toHumanReadable() {
        return this.toString();
    }

}
