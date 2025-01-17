package com.hiveworkshop.wc3.mdx;

import java.io.IOException;

import de.wc3data.stream.BlizzardDataInputStream;
import de.wc3data.stream.BlizzardDataOutputStream;

/**
 * "KEVT" chunk of MDX files
 */
public class Tracks {

    public static final String key = "KEVT";

    public int globalSequenceId;
    public int[] tracks = new int[0];

    public void load( BlizzardDataInputStream in ) throws IOException {

        MdxUtils.checkId( in, "KEVT" );
        int nrOfTracks = in.readInt();

        globalSequenceId = in.readInt();
        tracks = MdxUtils.loadIntArray( in, nrOfTracks );
    }

    public void save( BlizzardDataOutputStream out ) throws IOException {
        int nrOfTracks = tracks.length;
        out.writeNByteString( "KEVT", 4 );
        out.writeInt( nrOfTracks );
        out.writeInt( globalSequenceId );
        MdxUtils.saveIntArray( out, tracks );

    }

    public int getSize() {
        int a = 0;
        a += 4;
        a += 4;
        a += 4;
        a += 4 * tracks.length;

        return a;
    }
}
