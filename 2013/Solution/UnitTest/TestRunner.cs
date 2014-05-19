#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE


using System.Numerics;
using NUnit.Framework;
using CodeJamUtils;
using Round3;
using Utils;
using System.Globalization;

using Logger = Utils.LoggerFile;
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
//using System.Xml.Xsl;
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
    public class TestRunner
    {
    	//
#if !mono
        [ TestCaseSource("FetchTestCases")]
    	public void runMain(Type mainType, object main, Scanner scanner, string ansExpected, object answerType)
    	{
            var input = mainType.GetMethod("createInput").Invoke(main, new object[] { scanner });


            //string ans = ( (InputFileConsumer<LostInput,string>) main).processInput(input);
            var ans = mainType.GetMethod("processInput").Invoke(main, new object[] { input });

            if ("double".Equals(answerType))
            {
                Logger.LogInfo("String [{}]", (string)ans);
                try
                {
                    double ans_d = double.Parse((string)ans, new CultureInfo("en-US"));
                    double expected_d = double.Parse(ansExpected, new CultureInfo("en-US"));
                    Assert.AreEqual(expected_d, ans_d, 0.00001);
                }
                catch (System.FormatException )
                {
                    Logger.LogInfo("ERROR [{}] [{}]", (string)ans, ansExpected);
                    Assert.IsTrue(false);
                }
            }
            else
            {
                Assert.AreEqual(ansExpected, ans);
            }
    		
		}
#endif
		
		private static string getChildElemValue(XElement el, string elemName)
		{
			XElement child = el.Elements(elemName).FirstOrDefault();
			if (child == null)
				return null;
			
			return child.Value;
		}
		
		private static string getAttributeValue(XElement el, string attName, string defIfNull = null)
		{
			var att = el.Attributes(attName).FirstOrDefault();
			if (att == null)
				return defIfNull;
			
			return att.Value;
		}

#if !mono
        public IEnumerable<TestCaseData> FetchTestCases()
        {
            //string testSmall2 = 
            //"12 3 1 3 3 1 1 1 2 4";
            //testInput(testSmall2, "LEFT 3");
            XElement po = XElement.Load(@"C:\codejam\CodeJam\2013\Solution\TestData.xml");


            IEnumerable<XElement> testGroupes = po.Elements("tests");

            foreach (XElement tests in testGroupes)
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
                
                foreach (Type intType in mainType.GetInterfaces())
                {
                    Logger.LogDebug("intType {}", intType);
                    if (intType.IsGenericType && intType.GetGenericTypeDefinition() == typeof(InputFileConsumer<,>))
                    {
                        Logger.LogDebug("Produces {} Consumes {}", intType.GetGenericArguments()[0], intType.GetGenericArguments()[1]);
                    }
                }

                foreach (XElement el in tests.Elements("test"))
                {
                    //Console.WriteLine("Name: " + el.Name);

                    Logger.LogInfo("Testing {}", el.Attributes("name").FirstOrDefault());
                    string data = getChildElemValue(el, "data");
                    string ansExpected = getChildElemValue(el, "answer");
                    Logger.LogDebug("Data [{}]\nAns [{}]", data, ansExpected);

                    if (data == null)
                    {
                        Logger.LogInfo("Data null");
                        continue;
                    }
                    Scanner scanner = new Scanner(new StringReader(data));

                    Preconditions.checkState(main != null);
                    Preconditions.checkState(scanner != null);
                    Preconditions.checkState(mainType != null);

                    yield return new TestCaseData( mainType, main, scanner, ansExpected, answerType )
                        //.Throws(typeof(DivideByZeroException))
                        .SetName(getAttributeValue(el, "name"))
                        .SetDescription("A description");
                }
            }
        }

#endif

