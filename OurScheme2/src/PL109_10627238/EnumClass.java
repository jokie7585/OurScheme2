package PL109_10627238;

public class EnumClass {
  
} // class EnumClass

class Symbol {
  public String mLexicalValue;
  
  public Symbol( String type ) {
    mLexicalValue = type;
  } // Symbol()
  
  public static Symbol sL_PAREN = new Symbol( "sL_PAREN" );
  public static Symbol sR_PAREN = new Symbol( "sR_PAREN" );
  public static Symbol sINT = new Symbol( "sINT" );
  public static Symbol sSTRING = new Symbol( "sSTRING" );
  public static Symbol sDOT = new Symbol( "sDOT" );
  public static Symbol sFLOAT = new Symbol( "sFLOAT" );
  public static Symbol sNIL = new Symbol( "sNIL" );
  public static Symbol sT = new Symbol( "sT" );
  public static Symbol sQUOTE = new Symbol( "sQUOTE" );
  public static Symbol sSYMBOL = new Symbol( "sSYMBOL" );
  // project2 new symbol
  public static Symbol sSYMBOL_LEXICAL = new Symbol( "sSYMBOL_LEXICAL" );
  public static Symbol sPROCEDUREL = new Symbol( "sPROCEDUREL" );
  public static Symbol sEMPTYOBJ = new Symbol( "sEMPTYOBJ" );
  public static Symbol sBINDING = new Symbol( "sBINDING" );
  public static Symbol sMEMORYADDR = new Symbol( "sMEMORYADDR" );
  // project3 new symbol
  public static Symbol sBEGINECONTIMUE = new Symbol( "sBEGINECONTIMUE" );
  
} // class Symbol

// projecy2 enum below
class Primitive {
  public static String[] sPrimitive = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
  public static String[] sInnerFunc = { "atom?", "pair?", "list?", "null?", "integer?", "real?", "number?",
      "string?", "boolean?", "symbol?", "+", "-", "*", "/", "not", "and", "or", "cons", "list", "'", "quote",
      "define", "car", "cdr", ">", ">=", "<", "<=", "=", "string-append", "string>?", "string<?", "string=?",
      "eqv?", "equal?", "begin", "if", "cond", "clean-environment" };
  
  public static boolean Is_primitive( Token token ) {
    
    for ( int i = 0 ; i < sInnerFunc.length ; i++ ) {
      if ( sInnerFunc[ i ].equals( token.mContent ) ) {
        return true;
      } // if
    } // for
    
    for ( int i = 0 ; i < sPrimitive.length ; i++ ) {
      if ( sInnerFunc[ i ].equals( token.mContent ) ) {
        return true;
      } // if
    } // for
    
    if ( token.mType != Symbol.sSYMBOL ) {
      return true;
    } // if
    
    return false;
  } // Is_primitive()
} // class Primitive
