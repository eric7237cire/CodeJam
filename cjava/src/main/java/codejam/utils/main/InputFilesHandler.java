package codejam.utils.main;

import java.util.List;

import com.google.common.collect.Lists;

public class InputFilesHandler implements DefaultInputFiles
{

    boolean isSample = true;
    
    String[] inputFiles;
    
    public InputFilesHandler() {
        List<String> inputFiles = Lists.newArrayList();
        
       
            inputFiles.add("sample.in");
        
        
        this.inputFiles = new String[inputFiles.size()];
        this.inputFiles = inputFiles.toArray(this.inputFiles);
    }
    
    public InputFilesHandler(String letter,  boolean small, boolean large) {
        List<String> inputFiles = Lists.newArrayList();
        if (small) {
            inputFiles.add(letter + "-small-practice.in");
        }
        if (large) {
            inputFiles.add(letter + "-large-practice.in");
        }
                
        this.inputFiles = new String[inputFiles.size()];
        this.inputFiles = inputFiles.toArray(this.inputFiles);
    }

    @Override
    public String[] getDefaultInputFiles()
    {
        
        return this.inputFiles;
    }
    
    

}
