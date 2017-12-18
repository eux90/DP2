
package it.polito.dp2.NFFG.sol3.client2;

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
 *         &lt;element name="rchPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}rchPolicyType" minOccurs="0"/>
 *         &lt;element name="trvPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}trvPolicyType" minOccurs="0"/>
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
