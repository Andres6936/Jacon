package com.jacon;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.lang.StringBuilder;
import java.util.Vector;

final class Convert
{
    // Fields

    private List<String> buffer;

    private Vector<Dictionary> dictionaryList;

    private String commentHead;

    // Construct

    Convert()
    {
        dictionaryList = new Vector<>(  );
    }

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
        mergeTags();
        fillDictionaryList();
        deleteCharacterUnused();
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

    private void mergeTags()
    {
        for ( int i = 0; i < buffer.size( ); i++ )
        {
            if (buffer.get( i ).startsWith( "msgid " ))
            {
                StringBuilder wordKey = new StringBuilder( 1024 );

                wordKey.append( buffer.get( i ) );

                while ( i + 1 < buffer.size() )
                {
                    if (! buffer.get( i + 1 ).startsWith( "msgstr " ))
                    {
                        wordKey.append( buffer.remove( i + 1 ) );
                    }
                    else
                    {
                        break;
                    }
                }

                buffer.remove( i );
                buffer.add( i, wordKey.toString() );
            }
            else if (buffer.get( i ).startsWith( "msgstr " ))
            {
                StringBuilder wordKey = new StringBuilder( 1024 );

                wordKey.append( buffer.get( i ) );

                while ( i + 1 < buffer.size() )
                {
                    if (! buffer.get( i + 1 ).startsWith( "msgid " ))
                    {
                        wordKey.append( buffer.remove( i + 1 ) );
                    }
                    else
                    {
                        break;
                    }
                }

                buffer.remove( i );
                buffer.add( i, wordKey.toString() );
            }
        }
    }

    private void fillDictionaryList()
    {
        String key = "";
        String value = "";

        for (String string : buffer)
        {
            if (string.startsWith( "msgid " ))
            {
                key = string;
                continue;
            }
            else if (string.startsWith( "msgstr " ))
            {
                value = string;
            }
            else
            {
                System.out.println( "Entry in dictionary not recognized" );
            }

            dictionaryList.add( new Dictionary( key, value ) );
        }
    }

    private void deleteCharacterUnused()
    {
        for (Dictionary word : dictionaryList)
        {
            word.deleteCharacterInKey( '"' );
            word.deleteCharacterInKey( '.' );
            word.deleteCharacterInKey( '?' );
            word.deleteCharacterInKey( ':' );
            word.deleteCharacterInKey( '/' );
            word.deleteCharacterInKey( '\\' );
            word.deleteCharacterInKey( '<' );
            word.deleteCharacterInKey( '>' );
            word.deleteCharacterInKey( '(' );
            word.deleteCharacterInKey( ')' );
        }
    }
}
