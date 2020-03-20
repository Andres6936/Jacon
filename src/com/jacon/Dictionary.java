package com.jacon;

final class Dictionary
{
    // Fields

    public String key;

    public String value;

    // Construct

    Dictionary(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    // Methods

    public void deleteCharacterInKey(char character)
    {
        key = key.replace( character, '\0' );
    }
}
