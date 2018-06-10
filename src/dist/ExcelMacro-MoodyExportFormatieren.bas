Attribute VB_Name = "Modul1"
Sub SurveyExportFormatierung()
Attribute SurveyExportFormatierung.VB_Description = "Formatierung eines MoodyFX Survey Exports"
Attribute SurveyExportFormatierung.VB_ProcData.VB_Invoke_Func = "f\n14"
'
' SurveyExportFormatierung Makro
' Formatierung eines MoodyFX Survey Exports
'
' Tastenkombination: Strg+f
'
    Columns("A:A").Select
    Selection.TextToColumns Destination:=Range("A1"), DataType:=xlDelimited, _
        TextQualifier:=xlDoubleQuote, ConsecutiveDelimiter:=False, Tab:=False, _
        Semicolon:=False, Comma:=True, Space:=False, Other:=False, FieldInfo _
        :=Array(Array(1, 1), Array(2, 1), Array(3, 1), Array(4, 1)), TrailingMinusNumbers:= _
        True
    Columns("B:B").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Columns("C:C").Select
    Selection.Insert Shift:=xlToRight, CopyOrigin:=xlFormatFromLeftOrAbove
    Range("B2").Select
    ActiveCell.FormulaR1C1 = "=IF(ISBLANK(RC[-1]),"""",RC[-1]/86400000+25569)"
    Range("C2").Select
    ActiveCell.FormulaR1C1 = "=IF(ISBLANK(RC[-2]),"""",RC[-2]/86400000+25569)"
    Range("B2").Select
    Selection.AutoFill Destination:=Range("B2:B1000"), Type:=xlFillDefault
    Range("B2:B1000").Select
    Range("C2").Select
    Selection.AutoFill Destination:=Range("C2:C1000"), Type:=xlFillDefault
    Range("C2:C1000").Select
    Columns("B:B").Select
    Selection.NumberFormat = "m/d/yyyy"
    Columns("C:C").Select
    Selection.NumberFormat = "[$-F400]h:mm:ss AM/PM"
    Range("B1").Select
    ActiveCell.FormulaR1C1 = "Datum"
    Range("C1").Select
    ActiveCell.FormulaR1C1 = "Uhrzeit"
    Range("D1").Select
    ActiveCell.FormulaR1C1 = "Survey ID"
    Range("E1").Select
    ActiveCell.FormulaR1C1 = "Punkte"
    Range("F1").Select
    ActiveCell.FormulaR1C1 = "Standort"
    Columns("A:A").Select
    Selection.EntireColumn.Hidden = True
    Rows("1:1").Select
    Range("B1").Activate
    Selection.Font.Bold = True
    Range("E5").Select
End Sub
