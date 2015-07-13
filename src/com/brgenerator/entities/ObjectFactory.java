//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2015.07.12 a las 05:59:49 PM ART 
//


package com.brgenerator.entities;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
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

    private final static QName _ModelPropertiesPropertyEnum_QNAME = new QName("", "Enum");
    private final static QName _ModelPropertiesPropertyTrim_QNAME = new QName("", "Trim");
    private final static QName _ModelPropertiesPropertyDefault_QNAME = new QName("", "Default");
    private final static QName _ModelPropertiesPropertyValidate_QNAME = new QName("", "Validate");
    private final static QName _ModelPropertiesPropertyMatch_QNAME = new QName("", "Match");
    private final static QName _ModelPropertiesPropertyUnique_QNAME = new QName("", "Unique");
    private final static QName _ModelPropertiesPropertyRequired_QNAME = new QName("", "Required");
    private final static QName _ModelPropertiesPropertyRelation_QNAME = new QName("", "Relation");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Enum", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyEnum(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyEnum_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Trim", scope = Model.Properties.Property.class)
    public JAXBElement<Boolean> createModelPropertiesPropertyTrim(Boolean value) {
        return new JAXBElement<Boolean>(_ModelPropertiesPropertyTrim_QNAME, Boolean.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Default", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyDefault(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyDefault_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Validate", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyValidate(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyValidate_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Match", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyMatch(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyMatch_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Unique", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyUnique(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyUnique_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Required", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyRequired(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyRequired_QNAME, String.class, Model.Properties.Property.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Relation", scope = Model.Properties.Property.class)
    public JAXBElement<String> createModelPropertiesPropertyRelation(String value) {
        return new JAXBElement<String>(_ModelPropertiesPropertyRelation_QNAME, String.class, Model.Properties.Property.class, value);
    }

}
