package com.hiveworkshop.wc3.gui.datachooser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.hiveworkshop.blizzard.casc.io.WarcraftIIICASC;
import com.hiveworkshop.blizzard.casc.io.WarcraftIIICASC.FileSystem;
import com.hiveworkshop.nio.ByteBufferInputStream;

public class GetCascLanguageList {

    public static void main( final String[] args ) {

        try 
        {
            //Path installPath = Paths.get( "C:/Program Files/Warcraft III" );
            Path installPath = Paths.get( "E:/myGames/Warcraft3" );

            final WarcraftIIICASC casc = 
                    //new WarcraftIIICASC( installPath, true, CascDataSource.Product.WARCRAFT_III.getKey() );
                    new WarcraftIIICASC( installPath, true, "w3" );

            final FileSystem rootFileSystem = casc.getRootFileSystem();
            if ( rootFileSystem.isFile( "index" ) && rootFileSystem.isFileAvailable( "index" ) ) 
            {
                final ByteBuffer buffer = rootFileSystem.readFileData( "index" );

                final Set<String> categories = new HashSet<>();
                try ( BufferedReader reader = new BufferedReader( new InputStreamReader( new ByteBufferInputStream( buffer ) ) ) ) 
                {
                    String line;
                    while ( ( line = reader.readLine() ) != null ) 
                    {
                        final String[] splitLine = line.split( "\\|" );
                        if ( splitLine.length >= 3 ) {
                            final String category = splitLine[2];
                            categories.add( category );
                        }
                    }
                }
                
                /*System.out.println( "Categories:" );
                for ( final String category : categories ) {
                    System.out.println( "   " + category );
                }*/
                print( "Categories:", ANSI_PURPLE );
                for ( final String category : categories ) {
                    print( "   " + category, ANSI_YELLOW );
                }
            }
        } 
        catch ( final IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static final String ANSI_RESET   = "\u001B[0m";
    public static final String ANSI_BLACK   = "\u001B[30m";
    public static final String ANSI_RED     = "\u001B[31m";
    public static final String ANSI_GREEN   = "\u001B[32m";
    public static final String ANSI_YELLOW  = "\u001B[33m";
    public static final String ANSI_BLUE    = "\u001B[34m";
    public static final String ANSI_PURPLE  = "\u001B[35m";
    public static final String ANSI_CYAN    = "\u001B[36m";
    public static final String ANSI_WHITE   = "\u001B[37m";

    public static void print( String message, String color ) {
        System.out.println( color + message + ANSI_RESET );
    }
}
