
package it.polito.dp2.NFFG.sol3.client2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per trvPolicySetType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="trvPolicySetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="trvPolicy" type="{http://service.sol3.NFFG.dp2.polito.it}trvPolicyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trvPolicySetType", propOrder = {
    "trvPolicy"
})
public class TrvPolicySetType {

    @XmlElement(nillable = true)
    protected List<TrvPolicyType> trvPolicy;

    /**
     * Gets the value of the trvPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trvPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrvPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrvPolicyType }
     * 
     * 
     */
    public List<TrvPolicyType> getTrvPolicy() {
        if (trvPolicy == null) {
            trvPolicy = new ArrayList<TrvPolicyType>();
        }
        return this.trvPolicy;
    }

}
