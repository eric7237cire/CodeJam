Global $Paused
HotKeySet("{f3}", "TogglePause")
HotKeySet("{F4}", "StartPos")
;HotKeySet("d", "ShowMessage")  ;Shift-Alt-d
HotKeySet("{F5}", "EndPos")  ;Shift-Alt-d

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

Func StartPos()

Local $text = ClipGet() 

ToolTip('Wrote start pos  ' & $text,0,0)

Local $file = FileOpen("startpos.txt", 130)
; Check if file opened for writing OK
If $file = -1 Then
    MsgBox(0, "Error", "Unable to open file.")
    Exit
EndIf
Local $a = FileWrite($file, $text)
FileClose($file)




EndFunc

Func EndPos()

Local $text = ClipGet() 

ToolTip("Wrote end pos  " & $text,0,0)

Local $file = FileOpen("endpos.txt", 130)
; Check if file opened for writing OK
If $file = -1 Then
    MsgBox(0, "Error", "Unable to open file.")
    Exit
EndIf
Local $a = FileWrite($file, $text)
FileClose($file)




EndFunc

