package pingali.jeevan.alpakka.cassandra.first;

public class PersonalInfo {
    private Integer id;
    private String name;
    private String dob;

    public PersonalInfo(Integer id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }
}
