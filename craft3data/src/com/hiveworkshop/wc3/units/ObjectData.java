package com.hiveworkshop.wc3.units;

import java.util.Set;

/**
 * GameObject의 메타 데이터 인터페이스
 */
public interface ObjectData {
    GameObject get( String id );

    void setValue( String id, String field, String value );

    Set<String> keySet();
}
