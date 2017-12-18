
package it.polito.dp2.NFFG.sol3.client2;

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
 *       &lt;sequence>
 *         &lt;element name="srcNode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dstNode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
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
