//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.11.23 alle 06:15:18 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.dp2.NFFG.sol1.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _NffgInfo_QNAME = new QName("http://NFFG.dp2.polito.it", "nffgInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.dp2.NFFG.sol1.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NffgInfoType }
     * 
     */
    public NffgInfoType createNffgInfoType() {
        return new NffgInfoType();
    }

    /**
     * Create an instance of {@link RchPolicyType }
     * 
     */
    public RchPolicyType createRchPolicyType() {
        return new RchPolicyType();
    }

    /**
     * Create an instance of {@link NodeSetType }
     * 
     */
    public NodeSetType createNodeSetType() {
        return new NodeSetType();
    }

    /**
     * Create an instance of {@link PolicySetType }
     * 
     */
    public PolicySetType createPolicySetType() {
        return new PolicySetType();
    }

    /**
     * Create an instance of {@link GeneralPolicyType }
     * 
     */
    public GeneralPolicyType createGeneralPolicyType() {
        return new GeneralPolicyType();
    }

    /**
     * Create an instance of {@link LinkSetType }
     * 
     */
    public LinkSetType createLinkSetType() {
        return new LinkSetType();
    }

    /**
     * Create an instance of {@link NodeType }
     * 
     */
    public NodeType createNodeType() {
        return new NodeType();
    }

    /**
     * Create an instance of {@link NffgSetType }
     * 
     */
    public NffgSetType createNffgSetType() {
        return new NffgSetType();
    }

    /**
     * Create an instance of {@link NffgType }
     * 
     */
    public NffgType createNffgType() {
        return new NffgType();
    }

    /**
     * Create an instance of {@link TrvPolicyType }
     * 
     */
    public TrvPolicyType createTrvPolicyType() {
        return new TrvPolicyType();
    }

    /**
     * Create an instance of {@link TrvPolicySetType }
     * 
     */
    public TrvPolicySetType createTrvPolicySetType() {
        return new TrvPolicySetType();
    }

    /**
     * Create an instance of {@link RchPolicySetType }
     * 
     */
    public RchPolicySetType createRchPolicySetType() {
        return new RchPolicySetType();
    }

    /**
     * Create an instance of {@link LinkType }
     * 
     */
    public LinkType createLinkType() {
        return new LinkType();
    }

    /**
     * Create an instance of {@link VerificationType }
     * 
     */
    public VerificationType createVerificationType() {
        return new VerificationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NffgInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NFFG.dp2.polito.it", name = "nffgInfo")
    public JAXBElement<NffgInfoType> createNffgInfo(NffgInfoType value) {
        return new JAXBElement<NffgInfoType>(_NffgInfo_QNAME, NffgInfoType.class, null, value);
    }

}
