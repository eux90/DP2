//
// Questo file � stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andr� persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.12.18 alle 01:55:18 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per linkType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="linkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://service.sol3.NFFG.dp2.polito.it}srcAndDstNodeGroup"/>
 *       &lt;attribute name="name" use="required" type="{http://service.sol3.NFFG.dp2.polito.it}IDType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "linkType", propOrder = {
    "srcNode",
    "dstNode"
})
public class LinkType {

    @XmlElement(required = true)
    protected String srcNode;
    @XmlElement(required = true)
    protected String dstNode;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Recupera il valore della propriet� srcNode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcNode() {
        return srcNode;
    }

    /**
     * Imposta il valore della propriet� srcNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcNode(String value) {
        this.srcNode = value;
    }

    /**
     * Recupera il valore della propriet� dstNode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDstNode() {
        return dstNode;
    }

    /**
     * Imposta il valore della propriet� dstNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDstNode(String value) {
        this.dstNode = value;
    }

    /**
     * Recupera il valore della propriet� name.
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
     * Imposta il valore della propriet� name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
