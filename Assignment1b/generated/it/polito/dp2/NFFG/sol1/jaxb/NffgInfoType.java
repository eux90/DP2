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
 * <p>Classe Java per nffgInfoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nffgInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nffgSet" type="{http://NFFG.dp2.polito.it}nffgSetType"/>
 *         &lt;element name="policySet" type="{http://NFFG.dp2.polito.it}policySetType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nffgInfoType", propOrder = {
    "nffgSet",
    "policySet"
})
public class NffgInfoType {

    @XmlElement(required = true)
    protected NffgSetType nffgSet;
    @XmlElement(required = true)
    protected PolicySetType policySet;

    /**
     * Recupera il valore della proprietà nffgSet.
     * 
     * @return
     *     possible object is
     *     {@link NffgSetType }
     *     
     */
    public NffgSetType getNffgSet() {
        return nffgSet;
    }

    /**
     * Imposta il valore della proprietà nffgSet.
     * 
     * @param value
     *     allowed object is
     *     {@link NffgSetType }
     *     
     */
    public void setNffgSet(NffgSetType value) {
        this.nffgSet = value;
    }

    /**
     * Recupera il valore della proprietà policySet.
     * 
     * @return
     *     possible object is
     *     {@link PolicySetType }
     *     
     */
    public PolicySetType getPolicySet() {
        return policySet;
    }

    /**
     * Imposta il valore della proprietà policySet.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicySetType }
     *     
     */
    public void setPolicySet(PolicySetType value) {
        this.policySet = value;
    }

}
