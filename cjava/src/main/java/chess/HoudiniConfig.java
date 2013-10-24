package chess;


public class HoudiniConfig implements IConfiguration {

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getDirectory()
     */
    @Override
    public String getDirectory() {
        return "C:\\Program Files (x86)\\Common Files\\ChessBase\\Engines.uci\\Houdini 3";
    }

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getExecName()
     */
    @Override
    public String getExecName() {
        return "Houdini_3_Pro_x64";
    }

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#setOptions()
     */
    @Override
    public String[] getOptions() {
        return new String[] {
                "name Hash value 2048",
                "name Threads value 4",
                "name NalimovPath value " + "E:\\ChessDB\\tablebase\\tablebase.sesse.net\\3-4-5" 
        };
    }

    @Override
    public String getCacheFilename() {
        return "C:\\temp\\objCache";

    }
}
