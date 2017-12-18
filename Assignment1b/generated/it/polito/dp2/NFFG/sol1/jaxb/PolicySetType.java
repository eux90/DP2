//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.11.23 alle 06:15:18 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per policySetType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="policySetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rchPolicySet" type="{http://NFFG.dp2.polito.it}rchPolicySetType"/>
 *         &lt;element name="trvPolicySet" type="{http://NFFG.dp2.polito.it}trvPolicySetType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policySetType", propOrder = {
    "rchPolicySet",
    "trvPolicySet"
})
public class PolicySetType {

    @XmlElement(required = true)
    protected RchPolicySetType rchPolicySet;
    @XmlElement(required = true)
    protected TrvPolicySetType trvPolicySet;

    /**
     * Recupera il valore della proprietà rchPolicySet.
     * 
     * @return
     *     possible object is
     *     {@link RchPolicySetType }
     *     
     */
    public RchPolicySetType getRchPolicySet() {
        return rchPolicySet;
    }

    /**
     * Imposta il valore della proprietà rchPolicySet.
     * 
     * @param value
     *     allowed object is
     *     {@link RchPolicySetType }
     *     
     */
    public void setRchPolicySet(RchPolicySetType value) {
        this.rchPolicySet = value;
    }

    /**
     * Recupera il valore della proprietà trvPolicySet.
     * 
     * @return
     *     possible object is
     *     {@link TrvPolicySetType }
     *     
     */
    public TrvPolicySetType getTrvPolicySet() {
        return trvPolicySet;
    }

    /**
     * Imposta il valore della proprietà trvPolicySet.
     * 
     * @param value
     *     allowed object is
     *     {@link TrvPolicySetType }
     *     
     */
    public void setTrvPolicySet(TrvPolicySetType value) {
        this.trvPolicySet = value;
    }

}
