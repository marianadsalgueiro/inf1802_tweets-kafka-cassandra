import java.lang.String;
import java.util.UUID;
import twitter4j.GeoLocation;
import java.time.LocalDate;

public class Tweet {
    private long id;
    private String username;
    private String text;
    private String source;
    private boolean isTruncated;
    private boolean isFavorited;
    private String categoria;

    public Tweet (long id, String username, String text, String source, boolean isTruncated, boolean isFavorited, String categoria){
        this.id = id;
        this.username = username;
        this.text = text;
        this.source = source;
        this.isTruncated = isTruncated;
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

    public String getsource(){
        return source;
    }

    public boolean isTruncated(){
        return isTruncated;
    }


    public boolean isFavorited(){
        return isFavorited;
    }

    public String getCategoria(){
        return categoria;
    }

}
