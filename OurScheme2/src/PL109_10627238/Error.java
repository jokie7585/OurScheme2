package PL109_10627238;

import java.util.Vector;

public class Error extends Throwable {
  
  public String mTypeString;
  public String mMsgString;
  
  public Error( String type ) {
    mTypeString = type;
  } // Error()
  
  public Error( String type, String msg ) {
    mTypeString = type;
    mMsgString = msg;
  } // Error()
  
  public String Get_Msg() {
    StringBuffer tmpBuffer = new StringBuffer();
    if ( !mMsgString.equals( "" ) ) {
      tmpBuffer.append( "ERROR (" );
      tmpBuffer.append( mTypeString );
      tmpBuffer.append( ") : " );
      tmpBuffer.append( mMsgString );
    } // if
    else {
      tmpBuffer.append( "ERROR (" );
      tmpBuffer.append( mTypeString );
      tmpBuffer.append( ")" );
    } // else
    
    return tmpBuffer.toString();
  } // Get_Msg()
  
  public void Set_Msg( String msg ) {
    mMsgString = msg;
  } // Set_Msg()
  
} // class Error

class NoclosingQuoteError extends Error {
  public NoclosingQuoteError( int Line, int Col ) {
    super( "no closing quote" );
    
    // create message
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "END-OF-LINE encountered at Line " );
    tmpBuffer.append( Line );
    tmpBuffer.append( " Column " );
    tmpBuffer.append( Col );
    this.Set_Msg( tmpBuffer.toString() );
  } // NoclosingQuoteError()
  
} // class NoclosingQuoteError

class UnexpectedError extends Error {
  public UnexpectedError( Vector<String> expects, Token token, int line, int col ) {
    super( "unexpected token" );
    
    StringBuffer tmpBuffer = new StringBuffer();
    for ( int i = 0 ; i < expects.size() ; i++ ) {
      if ( i == 0 ) {
        tmpBuffer.append( expects.elementAt( i ) );
      } // if
      else {
        tmpBuffer.append( " or " );
        tmpBuffer.append( expects.elementAt( i ) );
      } // else
      
    } // for
    
    tmpBuffer.append( " expected when token at Line " );
    tmpBuffer.append( line );
    tmpBuffer.append( " Column " );
    tmpBuffer.append( col );
    tmpBuffer.append( " is >>" );
    tmpBuffer.append( token.mContent );
    tmpBuffer.append( "<<" );
    this.Set_Msg( tmpBuffer.toString() );
  } // UnexpectedError()
} // class UnexpectedError

class EOFEncounterError extends Error {
  public EOFEncounterError() {
    super( "no more input" );
    
    this.Set_Msg( "END-OF-FILE encountered" );
  } // EOFEncounterError()
} // class EOFEncounterError

class FinishProgramException extends Error {
  public FinishProgramException() {
    super( "End programe" );
    
  } // FinishProgramException()
} // class FinishProgramException

// project2 error Evaluate Error

class EvaluatingError extends Error {
  public EvaluatingError( String type, String msg ) {
    super( type, msg );
  } // EvaluatingError()
} // class EvaluatingError

class UnboundError extends EvaluatingError {
  public UnboundError( String symbol ) {
    super( "unbound symbol", symbol );
  } // UnboundError()
} // class UnboundError

class ApplyNonFunction extends EvaluatingError {
  public ApplyNonFunction( String symbol ) {
    super( "attempt to apply non-function", symbol );
  } // ApplyNonFunction()
} // class ApplyNonFunction

class ListError extends Error {
  private Node mSexpNode;
  
  public ListError( String type, Node Sexp ) {
    super( type, "" );
    mSexpNode = Sexp;
    
  } // EvaluatingError()
  
  public void Set_MainSexp( Node mainSexp ) {
    mSexpNode = mainSexp;
  } // Set_MainSexp()
  
  public String Get_Msg() {
    
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "ERROR (" );
    tmpBuffer.append( mTypeString );
    tmpBuffer.append( ") : " );
    return tmpBuffer.toString();
    
  } // Get_Msg()
  
  public Node Get_Sexp() {
    return mSexpNode;
  } // Get_Sexp()
} // class ListError

class OperationError extends ListError {
  
  public OperationError( String type, Node Sexp ) {
    super( type, Sexp );
  } // OperationError()
  
  public String Get_Msg() {
    
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "ERROR (" );
    tmpBuffer.append( mTypeString );
    tmpBuffer.append( " with incorrect argument type) : " );
    return tmpBuffer.toString();
    
  } // Get_Msg()
} // class OperationError

class PrimitiveRedefineError extends Error {
  public PrimitiveRedefineError() {
    super( "", "" );
  }
} // class PrimitiveRedefineError
