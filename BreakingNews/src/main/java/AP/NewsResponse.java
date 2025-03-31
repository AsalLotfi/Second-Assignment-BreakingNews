package AP;

import java.util.List;

public class NewsResponse {

    private List<News> articles; // Matches the JSON "articles" array

    public List<News> getArticles() {
        return articles;
    }
}
