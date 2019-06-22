package id.eiris.newsservice;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
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
		ResponseEntity<String> res=restTemplate.getForEntity(new URI("https://www.antaranews.com/rss/nas.xml"),String.class);
		//ResponseEntity<String> res = restTemplate.exchange(new URI("https://www.antaranews.com/rss/nas.xml"), HttpMethod.GET,entity,String.class);
		String response = res.getBody();
		testParse(response);
	}
	public void testParse(String res) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(res));
		Document doc = builder.parse(is);
		doc.getDocumentElement().normalize();
		NodeList nodeList=doc.getElementsByTagName("item");
		for (int i=0;i<nodeList.getLength();i++){
			Element el = (Element)nodeList.item(i);
			String link = el.getElementsByTagName("link").item(0).getTextContent();
			//System.out.println(el.getElementsByTagName("title").item(0).getTextContent());
			try {
				ResponseEntity<String> isi = restTemplate.getForEntity(new URI(link),String.class);
				org.jsoup.nodes.Document dc = Jsoup.parse(isi.getBody());
				org.jsoup.nodes.Document ds=dc.normalise();
				org.jsoup.select.Elements article =ds.getElementsByTag("article");
				int j=0;
				article.forEach((org.jsoup.nodes.Element x)->{
					if(x.hasClass("post-wrapper")){
						org.jsoup.select.Elements head = x.getElementsByTag("header");
						Elements div = x.getElementsByTag("div");
						System.out.println(head.get(0).html());
						System.out.println(div.get(0).html());
					}
				});
			}catch (URISyntaxException e){
				e.printStackTrace(System.err);
			}
		}
	}
	@Test
	public void removeTagTest(){
		String html = "<p>Orang yang menelpon balik, pulsanya akan membengkak. Tagihan inilah yang kemudian akan masuk ke kantong penipu tersebut. Hati-hati jika Anda mendapat &#8220;Missed Call&#8221; dari Luar Negeri. Nomer telepon +237222258673 dari Kamerun. Kemudian +237222258671, +237222258252, +237222258252, +237222258233 dan +23722258186. Ada juga dari Kasai-Oriental/Kasai-Occidental nomer +237222258186, +234300035. Seorang coach Densus Digital memperingatkan, &#8220;missed call&#8221; misterius itu lagi [&#8230;]</p>\\n<p>The post <a rel=\\\"nofollow\\\" href=\\\"http://antaranews.id/1081/\\\"";
		org.jsoup.nodes.Document doc = Jsoup.parse(html);
		doc.select("a").remove();
		System.out.println("val: "+doc.val());
		System.out.println("text: "+doc.text());
		System.out.println(doc.data());
	}

}
