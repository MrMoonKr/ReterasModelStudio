package com.hiveworkshop.blizzard.casc;

import com.hiveworkshop.ReteraCASCUtils;
import com.hiveworkshop.lang.Hex;

/**
 * byte[] 형태로 표현되는 키( 16바이트 또는 9바이트 )를 표현하는 클래스.  
 * Class representing a CASC related key such as an encoding key.
 * <p>
 * When testing equality and comparing the length of the shortest key is used.
 */
public final class Key implements Comparable<Key> {
    /**
     * Key array.
     */
    private final byte[] key;

    /**
     * Wraps a byte array into a key. The array is used directly so must not be
     * modified.
     *
     * @param key  9 or 16 byte key.
     */
    public Key( final byte[] key ) {
        this.key = key;
    }

    /**
     * Constructs a key from a hexadecimal key string. Bytes are order in the order
     * they appear in the string, which can be considered big endian.
     *
     * @param keyString hexadecimal key form of key. (ex) 85b63b814f10bcde9a643bba3e3aa054
     */
    public Key( final CharSequence key ) {
        this.key = Hex.decodeHex( key );
    }

    @Override
    public int compareTo( final Key o ) {
        final int commonLength = Math.min( key.length, o.key.length );
        return ReteraCASCUtils.arraysCompareUnsigned( key, 0, commonLength, o.key, 0, commonLength );
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( ( obj == null ) || !( obj instanceof Key ) ) {
            return false;
        }

        final Key otherKey = ( Key )obj;
        final int commonLength = Math.min( key.length, otherKey.key.length );
        if ( !ReteraCASCUtils.arraysEquals( key, 0, commonLength, otherKey.key, 0, commonLength ) ) {
            return false;
        }

        return true;
    }

    /**
     * Return the wrapped key array for low level interaction.
     *
     * @return Key array.
     */
    public byte[] getKey() {
        return key.clone();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException(
                "key hash code not safe to use due to variable sizes between systems" );
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder( ( key.length + 1 ) );
        Hex.stringBufferAppendHex( builder, key );
        return builder.toString();
    }
}
