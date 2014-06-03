package si.teandro.model;

import si.teandro.model.EtalioCharacter;

/**
 * Created by jernej on 3.6.14.
 */
public class EtalioUser {

    private EtalioCharacter character;
    private String name;
    private String age;
    private String location;
    private String bio;
    private String image;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EtalioCharacter getCharacter() {
        return character;
    }

    public void setCharacter(EtalioCharacter character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "EtalioUser{" +
                "character=" + character +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", location='" + location + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
