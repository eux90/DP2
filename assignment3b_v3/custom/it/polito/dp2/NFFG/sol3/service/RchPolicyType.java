//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.12.18 alle 01:55:18 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per rchPolicyType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="rchPolicyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.sol3.NFFG.dp2.polito.it}generalPolicyType">
 *       &lt;group ref="{http://service.sol3.NFFG.dp2.polito.it}srcAndDstNodeGroup"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rchPolicyType", propOrder = {
    "srcNode",
    "dstNode"
})
@XmlSeeAlso({
    TrvPolicyType.class
})
public class RchPolicyType
    extends GeneralPolicyType
{

    @XmlElement(required = true)
    protected String srcNode;
    @XmlElement(required = true)
    protected String dstNode;

    /**
     * Recupera il valore della proprietà srcNode.
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
     * Imposta il valore della proprietà srcNode.
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
     * Recupera il valore della proprietà dstNode.
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
     * Imposta il valore della proprietà dstNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDstNode(String value) {
        this.dstNode = value;
    }

}