#if mono
        [Test]
        public void runAllFetchTestCases()
        {
        	
        	//string testSmall2 = 
        	//"12 3 1 3 3 1 1 1 2 4";
            //testInput(testSmall2, "LEFT 3");

            XElement po = XElement.Load(@"/home/ent/mono/CodeJam/2013/Solution/TestData.xml");
            
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
            	
            	//Type inputType = null;
            	//Type returnType = null;
            	
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
					Logger.LogInfo("Testing {}", el.Attributes("name").FirstOrDefault());
					string data =  getChildElemValue(el, "data");
					string ansExpected = getChildElemValue(el, "answer");
					Logger.LogDebug("Data [{}]\nAns [{}]",data, ansExpected);
					
					if (data == null)
					{
						Logger.LogInfo("Data null");
						continue;
					}
					Scanner scanner = new Scanner(new StringReader(data));
					
					Preconditions.checkState(main != null);
					Preconditions.checkState(scanner != null);
					Preconditions.checkState(mainType != null);
					
					//var input = ( (InputFileProducer<LostInput>) main).createInput(scanner);
					var input = mainType.GetMethod("createInput").Invoke(main, new object[] {scanner});
			
			
			//string ans = ( (InputFileConsumer<LostInput,string>) main).processInput(input);
					var ans = mainType.GetMethod("processInput").Invoke(main, new object[] {input});
			
					if ("double".Equals(answerType))
					{
						//Logger.LogInfo("String [{}]", (string)ans);
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
					
				}
            }
        }
#endif        
       
        private string setBaseDir(string baseDir, bool set = true)
        {
            baseDir = @"C:\codejam\CodeJam\" + baseDir.Replace('/', '\\');
            if (set) Directory.SetCurrentDirectory(baseDir);

            return baseDir;
        }

        

