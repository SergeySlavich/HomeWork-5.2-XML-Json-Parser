import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        createXmlFile("data.xml"); // Создание XML-файла//Задание 5.2
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        System.out.println(json);
    }

    private static void createXmlFile(String fileName){ // Задание 5.2
        try(CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element staff = document.createElement("staff");
            document.appendChild(staff);
            Element employee = document.createElement("employee");
            staff.appendChild(employee);
            Element id = document.createElement("id");
            employee.appendChild(id);
            Element firstName = document.createElement("FirstName");
            employee.appendChild(firstName);
            Element lastName = document.createElement("LastName");
            employee.appendChild(lastName);
            Element country = document.createElement("country");
            employee.appendChild(country);
            Element age = document.createElement("age");
            employee.appendChild(age);

            Element employee2 = document.createElement("employee");
            staff.appendChild(employee2);
            Element id2 = document.createElement("id");
            employee2.appendChild(id2);
            Element firstName2 = document.createElement("FirstName");
            employee2.appendChild(firstName2);
            Element lastName2 = document.createElement("LastName");
            employee2.appendChild(lastName2);
            Element country2 = document.createElement("country");
            employee2.appendChild(country2);
            Element age2 = document.createElement("age");
            employee2.appendChild(age2);

            id.appendChild(document.createTextNode("1"));
            firstName.appendChild(document.createTextNode("Jhon"));
            lastName.appendChild(document.createTextNode("Smith"));
            country.appendChild(document.createTextNode("USA"));
            age.appendChild(document.createTextNode("25"));

            id2.appendChild(document.createTextNode("2"));
            firstName2.appendChild(document.createTextNode("Ivan"));
            lastName2.appendChild(document.createTextNode("Petrov"));
            country2.appendChild(document.createTextNode("RU"));
            age2.appendChild(document.createTextNode("23"));

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String fileName){
        List<Employee> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            NodeList nodeList = document.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++){
                Node element = nodeList.item(i);
                if (Node.ELEMENT_NODE == element.getNodeType()){
                    read(element);
                    NodeList nodeId = document.getElementsByTagName("id");
                    String stringId = nodeId.item(i).getTextContent();
                    long id = Long.parseLong(stringId);
                    NodeList nodeFirstName = document.getElementsByTagName("FirstName");
                    String firstName = nodeFirstName.item(i).getTextContent();
                    NodeList nodeLastName = document.getElementsByTagName("LastName");
                    String lastName = nodeLastName.item(i).getTextContent();
                    NodeList nodeCountry = document.getElementsByTagName("country");
                    String country = nodeCountry.item(i).getTextContent();
                    NodeList nodeAge = document.getElementsByTagName("age");
                    String stringAge = nodeAge.item(i).getTextContent();
                    int age = Integer.parseInt(stringAge);
                    Employee employee = new Employee(id, firstName, lastName, country, age); //
                    result.add(employee);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
        }
        return result;
    }

    private static void read(Node root){
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                element.getTextContent();
                read(node);
            }
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return writeString(json);
    }

    private static String writeString(String json){
        try (FileWriter file = new FileWriter("data2.json")){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }
}
