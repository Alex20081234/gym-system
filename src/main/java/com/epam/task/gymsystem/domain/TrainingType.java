package com.epam.task.gymsystem.domain;

public class TrainingType {

    private String name;

    public TrainingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TrainingType type = (TrainingType) obj;
        return name.equals(type.getName());
    }

}
