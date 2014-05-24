using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Runtime.Serialization;
using System.Xml;
using System.Xml.Schema;
using System.Xml.Serialization;
 /*
namespace BrookNovak.Collections
{
[TestFixture]
public class ExampleTestFixture {
     
    [Test]
    public void FindAt_Overlapping ()
    {
        // ARRANGE
        var int1 = new TestInterval (20, 60);
        var int2 = new TestInterval (10, 50);
        var int3 = new TestInterval (40, 70);
 
        var intervalColl = new[] { 
            int1, int2, int3
        };
        var tree = new IntervalTree<TestInterval, int>(intervalColl, new TestIntervalSelector());
 
        // ACT
        var res1 = tree.FindAt (0);
        var res2 = tree.FindAt (10);
        var res3 = tree.FindAt (15);
        var res4 = tree.FindAt (20);
        var res5 = tree.FindAt (30);
        var res6 = tree.FindAt (40);
        var res7 = tree.FindAt (45);
        var res8 = tree.FindAt (50);
        var res9 = tree.FindAt (55);
        var res10 = tree.FindAt (60);
        var res11 = tree.FindAt (65);
        var res12 = tree.FindAt (70);
        var res13 = tree.FindAt (75);
 
        // ASSERT
        Assert.That (res1, Is.Empty);
        Assert.That (res2, Is.EquivalentTo(new[] { int2 }));
        Assert.That (res3, Is.EquivalentTo(new[] { int2 }));
        Assert.That (res4, Is.EquivalentTo(new[] { int1, int2 }));
        Assert.That (res5, Is.EquivalentTo(new[] { int1, int2 }));
        Assert.That (res6, Is.EquivalentTo(new[] { int1, int2, int3 }));
        Assert.That (res7, Is.EquivalentTo(new[] { int1, int2, int3 }));
        Assert.That (res8, Is.EquivalentTo(new[] { int1, int2, int3 }));
        Assert.That (res9, Is.EquivalentTo(new[] { int1, int3 }));
        Assert.That (res10, Is.EquivalentTo(new[] { int1, int3 }));
        Assert.That (res11, Is.EquivalentTo(new[] { int3 }));
        Assert.That (res12, Is.EquivalentTo(new[] { int3 }));
        Assert.That (res13, Is.Empty);
    }
}
 
[Serializable]
public class TestInterval 
{
    private TestInterval() {}
 
    public TestInterval(int low, int hi) 
    {
        if(low > hi)
            throw new ArgumentOutOfRangeException("lo higher the hi");
        Low = low;
        Hi = hi;
    }
 
    public int Low { get; private set; }
    public int Hi { get; private set; }
    public string MutableData { get; set; }
 
    public override string ToString ()
    {
        return string.Format ("[Low={0}, Hi={1}, Data={2}]", Low, Hi, MutableData);
    }
}
 
[Serializable]
public class TestIntervalSelector : IIntervalSelector<TestInterval, int>
{
    public int GetStart (TestInterval item) 
    {
        return item.Low;
    }
 
    public int GetEnd (TestInterval item) 
    {
        return item.Hi;
    }
}

}*/