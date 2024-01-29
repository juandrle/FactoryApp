package de.swtpro.factorybuilder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "factory")
public class  Factory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    private long version;
    @NotBlank
    private String name;

    private String password;
    private int width;
    private int height;
    private int depth;
    @Lob
    private byte[] screenshot;
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username")
    private User author;

    public String getPassword() {
        return password;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot;
    }

    public byte[] getScreenshot() {
        return screenshot;
    }

    public long getFactoryID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(long id) { this.id = id;}

    public long getVersion() {
        return version;
    }
}
