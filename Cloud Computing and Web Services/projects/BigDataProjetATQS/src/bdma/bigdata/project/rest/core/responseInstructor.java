package bdma.bigdata.project.rest.core;

public class responseInstructor {
    private String Name;
    private String Rate;

    public responseInstructor(String name, String rate) {
        this.Name = name;
        this.Rate = rate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}