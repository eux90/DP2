
package it.polito.dp2.NFFG.sol3.client2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per rchPolicySetType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="rchPolicySetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rchPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}rchPolicyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rchPolicySetType", propOrder = {
    "rchPolicy"
})
public class RchPolicySetType {

    @XmlElement(nillable = true)
    protected List<RchPolicyType> rchPolicy;

    /**
     * Gets the value of the rchPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rchPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRchPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RchPolicyType }
     * 
     * 
     */
    public List<RchPolicyType> getRchPolicy() {
        if (rchPolicy == null) {
            rchPolicy = new ArrayList<RchPolicyType>();
        }
        return this.rchPolicy;
    }

}
