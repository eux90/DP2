//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.12.18 alle 01:55:18 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="links" type="{http://service.sol3.NFFG.dp2.polito.it}linksType" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="rchPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}rchPolicyType"/>
 *           &lt;element name="trvPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}trvPolicyType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "links",
    "rchPolicy",
    "trvPolicy"
})
@XmlRootElement(name = "policyType")
public class PolicyType {

    protected LinksType links;
    protected RchPolicyType rchPolicy;
    protected TrvPolicyType trvPolicy;

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
     * Recupera il valore della proprietà rchPolicy.
     * 
     * @return
     *     possible object is
     *     {@link RchPolicyType }
     *     
     */
    public RchPolicyType getRchPolicy() {
        return rchPolicy;
    }

    /**
     * Imposta il valore della proprietà rchPolicy.
     * 
     * @param value
     *     allowed object is
     *     {@link RchPolicyType }
     *     
     */
    public void setRchPolicy(RchPolicyType value) {
        this.rchPolicy = value;
    }

    /**
     * Recupera il valore della proprietà trvPolicy.
     * 
     * @return
     *     possible object is
     *     {@link TrvPolicyType }
     *     
     */
    public TrvPolicyType getTrvPolicy() {
        return trvPolicy;
    }

    /**
     * Imposta il valore della proprietà trvPolicy.
     * 
     * @param value
     *     allowed object is
     *     {@link TrvPolicyType }
     *     
     */
    public void setTrvPolicy(TrvPolicyType value) {
        this.trvPolicy = value;
    }

}
