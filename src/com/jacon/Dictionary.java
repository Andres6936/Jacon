package com.jacon;

final class Dictionary
{
    // Fields

    String key;

    private String value;

    // Construct

    Dictionary(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    // Methods

    void deleteCharacterInKey( char character )
    {
        key = key.replace( character, '\0' );
    }

    void deleteCharacterInValue(char character)
    {
        value = value.replace( character, '\0' );
    }

    void deleteStringInKey(String string)
    {
        key = key.replace( string, "\0" );
    }

    void deleteStringInValue(String string)
    {
        value = value.replace( "msgstr ", "\0" );
    }
}
