package topics.polymorphism;

class Announcement extends Publication {

    private final int daysToExpire;

    public Announcement(String title, int daysToExpire) {
        super(title);
        this.daysToExpire = daysToExpire;
    }

    @Override
    public String getType() {
        return String.format("Announcement (days to expire - %d)", daysToExpire);
    }
}
