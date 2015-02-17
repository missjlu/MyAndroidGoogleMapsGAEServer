package com.yunjial;


import java.io.IOException;
import java.util.Random;

import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
/**
 * Get the request from user's mobile app and search the places related to user's request with 
 * Google Search Place API and display one of the related place's name, address, rating and if it is
 * open to user.
 * 
 * @author Jelena
 *
 */
@SuppressWarnings("serial")
public class MyAndroidGoogleMapsGAEServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//String errMsg = "";
		String searchTerm = req.getParameter("query");
        StringBuilder response = new StringBuilder();
		
		try{
            // Create a URL for the request page 
			Document doc = getRemoteXML("https://maps.googleapis.com/maps/api/place/textsearch/xml?query="+searchTerm+"&key=AIzaSyD8-4za08uEWiYi2GenxdYbUeXSsCpwHAY");			
			doc.getDocumentElement().normalize();      	
			
			NodeList nl = doc.getElementsByTagName("result"); 
			
			if (nl.getLength() == 0) {
	        	response.append("<PlaceSearchResponse>"); // no places found
	        	response.append("</PlaceSearchResponse>");
	       } else {
	        	int plc = new Random().nextInt(nl.getLength()); //choose a random picture
	        	
	        	Node n = nl.item(plc);
	        	
	        	Element e = (Element) n;
	        		        	
	        	response.append("<PlaceSearchResponse>");   
	    		response.append("<result>");	    		
	    		response.append("<name>");
	    		response.append(e.getElementsByTagName("name").item(0).getTextContent());
	    		response.append("</name>");
	    		
	    		response.append("<formatted_address>");
	    		response.append(e.getElementsByTagName("formatted_address").item(0).getTextContent());
	    		response.append("</formatted_address>");
	    		
	    		response.append("<rating>");
	    		response.append(e.getElementsByTagName("rating").item(0).getTextContent());
	    		response.append("</rating>");
	    		
	    		response.append("<open_now>");
	    		//if (e.getElementsByTagName("open_now").item(0).getTextContent()=="true")
	    			//response.append("Yes");
	    		//else
	    			//response.append("No");
	    		response.append(e.getElementsByTagName("open_now").item(0).getTextContent());
	    		response.append("</open_now>");
	    		
	    			    		
	    		response.append("</result>");
	    		response.append("</PlaceSearchResponse>");
	        }		
		
		resp.setContentType("text/xml");
		resp.getWriter().println(response);
		} catch (Exception e){
			response = null;
			resp.getWriter().println(response);
		}
		
	}
	
	/* 
     * Given a url that will request XML, return a Document with that XML, else null
     */
    private Document getRemoteXML(String url) {
    	 try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(url);
                return db.parse(is);
        } catch (Exception e) {
        	System.out.print("Hit the error: "+e);
        	return null;
        }
    }
	
	
}
