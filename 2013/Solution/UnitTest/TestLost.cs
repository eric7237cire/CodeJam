#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE


using System.Numerics;
using NUnit.Framework;
using CodeJamUtils;
using Round3;
using Utils;

using Logger = Utils.LoggerFile;
using System.Collections.Generic;
using Utils.math;

using System;
using System.Diagnostics;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using System.Linq;
using System.Xml;
using System.Xml.Linq;
using System.Xml.Schema;
using System.Xml.XPath;
using System.Xml.Xsl;
using System.IO;
using System.Threading;
using System.Reflection;
//using System.IO.Packaging;

/*
 *  2 questions, does the cycle hit the max diff without fail
 *  
 * 2: for extremely short cycles
 * 
 * delta-diff = 2 * (height - maxdiff) ?
 * 
 * 99, h: 100 -- delta diff = 2
 * 
 */

namespace UnitTest
{
   

       
    
    [TestFixture] 
    public class TestLost
    {
    	//[Test, TestCaseSource("FetchTestCases")]
    	public void runMain(Type mainType, object main, Scanner scanner, object ansExpected)
    	{
    		
    		
		}
    	
    	  	
		[Test]
        public void FetchTestCases()
        {
        	//string testSmall2 = 
        	//"12 3 1 3 3 1 1 1 2 4";
            //testInput(testSmall2, "LEFT 3");
            XElement po = XElement.Load(@"/home/ent/mono/CodeJam/2013/Solution/UnitTest/Properties/MonoResources.resx");
            
            
            IEnumerable<XElement> testGroupes = po.Elements("tests");
            
            foreach(XElement tests in testGroupes)
            {
            	string mainClassName = tests.Attributes("className").First().Value;
            	
            	if (tests.Attributes("ignore").Count() > 0 &&  "true".Equals(tests.Attributes("ignore").First().Value))
            	{
            		continue;
            	}
            	
            	Logger.LogInfo("Testing class {}", mainClassName);
 
            	Type mainType = Type.GetType(mainClassName, true);
            	object main = Activator.CreateInstance(mainType);
            	
            	Type inputType = null;
            	Type returnType = null;
            	
            	foreach(Type intType in mainType.GetInterfaces()) 
            	{
            		Logger.LogDebug("intType {}", intType);
            		if(intType.IsGenericType && intType.GetGenericTypeDefinition() == typeof(InputFileConsumer<,>)) 
            		{
            			Logger.LogDebug( "Produces {} Consumes {}", intType.GetGenericArguments()[0], intType.GetGenericArguments()[1]);
            		}
            	}
            	
				foreach (XElement el in tests.Elements("test"))
				{
					Console.WriteLine("Name: " + el.Name);
					
					Logger.LogInfo("Testing {}", el.Attributes("name").FirstOrDefault());
					string data =  el.Elements("data").FirstOrDefault().Value;
					string ansExpected = el.Elements("answer").FirstOrDefault().Value;
					Logger.LogInfo("Data [{}] Ans {}",data, ansExpected);
					
					Scanner scanner = new Scanner(new StringReader(data));
					
					//var input = ( (InputFileProducer<LostInput>) main).createInput(scanner);
			var input = mainType.GetMethod("createInput").Invoke(main, new object[] {scanner});
			//string ans = ( (InputFileConsumer<LostInput,string>) main).processInput(input);
			var ans = mainType.GetMethod("processInput").Invoke(main, new object[] {input});
			
			Assert.AreEqual(ansExpected, ans);
					//yield return new object[] {mainType, main, scanner, ansExpected };
					/*yield return new TestCaseData( mainType, main, scanner, ansExpected )
						//.Throws(typeof(DivideByZeroException))
						.SetName(el.Name.Value)
						.SetDescription("An exception is expected");*/
				}
            }
        }
        
       // [Test]
        public void TestNothing()
        {
        	Assert.IsTrue(false);	
        }
       
        
        
        private void testInput(string inputTxt, string expectedAns)
        {
            Scanner scanner = new Scanner(new StringReader(inputTxt));

            Lost pong = new Lost();

            LostInput input = pong.createInput(scanner);

            string ans = pong.processInput(input);

            Assert.AreEqual(expectedAns, ans);

        }

    }
}
