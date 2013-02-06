package codejam.utils;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import codejam.utils.utils.ListCompare;

import com.google.common.collect.Lists;

public class ListCompTest
{

    @Test
    public void testComp() {
        List<String> list1 = Lists.newArrayList("foo", "jam");
        
        List<String> list2 = Lists.newArrayList("code", "foo");
        
        assertTrue(
                new ListCompare.ListStringComparator().compare(list1,list2) > 0);
        
    }
}
