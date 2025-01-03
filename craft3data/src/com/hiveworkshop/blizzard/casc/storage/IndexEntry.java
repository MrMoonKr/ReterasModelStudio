package com.hiveworkshop.blizzard.casc.storage;

import com.hiveworkshop.blizzard.casc.Key;

/**
 * ".idx" 파일에 저장되는 에셋의 정보를 담는 클래스.
 */
public class IndexEntry {
    /**
     * Index encoding key.
     */
    private final Key key;

    /**
     * Logical offset of storage container.
     */
    private final long dataOffset;

    /**
     * Size of storage container.
     */
    private final long fileSize;

    /**
     * ".idx" 파일에 저장되는 에셋의 정보를 담는 클래스.
     * @param key           9 bytes ( Encoding Key )
     * @param dataOffset    offset of the data in the data file
     * @param fileSize      size of the data in the data file
     */
    public IndexEntry( final byte[] key, final long dataOffset, final long fileSize ) {
        this.key        = new Key( key );
        this.dataOffset = dataOffset;
        this.fileSize   = fileSize;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append( "IndexEntry{key=" );
        builder.append( key );
        builder.append( ", dataOffset=" );
        builder.append( dataOffset );
        builder.append( ", fileSize=" );
        builder.append( fileSize );
        builder.append( "}" );

        return builder.toString();
    }

    public long getDataOffset() {
        return dataOffset;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getKeyString() {
        return key.toString();
    }

    public Key getKey() {
        return key;
    }

    public int compareKey( final Key otherKey ) {
        return otherKey.compareTo( key );
    }
}
