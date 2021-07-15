package model;

public class Person extends User {

    private int rank;

    public Person(User user, int rank) {
        super(user.getUsername(), user.getNickname(), user.getPassword());
        this.rank = rank;
        setScore(user.getScore());
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}