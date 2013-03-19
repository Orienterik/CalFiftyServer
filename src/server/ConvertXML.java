package server;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import data.Appointment;
import data.Group;
import data.Member;
import data.Participant;
import data.Room;
import data.Subgroup;
import data.User;

public class ConvertXML {
	
	static String ObjectsToXml(ArrayList<Object> objects) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("objects");
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i) instanceof Appointment) {
					Appointment appointment = (Appointment) objects.get(i);
					Element element = doc.createElement("appointment");
					element.setAttribute("appointmentId", String.valueOf(appointment.getAppointmentId()));
					element.setAttribute("title", appointment.getTitle());
					element.setAttribute("description", appointment.getDescription());
					element.setAttribute("startTime", String.valueOf(appointment.getStartTime().getTimeInMillis()));
					element.setAttribute("finishTime", String.valueOf(appointment.getFinishTime().getTimeInMillis()));
					element.setAttribute("room", (appointment.getRoom() != null) ? appointment.getRoom().getRoomNumber() : "null");
					element.setAttribute("owner", (appointment.getOwner() != null) ? appointment.getOwner().getUsername() : "null");
					root.appendChild(element);
				} else if (objects.get(i) instanceof Group) {
					Group group = (Group) objects.get(i);
					Element element = doc.createElement("group");
					element.setAttribute("groupName", group.getGroupName());
					element.setAttribute("leader", (group.getLeader() != null) ? group.getLeader().getUsername() : "null");
					root.appendChild(element);
				} else if (objects.get(i) instanceof Member) {
					Member member = (Member) objects.get(i);
					Element element = doc.createElement("member");
					element.setAttribute("group", (member.getGroup() != null) ? member.getGroup().getGroupName() : "null");
					element.setAttribute("user", (member.getUser() != null) ? member.getUser().getUsername() : "null");
					root.appendChild(element);
				} else if (objects.get(i) instanceof Participant) {
					Participant participant = (Participant) objects.get(i);
					Element element = doc.createElement("participant");
					element.setAttribute("appointment", (participant.getAppointment() != null) ? String.valueOf(participant.getAppointment().getAppointmentId()) : "null");
					element.setAttribute("user", (participant.getUser() != null) ? participant.getUser().getUsername() : "null");
					element.setAttribute("alarm", String.valueOf(participant.getAlarm().getTimeInMillis()));
					element.setAttribute("status", participant.getStatus());
					root.appendChild(element);
				} else if (objects.get(i) instanceof Room) {
					Room room = (Room) objects.get(i);
					Element element = doc.createElement("room");
					element.setAttribute("roomNumber", room.getRoomNumber());
					element.setAttribute("size", String.valueOf(room.getSize()));
					root.appendChild(element);
				} else if (objects.get(i) instanceof Subgroup) {
					Subgroup subgroup = (Subgroup) objects.get(i);
					Element element = doc.createElement("subgroup");
					element.setAttribute("parentGroup", (subgroup.getParentGroup() != null) ? subgroup.getParentGroup().getGroupName() : "null");
					element.setAttribute("subgroup", (subgroup.getSubgroup() != null) ? subgroup.getSubgroup().getGroupName() : "null");
					root.appendChild(element);
				} else if (objects.get(i) instanceof User) {
					User user = (User) objects.get(i);
					Element element = doc.createElement("user");
					element.setAttribute("username", user.getUsername());
					element.setAttribute("password", user.getPassword());
					element.setAttribute("name", user.getName());
					root.appendChild(element);
				}
			}
			doc.appendChild(root);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Writer writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static ArrayList<Object> XmlToObjects(String xml, Database database) {
		try {
			ArrayList<Object> objects = new ArrayList<Object>();
			new ByteArrayInputStream(xml.getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
			NodeList rooms = doc.getElementsByTagName("room");
			for (int i = 0; i < rooms.getLength(); i++) {
				NamedNodeMap attributes = rooms.item(i).getAttributes();
				String roomNumber = attributes.getNamedItem("roomNumber").getTextContent();
				int size = Integer.parseInt(attributes.getNamedItem("size").getTextContent());
				objects.add(new Room(roomNumber, size));
			}
			NodeList users = doc.getElementsByTagName("user");
			for (int i = 0; i < users.getLength(); i++) {
				NamedNodeMap attributes = users.item(i).getAttributes();
				String username = attributes.getNamedItem("username").getTextContent();
				String password = attributes.getNamedItem("password").getTextContent();
				String name = attributes.getNamedItem("name").getTextContent();
				objects.add(new User(username, password, name));
			}
			NodeList appointments = doc.getElementsByTagName("appointment");
			for (int i = 0; i < appointments.getLength(); i++) {
				NamedNodeMap attributes = appointments.item(i).getAttributes();
				int appointmentId = Integer.parseInt(attributes.getNamedItem("appointmentId").getTextContent());
				String title = attributes.getNamedItem("title").getTextContent();
				String description = attributes.getNamedItem("description").getTextContent();
				long startTime = Long.parseLong(attributes.getNamedItem("startTime").getTextContent());
				long finishTime = Long.parseLong(attributes.getNamedItem("finishTime").getTextContent());
				Room room = database.getRooms().get(attributes.getNamedItem("room").getTextContent());
				User owner = database.getUsers().get(attributes.getNamedItem("owner").getTextContent());
				objects.add(new Appointment(appointmentId, title, description, startTime, finishTime, room, owner));
			}
			NodeList groups = doc.getElementsByTagName("group");
			for (int i = 0; i < groups.getLength(); i++) {
				NamedNodeMap attributes = groups.item(i).getAttributes();
				String groupName = attributes.getNamedItem("groupName").getTextContent();
				User leader = database.getUsers().get(attributes.getNamedItem("leader").getTextContent());
				objects.add(new Group(groupName, leader));
			}
			NodeList members = doc.getElementsByTagName("member");
			for (int i = 0; i < members.getLength(); i++) {
				NamedNodeMap attributes = members.item(i).getAttributes();
				Group group = database.getGroups().get(attributes.getNamedItem("group").getTextContent());
				User user = database.getUsers().get(attributes.getNamedItem("user").getTextContent());
				objects.add(new Member(group, user));
			}
			NodeList participants = doc.getElementsByTagName("participant");
			for (int i = 0; i < participants.getLength(); i++) {
				NamedNodeMap attributes = participants.item(i).getAttributes();
				Appointment appointment = database.getAppointments().get(Integer.parseInt(attributes.getNamedItem("appointment").getTextContent()));
				User user = database.getUsers().get(attributes.getNamedItem("user").getTextContent());
				long alarm = Long.parseLong(attributes.getNamedItem("alarm").getTextContent());
				String status = attributes.getNamedItem("status").getTextContent();
				objects.add(new Participant(appointment, user, alarm, status));
			}
			NodeList subgroups = doc.getElementsByTagName("subgroup");
			for (int i = 0; i < subgroups.getLength(); i++) {
				NamedNodeMap attributes = subgroups.item(i).getAttributes();
				Group parentGroup = database.getGroups().get(attributes.getNamedItem("parentGroup").getTextContent());
				Group subgroup = database.getGroups().get(attributes.getNamedItem("subgroup").getTextContent());
				objects.add(new Subgroup(parentGroup, subgroup));
			}
			return objects;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
