package xmlToTap;
//http://stackoverflow.com/questions/17390276/how-to-get-data-from-xml-into-javas
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
public class xmlToTap {
	public static void main(String argv[]) throws IOException {
		Properties prop = new Properties();
		//String projectRoot ="C:/Users/yueran/Documents/apiTest/KenCastAPITest/CinemaCMSAPITest_PerformanceTest/";
		prop.load(new FileInputStream("input/config.properties"));
		String inputFile = prop.getProperty("inputFile");
		String outputDir = prop.getProperty("outPutDir");
	       try {
	        File fXmlFile = new File(inputFile);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(fXmlFile); 
	    //    String specChaFilter = "!@#%^&amp;*()_+-=|\\}[{]:&lt;,&gt;?/";
	        //optional, but recommended
	        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	        doc.getDocumentElement().normalize(); 
	        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	        NodeList nList = doc.getElementsByTagName("httpSample");
	        System.out.println("----------------------------");

	            for (int temp = 0; temp < nList.getLength(); temp++) {
	            	
	            	Node nNode = nList.item(temp);
	                System.out.println("\nCurrent Element :" + nNode.getNodeName());
			        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			        Element eElement = (Element) nNode; 
			        String fileName_Original = eElement.getAttribute("lb");
			        String fileName = fileName_Original.replaceAll("[^\\w\\s-]","");
			        System.out.println("testCase: " + fileName);
			      //create a tap file:
	            		PrintWriter out = new PrintWriter(outputDir+fileName+".tap");
	            		out.println("TAP version 13");
	            		out.println("1..1");
	            		out.println("testCase: " + fileName);
	            		//check return true /false:
	            		boolean resultCheck=true;
	            		String resultLine ="";
			        	NodeList nAssertionResults = eElement.getElementsByTagName("assertionResult");
			        	for(int temp2 =0;temp2<nAssertionResults.getLength();temp2++){
			        		System.out.println("temp2: "+temp2);
					        System.out.println("name: " + eElement.getElementsByTagName("name").item(temp2).getTextContent());
					        System.out.println("failure: " + eElement.getElementsByTagName("failure").item(temp2).getTextContent());
					        System.out.println("error: " + eElement.getElementsByTagName("error").item(temp2).getTextContent());
					        String failureCheck= eElement.getElementsByTagName("failure").item(temp2).getTextContent();
					        String errorCheck= eElement.getElementsByTagName("error").item(temp2).getTextContent();
					        boolean check1=failureCheck.equals("true")||errorCheck.equals("true");
					        System.out.println("failureCheck.equals(\"true\")||errorCheck.equals(\"true\"):"+check1);
					        if(failureCheck.equals("true")||errorCheck.equals("true")){
					        	resultCheck=false;
					        	//break;
					        }
					        System.out.println("resultCheck: "+resultCheck);
					        if(resultCheck==false){
					        	resultLine="not ok 1  ";
					        }else if(resultCheck==true){
					        	resultLine="ok 0  ";
					        }

			        	}					        
			        	System.out.println("resultLine: "+resultLine);
//				        out.println("name: " +eElement.getElementsByTagName("name").item(temp2).getTextContent());
//				        out.println("failure: " + eElement.getElementsByTagName("failure").item(temp2).getTextContent());
//				        out.println("error: " + eElement.getElementsByTagName("error").item(temp2).getTextContent());
				        out.println(resultLine+"- "+fileName);
				        out.close();

		
			        }
	        }
	       } catch (Exception e) {
	        e.printStackTrace();
	       }
	    }
}
