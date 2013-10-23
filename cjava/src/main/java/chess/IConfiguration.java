package chess;


public interface IConfiguration {
    public String getDirectory();
    public String getExecName();
    public String[] getOptions();
    
    public String getCacheFilename();
}
