package jlm.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Course {
    
    public void create();
    
    public void refresh();
    
    public void delete();
    
    public ArrayList<String> getAllCoursesId() throws IOException;
    
    public String getCourseId();
    
    public void setCourseId(String id);
    
    public void setPassword(String pass);
    
    public HashMap<String, Integer> getStudentsResults();
    
    public HashMap<String, Integer> getExoResults();
}
