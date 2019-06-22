package id.eiris.newsservice.web.rest;


import id.eiris.newsservice.domain.Berita;
import id.eiris.newsservice.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.OPTIONS,}, maxAge = 3600)
    public Flux<Berita> getBeritaAntara() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        List<Berita> beritaList= newsService.antaraNews();
        return Flux.fromIterable(beritaList);
    }
    @GetMapping("/detik/nasional")
    @CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.OPTIONS,}, maxAge = 3600)
    public Flux<Berita> getBeritaDetikNasional() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        List<Berita> beritaList= newsService.detikNasional();
        return Flux.fromIterable(beritaList);
    }
    @CrossOrigin(origins="*", methods={RequestMethod.GET})
    @GetMapping({"?{rss}","/?{rss}"})
    public Flux<Berita> getBerita(@PathVariable(name = "rss") String rss) throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        System.out.println(rss);
        List<Berita> beritas = newsService.getBerita(rss);
        return Flux.fromIterable(beritas);
    }

}
