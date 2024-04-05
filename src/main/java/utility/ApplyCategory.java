package utility;

public enum ApplyCategory {
    EXP("경력"), EMP("재직");

    private final String name;

    ApplyCategory(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
