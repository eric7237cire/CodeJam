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

	public class PeekingStreamReader
	{
		TextReader reader;
		private Queue<string> _peeks;
	
		public PeekingStreamReader(TextReader stream) 
		{
			_peeks = new Queue<string>();   
			reader = stream;
		}
	
		public string ReadLine()
		{
			if (_peeks.Count > 0)
			{
				var nextLine = _peeks.Dequeue();
				return nextLine;
			}
			return reader.ReadLine();
		}
	
		public string PeekReadLine()
		{
			var nextLine = ReadLine();
			_peeks.Enqueue(nextLine);
			return nextLine;
		}
	}


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
	private string setBaseDir(string baseDir, bool set = true)
        {
            baseDir = @"/home/ent/mono/CodeJam/csharp/Solution/" + baseDir.Replace('\\', '/');
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
            internal string category;
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
            
            if ("slow".Equals(testData.category))
                    {
#if !INCLUDE_SLOW
                        return;
#endif
                    }
                    
            Scanner scanner = testData.scanner;
            List<string> checkStrList = testData.checkStrList;
            int testCases = testData.testCases;

            Directory.SetCurrentDirectory(baseDir);

            Stopwatch timer = Stopwatch.StartNew();

            Type mainType = Type.GetType(mainClassName, true);
            object main = Activator.CreateInstance(mainType);

            Regex regex = new Regex(@"(\..*)?$");
            string outputFileName = regex.Replace(testData.inputFileName, ".out", 1);
            List<String> answers = new List<string>();
            
            Logger.LogInfo("\nStarting Class {}\nProcess method {}.  InputFile {}\n",
                            mainType.FullName, processInputMethodName, 
                            testData.inputFileName
                            );

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

            Logger.LogInfo("\n\nClass {}\nTC {} \nProcess method {}.  InputFile {}\nTime {}\n\n",
                            mainType.FullName, testCases, processInputMethodName, 
                            testData.inputFileName,
                            timeSpanStr);

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
        	Logger.LogInfo("ReadTestFileCases");
        	
            testList = new List<MainTestData>();

            XElement po = XElement.Load(setBaseDir(@"TestData.xml", false));

            foreach (XElement testFileRunner in po.Elements("testFileRunner"))
            {
                string baseDir = getAttributeValue(testFileRunner, "basedir");
                
                string autotest = getAttributeValue(testFileRunner, "autotest");

                baseDir =  setBaseDir(baseDir);
                
                Logger.LogDebug("base dir {}", baseDir);

                                 	
				List<MainTestData> explicitCases = new List<MainTestData>();

                foreach (XElement run in testFileRunner.Elements("run"))
                {
                    string mainClassName = getAttributeValue(run, "className");

                    string inputFileName = getAttributeValue(run, "inputFile");
                    string checkFileName = getAttributeValue(run, "checkFile");
                    string inputMethodName = getAttributeValue(run, "createInputMethod");
                    string processInputMethodName = getAttributeValue(run, "processInputMethod");

                    
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
                            category = getAttributeValue(run, "category"),
                            testName = getAttributeValue(run, "testName"),
                            testDescription = string.Format("Class {0}\nMethod {1} \ninput file {2}",
                            mainClassName, processInputMethodName, inputFileName)
                        });

						explicitCases.Add(testList[testList.Count-1]);

                    }

                }
                
                if ("true".Equals(autotest)) {
                	getAutoTests(baseDir, testFileRunner, explicitCases);
                }
            }


        }
        
        private void getAutoTests(string baseDir, XElement testFileRunner, List<MainTestData> explicitCases)
        {
        	
	
        	Logger.LogInfo("get auto tests {}  {}", baseDir, 
        		explicitCases.Select(td => td.mainClassName).ToCommaString());
			string nsPrefix = getAttributeValue(testFileRunner, "namespacePrefix");
			string assemblyName = getAttributeValue(testFileRunner, "assemblyName");
			
			Assembly assembly = Assembly.Load(assemblyName);
			
			Preconditions.checkState(assembly != null, "Assembly null");
			Preconditions.checkState(assembly.GetTypes() != null);
			
			//Logger.LogInfo("nsPrefix {} assemblyName {}", nsPrefix, assemblyName);
			//Logger.LogInfo("Types {}", assembly.GetTypes().Count());
			Type[] types = assembly.GetTypes().Where
			(t => (t.Namespace ?? "").StartsWith(nsPrefix)).OrderBy(t => t.Namespace).ToArray();
			
			
			
			foreach(Type type in types)
			{
				MethodInfo m = type.GetMethod("createInput");
				
				if (m == null)
					continue;
				
				string mainClassName = type.AssemblyQualifiedName;

				Logger.LogTrace("Type {} method {} mainClass {}", type, m, mainClassName);	
				
				Regex regex = new Regex(@".*Problem(\d).*");
				string letter = "" + (char) ('A' + int.Parse(regex.Match(type.Namespace).Groups[1].Value) - 1);
				
				foreach(string inputFileName in
					new DirectoryInfo(baseDir).GetFiles(letter + "*.in").Select(fi => fi.Name))
				
				{
					//string inputFileName = letter + "-small-practice.in";
					string checkFileName = new Regex(@"\.in$").Replace(inputFileName, ".correct");
					string inputMethodName = "createInput";
					string processInputMethodName = "processInput";

					if (explicitCases.Any( td =>
						
							 td.mainClassName.StartsWith(type.FullName)
							&& td.inputFileName.Equals(inputFileName) 
							&& td.processInputMethodName.Equals(processInputMethodName)
							
						))
					{
						Logger.LogInfo("Skipping {} {} {} {}",
							type.FullName,
							processInputMethodName,
							inputFileName,
							inputMethodName);
						continue;
					}
					
					
					TextReader inputReader = File.OpenText(inputFileName);
					Scanner scanner = new Scanner(inputReader);
					using (TextReader checkReader = File.OpenText(checkFileName))
					{
						int testCases = scanner.nextInt();

						//Logger.LogInfo("Begin testing class {} method {} testcases {}",
						//  mainClassName, processInputMethodName, testCases);


						List<string> checkStrs = new List<string>();
						
						PeekingStreamReader peekSR = new PeekingStreamReader(checkReader);
						
						for (int tc = 1; tc <= testCases; ++tc)
						{
							StringBuilder sb = new StringBuilder();
							string line;
							//while (true // 
								//&& (string line = peekSR.ReadLine()) != null )
							while( (line = peekSR.ReadLine()) != null)
							{
								sb.Append(line);
								sb.Append("\n");
								if (new Regex(@"Case #" + (tc+1)).Match(peekSR.PeekReadLine() ?? "").Success)
								{
									break;
								}
								
							}
							
							sb.Length --;
							
							//sb.RemoveAt(sb.Count() - 1);
							checkStrs.Add(sb.ToString());
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
