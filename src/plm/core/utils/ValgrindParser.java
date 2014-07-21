package plm.core.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ValgrindParser {
	
	public static StringBuffer parse(File valgrindFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(valgrindFile);
		doc.getDocumentElement().normalize();
		StringBuffer resCompilationErr=new StringBuffer();
		NodeList nodes = doc.getElementsByTagName("error");
		int nbError=nodes.getLength();
		if(nbError>0){
			resCompilationErr.append("========= "+nbError+" Error"+(nbError>1?"s":"")+" =========\n");
			for (int i = 0; i < nbError; i++) {
				Node node = nodes.item(i);
				if(node!=null){
					String space="";
					resCompilationErr.append("========= Error "+(i+1)+" =========\n");
					resCompilationErr.append("cause : "+getValue("kind", ((Element)node))+"\n");
					resCompilationErr.append("details : "+getValue("auxwhat", ((Element)node))+"\n");
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						for(int j=0;j<node.getChildNodes().getLength();j++){
							Node node2 = node.getChildNodes().item(j);
							if (node2!=null && node2.getNodeType() == Node.ELEMENT_NODE) {
								if(node2.getNodeName().toLowerCase().equals("stack")){
									for(int k=0;k<node2.getChildNodes().getLength();k++){
										Node node3 = node2.getChildNodes().item(k);
										if(node3!=null && node3.getNodeType() == Node.ELEMENT_NODE){
											Element element = (Element) node3;
											if(!getValue("fn", element).toLowerCase().equals("main")){
												resCompilationErr.append(space+"fonction: " + getValue("fn", element)+"\n");
												resCompilationErr.append(space+"line: " + getValue("line", element)+"\n");
												space+="    ";
											}
										}
									}
								}else if(node2.getNodeName().toLowerCase().equals("xwhat")){
									for(int k=0;k<node2.getChildNodes().getLength();k++){
										Node node3 = node2.getChildNodes().item(k);
										if(node3!=null && node3.getNodeType() == Node.ELEMENT_NODE){
											Element element = (Element) node3;
											//if(!getValue("fn", element).toLowerCase().equals("main")){
												resCompilationErr.append(space+"fonction: " + getValue("fn", element)+"\n");
												resCompilationErr.append(space+"line: " + getValue("line", element)+"\n");
												space+="    ";
											//}
										}
									}
								}
							}
						}
					}

					resCompilationErr.append("======= End Error "+(i+1)+" =======\n");
				}
			}
		}
		return resCompilationErr;
	}
	
	
	private static String getValue(String tag, Element element) {
		if(element.getElementsByTagName(tag)!=null && element.getElementsByTagName(tag).item(0)!=null){
			NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
			Node node = (Node) nodes.item(0);
			return node.getNodeValue();
		}else{
			return "";
		}
	}

}
