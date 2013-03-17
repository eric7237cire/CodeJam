package uva;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.MincostMaxflowLong;
import codejam.utils.datastructures.graph.MincostMaxflowLong.Edge;

import com.google.common.collect.Maps;

public class Prob753
{

    final protected static Logger log = LoggerFactory.getLogger("main");
    
    
    public Prob753() {
        // TODO Auto-generated constructor stub
    }
    
    private static final int PLUG_ID_OFFSET = 102;
    private static final int DEVICE_ID_OFFSET = 2;
    
    public static void main(String[] args) throws FileNotFoundException
    {
        Map<String, Integer> plugIds = Maps.newHashMap();
        Map<String, Integer> deviceIds = Maps.newHashMap();
        
        File f = new File("C:\\codejam\\CodeJam\\uva\\753 - maxflow\\cases.txt");
        Scanner sc = new Scanner(f);
        
        int tc = sc.nextInt();
        
        int source = 0;
        int sink = 1;
        
        MincostMaxflowLong flow = new MincostMaxflowLong();
                
        for(int i = 1; i <= tc; ++i)
        {
            int nPlugs = sc.nextInt();
            
            for(int p = 0; p < nPlugs; ++p)
            {
                String plugName = sc.next();
                Integer plugId = plugIds.get(plugName);
                
                if (plugId == null)
                {
                    plugId = plugIds.size();
                    plugIds.put(plugName, plugId);
                }   
                 
                flow.addArc(plugId + PLUG_ID_OFFSET, sink, 1, 0);
                            
            }
            
            int nDevice = sc.nextInt();
            
            for(int d = 0; d < nDevice; ++d)
            {
                String deviceName = sc.next();
                String plugName = sc.next();
                
                Integer plugId = plugIds.get(plugName);
                
                if (plugId == null)
                {
                    plugId = plugIds.size();
                    plugIds.put(plugName, plugId);
                }   
                 
                flow.addArc(source, plugId + PLUG_ID_OFFSET, 1, 0);
                
            }
            
            int nAdap = sc.nextInt();
            
            for(int a = 0; a < nAdap; ++a)
            {
                String plugName1 = sc.next();
                String plugName2 = sc.next();
                
                Integer plugId1 = plugIds.get(plugName1);
                Integer plugId2 = plugIds.get(plugName2);
                
                if (plugId1 == null)
                {
                    plugId1 = plugIds.size();
                    plugIds.put(plugName1, plugId1);
                }
                if (plugId2 == null)
                {
                    plugId2 = plugIds.size();
                    plugIds.put(plugName2, plugId2);
                }
                
                flow.addArc(plugId1 + PLUG_ID_OFFSET, plugId2 + PLUG_ID_OFFSET, 100000, 0);
            }
            
            Pair<Long,Long> flowAmt = flow.getFlow(source, sink);
            
            for(int e = 0; e < flow.E.size(); ++e)
            {
                if (e % 2 == 1)
                    continue;
                
                Edge edge = flow.E.get(e);
                log.info("Id: {} {} --> {} flow {} / {}", e, edge.source, edge.destination, edge.capacity - edge.residue, edge.capacity);
            }
            
            log.error("Flow {}", flowAmt);
            
            
        }
    }

}
