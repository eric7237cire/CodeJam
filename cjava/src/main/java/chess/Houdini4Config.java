package chess;
//
public class Houdini4Config  implements IConfiguration {

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getDirectory()
     */
    @Override
    public String getDirectory() {
        return "C:\\Program Files (x86)\\Common Files\\ChessBase\\Engines.uci";
    }

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getExecName()
     */
    @Override
    public String getExecName() {
        //return "Houdini_4_Pro_w32-v1";
        return "Houdini_4_Pro_x64B-v1x";
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
                "name Threads value 3",
                "name NalimovPath value " + "E:\\ChessDB\\tablebase\\tablebase.sesse.net\\3-4-5",
                "name EGTB Probe Depth value 5",
                "name SyzygyPath value " + "E:\\ChessDB\\tablebase6\\wdl",
                "name Contempt value 0",
                "name MultiPV value 4",
               /// "name Learning File value " + "C:\\temp\\learn.dat",
              //  "name Learning value true",
               // "name Hash File value " + "C:\\temp\\hash.dat",
                "name Never Clear Hash value true"

        };
    }

    @Override
    public String getCacheFilename() {
        return "C:\\temp\\objCache";

    }
}