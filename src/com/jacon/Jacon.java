package com.jacon;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

final class Jacon
{
    private File filename;

    private File directory;

    Jacon( String[] args )
    {
        for(String arg: args)
        {
            if (arg.startsWith( "filename" ))
            {
                filename = new File( arg.substring( arg.indexOf( '=' ) + 1 ) );
            }
            else if (arg.startsWith( "directory" ))
            {
                directory = new File( arg.substring( arg.indexOf( "=" ) + 1 ) );
            }
        }

        ProcessSingleFile();
        //ProcessMultiplesFiles();
    }

    private void ProcessSingleFile( )
    {
        if (filename.exists() && filename.isFile())
        {
            new Convert().convertXML( filename );
        }
        else
        {
            Logger.getGlobal().log( Level.CONFIG, "The filename not exist." );
        }
    }

    private void ProcessMultiplesFiles( )
    {
        if (directory.exists() && directory.isDirectory())
        {
            File[] files = directory.listFiles();

            assert (files != null);

            for (File file : files)
            {
                if (file.getPath().endsWith( ".po" ))
                {
                    new Convert().convertXML( file );
                }
            }
        }
        else
        {
            Logger.getGlobal().log( Level.CONFIG, "The directory not exist." );
        }
    }
}
