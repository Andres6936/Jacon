package com.jacon;

public class Main {

    public static void main(String[] args)
    {
        for ( int i = 0; i < 69; i++ )
        {
            var start = System.nanoTime( );
            new Jacon( args );
            var end = System.nanoTime( ) - start;
            System.out.println( "Milliseconds: " + end / 1_000_000 );
        }
    }
}
