package com.hiveworkshop.blizzard.casc;

import java.util.Map;

/**
 * 빌드 파일에 저장된 항목 저장 클래스
 * A reference to a file extracted from a configuration file.
 */
public class StorageReference {
    /**
     * Suffix for sizes mapping entry in configuration files.
     */
    private static final String SIZES_SUFFIX = "-size";

    /**
     * 저장 파일 사이즈
     */
    private final long storedSize;
    /** 
     * 원본 파일 사이즈 
     */
    private final long size;
    /**
     * eKey 압축된 파일의 해시
     */
    private final Key encodingKey;
    /**
     * cKey 원본 파일의 해시
     */
    private final Key contentKey;

    /**
     * 설정 항목 이름과 값을 저장
     * Decodes a storage reference from a configuration file.
     *
     * @param name          항목 이름 Name of reference.
     * @param configuration 항목 값 Map of configuration file content.
     */
    public StorageReference( final String name, final Map<String, String> configuration ) {

        final String keys = configuration.get( name ); // "cKey eKey"
        if ( keys == null ) {
            throw new IllegalArgumentException( "name does not exist in configuration" );
        }
        final String sizes = configuration.get( name + SIZES_SUFFIX ); // "originalSize storedSize"
        if ( sizes == null ) {
            throw new IllegalArgumentException( "size missing in configuration" );
        }

        final String[] keyStrings = keys.split( " " );
        contentKey  = new Key( keyStrings[0] );
        encodingKey = new Key( keyStrings[1] );

        final String[] sizeStrings = sizes.split( " " );
        size        = Long.parseLong( sizeStrings[0] );
        storedSize  = Long.parseLong( sizeStrings[1] );
    }

    /**
     * Content key?
     *
     * @return Content key.
     */
    public Key getContentKey() {
        return contentKey;
    }

    /**
     * Encoding key used to lookup the file from CASC storage.
     *
     * @return Encoding key.
     */
    public Key getEncodingKey() {
        return encodingKey;
    }

    /**
     * 원본 파일 사이즈 File size.
     *
     * @return File size in bytes of the file.
     */
    public long getSize() {
        return size;
    }

    /**
     * 저장 파일 사이즈 Size of file content in CASC storage.
     *
     * @return Approximate byte usage of file in CASC storage.
     */
    public long getStoredSize() {
        return storedSize;
    }

}
