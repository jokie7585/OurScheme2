package PL109_10627238;

public class Main {
  public static int suTestNum;
  
  public static void main( String[] args ) throws Throwable {
    try {
      System.out.println( "Welcome to OurScheme!" );
      System.out.println( "" );
      System.out.print( "> " );
      
      while ( true ) {
        try {
          Node sexpNode = Interpreter.ReadSexp();
          
          sexpNode = OurSchemVM.Get_Instance().Apply( sexpNode );
          
          // Interpreter.Printer( sexpNode, 1 );
          Interpreter.NewPrinter( sexpNode );
          
          MyScanner.Get_Instance().FinishReset();
          System.out.println( "" );
          System.out.print( "> " );
        } // tru
        catch ( NoclosingQuoteError e ) {
          System.out.println( e.Get_Msg() );
          System.out.println( "" );
          MyScanner.Get_Instance().ErrorReset();
        } // catch
        catch ( UnexpectedError e ) {
          System.out.println( e.Get_Msg() );
          System.out.println( "" );
          MyScanner.Get_Instance().ErrorReset();
        } // catch
        catch ( EvaluatingError e ) {
          System.out.println( e.Get_Msg() );
          System.out.println( "" );
          OurSchemVM.Get_Instance().mCallStack.Exception_Process();
          MyScanner.Get_Instance().FinishReset();
          System.out.print( "> " );
        } // catch
        catch ( IncorrectArgNumError e ) {
          System.out.println( e.Get_Msg() );
          System.out.println( "" );
          OurSchemVM.Get_Instance().mCallStack.Exception_Process();
          MyScanner.Get_Instance().FinishReset();
          System.out.print( "> " );
        } // catch
        catch ( ListError e ) {
          System.out.print( e.Get_Msg() );
          Interpreter.NewPrinter( OurSchemVM.Get_Instance().Get_FailedList() );
          System.out.println( "" );
          OurSchemVM.Get_Instance().mCallStack.Exception_Process();
          MyScanner.Get_Instance().FinishReset();
          System.out.print( "> " );
        } // catch
        catch ( MainSexpError e ) {
          System.out.print( e.Get_Msg() );
          Interpreter.NewPrinter( OurSchemVM.Get_Instance().Get_FailedMainSexp() );
          System.out.println( "" );
          OurSchemVM.Get_Instance().mCallStack.Exception_Process();
          MyScanner.Get_Instance().FinishReset();
          System.out.print( "> " );
        } // catch
        catch ( VerboseException e ) {
          MyScanner.Get_Instance().FinishReset();
          System.out.println( "" );
          System.out.print( "> " );
        } // catch
      } // while
      
    } // try
    catch ( EOFEncounterError e ) {
      System.out.println( e.Get_Msg() );
      System.out.println( "Thanks for using OurScheme!" );
      
    } // catch
    catch ( FinishProgramException e ) {
      System.out.println( "" );
      System.out.println( "Thanks for using OurScheme!" );
      
    } // catch
    
  } // main()
  
} // class Main
