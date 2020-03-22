package com.jacon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Vector;

class WriterXML
{
    static void writer( Vector< Dictionary > dictionary ) throws ParserConfigurationException,
            TransformerException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element root = document.createElement( "LanguageInject" );
        document.appendChild( root );

        for ( Dictionary word : dictionary )
        {
            Element element = document.createElement( word.key );
            element.appendChild( document.createTextNode( word.value ) );
            root.appendChild( element );
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        DOMSource domSource = new DOMSource( document );
        StreamResult streamResult = new StreamResult( new File( "./Output.xml" ) );

        transformer.transform( domSource, streamResult );
    }
}
