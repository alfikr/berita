package id.eiris.newsservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

@Cacheable
public class Berita {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String link;
    @Getter
    @Setter
    private String permalink;
    @Getter
    @Setter
    private String shortContent;
    @Getter
    @Setter
    private String konten;
}
