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
import com.gmail.socraticphoenix.parse.Strings;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This class provides a convenient builder-syntac for constructing a unique and informational string to represent an
 * object/ This class uses a {@code seen} list to check already processed values, and accesses the methods from the
 * {@link Stringable} interface to propogate the list. The list ensures cyclic references are handled properly in arrays
 * and {@link Stringable} values. The chain works by holding a StringBuilder and creating an indented, multiline string.
 * When all values are processed, the result is returned through the {@link StringChain#finish()} method. It should be
 * noted that this class is meant to create <b>informational</b> strings, which aid in <b>debugging</b>, and are not
 * necessarily human readable. The {@link Stringable#toHumanReadable()} method should be used to create the conventional
 * representation of the object
 */
public class StringChain {
    private StringBuilder builder;
    private String ls;
    private int indent;
    private List seen;
    private boolean looseCollections;

    /**
     * Creates a new StringChain
     */
    public StringChain() {
        this(0, new IdentityList());
    }

    /**
     * Creates a new StringChain with the given {@code indent} and {@code seen} list
     *
     * @param indent The current indent level
     * @param seen   The {@code seen} list
     */
    public StringChain(int indent, List seen) {
        this(indent, seen, true);
    }

    /**
     * Creates a new StringChain with the given {@code indent}, {@code seen} list, and {@code looseCollections}
     * property. If {@code looseCollections} is true, instances of the {@link Collection} or {@link Map} class will be
     * converted to arrays and then compared
     *
     * @param indent           The current indent level
     * @param seen             The {@code seen} list
     * @param looseCollections Whether or not to treat collections and maps loosely
     */
    public StringChain(int indent, List seen, boolean looseCollections) {
        this.builder = new StringBuilder();
        this.ls = System.lineSeparator();
        this.seen = seen;
        this.indent = indent;
        this.looseCollections = looseCollections;
    }

    /**
     * Includes {@code "$identity"} -&#62; {@code Integer.toHexString(System.identityHashCode(val))}. This method is designed to be
     * called on the object that created the chain
     *
     * @param val The value to include
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain start(Object val) {
        this.seen.add(val);
        this.include("$identity", Integer.toHexString(System.identityHashCode(val)));
        return this;
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, int val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, byte val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, short val) {
        return this.include(name, String.valueOf(val));
    }

    public StringChain next(String name, char val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, long val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, boolean val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, float val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> val}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, double val) {
        return this.include(name, String.valueOf(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, int[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, byte[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, short[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, char[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, long[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, boolean[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, float[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, double[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(val));
    }

    /**
     * Includes {@code name -> " + Strings.escape(val.toString()) + "}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, CharSequence val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, "\"" + Strings.escape(val.toString()) + "\"");
    }

    /**
     * Includes {@code name -> Arrays.toString(val)}
     *
     * @param name The name of the variable
     * @param val  The value of the variable
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain next(String name, CharSequence[] val) {
        if (val == null) {
            return this.include(name, "null");
        }
        return this.include(name, Arrays.toString(Stream.of(val).map(sequence -> "\"" + Strings.escape(sequence.toString()) + "\"").toArray(String[]::new)));
    }

    private String toString(Object[] val) {
        if (val == null) {
            return "null";
        }
        StringChain sub = new StringChain(this.indent + 1, this.seen, this.looseCollections);
        for (int i = 0; i < val.length; i++) {
            sub.next(String.valueOf(i), val[i]);
        }
        return sub.finish();
    }

    /**
     * Includes {@code "$super" -> target.superToString()}. This method is designed to be
     * called on the object that created the chain
     *
     * @param target The value to include
     *
     * @return this
     *
     * @see StringChain#include(String, String)
     */
    public StringChain $super(Stringable target) {
        this.builder.append(Strings.indent(this.indent)).append("$super").append("= {");
        if (target.superIsStringable()) {
            this.builder.append(this.ls);
        }
        this.builder.append(target.superToString(this.indent + 1, this.seen));
        if (target.superIsStringable()) {
            this.builder.append(Strings.indent(this.indent));
        }
        this.builder.append("}").append(this.ls);
        return this;
    }

    private String toString(Object val) {
        StringBuilder builder = new StringBuilder();
        if (val == null) {
            return "null";
        } else if (val instanceof int[]) {
            builder.append(Arrays.toString((int[]) val));
        } else if (val instanceof byte[]) {
            builder.append(Arrays.toString((byte[]) val));
        } else if (val instanceof short[]) {
            builder.append(Arrays.toString((short[]) val));
        } else if (val instanceof char[]) {
            builder.append(Arrays.toString((char[]) val));
        } else if (val instanceof long[]) {
            builder.append(Arrays.toString((long[]) val));
        } else if (val instanceof boolean[]) {
            builder.append(Arrays.toString((boolean[]) val));
        } else if (val instanceof float[]) {
            builder.append(Arrays.toString((float[]) val));
        } else if (val instanceof double[]) {
            builder.append(Arrays.toString((double[]) val));
        } else if (val instanceof CharSequence || val instanceof Number) {
            builder.append("\"" + Strings.escape(val.toString()) + "\"");
        } else if (val instanceof CharSequence[]) {
            builder.append(Arrays.toString(Stream.of((CharSequence[]) val).map(s -> "\"" + Strings.escape(s.toString()) + "\"").toArray()));
        } else if (this.seen.contains(val)) {
            builder.append("{").append(val.getClass().getSimpleName()).append("@").append(Integer.toHexString(System.identityHashCode(val))).append("}");
        } else {
            this.seen.add(val);
            if (val instanceof Stringable) {
                builder.append("{").append(val.getClass().getSimpleName()).append("@").append(Integer.toHexString(System.identityHashCode(val)));
                builder.append(this.ls);
                builder.append(((Stringable) val).toString(this.indent + 1, this.seen));
                builder.append(Strings.indent(this.indent)).append("}");
            } else if (val instanceof Object[]) {
                builder.append("{").append(val.getClass().getSimpleName()).append("@").append(Integer.toHexString(System.identityHashCode(val))).append(this.ls);
                builder.append(this.toString((Object[]) val));
                builder.append(Strings.indent(this.indent)).append("}");
            } else if (val instanceof Collection && this.looseCollections) {
                return this.toString(((Collection) val).toArray());
            } else if (val instanceof Map && this.looseCollections) {
                builder.append("{").append(val.getClass().getSimpleName()).append("@").append(Integer.toHexString(System.identityHashCode(val)));
                builder.append(this.ls);
                Set<Map.Entry> set = ((Map) val).entrySet();
                Object[] keys = set.stream().map(Map.Entry::getKey).toArray();
                Object[] vals = set.stream().map(Map.Entry::getValue).toArray();
                StringChain sub = new StringChain(this.indent + 1, this.seen, this.looseCollections);
                builder.append(sub.next("keys", keys).next("values", vals).finish());
                builder.append(Strings.indent(this.indent)).append("}");
            } else {
                builder.append("{").append(val.getClass().getSimpleName()).append("@").append(Integer.toHexString(System.identityHashCode(val)));
                builder.append(" ").append(Strings.escape(String.valueOf(val)));
                builder.append("}");
            }
        }
        return builder.toString();
    }

    /**
     * Includes the given object. If val is null, "null" is included. Otherwise, if val is a primitive value, or a
     * primitive array, it will be appended using Arrays.toString. If val is an array, it will be appended by including
     * index -&#62; value, and if val is an object, String.valueOf() will be used
     *
     * @param name The name of the variable
     * @param val The value of the variable
     *
     * @return this
     */
    public StringChain next(String name, Object val) {
        this.builder.append(Strings.indent(this.indent)).append(name).append("= ").append(this.toString(val)).append(this.ls);
        return this;
    }

    /**
     * Appends the current indent to the builder, appends the name, and equals sign, the value, and finally a new line
     *
     * @param name The name to append
     * @param value The value to append
     * @return this
     */
    public StringChain include(String name, String value) {
        this.builder.append(Strings.indent(this.indent)).append(name).append("= ").append(value).append(this.ls);
        return this;
    }

    /**
     * @return The final result of the chain
     */
    public String finish() {
        return this.builder.toString();
    }

}
