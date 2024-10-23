import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This program's function is to read an XML file from a route previously asked from the user.
 *
 * @author Javier Otero
 */

public class DomXmlReader {


    public static void main(String[] args) {

        String route = "";

        try (Scanner input = new Scanner(System.in)) {

            route = EnterAndValidateRoute(input);

        }

        try {

            Document document = parseXmlFile(route);

            Element root = document.getDocumentElement();

            readElement(root, "");

        } catch (SAXException | IOException ex) {
            System.err.println("Error parsing the XML file (SAXException): " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            System.err.println("Parser configuration error: " + ex.getMessage());
            Logger.getLogger(DomXmlReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Returns a boolean that will be true if the route entered is valid and exists.
     *
     * @param input the scanner class instance used to ask the user for the file's route
     * @return      the boolean indicating the validity of the entered route
     */
    private static String EnterAndValidateRoute(Scanner input) {

        String route = null;
        boolean isValid = false;

        while (!isValid) {

            System.out.println("Enter the route for the xml file you want to read: ");
            route = input.nextLine();

            // Checking existence of path
            File file = new File(route);
            if (!file.exists()) {

                System.out.println("File not found, try again.");

            } else {

                isValid = true;

            }

        }

        return route;

    }

    /**
     * Contains the methods for the document builder factory, the document builder and the document's parsed structure.
     *
     * @param route the string containing the file's route
     * @return the document that will be handled through the program
     */

    private static Document parseXmlFile(String route) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new File(route));

    }

    /**
     * Reads an element type node from an XML file. If it contains child nodes, they will be read too.
     * Checks for child nodes, element content and attributes.
     *
     * @param element the element to be read.
     * @param tabs the indentation parameter, it's meant to be an empty String ("")
     * @see Element
     */

    private static void readElement(Element element, String tabs) {

        // Checking if the node has child elements, to avoid de #text elements

        if (hasElementTypeChildNodes(element)) {

            System.out.println(tabs + element.getNodeName());

        } else {

            System.out.println(tabs + element.getNodeName() + ": " + element.getTextContent());

        }

        // ATTRIBUTES

        NamedNodeMap attributes = element.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++) {

            Node attribute = attributes.item(i);

            System.out.println(tabs + "\t" + attribute.getNodeName() + ": " + attribute.getNodeValue());

        }

        // CHILD NODES

        NodeList childNodes = element.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {

            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                Element childElement = (Element) childNode;
                readElement(childElement, tabs + "\t");

            }

        }

    }

    /**
     * Checks if a node has Element type child nodes, since all nodes have a text child node.
     *
     * @param element the element to be checked
     * @return true if the element has 1 or more Element child nodes, false if it only has text child nodes.
     * @see Element
     */

    private static boolean hasElementTypeChildNodes(Element element) {

        NodeList childNodes = element.getChildNodes();

        for (int i = 0; i< childNodes.getLength(); i++) {

            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                return true; // Has element type child nodes

            }

        }

        return false; // It's child nodes are just #text nodes, not elements

    }

}