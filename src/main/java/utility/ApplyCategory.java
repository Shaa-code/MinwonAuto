package utility;

public enum ApplyCategory {
    EXPERIENCE("경력"), EMPLOYEE("재직");

    private final String name;

    ApplyCategory(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
