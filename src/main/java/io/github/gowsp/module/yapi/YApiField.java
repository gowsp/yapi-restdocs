package io.github.gowsp.module.yapi;

public class YApiField {
    private String name;
    private String type;
    private String example;
    private String desc;
    private String required;

    public String getName() {
        return name;
    }

    public YApiField setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public YApiField setType(String type) {
        this.type = type;
        return this;
    }

    public String getExample() {
        return example;
    }

    public YApiField setExample(String example) {
        this.example = example;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public YApiField setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getRequired() {
        return required;
    }

    public YApiField setRequired(String required) {
        this.required = required;
        return this;
    }
}
