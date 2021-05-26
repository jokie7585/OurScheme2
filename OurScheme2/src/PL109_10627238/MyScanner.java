package PL109_10627238;

import java.util.Scanner;

public class MyScanner {
  
  private static MyScanner sMyScanner_S;
  private Scanner mStdin;
  private int mLine;
  private boolean mIs_EOF;
  private InputLineProcessor mTokenStream;
  private Token mPreToken;
  
  public MyScanner() {
    mStdin = new Scanner( System.in );
    // init pal
    // read UtestNumber
    Main.suTestNum = mStdin.nextInt();
    // read line enter
    mStdin.nextLine();
    
    mIs_EOF = false;
    mLine = 0;
  } // MyScanner()
  
  public Token Next() throws Throwable {
    
    while ( mTokenStream == null || mTokenStream.Is_Empty() ) {
      ReadInALine();
    } // while
    
    Token tmpToken = mTokenStream.Next();
    while ( tmpToken == null ) {
      tmpToken = Next();
    } // while
    
    mPreToken = tmpToken;
    return tmpToken;
  } // Next()
  
  public static MyScanner Get_Instance() {
    if ( sMyScanner_S == null ) {
      sMyScanner_S = new MyScanner();
      return sMyScanner_S;
    } // if
    else {
      return sMyScanner_S;
    } // else
  } // Get_Instance()
  
  public void ErrorReset() throws Throwable {
    // delete remained line
    mTokenStream = null;
    mLine = 0;
    
  } // ErrorReset()
  
  public void FinishReset() throws Throwable {
    // if cur token stream not empty reset m line to 1
    // else reset to 0
    if ( mTokenStream != null ) {
      mTokenStream.ResetCol();
      if ( mTokenStream.Is_Empty() ) {
        mLine = 0;
      } // if
      else {
        mLine = 1;
      } // else
    } // if
    else {
      // if ErrorReset is called by ourschemeVM
      // do nothing
    } // else
    
  } // FinishReset()
  
  public int CurLine() {
    return mLine;
  } // CurLine()
  
  public int PreTokenCol() {
    return mTokenStream.PreToken_Col();
  } // PreTokenCol()
  
  public Token PreToken() {
    return mPreToken;
  } // PreToken()
  
  private void ReadInALine() throws Throwable {
    if ( mStdin.hasNext() ) {
      // read a line
      mTokenStream = new InputLineProcessor( mStdin.nextLine() );
      mLine++;
      
    } // if
    else {
      // hasNext will process the end of file character
      // so in PAL
      // if you dont init the mTokenStream
      // // parser will try to get the unsafe input
      // // because process not block by input when program run in back
      // // // (equal? 15 15)( . 5)<<
      if ( mIs_EOF == false ) {
        mIs_EOF = true;
      } // if
      else {
        throw new EOFEncounterError();
      } // else
      
    } // else
  } // ReadInALine()
  
} // class MyScanner
