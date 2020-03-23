package com.jacon;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

final class Convert extends WriterXML
{
    // Fields

    private List< String > buffer;

    private Vector< Dictionary > dictionaryList;

    private String commentHead;

    private String license;

    private String metadata;

    // Construct

    Convert( )
    {
        dictionaryList = new Vector<>( );
    }

    // Methods

    void convertXML( File file )
    {
        try
        {
            buffer = Files.readAllLines( file.toPath( ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
        }

        extractCommentHead( );
        deleteComments( );
        deleteLinesEmpty( );
        mergeLinesSeparates( );
        fillDictionaryList( );
        deleteCharacterUnused( );
        extractMetadata( );
        extractLicense( );
        createTags( );

        try
        {
            writer( dictionaryList );
        }
        catch ( ParserConfigurationException | TransformerException e )
        {
            e.printStackTrace( );
        }
    }

    private void extractCommentHead( )
    {
        StringBuilder stringBuilder = new StringBuilder( );

        for ( String comment : buffer )
        {
            if ( comment.startsWith( "#" ) )
            {
                stringBuilder.append( comment );
            }
            else
            {
                break;
            }
        }

        commentHead = stringBuilder.toString( );
    }

    private void extractLicense( )
    {
        StringBuilder stringBuilder = new StringBuilder( );

        // The license is divided in three parts
        for ( int i = 0; i < 3; i++ )
        {
            buffer.remove( 0 );
            stringBuilder.append( buffer.remove( 0 ) );
        }

        license = stringBuilder.toString( );
    }

    private void extractMetadata( )
    {
        StringBuilder stringBuilder = new StringBuilder( );

        buffer.remove( 0 );
        stringBuilder.append( buffer.remove( 0 ) );

        metadata = stringBuilder.toString( );
    }

    private void deleteComments( )
    {
        for ( int i = 0; i < buffer.size( ); i++ )
        {
            if ( buffer.get( i ).startsWith( "#" ) )
            {
                buffer.remove( i );
                i -= 1;
            }
        }
    }

    private void deleteLinesEmpty( )
    {
        for ( int i = 0; i < buffer.size( ); i++ )
        {
            if ( buffer.get( i ).isEmpty( ) )
            {
                buffer.remove( i );
                i -= 1;
            }
        }
    }

    private void mergeLinesSeparates( )
    {
        for ( int i = 0; i < buffer.size( ); i++ )
        {
            if ( buffer.get( i ).startsWith( "msgid " ) )
            {
                StringBuilder wordKey = new StringBuilder( 1024 );

                wordKey.append( buffer.get( i ) );

                while ( i + 1 < buffer.size( ) )
                {
                    if ( ! buffer.get( i + 1 ).startsWith( "msgstr " ) )
                    {
                        wordKey.append( buffer.remove( i + 1 ) );
                    }
                    else
                    {
                        break;
                    }
                }

                buffer.remove( i );
                buffer.add( i, wordKey.toString( ) );
            }
            else if ( buffer.get( i ).startsWith( "msgstr " ) )
            {
                StringBuilder wordKey = new StringBuilder( 1024 );

                wordKey.append( buffer.get( i ) );

                while ( i + 1 < buffer.size( ) )
                {
                    if ( ! buffer.get( i + 1 ).startsWith( "msgid " ) )
                    {
                        wordKey.append( buffer.remove( i + 1 ) );
                    }
                    else
                    {
                        break;
                    }
                }

                buffer.remove( i );
                buffer.add( i, wordKey.toString( ) );
            }
        }
    }

    private void fillDictionaryList( )
    {
        String key = "";
        String value = "";

        for ( String string : buffer )
        {
            if ( string.startsWith( "msgid " ) )
            {
                key = string;
                continue;
            }
            else if ( string.startsWith( "msgstr " ) )
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

    private void deleteCharacterUnused( )
    {
        for ( Dictionary word : dictionaryList )
        {
            word.deleteStringInKey( "msgid " );
            word.deleteStringInValue( "msgstr " );
            word.deleteStringInKey( "\\n" );
            word.deleteStringInValue( "\\n" );
            word.deleteCharacterInKey( '"' );
            word.deleteCharacterInValue( '"' );
            word.deleteCharacterInKey( '.' );
            word.deleteCharacterInKey( ';' );
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

    private void createTags( )
    {
        for ( Dictionary word : dictionaryList )
        {
            if ( haveXWords( 5, word.key ) )
            {
                word.setKey( generateKeyOfXWords( 5, word.key ) );
            }
            else if ( haveXWords( 4, word.key ) )
            {
                word.setKey( generateKeyOfXWords( 4, word.key ) );
            }
            else if ( haveXWords( 3, word.key ) )
            {
                word.setKey( generateKeyOfXWords( 3, word.key ) );
            }
            else if ( haveXWords( 2, word.key ) )
            {
                word.setKey( generateKeyOfXWords( 2, word.key ) );
            }
            else
            {
                word.setKey( capitalizeWord( word.key ) );
            }
        }
    }

    private boolean haveXWords( int x, String string )
    {
        int counterSpaceBlank = 0;

        for ( int i = 0; i < string.length( ); i++ )
        {
            if ( string.charAt( i ) == ' ' )
            {
                counterSpaceBlank += 1;
            }
        }

        return counterSpaceBlank >= x - 1;
    }

    private String generateKeyOfXWords( int x, String key )
    {
        Vector< String > words = new Vector( Arrays.asList( key.split( "\\s" ) ) );

        while ( words.size( ) > x )
        {
            int positionWordMoreShorter = 0;
            int sizeWordMoreShorter = words.get( 0 ).length( );

            for ( int i = 1; i < words.size( ); i++ )
            {
                if ( words.get( i ).length( ) < sizeWordMoreShorter )
                {
                    positionWordMoreShorter = i;
                    sizeWordMoreShorter = words.get( i ).length( );
                }
            }

            words.remove( positionWordMoreShorter );
        }

        StringBuilder newKey = new StringBuilder( 69 );

        for ( String word : words )
        {
            newKey.append( capitalizeWord( word ) );
        }

        return newKey.toString( );
    }

    private String capitalizeWord( String string )
    {
        char[] buffer = string.toCharArray( );

        for ( int i = 0; i < buffer.length; i++ )
        {
            buffer[ i ] = Character.toLowerCase( buffer[ i ] );
        }

        buffer[ 0 ] = Character.toUpperCase( buffer[ 0 ] );

        return new String( buffer );
    }
}
