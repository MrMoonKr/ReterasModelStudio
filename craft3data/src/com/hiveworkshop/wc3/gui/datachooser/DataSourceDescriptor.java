package com.hiveworkshop.wc3.gui.datachooser;

import java.io.Serializable;

/**
 * 에셋 데이터 제공자 팩토리 인터페이스
 */
public interface DataSourceDescriptor extends Serializable {

    DataSource createDataSource();

    String getDisplayName();

    DataSourceDescriptor duplicate();
}
