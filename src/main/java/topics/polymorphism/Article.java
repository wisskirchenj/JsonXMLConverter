package topics.polymorphism;

class Article extends Publication {

    private final String author;

    public Article(String title, String author) {
        super(title);
        this.author = author;
    }

    @Override
    public String getType() {
        return String.format("Article (author - %s)", author);
    }
}
