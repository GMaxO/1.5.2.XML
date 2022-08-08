import com.opencsv.CSVWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String xmlFileName = "data.xml";
        List<Employee> employeeList = new ArrayList<>();
        try {
            employeeList = parseXML(xmlFileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            employeeList = jsonToList("data2.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        System.out.println(employeeList.toString());
    }

    private static List<Employee> jsonToList (String fileName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        List<Employee> employeeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileName));
        JSONArray employeeJsonArray = (JSONArray) obj;
        for (Object it : employeeJsonArray) {
            JSONObject employeeJsonObject = (JSONObject) it;
            long id = (Long) employeeJsonObject.get("id");
            String firstName = (String) employeeJsonObject.get("firstName");
            String lastName = (String) employeeJsonObject.get("lastName");
            String country = (String) employeeJsonObject.get("country");
            long age = (Long) employeeJsonObject.get("age");
            Employee employee = new Employee(id, firstName, lastName, country, (int) age);
            employeeList.add(employee);
        }
        return  employeeList;
    }


    private static List<Employee> parseXML(String xmlFileName) {
        List<Employee> employeeList = new ArrayList();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFileName));
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                employeeList.add(getEmployee(nodeList.item(i)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    private static Employee getEmployee(Node node) {
        Employee employee = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee.setId(Long.valueOf(getTagValue("id", element)));
            employee.setFirstName(getTagValue("firstName", element));
            employee.setLastName(getTagValue("lastName", element));
            employee.setCountry(getTagValue("country", element));
            employee.setAge(Integer.parseInt(getTagValue("age", element)));
        }
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private static void writeString(String json) {
        File file = new File("C:\\Users\\Максим\\1.5.2.csvAndXml\\data2.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            file.createNewFile();
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
