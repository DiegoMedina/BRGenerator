//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2015.08.24 a las 10:30:39 PM ART 
//


package com.brgenerator.entities;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.brgenerator.entities package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.brgenerator.entities
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Model }
     * 
     */
    public Model createModel() {
        return new Model();
    }

    /**
     * Create an instance of {@link Model.Properties }
     * 
     */
    public Model.Properties createModelProperties() {
        return new Model.Properties();
    }

    /**
     * Create an instance of {@link Model.Properties.Property }
     * 
     */
    public Model.Properties.Property createModelPropertiesProperty() {
        return new Model.Properties.Property();
    }

    /**
     * Create an instance of {@link Model.Properties.Property.Atts }
     * 
     */
    public Model.Properties.Property.Atts createModelPropertiesPropertyAtts() {
        return new Model.Properties.Property.Atts();
    }

    /**
     * Create an instance of {@link Model.Properties.Property.Atts.Att }
     * 
     */
    public Model.Properties.Property.Atts.Att createModelPropertiesPropertyAttsAtt() {
        return new Model.Properties.Property.Atts.Att();
    }

}
