package hu.oe.nik.szfmv17t.environment.utils;

import hu.oe.nik.szfmv17t.environment.interfaces.IWorldObject;
import hu.oe.nik.szfmv17t.environment.domain.Bycicle;
import hu.oe.nik.szfmv17t.environment.domain.Car;
import hu.oe.nik.szfmv17t.environment.domain.ParkingLot;
import hu.oe.nik.szfmv17t.environment.domain.Pavement;
import hu.oe.nik.szfmv17t.environment.domain.Pedestrian;
import hu.oe.nik.szfmv17t.environment.domain.Road;
import hu.oe.nik.szfmv17t.environment.domain.Sign;
import hu.oe.nik.szfmv17t.environment.domain.Tree;
import hu.oe.nik.szfmv17t.environment.domain.Turn;
import hu.oe.nik.szfmv17t.environment.domain.ZebraCrossing;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Gellert Babel <OE-NIK>
 */
public class XmlParser {

    private String pathToXml;
    private List<IWorldObject> mapObjects;
    private int mapHeight;
    private int mapWidth;

    //optional
    private int measureType;
    private String color;

    public XmlParser(String newPathToXml) {
        pathToXml = newPathToXml;
        mapObjects = new ArrayList<IWorldObject>();
        ReadXml();
    }

    private void ReadXml() {
        try {

            File fXmlFile = new File(pathToXml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            Element rootElement = (Element) doc.getElementsByTagName("Scene").item(0);
            mapHeight = Integer.parseInt(rootElement.getAttribute("height"));
            measureType = Integer.parseInt(rootElement.getAttribute("measureType"));
            color = rootElement.getAttribute("color");
            mapWidth = Integer.parseInt(rootElement.getAttribute("width"));

            NodeList objectList = doc.getElementsByTagName("Object");

            for (int i = 0; i < objectList.getLength(); i++) {

                Node objectNode = objectList.item(i);

                if (objectNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element objectElement = (Element) objectNode;
                    createObjectFromElement(objectElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createObjectFromElement(Element objectElement) {
        NamedNodeMap positionAttributes = objectElement.getElementsByTagName("Position").item(0).getAttributes();
        double posX = Double.parseDouble(positionAttributes.item(0).getTextContent());
        double posY = Double.parseDouble(positionAttributes.item(1).getTextContent());

        NamedNodeMap transformAttributes = objectElement.getElementsByTagName("Transform").item(0).getAttributes();
        double m11 = Double.parseDouble(transformAttributes.item(0).getTextContent());
        double m12 = Double.parseDouble(transformAttributes.item(1).getTextContent());
        double m21 = Double.parseDouble(transformAttributes.item(2).getTextContent());
        double m22 = Double.parseDouble(transformAttributes.item(3).getTextContent());

        int roadPainting1 = 1;
        int roadPainting2 = 1;
        int roadPainting3 = 1;

        if (objectElement.getElementsByTagName("Parameter").getLength() != 0) {
            NamedNodeMap firstParamAttributes = objectElement.getElementsByTagName("Parameter").item(0).getAttributes(); //A NEVE AZ XML-BOL VAN

            roadPainting1 = Integer.parseInt(firstParamAttributes.item(1).getTextContent()); //VALUE

            NamedNodeMap secondParamAttributes = objectElement.getElementsByTagName("Parameter").item(1).getAttributes();

            roadPainting2 = Integer.parseInt(firstParamAttributes.item(1).getTextContent()); //VALUE

            NamedNodeMap thirdParamAttributes = objectElement.getElementsByTagName("Parameter").item(2).getAttributes();

            roadPainting3 = Integer.parseInt(firstParamAttributes.item(1).getTextContent()); //VALUE
        }

        switch (objectElement.getAttribute("type")) {
            case "road_2lane_straight":
                mapObjects.add(new Road(posX, posY, 350, 350, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35, roadPainting1, roadPainting2, roadPainting3));
                break;

            case "road_2lane_90right":
            case "road_2lane_90left":
                mapObjects.add(new Turn(posX, posY, 527, 527, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35, roadPainting1, roadPainting2, roadPainting3));
                break;

            case "road_2lane_tjunctionleft":
            case "road_2lane_tjunctionright":
                mapObjects.add(new Turn(posX, posY, 877, 1402, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35, roadPainting1, roadPainting2, roadPainting3));
                break;

            case "road_2lane_45right":
            case "road_2lane_45left":
                mapObjects.add(new Turn(posX, posY, 403, 373, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35, roadPainting1, roadPainting2, roadPainting3));
                break;

            case "parking_space_parallel":
                mapObjects.add(new ParkingLot(posX, posY, 141, 624, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35));
                break;

            case "crosswalk":
                mapObjects.add(new ZebraCrossing(posX, posY, 338, 199, 35, 0, "AutomatedCar/src/main/resources/" + objectElement.getAttribute("type"), 35));
                break;

            case "roadsign_parking_right":
            case "roadsign_priority_stop":
            case "roadsign_speed_40":
            case "roadsign_speed_50":
            case "roadsign_speed_60":
                mapObjects.add(new Sign(posX, posY, 80, 80, 35, 0, "AutomatedCar/src/main/resources/road_2lane_straight.png", 10, 0, 35));
                break;

            case "tree":
                mapObjects.add(new Tree(posX, posY, 80, 80, 35, 0, "AutomatedCar/src/main/resources/road_2lane_straight.png", 10, 0, 35));
                break;
        }
    }

    public List<IWorldObject> getWorldObjects() {
        return mapObjects;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }
}