#if !mono

        class MainTestData
        {
            internal string baseDir;
            internal string mainClassName;
            internal string inputMethodName;
            internal string processInputMethodName;
            internal Scanner scanner;
            internal List<string> checkStrList;
            internal int testCases;

            internal string testName;
            internal string testDescription;
        }

        
        private void runTestFile(MainTestData testData)
        {
            string baseDir = testData.baseDir;                
            string mainClassName = testData.mainClassName;
            string inputMethodName = testData.inputMethodName;
            string processInputMethodName = testData.processInputMethodName;
            Scanner scanner = testData.scanner;
            List<string> checkStrList = testData.checkStrList;
            int testCases = testData.testCases;

            setBaseDir(baseDir);

            Stopwatch timer = Stopwatch.StartNew();

            Type mainType = Type.GetType(mainClassName, true);
            object main = Activator.CreateInstance(mainType);

            for (int tc = 1; tc <= testCases; ++tc)
            {
                string checkStr = checkStrList[tc - 1];
                var input = mainType.GetMethod(inputMethodName).Invoke(main, new object[] { scanner });
                var ans = mainType.GetMethod(processInputMethodName).Invoke(main, new object[] { input });

                string ansStr = String.Format("Case #{0}: {1}", tc, ans);


                Logger.LogDebug("Checking {} = {}", checkStr, ansStr);
                Assert.AreEqual(checkStr, ansStr);
            }

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            string timeSpanStr = String.Format("{0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10);

            Logger.LogInfo("\n\nClass {}\nTC {} \nProcess method {}\nTime {}\n\n",
                            mainType.FullName, testCases, processInputMethodName, timeSpanStr);

        }

        [Test]
        public void TestFinal2013ProblemGraduation()
        {
            testName("2013.RoundFinal.Problem1.Small-0");
        }

        [Test]
        [Category ("current")]
        public void TestSample()
        {
            testName("2013.RoundFinal.Problem1.Sample");
        }

        private void testName(string name)
        {
            var test = testList.Where((td) => td != null && name.Equals( td.testName));

            var actual = test.FirstOrDefault();

            Assert.IsNotNull(actual);

            runTestFile(actual);
        }

        

        List<MainTestData> testList = new List<MainTestData>();

        [TestFixtureSetUp]
        public void ReadTestFileCases()
        {
            testList = new List<MainTestData>();

            XElement po = XElement.Load( setBaseDir(@"2013\Solution\TestData.xml", false) );
            
            foreach (XElement testFileRunner in po.Elements("testFileRunner"))
            {
                string baseDir = getAttributeValue(testFileRunner, "basedir");

                setBaseDir(baseDir);

                if ("true".Equals(getAttributeValue(testFileRunner, "ignore")))
                {
                    continue;
                }

                foreach (XElement run in testFileRunner.Elements("run"))
                {
                    string mainClassName = getAttributeValue(run, "className");

                    string inputFileName = getAttributeValue(run, "inputFile");
                    string checkFileName = getAttributeValue(run, "checkFile");
                    string inputMethodName = getAttributeValue(run, "createInputMethod");
                    string processInputMethodName = getAttributeValue(run, "processInputMethod");

                    TextReader inputReader = File.OpenText(inputFileName);
                    Scanner scanner = new Scanner(inputReader);
                    using(TextReader checkReader = File.OpenText(checkFileName))
                    {
                        int testCases = scanner.nextInt();

                        //Logger.LogInfo("Begin testing class {} method {} testcases {}",
                          //  mainClassName, processInputMethodName, testCases);

                    
                        List<string> checkStrs = new List<string>();
                        for (int tc = 1; tc <= testCases; ++tc)
                        {
                            checkStrs.Add( checkReader.ReadLine() );
                        }

                        testList.Add(new MainTestData{
                            baseDir=baseDir,
                            mainClassName = mainClassName, 
                            inputMethodName =inputMethodName,
                            processInputMethodName = processInputMethodName,
                            scanner = scanner,
                            checkStrList = checkStrs,
                            testCases = testCases,
                            testName = getAttributeValue(run, "testName"),
                            testDescription = string.Format("Class {0}\nMethod {1} \ninput file {2}",
                            mainClassName, processInputMethodName, inputFileName)});
                        
                            

                    }

                }
            }

            
        }
        
        #endif
#if mono
		[Test]
        public void runTestFiles()
        {
        	//string testSmall2 = 
        	//"12 3 1 3 3 1 1 1 2 4";
            //testInput(testSmall2, "LEFT 3");
            string mustMatch = "Round2";

            XElement po = XElement.Load(@"/home/ent/mono/CodeJam/2013/Solution/TestData.xml");
            
            foreach( XElement testFileRunner in po.Elements("testFileRunner"))
            {
				string baseDir = getAttributeValue(testFileRunner, "basedir");
				
				Directory.SetCurrentDirectory(baseDir);
				
				if ("true".Equals(getAttributeValue(testFileRunner, "ignore")))
            	{
            		continue;
            	}
				
				foreach(XElement run in testFileRunner.Elements("run"))
				{
					string mainClassName = getAttributeValue(run, "className");
					
					if (mustMatch != null && mainClassName.IndexOf(mustMatch, StringComparison.OrdinalIgnoreCase) < 0)
						continue;
					
					string inputFileName = getAttributeValue(run, "inputFile" );
					string checkFileName = getAttributeValue(run, "checkFile" );
					string inputMethodName = getAttributeValue(run, "createInputMethod" );
					string processInputMethodName = getAttributeValue(run, "processInputMethod" );
					
					Type mainType = Type.GetType(mainClassName, true);
					object main = Activator.CreateInstance(mainType);
					
					
					TextReader checkReader = File.OpenText(  checkFileName);
	
					using (TextReader inputReader = File.OpenText(  inputFileName))
					using (Scanner scanner = new Scanner(inputReader))
					{
						int testCases = scanner.nextInt();
						
						Logger.LogInfo("Begin testing class {} method {} testcases {}",
							mainClassName, processInputMethodName, testCases);
						
						MethodInfo m = mainType.GetMethod(inputMethodName);
						Logger.LogInfo( "{} method {} ", main, m);
						
						Stopwatch timer = Stopwatch.StartNew();
	
						for (int tc = 1; tc <= testCases; ++tc)
						{
							Preconditions.checkState( mainType.GetMethod(inputMethodName) != null, "Input method does not exist");
							var input = mainType.GetMethod(inputMethodName).Invoke(main, new object[] {scanner});			
							var ans = mainType.GetMethod(processInputMethodName).Invoke(main, new object[] {input});
							
							string ansStr = String.Format("Case #{0}: {1}", tc, ans);
							string checkStr = checkReader.ReadLine();
							
							Logger.LogDebug("Checking {} = {}", checkStr, ansStr);
							Assert.AreEqual(checkStr, ansStr);
						}
						
						timer.Stop();
						TimeSpan timespan = timer.Elapsed;
	
						string timeSpanStr = String.Format("{0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10);
						Logger.LogInfo("\n\nClass {}\nMethod {} \ninput file {}\nTime {}\n\n",
							mainClassName, processInputMethodName, inputFileName, timeSpanStr);
	
					}
				}
            }
        }
        #endif
        
    }
}
