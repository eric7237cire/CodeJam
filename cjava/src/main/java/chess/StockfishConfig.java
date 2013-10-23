package chess;

public class StockfishConfig implements IConfiguration {

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getDirectory()
     */
    @Override
    public String getDirectory() {
        return "C:\\Documents and Settings\\Groning-E\\Mes documents\\Downloads\\stockfish-4-win\\stockfish-4-win\\Windows";
    }

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#getExecName()
     */
    @Override
    public String getExecName() {
        return "stockfish_4_32bit.exe";
    }

    /*
     * (non-Javadoc)
     * 
     * @see chess.IConfiguration#setOptions()
     */
    @Override
    public String[] getOptions() {
        return new String[] {
                "name Write Debug Log value true",
                "name Hash value 256",
                "name Threads value 1",
                "name Ponder value false",
                
        };
    }

    /* (non-Javadoc)
     * @see chess.IConfiguration#getCacheFilename()
     */
    @Override
    public String getCacheFilename() {
        return "C:\\Documents and Settings\\Groning-E\\Mes documents\\Downloads\\uci\\objCache";

    }
    
    

}

