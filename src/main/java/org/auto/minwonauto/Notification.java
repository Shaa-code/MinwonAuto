package org.auto.minwonauto;
import java.util.ArrayList;
import java.util.List;

public class Notification {
    private final List<String> errors = new ArrayList();

    public void addError(final String message){
        errors.add(message);
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public List<String> errorMessage(){
        return this.errors;
    }
}
