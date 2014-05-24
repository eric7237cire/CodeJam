#if DEBUG
#define LOGGING_DEBUG
#endif

#define LOGGING_INFO


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
using System.IO;
using System.Threading;
using System.Reflection;
using System.Text.RegularExpressions;


namespace UnitTest
{




    [TestFixture]
    public class TestRunner
    {
        //
#if !mono

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
                catch (System.FormatException)
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
						} catch (System.FormatException ) {
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

#if mono
	private string setBaseDir(string baseDir, bool set = true)
        {
            baseDir = @"/home/ent/mono/CodeJam/" + baseDir.Replace('\\', '/');
            if (set) Directory.SetCurrentDirectory(baseDir);

            return baseDir;
        }
#else
        private string setBaseDir(string baseDir, bool set = true)
        {
            baseDir = @"C:\codejam\CodeJam\csharp\Solution\" + baseDir.Replace('/', '\\');
            if (set) Directory.SetCurrentDirectory(baseDir);

            return baseDir;
        }
#endif




        class MainTestData
        {
            internal string baseDir;
            internal string mainClassName;
            internal string inputMethodName;
            internal string inputFileName;
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

            Regex regex = new Regex(@"(\..*)?$");
            string outputFileName = regex.Replace(testData.inputFileName, ".out", 1);
            List<String> answers = new List<string>();

            using (StreamWriter writer = new StreamWriter(outputFileName, false))
            {
                for (int tc = 1; tc <= testCases; ++tc)
                {

                    // scanner.enablePlayBack();

                    var input = mainType.GetMethod(inputMethodName).Invoke(main, new object[] { scanner });
                    var ans = mainType.GetMethod(processInputMethodName).Invoke(main, new object[] { input });

                    string ansStr = String.Format("Case #{0}: {1}", tc, ans);
                    writer.WriteLine(ansStr);
                    answers.Add(ansStr);

                    // string inputCase = scanner.finishPlayBack();
                    //Logger.LogDebug("Test\n{}", inputCase);

                }
            }




            for (int tc = 1; tc <= testCases; ++tc)
            {

                string checkStr = checkStrList[tc - 1];
                string ansStr = answers[tc - 1];
                Logger.LogDebug("Checking {} = {}", checkStr, ansStr);
                Assert.AreEqual(checkStr, ansStr);

            }


            scanner.Dispose();
            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            string timeSpanStr = String.Format("{0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10);

            Logger.LogInfo("\n\nClass {}\nTC {} \nProcess method {}\nTime {}\n\n",
                            mainType.FullName, testCases, processInputMethodName, timeSpanStr);

        }

        //[Test]
        public void TestFinal2013ProblemGraduation()
        {
            testName("2013.RoundFinal.Problem1.Small-0");
        }

        //[Test]        
        public void TestFinal2013ProblemGraduationSmall()
        {
            testName("2013.RoundFinal.Problem1.Small");
        }

        //[Test]
        public void TestFinal2013ProblemDrummerLarge()
        {
            testName("2013.RoundFinal.Problem2.Large");
        }

        private void testName(string name)
        {
            var test = testList.Where((td) => td != null && name.Equals(td.testName));

            var actual = test.FirstOrDefault();

            Assert.IsNotNull(actual);

            runTestFile(actual);
        }



        List<MainTestData> testList = new List<MainTestData>();

        [TestFixtureSetUp]
        public void ReadTestFileCases()
        {
            testList = new List<MainTestData>();

            XElement po = XElement.Load(setBaseDir(@"TestData.xml", false));

            foreach (XElement testFileRunner in po.Elements("testFileRunner"))
            {
                string baseDir = getAttributeValue(testFileRunner, "basedir");

                setBaseDir(baseDir);



                foreach (XElement run in testFileRunner.Elements("run"))
                {
                    string mainClassName = getAttributeValue(run, "className");

                    string inputFileName = getAttributeValue(run, "inputFile");
                    string checkFileName = getAttributeValue(run, "checkFile");
                    string inputMethodName = getAttributeValue(run, "createInputMethod");
                    string processInputMethodName = getAttributeValue(run, "processInputMethod");

                    if ("slow".Equals(getAttributeValue(run, "category")))
                    {
#if !INCLUDE_SLOW
                        continue;
#endif
                    }
                    TextReader inputReader = File.OpenText(inputFileName);
                    Scanner scanner = new Scanner(inputReader);
                    using (TextReader checkReader = File.OpenText(checkFileName))
                    {
                        int testCases = scanner.nextInt();

                        //Logger.LogInfo("Begin testing class {} method {} testcases {}",
                        //  mainClassName, processInputMethodName, testCases);


                        List<string> checkStrs = new List<string>();
                        for (int tc = 1; tc <= testCases; ++tc)
                        {
                            checkStrs.Add(checkReader.ReadLine());
                        }

                        testList.Add(new MainTestData
                        {
                            baseDir = baseDir,
                            mainClassName = mainClassName,
                            inputMethodName = inputMethodName,
                            inputFileName = inputFileName,
                            processInputMethodName = processInputMethodName,
                            scanner = scanner,
                            checkStrList = checkStrs,
                            testCases = testCases,
                            testName = getAttributeValue(run, "testName"),
                            testDescription = string.Format("Class {0}\nMethod {1} \ninput file {2}",
                            mainClassName, processInputMethodName, inputFileName)
                        });



                    }

                }
            }


        }

        [Test]
        public void runAllTestFiles()
        {

            foreach (MainTestData mtd in testList)
            {
                runTestFile(mtd);
            }

        }


    }
}
