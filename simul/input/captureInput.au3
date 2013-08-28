Global $Paused
HotKeySet("{f3}", "TogglePause")
HotKeySet("{F4}", "Terminate")
;HotKeySet("d", "ShowMessage")  ;Shift-Alt-d
HotKeySet("{F2}", "TakeText")  ;Shift-Alt-d

;;;; Body of program would go here ;;;;
Local $a = 3
While 1
    Sleep(100)
	$a = $a + 1
	;ToolTip('Script is ' & $a,0,0)
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
sleep(260)
Send("^a")
sleep(260)
Send("^c")
sleep(260)

Local $text = ClipGet() 

ClipPut("")
Local $t = ClipPut(@HOUR & " " & @MIN & " " & @SEC & @CRLF)

if $t <> 1 then
MsgBox(0, "Clipput failed", $t)
endif

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
;FileWrite($file, @CRLF & "//" & @HOUR & " " & @MIN & " " & @SEC & @CRLF)
FileWrite($file, @CRLF & "**" & @CRLF)
FileClose($file)

sleep(60)

$a = Run("C:\codejam\codejam\simul\runsimul.bat", "C:\codejam\codejam\simul", @SW_MINIMIZE)
;MsgBox(0, "Error", $a)

endif


EndFunc

