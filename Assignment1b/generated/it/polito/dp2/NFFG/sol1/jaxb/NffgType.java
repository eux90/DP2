//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.11.23 alle 06:15:18 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per nffgType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="nffgType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeSet" type="{http://NFFG.dp2.polito.it}nodeSetType"/>
 *         &lt;element name="linkSet" type="{http://NFFG.dp2.polito.it}linkSetType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://NFFG.dp2.polito.it}IDType" />
 *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nffgType", propOrder = {
    "nodeSet",
    "linkSet"
})
public class NffgType {

    @XmlElement(required = true)
    protected NodeSetType nodeSet;
    @XmlElement(required = true)
    protected LinkSetType linkSet;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;

    /**
     * Recupera il valore della proprietà nodeSet.
     * 
     * @return
     *     possible object is
     *     {@link NodeSetType }
     *     
     */
    public NodeSetType getNodeSet() {
        return nodeSet;
    }

    /**
     * Imposta il valore della proprietà nodeSet.
     * 
     * @param value
     *     allowed object is
     *     {@link NodeSetType }
     *     
     */
    public void setNodeSet(NodeSetType value) {
        this.nodeSet = value;
    }

    /**
     * Recupera il valore della proprietà linkSet.
     * 
     * @return
     *     possible object is
     *     {@link LinkSetType }
     *     
     */
    public LinkSetType getLinkSet() {
        return linkSet;
    }

    /**
     * Imposta il valore della proprietà linkSet.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkSetType }
     *     
     */
    public void setLinkSet(LinkSetType value) {
        this.linkSet = value;
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
     * Recupera il valore della proprietà date.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Imposta il valore della proprietà date.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

}
