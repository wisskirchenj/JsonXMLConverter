package topics.polymorphism;

class Publication {

    private final String title;

    public Publication(String title) {
        this.title = title;
    }

    public final String getInfo() {
        // write your code here
        return String.format("%s: %s", getType(), getDetails());
    }

    public String getType() {
        return "Publication";
    }

    public String getDetails() {
        return title;
    }

}

