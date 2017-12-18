//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.12.18 alle 01:55:18 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per generalPolicyType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="generalPolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="links" type="{http://service.sol3.NFFG.dp2.polito.it}linksType" minOccurs="0"/>
 *         &lt;element name="verification" type="{http://service.sol3.NFFG.dp2.polito.it}verificationType" minOccurs="0"/>
 *         &lt;element name="isPositive" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://service.sol3.NFFG.dp2.polito.it}IDType" />
 *       &lt;attribute name="nffg" use="required" type="{http://service.sol3.NFFG.dp2.polito.it}IDType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generalPolicyType", propOrder = {
    "links",
    "verification",
    "isPositive"
})
@XmlSeeAlso({
    RchPolicyType.class
})
public class GeneralPolicyType {

    protected LinksType links;
    protected VerificationType verification;
    protected boolean isPositive;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "nffg", required = true)
    protected String nffg;

    /**
     * Recupera il valore della proprietà links.
     * 
     * @return
     *     possible object is
     *     {@link LinksType }
     *     
     */
    public LinksType getLinks() {
        return links;
    }

    /**
     * Imposta il valore della proprietà links.
     * 
     * @param value
     *     allowed object is
     *     {@link LinksType }
     *     
     */
    public void setLinks(LinksType value) {
        this.links = value;
    }

    /**
     * Recupera il valore della proprietà verification.
     * 
     * @return
     *     possible object is
     *     {@link VerificationType }
     *     
     */
    public VerificationType getVerification() {
        return verification;
    }

    /**
     * Imposta il valore della proprietà verification.
     * 
     * @param value
     *     allowed object is
     *     {@link VerificationType }
     *     
     */
    public void setVerification(VerificationType value) {
        this.verification = value;
    }

    /**
     * Recupera il valore della proprietà isPositive.
     * 
     */
    public boolean isIsPositive() {
        return isPositive;
    }

    /**
     * Imposta il valore della proprietà isPositive.
     * 
     */
    public void setIsPositive(boolean value) {
        this.isPositive = value;
    }

    /**
     * Recupera il valore della proprietà name.
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
     * Imposta il valore della proprietà name.
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
     * Recupera il valore della proprietà nffg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffg() {
        return nffg;
    }

    /**
     * Imposta il valore della proprietà nffg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffg(String value) {
        this.nffg = value;
    }

}
