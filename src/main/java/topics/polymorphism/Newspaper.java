package topics.polymorphism;

class Newspaper extends Publication {

    private final String source;

    public Newspaper(String title, String source) {
        super(title);
        this.source = source;
    }

    @Override
    public String getType() {
        return String.format("Newspaper (source - %s)", source);
    }
}
