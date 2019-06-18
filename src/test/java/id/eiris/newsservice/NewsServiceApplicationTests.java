package id.eiris.newsservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceApplicationTests {
	@Autowired
	private RestTemplate restTemplate;
	@Test
	public void contextLoads() throws URISyntaxException, ParserConfigurationException, IOException, SAXException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_RSS_XML));
		HttpEntity<String> entity = new HttpEntity<String>("paramenters",headers);
		ResponseEntity<String> res = restTemplate.exchange(new URI("http://antaranews.id/feed"), HttpMethod.GET,entity,String.class);
		String response = res.getBody();
		testParse(response);
	}
	public void testParse(String res) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(res));
		Document doc = builder.parse(is);
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nodeList=doc.getElementsByTagName("item");
		for (int i=0;i<nodeList.getLength();i++){
			Element el = (Element)nodeList.item(i);
			System.out.println(el.getElementsByTagName("title").item(0).getTextContent());
		}

	}

}
