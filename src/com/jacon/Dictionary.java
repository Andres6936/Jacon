package com.jacon;

final class Dictionary
{
    // Fields

    String key;

    String value;

    // Construct

    Dictionary(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    // Methods

    void deleteCharacterInKey( char character )
    {
        key = key.replace( String.valueOf( character ), "" );
    }

    void deleteCharacterInValue(char character)
    {
        value = value.replace( String.valueOf( character ), "" );
    }

    void deleteStringInKey(String string)
    {
        key = key.replace( string, "" );
    }

    void deleteStringInValue(String string)
    {
        value = value.replace( "msgstr ", "" );
    }

    // Setters

    void setKey( String key )
    {
        this.key = key;
    }
}
