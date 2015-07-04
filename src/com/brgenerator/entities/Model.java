//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2015.07.02 a las 03:52:36 PM GMT-03:00 
//


package com.brgenerator.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Atributes"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Atribute" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="Enum" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="Audi"/&gt;
 *                                   &lt;enumeration value="Golf"/&gt;
 *                                   &lt;enumeration value="BMW"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="Trim" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                             &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Validate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Match" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Unique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Required" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="Relation" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="OneToOne"/&gt;
 *                                   &lt;enumeration value="OneToSome"/&gt;
 *                                   &lt;enumeration value="OneToMany"/&gt;
 *                                   &lt;enumeration value="ManyToMany"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "atributes"
})
@XmlRootElement(name = "Model")
public class Model {

    @XmlElement(name = "Atributes", required = true)
    protected Model.Atributes atributes;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Obtiene el valor de la propiedad atributes.
     * 
     * @return
     *     possible object is
     *     {@link Model.Atributes }
     *     
     */
    public Model.Atributes getAtributes() {
        return atributes;
    }

    /**
     * Define el valor de la propiedad atributes.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.Atributes }
     *     
     */
    public void setAtributes(Model.Atributes value) {
        this.atributes = value;
    }

    /**
     * Obtiene el valor de la propiedad name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Atribute" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="Enum" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="Audi"/&gt;
     *                         &lt;enumeration value="Golf"/&gt;
     *                         &lt;enumeration value="BMW"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="Trim" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *                   &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Validate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Match" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Unique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Required" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="Relation" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="OneToOne"/&gt;
     *                         &lt;enumeration value="OneToSome"/&gt;
     *                         &lt;enumeration value="OneToMany"/&gt;
     *                         &lt;enumeration value="ManyToMany"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "atribute"
    })
    public static class Atributes {

        @XmlElement(name = "Atribute")
        protected List<Model.Atributes.Atribute> atribute;

        /**
         * Gets the value of the atribute property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the atribute property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAtribute().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Model.Atributes.Atribute }
         * 
         * 
         */
        public List<Model.Atributes.Atribute> getAtribute() {
            if (atribute == null) {
                atribute = new ArrayList<Model.Atributes.Atribute>();
            }
            return this.atribute;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Enum" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="Audi"/&gt;
         *               &lt;enumeration value="Golf"/&gt;
         *               &lt;enumeration value="BMW"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="Trim" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
         *         &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Validate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Match" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Unique" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Required" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="Relation" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="OneToOne"/&gt;
         *               &lt;enumeration value="OneToSome"/&gt;
         *               &lt;enumeration value="OneToMany"/&gt;
         *               &lt;enumeration value="ManyToMany"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "type",
            "_enum",
            "trim",
            "_default",
            "validate",
            "match",
            "unique",
            "required",
            "relation"
        })
        public static class Atribute {

            @XmlElement(name = "Name", required = true)
            protected String name;
            @XmlElement(name = "Type", required = true)
            protected String type;
            @XmlElement(name = "Enum", required = false)
            protected String _enum;
            @XmlElement(name = "Trim" , required = false)
            protected Boolean trim;
            @XmlElement(name = "Default" , required = false)
            protected String _default;
            @XmlElement(name = "Validate", required = false)
            protected String validate;
            @XmlElement(name = "Match", required = false)
            protected String match;
            @XmlElement(name = "Unique", required = false)
            protected String unique;
            @XmlElement(name = "Required", required = false)
            protected String required;
            @XmlElement(name = "Relation", required = false)
            protected String relation;

            /**
             * Obtiene el valor de la propiedad name.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Define el valor de la propiedad name.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Obtiene el valor de la propiedad type.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Define el valor de la propiedad type.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Obtiene el valor de la propiedad enum.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEnum() {
                return _enum;
            }

            /**
             * Define el valor de la propiedad enum.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEnum(String value) {
                this._enum = value;
            }

            /**
             * Obtiene el valor de la propiedad trim.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isTrim() {
                return trim;
            }

            /**
             * Define el valor de la propiedad trim.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setTrim(Boolean value) {
                this.trim = value;
            }

            /**
             * Obtiene el valor de la propiedad default.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDefault() {
                return _default;
            }

            /**
             * Define el valor de la propiedad default.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDefault(String value) {
                this._default = value;
            }

            /**
             * Obtiene el valor de la propiedad validate.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValidate() {
                return validate;
            }

            /**
             * Define el valor de la propiedad validate.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValidate(String value) {
                this.validate = value;
            }

            /**
             * Obtiene el valor de la propiedad match.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMatch() {
                return match;
            }

            /**
             * Define el valor de la propiedad match.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMatch(String value) {
                this.match = value;
            }

            /**
             * Obtiene el valor de la propiedad unique.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUnique() {
                return unique;
            }

            /**
             * Define el valor de la propiedad unique.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUnique(String value) {
                this.unique = value;
            }

            /**
             * Obtiene el valor de la propiedad required.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRequired() {
                return required;
            }

            /**
             * Define el valor de la propiedad required.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRequired(String value) {
                this.required = value;
            }

            /**
             * Obtiene el valor de la propiedad relation.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRelation() {
                return relation;
            }

            /**
             * Define el valor de la propiedad relation.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRelation(String value) {
                this.relation = value;
            }

        }

    }

}
