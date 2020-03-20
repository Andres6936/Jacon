package com.jacon;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.lang.StringBuilder;

final class Convert
{
    // Fields

    private List<String> buffer;

    private String commentHead;

    // Methods

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

        extractCommentHead();
        deleteComments();
        deleteLinesEmpty();
    }

    private void extractCommentHead()
    {
        StringBuilder stringBuilder = new StringBuilder(  );

        for ( String comment : buffer )
        {
            if (comment.startsWith( "#" ))
            {
                stringBuilder.append( comment );
            }
            else
            {
                break;
            }
        }

        commentHead = stringBuilder.toString();
    }

    private void deleteComments()
    {
        for ( int i = 0; i < buffer.size(); i++ )
        {
            if ( buffer.get( i ).startsWith( "#" ))
            {
                buffer.remove( i );
                i -= 1;
            }
        }
    }

    private void deleteLinesEmpty()
    {
        for ( int i = 0; i < buffer.size(); i++ )
        {
            if (buffer.get( i ).isEmpty())
            {
                buffer.remove( i );
                i -= 1;
            }
        }
    }
}
