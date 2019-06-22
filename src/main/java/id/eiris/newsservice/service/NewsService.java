package id.eiris.newsservice.service;

import id.eiris.newsservice.domain.Berita;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private RestTemplate restTemplate;
    private Berita convertFromElement(Element el){
        Berita b = new Berita();
        b.setTitle(el.getElementsByTagName("title").item(0).getTextContent());
        b.setLink(el.getElementsByTagName("link").item(0).getTextContent());
        b.setPermalink(el.getElementsByTagName("guid").item(0).getTextContent());
        b.setShortContent(el.getElementsByTagName("description").item(0).getTextContent());
        b.setKonten(el.getElementsByTagName("content:encoded").item(0).getTextContent());
        org.jsoup.nodes.Document doc = Jsoup.parse(b.getShortContent());
        doc.select("a").remove();
        b.setShortContent(doc.text());
        return b;
    }
    public List<Berita> antaraNews() throws URISyntaxException, ParserConfigurationException, IOException, SAXException {
        return getBerita("https://www.antaranews.com/rss/nas.xml");
    }
    public List<Berita> detikNasional ()  throws URISyntaxException, ParserConfigurationException, IOException, SAXException{
        return getBerita("http://rss.detik.com/index.php/detikcom_nasional");
    }
    @Cacheable("beritaByUri")
    public List<Berita> getBerita(String uri) throws URISyntaxException, ParserConfigurationException, IOException, SAXException {
        List<Berita> beritas = new ArrayList<>();
        ResponseEntity<String> res = restTemplate.getForEntity(new URI(uri),String.class);
        String response = res.getBody();
        System.out.println(response);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(response));
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();
        NodeList nodeList=doc.getElementsByTagName("item");
        for (int i=0;i<nodeList.getLength();i++){
            Element el = (Element)nodeList.item(i);
            Berita b = convertFromElement(el);
            beritas.add(b);
        }
        return beritas;
    }
}
