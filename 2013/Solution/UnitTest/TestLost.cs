#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE


using System.Numerics;
using NUnit.Framework;
using CodeJamUtils;
using Round3;
using Utils;
using System.Globalization;

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
		
		private static string getChildElemValue(XElement el, string elemName)
		{
			XElement child = el.Elements(elemName).FirstOrDefault();
			if (child == null)
				return null;
			
			return child.Value;
		}
		
		private static string getAttributeValue(XElement el, string attName)
		{
			var att = el.Attributes(attName).FirstOrDefault();
			if (att == null)
				return null;
			
			return att.Value;
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
            	string mainClassName = getAttributeValue(tests, "className");
            	string answerType = getAttributeValue(tests, "answerType");
            	
            	if ("true".Equals(getAttributeValue(tests, "ignore")))
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
					string data =  getChildElemValue(el, "data");
					string ansExpected = getChildElemValue(el, "answer");
					Logger.LogDebug("Data [{}] Ans {}",data, ansExpected);
					
					if (data == null)
					{
						Logger.LogInfo("Data null");
						continue;
					}
					Scanner scanner = new Scanner(new StringReader(data));
					
					//var input = ( (InputFileProducer<LostInput>) main).createInput(scanner);
			var input = mainType.GetMethod("createInput").Invoke(main, new object[] {scanner});
			//string ans = ( (InputFileConsumer<LostInput,string>) main).processInput(input);
			var ans = mainType.GetMethod("processInput").Invoke(main, new object[] {input});
			
					if ("double".Equals(answerType))
					{
						Logger.LogInfo("String [{}]", (string)ans);
						try {
						double ans_d = double.Parse( (string)ans, new CultureInfo("en-US"));
						double expected_d = double.Parse(ansExpected, new CultureInfo("en-US"));
						Assert.AreEqual(expected_d, ans_d, 0.00001);
						} catch (System.FormatException ex) {
						Logger.LogInfo("ERROR [{}] [{}]", (string)ans, ansExpected);	
						Assert.IsTrue(false);
						}
					} else {
						Assert.AreEqual(ansExpected, ans);
					}
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
