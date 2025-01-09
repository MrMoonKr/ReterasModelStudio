package com.hiveworkshop.wc3.gui.datachooser;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import mpq.MPQArchive;
import mpq.MPQException;

/**
 * MPQ 파일에 기반한 에셋 데이터 제공자 팩토리
 */
public class MpqDataSourceDescriptor implements DataSourceDescriptor {

    private static final long serialVersionUID = 8424254987711783598L;

    /**
     * .mpq 에셋 파일 이름
     */
    private final String mpqFilePath;

    public MpqDataSourceDescriptor( final String mpqFilePath ) {
        this.mpqFilePath = mpqFilePath;
    }

    @Override
    public DataSource createDataSource() {
        try 
        {
            SeekableByteChannel byteChannel;
            byteChannel = Files.newByteChannel( Paths.get( mpqFilePath ), EnumSet.of( StandardOpenOption.READ ) );
            return new MpqDataSource( new MPQArchive( byteChannel ), byteChannel );
        } 
        catch ( final IOException e ) {
            throw new RuntimeException( e );
        } 
        catch ( final MPQException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public String getDisplayName() {
        return "MPQ Archive: " + mpqFilePath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( mpqFilePath == null ) ? 0 : mpqFilePath.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final MpqDataSourceDescriptor other = ( MpqDataSourceDescriptor )obj;
        if ( mpqFilePath == null ) {
            if ( other.mpqFilePath != null ) {
                return false;
            }
        } else if ( !mpqFilePath.equals( other.mpqFilePath ) ) {
            return false;
        }
        return true;
    }

    public String getMpqFilePath() {
        return mpqFilePath;
    }

    @Override
    public DataSourceDescriptor duplicate() {
        return new MpqDataSourceDescriptor( mpqFilePath );
    }
}
