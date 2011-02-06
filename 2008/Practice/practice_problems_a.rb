testCases = STDIN.gets.to_i

#puts "Number of Test Cases: " + testCases.to_s

for testCase in 1..testCases 
  alienNumber, alienLanguage, targetLanguage = STDIN.gets.split;
  #alienLanguage = testCaseElements[1];
  #targetLanguage = testCaseElements[2];
  
  #puts "Alien number " + alienNumber
  #puts "Alien language " + alienLanguage
  #puts "Target language " + targetLanguage
  
  alBase = alienLanguage.size
  tlBase = targetLanguage.size
  
  alConverted = 0
  
  #First convert alienNumber
  for i in 0..alienNumber.length - 1
    #puts "Digit is " + alienNumber[i..i].to_s
    alDigit = alienNumber[i..i]
    alDigitValue = alienLanguage.index(alDigit).to_i
    #puts "Digit Value " + alDigitValue.to_s
    alConverted = alConverted + alDigitValue * alBase ** (alienNumber.length - i - 1)
  end

  
  
  #puts "Alien # converted is " + alConverted.to_s

  tlConverted = ""
  while alConverted > 0
    tlDigit = alConverted % tlBase
    #puts "tlDigit " + tlDigit.to_s
    tlConverted << targetLanguage[tlDigit..tlDigit]
    alConverted = alConverted / tlBase    
  end 

  tlConverted.reverse!
  puts "Case #" + testCase.to_s + ": " + tlConverted
  #puts "Target # converted is " + tlConverted.to_s
  
  #puts "Last alien lang char: " + alienLanguage[-1].to_s
  end