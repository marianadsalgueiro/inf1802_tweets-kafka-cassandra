import java.util.Date;

public class Tweet {
    public String user;
    public String text;
    public Date createdAt;
    public String language;
    public String source;

    public Tweet() {
    }

    public Tweet(String user, String text, Date createdAt, String language, String source) {
        this.user = user;
        this.text = text;
        this.createdAt = createdAt;
        this.language = language;
        this.source = source;
    }

    public String getUser() { return this.user; }
    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {return this.createdAt; }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getSource() { return this.source; }
    public void setSource(String source) { this.source = source;}

    @Override
    public String toString() {
        return text + "-- from: " + source + " -- lang: " + language;
    }


}
