package bdma.bigdata.project.rest.core;

public class responseCourse {
    private String Name;
    private String Grade;

    public responseCourse(String name, String grade) {
        Name = name;
        Grade = grade;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }
}