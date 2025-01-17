package com.hiveworkshop.wc3.mdx;

import java.io.IOException;

import de.wc3data.stream.BlizzardDataInputStream;
import de.wc3data.stream.BlizzardDataOutputStream;

/**
 * "VERS" chunk of MDX files, which contains the version of the file.
 */
public class VersionChunk {

    public static final String key = "VERS";

    public int version;

    public void load( final BlizzardDataInputStream in ) throws IOException {

        MdxUtils.checkId( in, "VERS" );
        final int chunkSize = in.readInt();

        version = in.readInt();
    }

    public void save( final BlizzardDataOutputStream out ) throws IOException {

        out.writeNByteString( "VERS", 4 );
        out.writeInt( getSize() - 8 );// ChunkSize

        out.writeInt( version );

    }

    public int getSize() {
        int a = 0;
        a += 4;
        a += 4;
        a += 4;

        return a;
    }
}
