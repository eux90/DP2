
package it.polito.dp2.NFFG.sol3.client2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="nodeSet" type="{http://service.sol3.NFFG.dp2.polito.it}nodeSetType"/>
 *         &lt;element name="linkSet" type="{http://service.sol3.NFFG.dp2.polito.it}linkSetType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
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
    "nodeSet",
    "linkSet"
})
@XmlRootElement(name = "nffgType")
public class NffgType {

    protected LinksType links;
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
