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
        mergeLinesSeparates();
        fillDictionaryList();
        deleteCharacterUnused();
        createTags();
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

    private void mergeLinesSeparates()
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
            word.deleteStringInKey( "msgid " );
            word.deleteStringInValue( "msgstr " );
            word.deleteStringInKey( "\\n" );
            word.deleteStringInValue( "\\n" );
            word.deleteCharacterInKey( '"' );
            word.deleteCharacterInValue( '"' );
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

    private void createTags()
    {
        for (Dictionary word : dictionaryList)
        {
            if (haveXWords( 5, word.key ))
            {

            }
            else if (haveXWords( 4, word.key ))
            {

            }
            else if (haveXWords( 3, word.key ))
            {

            }
            else if (haveXWords( 2, word.key ))
            {

            }
            else
            {

            }
        }
    }

    private boolean haveXWords(int x, String string)
    {
        int counterSpaceBlank = 0;

        for ( int i = 0; i < string.length(); i++ )
        {
            if (string.charAt( i ) == ' ')
            {
                counterSpaceBlank += 1;
            }
        }

        return counterSpaceBlank >= x - 1;
    }
}
