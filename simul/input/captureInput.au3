





Global $Paused
HotKeySet("{F3}", "TogglePause")
HotKeySet("{F4}", "Terminate")
;HotKeySet("d", "ShowMessage")  ;Shift-Alt-d
HotKeySet("{F2}", "TakeText")  ;Shift-Alt-d

;;;; Body of program would go here ;;;;
While 1
    Sleep(100)
WEnd
;;;;;;;;

Func TogglePause()
    $Paused = NOT $Paused
    While $Paused
        sleep(100)
        ToolTip('Script is "Paused"',0,0)
    WEnd
    ToolTip("Script is no longer paused", 0, 0)
EndFunc

Func Terminate()
	
	ToolTip('Script is "Done"',0,0)
    Exit 0
EndFunc

Func ShowMessage()
    MsgBox(4096,"","This is a message.")
EndFunc

Func TakeText()

Send("^a")
sleep(30)
Send("^c")
sleep(30)
Send("^c")

Local $text = ClipGet() 

if ( StringInStr($text, "_____________________________") = 0) then
MsgBox(0, "Clipboard contains:", $text)
else

Local $file = FileOpen("handshistory.txt", 129)
; Check if file opened for writing OK
If $file = -1 Then
    MsgBox(0, "Error", "Unable to open file.")
    Exit
EndIf
Local $a = FileWrite($file, $text)
FileWrite($file, @CRLF & "**" & @CRLF)
FileClose($file)

$a = Run("C:\codejam\codejam\simul\runsimul.bat", "C:\codejam\codejam\simul", @SW_MINIMIZE)
;MsgBox(0, "Error", $a)

endif


EndFunc

