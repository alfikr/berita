package id.eiris.newsservice.web.rest;


import com.sun.jndi.ldap.Ber;
import id.eiris.newsservice.domain.Berita;
import id.eiris.newsservice.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import reactor.core.publisher.Flux;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/berita")
public class NewsResource {
    @Autowired
    private NewsService newsService;
    @GetMapping("/antara")
    public Flux<Berita> getBeritaAntara() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        List<Berita> beritaList= newsService.antaraNews();
        return Flux.fromIterable(beritaList);
    }
    @GetMapping({"","/"})
    public Flux<Berita> getBerita(@PathVariable(name = "rss") String rss) throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        List<Berita> beritas = newsService.getBerita(rss);
        return Flux.fromIterable(beritas);
    }

}
