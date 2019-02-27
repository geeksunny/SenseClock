package com.radicalninja.senseclock.util;

import java.lang.reflect.Array;

import androidx.annotation.NonNull;

public class ArrayUtils {

    /**
     * Combine two or more Arrays of the same type.
     */
    public static <T> T[] combine(@NonNull final T[] ... arrays) {
        int length = 0;
        for (final T[] array : arrays) {
            if (null == array) {
                throw new IllegalArgumentException("Arrays must not be null objects.");
            }
            length += array.length;
        }
        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);
        int index = 0;
        for (final T[] array : arrays) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }
        return result;
    }

}
