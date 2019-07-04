import java.lang.String;
import java.util.UUID;
import twitter4j.GeoLocation;
import java.time.LocalDate;

public class Tweet {
    private long id;
    private String username;
    private String text;
    private LocalDate createdDate;
    private String source;
    private boolean isTruncated;
    private GeoLocation geo;
    private boolean isFavorited;
    private String categoria;

    public Tweet (long id, String username, String text, LocalDate date, String source, boolean isTruncated, GeoLocation geo, boolean isFavorited, String categoria){
        this.id = id;
        this.username = username;
        this.text = text;
        this.createdDate = date;
        this.source = source;
        this.isTruncated = isTruncated;
        this.geo = geo;
        this.isFavorited = isFavorited;
        this.categoria = categoria;
    }

    public long getId(){
        return id;
    }

    public String getuser(){
        return username;
    }

    public String gettext(){
        return text;
    }

    public LocalDate getcreatedDate(){
        return createdDate;
    }

    public String getsource(){
        return source;
    }

    public boolean isTruncated(){
        return isTruncated;
    }

    public GeoLocation getGeoLocation(){
        return geo;
    }

    public boolean isFavorited(){
        return isFavorited;
    }

    public String getCategoria(){
        return categoria;
    }

}
