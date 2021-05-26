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
  String tokenString;
  
  public UnexpectedError( Vector<String> expects, Token token, int line, int col ) {
    super( "unexpected token" );
    tokenString = token.mContent;
    
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

class IncorrectArgNumError extends Error {
  IncorrectArgNumError( String msg ) {
    super( "incorrect number of arguments", msg );
  } // IncorrectArgNumError()
} // class IncorrectArgNumError

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

class ApplyNonFunction extends ListError {
  public ApplyNonFunction() {
    super( "attempt to apply non-function" );
  } // ApplyNonFunction()
} // class ApplyNonFunction

class NoReturnValue extends ListError {
  public NoReturnValue() {
    super( "no return value" );
  } // NoReturnValue()
} // class NoReturnValue

class NoReturnParame extends ListError {
  public NoReturnParame() {
    super( "unbound parameter" );
  } // NoReturnParame()
} // class NoReturnParame

class UnboundCondition extends ListError {
  public UnboundCondition() {
    super( "unbound condition" );
  } // UnboundCondition()
} // class UnboundCondition

class UnboundTestCondition extends ListError {
  public UnboundTestCondition() {
    super( "unbound test-condition" );
  } // UnboundTestCondition()
} // class UnboundTestCondition

class DefineOrLetANoReturn extends ListError {
  // PALProject3 the d type
  public DefineOrLetANoReturn() {
    super( "no return value" );
  } // DefineOrLetANoReturn()
  
} // class DefineOrLetANoReturn

class EvaluateNonBinding extends ListError {
  // PALProject3 the e type
  public EvaluateNonBinding() {
    super( "no return value" );
  } // EvaluateNonBinding()
  
} // class EvaluateNonBinding

class ListError extends Error {
  
  public ListError( String type ) {
    super( type, "" );
    
  } // ListError()
  
  public String Get_Msg() {
    
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "ERROR (" );
    tmpBuffer.append( mTypeString );
    tmpBuffer.append( ") : " );
    return tmpBuffer.toString();
    
  } // Get_Msg()
  
} // class ListError

class FormatError extends ListError {
  public FormatError( String type ) {
    super( type );
  } // FormatError()
  
} // class FormatError

class MainSexpError extends Error {
  
  public MainSexpError( String type ) {
    super( type, "" );
    
  } // MainSexpError()
  
  public String Get_Msg() {
    
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "ERROR (" );
    tmpBuffer.append( mTypeString );
    tmpBuffer.append( ") : " );
    return tmpBuffer.toString();
    
  } // Get_Msg()
  
} // class MainSexpError

class OperationError extends ListError {
  
  public OperationError( String type ) {
    super( type );
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
  } // PrimitiveRedefineError()
  
} // class PrimitiveRedefineError

// project 3

class VerboseException extends Exception {
  public VerboseException() {
    
  } // VerboseException()
  
} // class VerboseException

// project 4 error
class ReadException extends Error {
  public ReadException( String token ) {
    super( "unexpected character" );
    StringBuffer tmpBuffer = new StringBuffer();
    
    tmpBuffer.append( " Line " );
    tmpBuffer.append( MyScanner.Get_Instance().CurLine() );
    tmpBuffer.append( " Column " );
    tmpBuffer.append( MyScanner.Get_Instance().PreTokenCol() );
    tmpBuffer.append( " '" );
    tmpBuffer.append( token.charAt( 0 ) );
    tmpBuffer.append( "'" );
    this.Set_Msg( "END-OF-FILE encountered" );
  } // ReadException()
} // class ReadException
