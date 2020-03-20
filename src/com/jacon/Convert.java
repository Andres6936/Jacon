package com.jacon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

final class Convert
{
    private List<String> buffer;

    void convertXML( File file )
    {
        try
        {
            buffer = Files.readAllLines( file.toPath() );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
        }

        System.out.print( buffer.toString() );
    }
}
