//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.12.18 alle 01:55:18 PM CET 
//


package it.polito.dp2.NFFG.sol3.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per trvPolicyType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="trvPolicyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.sol3.NFFG.dp2.polito.it}rchPolicyType">
 *       &lt;sequence>
 *         &lt;element name="functionList" type="{http://service.sol3.NFFG.dp2.polito.it}functionListType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "trvPolicyType", propOrder = {
    "functionList"
})
public class TrvPolicyType
    extends RchPolicyType
{

    @XmlList
    @XmlElement(required = true)
    protected List<FuncType> functionList;

    /**
     * Gets the value of the functionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuncType }
     * 
     * 
     */
    public List<FuncType> getFunctionList() {
        if (functionList == null) {
            functionList = new ArrayList<FuncType>();
        }
        return this.functionList;
    }

}
