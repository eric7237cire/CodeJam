package codejam.utils.main;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import com.google.common.collect.Lists;

public class InputFilesHandler implements DefaultInputFiles
{
    
    final protected static Logger log = LoggerFactory.getLogger("main");

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
        
        if (!small && !large) {
            inputFiles.add("sample.in");
        }
                
        this.inputFiles = new String[inputFiles.size()];
        this.inputFiles = inputFiles.toArray(this.inputFiles);
        
        if (small || large) {
            (( ch.qos.logback.classic.Logger) log).setLevel(Level.OFF);
        } else {
           // (( ch.qos.logback.classic.Logger) log).setLevel(Level.ALL);
        }
    }
    
    
    public InputFilesHandler(String letter,  int small, int large) {
        this(letter, small==1, large==1);
    }
    
    @Deprecated
    public InputFilesHandler(String letter,  int small, int large, int logging) {
        this(letter, small==1, large==1);
    }

    @Override
    public String[] getDefaultInputFiles()
    {
        
        return this.inputFiles;
    }
    
    

}
