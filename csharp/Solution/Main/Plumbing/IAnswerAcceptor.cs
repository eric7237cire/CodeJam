using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeJam.Main
{
    public interface IAnswerAcceptor
    {
        void Accept<T>(T i);

        String GetAnswer(int testCase);
    }

    public class AnswerAcceptor : IAnswerAcceptor
    {
        List<String> answers;

        public AnswerAcceptor()
        {
            answers = new List<string>();
        }
        public void Accept<T>(T ans)
        {
            string line = String.Format("Case #{0}: {1}", answers.Count+1, ans);

            answers.Add(line);
        }

        public string GetAnswer(int testCase)
        {
            return answers[testCase - 1];
        }
    }
}
