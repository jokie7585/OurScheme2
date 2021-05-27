package PL109_10627238;

import java.util.Vector;

public class Interpreter {
  private static Token sTmpToken;
  private static int sPrintLevel = 0;
  
  public static Node ReadSexp() throws Throwable {
    Node rootNode = NewFindExp();
    return rootNode;
  } // ReadSexp()
  
  private static Node NewFindExp() throws Throwable {
    Token tmp = PeekNextToken();
    
    if ( tmp.mType == Symbol.sQUOTE ) {
      ComfirmNextToken();
      Node retNode = new Node();
      retNode.Add_LeftChild( Node.Generate_Quote() );
      retNode.Add_NoDot_Child( NewFindExp() );
      return retNode;
      
    } // if
    else if ( tmp.mType == Symbol.sL_PAREN ) {
      // check the left PAREN
      ComfirmNextToken();
      // create new tree
      Node newSexp = new Node();
      // find first sEXP
      Node retNode = null;
      Node bones = null;
      
      PeekNextToken();
      if ( sTmpToken.mType != Symbol.sR_PAREN ) {
        retNode = NewFindExp();
        bones = newSexp.Add_LeftChild( retNode );
        
        boolean is_Skip = false;
        while ( retNode != null && !is_Skip ) {
          tmp = PeekNextToken();
          
          if ( tmp.mType == Symbol.sDOT || tmp.mType == Symbol.sR_PAREN ) {
            is_Skip = true;
          } // if
          else {
            retNode = NewFindExp();
            bones = bones.Add_NoDot_Child( retNode );
          } // else
          
        } // while
        
        tmp = PeekNextToken();
        if ( tmp.mType == Symbol.sDOT ) {
          ComfirmNextToken();
          retNode = NewFindExp();
          bones.Add_Dot_Child( retNode );
        } // if
      } // if
      
      tmp = PeekNextToken();
      if ( tmp.mType == Symbol.sR_PAREN ) {
        ComfirmNextToken();
        if ( newSexp.Is_Nil() ) {
          return new Node( new Token( "()", Symbol.sNIL ) );
        } // if
        
        return newSexp;
      } // if
      else {
        ComfirmNextToken();
        Vector<String> expects = new Vector<String>();
        expects.add( "')'" );
        int line = MyScanner.Get_Instance().CurLine();
        int col = MyScanner.Get_Instance().PreTokenCol();
        throw new UnexpectedError( expects, tmp, line, col );
      } // else
      
    } // else if
    else if ( tmp.mType == Symbol.sFLOAT || tmp.mType == Symbol.sINT || tmp.mType == Symbol.sNIL
        || tmp.mType == Symbol.sT || tmp.mType == Symbol.sSTRING || tmp.mType == Symbol.sSYMBOL ) {
      Node node = new Node( tmp );
      ComfirmNextToken();
      return node;
    } // else if
    else {
      ComfirmNextToken();
      Vector<String> expects = new Vector<String>();
      expects.add( "atom" );
      expects.add( "'('" );
      int line = MyScanner.Get_Instance().CurLine();
      int col = MyScanner.Get_Instance().PreTokenCol();
      throw new UnexpectedError( expects, tmp, line, col );
    } // else
  } // NewFindExp()
  
  public static void NewPrinter( Node root ) throws Throwable {
    if ( root == null ) {
      System.out.println( "null in printer" );
      return;
    } // if
    
    if ( root.Is_Dot() ) {
      NewSubprinter( root.Get(), 0 + 1 );
    } // if
    else {
      // print atom
      System.out.print( Evaluate( root.Get().mToken ) );
      
    } // else
    
  } // NewPrinter()
  
  private static void NewSubprinter( Node root, int base ) throws Throwable {
    if ( root.mL_Child.Is_Dot() ) {
      System.out.print( "( " );
      sPrintLevel++;
      NewSubprinter( root.mL_Child.Get(), base + 1 );
      
      // print through bone
      Node boneNode = root.mR_Child;
      
      while ( boneNode != null ) {
        boneNode = boneNode.Get();
        
        if ( boneNode.mL_Child != null ) {
          if ( boneNode.mL_Child.Is_Dot() ) {
            
            // print inner indent
            System.out.print( IndentGenerator( base ) );
            NewSubprinter( boneNode.mL_Child.Get(), base + 1 );
            
          } // if
          else if ( boneNode.mL_Child == null ) {
            throw new EvaluatingError( "Warning", "shuold not have the exception" );
          } // else if
          else {
            // print a dot pair
            System.out.println( IndentGenerator( base ) + Evaluate( boneNode.mL_Child.Get().mToken ) );
            
          } // else
        } // if
        else {
          if ( boneNode.mR_Child == null && !boneNode.Is_Nil() ) {
            System.out.println( IndentGenerator( base ) + "." );
            System.out.println( IndentGenerator( base ) + Evaluate( boneNode.Get().mToken ) );
          } // if
          else {
            // do nothing
          } // else
        } // else
        
        boneNode = boneNode.mR_Child;
      } // while
      
      sPrintLevel--;
      
      if ( sPrintLevel == 0 ) {
        System.out.print( IndentGenerator( base - 1 ) + ")" );
      } // if
      else {
        System.out.println( IndentGenerator( base - 1 ) + ")" );
      } // else
      
    } // if
    else if ( root.mL_Child == null ) {
      throw new EvaluatingError( "Warning", "shuold not have the exception" );
    } // else if
    else {
      
      // print start quote
      System.out.print( "( " );
      // print first node
      sPrintLevel++;
      
      System.out.println( Evaluate( root.mL_Child.Get().mToken ) );
      // print through bone
      Node boneNode = root.mR_Child;
      
      while ( boneNode != null ) {
        boneNode = boneNode.Get();
        
        if ( boneNode.mL_Child != null ) {
          if ( boneNode.mL_Child.Is_Dot() ) {
            // print inner indent
            System.out.print( IndentGenerator( base ) );
            NewSubprinter( boneNode.mL_Child.Get(), base + 1 );
          } // if
          else if ( boneNode.mL_Child == null ) {
            throw new EvaluatingError( "Warning", "shuold not have the exception" );
          } // else if
          else {
            // print a dot pair
            System.out.println( IndentGenerator( base ) + Evaluate( boneNode.mL_Child.Get().mToken ) );
            
          } // else
        } // if
        else {
          if ( boneNode.mR_Child == null && !boneNode.Is_Nil() ) {
            System.out.println( IndentGenerator( base ) + "." );
            System.out.println( IndentGenerator( base ) + Evaluate( boneNode.Get().mToken ) );
          } // if
          else {
            // do nothing
          } // else
        } // else
        
        boneNode = boneNode.mR_Child;
      } // while
      
      sPrintLevel--;
      
      // print end quote
      if ( sPrintLevel == 0 ) {
        System.out.print( IndentGenerator( base - 1 ) + ")" );
      } // if
      else {
        System.out.println( IndentGenerator( base - 1 ) + ")" );
      } // else
      
    } // else
  } // NewSubprinter()
  
  private static String IndentGenerator( int level ) {
    int indentCounter = level * 2;
    StringBuffer indent = new StringBuffer();
    for ( int i = 0 ; i < indentCounter ; i++ ) {
      indent.append( " " );
    } // for
    
    return indent.toString();
  } // IndentGenerator()
  
  private static Token PeekNextToken() throws Throwable {
    if ( sTmpToken == null ) {
      sTmpToken = MyScanner.Get_Instance().Next();
      // System.out.println( "read in : " + sTmpToken.mContent );
      return sTmpToken;
    } // if
    
    return sTmpToken;
  } // PeekNextToken()
  
  private static void ComfirmNextToken() {
    sTmpToken = null;
  } // ComfirmNextToken()
  
  public static String Evaluate( Token token ) {
    if ( token.mType == Symbol.sINT ) {
      return Integer.toString( Integer.parseInt( token.mContent ) );
    } // if
    else if ( token.mType == Symbol.sFLOAT ) {
      float temp = Float.parseFloat( token.mContent ) * 1000;
      boolean isminus = false;
      int round = 0;
      
      if ( temp >= 0 ) {
        
      } // if
      else {
        temp = temp * -1;
        isminus = true;
      } // else
      
      while ( round < temp ) {
        round = round + 1;
        
      } // while
      
      float up = round - temp;
      float down = temp - ( round - 1 );
      
      String result;
      
      if ( up > down ) {
        result = Integer.toString( round - 1 );
      } // if
      else {
        result = Integer.toString( round );
      } // else
      
      while ( result.length() < 4 ) {
        result = "0" + result;
        
      } // while
      
      if ( isminus ) {
        result = "-" + result;
      } // if
      
      result = result.substring( 0, result.length() - 3 ) + "." + result.substring( result.length() - 3 );
      
      return result;
    } // else if
    else if ( token.mType == Symbol.sSYMBOL || token.mType == Symbol.sSTRING
        || token.mType == Symbol.sERROR ) {
      return token.mContent;
    } // else if
    else if ( token.mType == Symbol.sSYMBOL_LEXICAL ) {
      return token.mContent.substring( 2 );
    } // else if
    else if ( token.mType == Symbol.sNIL ) {
      return "nil";
    } // else if
    else if ( token.mType == Symbol.sT ) {
      return "#t";
    } // else if
    else if ( token.mType == Symbol.sPROCEDUREL ) {
      if ( token.mContent.charAt( 0 ) == '(' ) {
        return "#<procedure " + token.mContent.substring( 1, token.mContent.length() - 1 ) + ">";
      } // if
      else if ( token.mContent.charAt( 0 ) == ')' ) {
        return "#<procedure " + token.mContent.substring( 2 ) + ">";
      } // else if
      else {
        return "#<procedure " + token.mContent + ">";
      } // else
      
    } // else if
    else if ( token.mType == Symbol.sEMPTYOBJ ) {
      return "";
    } // else if
    else if ( token.mType == Symbol.sQUOTE ) {
      return "quote";
    } // else if
    
    return null;
  } // Evaluate()
  
} // class Interpreter

class Node {
  public Node mL_Child;
  public Node mR_Child;
  public Token mToken;
  public Vector<BindingTB> mScope;
  
  public static Node Generate_Empty() {
    return new Node( new Token( "", Symbol.sEMPTYOBJ ) );
  } // Generate_Empty()
  
  public static Node Generate_String( String string ) {
    return new Node( new Token( string, Symbol.sSTRING ) );
  } // Generate_String()
  
  public static Node Generate_Error( String string ) {
    return new Node( new Token( string, Symbol.sERROR ) );
  } // Generate_String()
  
  public static Node Generate_Quote() {
    return new Node( new Token( "quote", Symbol.sSYMBOL ) );
  } // Generate_Quote()
  
  public Node() {
    mToken = new Token( ".", Symbol.sDOT );
    mScope = new Vector<BindingTB>();
  } // Node()
  
  public Node( Token token ) {
    mToken = token;
    mScope = new Vector<BindingTB>();
  } // Node()
  
  public Node Get() {
    return this;
  } // Get()
  
  public Node Get_R_C() {
    return this.mR_Child;
  } // Get_R_C()
  
  public Node Get_L_C() {
    return this.mL_Child;
  } // Get_L_C()
  
  public String Get_Symbol() {
    return mToken.mContent;
  } // Get_Symbol()
  
  public void Push_Scope( BindingTB tb ) {
    mScope.add( tb );
  } // Push_Scope()
  
  public void Pop_Scope() {
    mScope.removeElementAt( mScope.size() - 1 );
  } // Pop_Scope()
  
  public boolean Is_Dot() {
    if ( Get().mToken.mType == Symbol.sDOT ) {
      return true;
    } // if
    
    return false;
  } // Is_Dot()
  
  public boolean Is_Nil() {
    if ( ( Get().mToken.mType == Symbol.sDOT && Get().mL_Child == null )
        || Get().mToken.mType == Symbol.sNIL ) {
      return true;
    } // if
    
    return false;
  } // Is_Nil()
  
  public boolean Is_T() {
    if ( Get().mToken.mType == Symbol.sT ) {
      return true;
    } // if
    
    return false;
  } // Is_T()
  
  public boolean Is_Quote() {
    if ( mToken.mType == Symbol.sQUOTE ) {
      return true;
    } // if
    
    return false;
  } // Is_Quote()
  
  public Node Add_LeftChild( Node node ) throws Throwable {
    
    if ( mToken.mType != Symbol.sDOT ) {
      throw new EvaluatingError( "Foundamental error", "Must fix" );
    } // if
    
    mL_Child = node;
    return this;
  } // Add_LeftChild()
  
  public Node Add_NoDot_Child( Node node ) throws Throwable {
    if ( mToken.mType != Symbol.sDOT ) {
      throw new EvaluatingError( "Foundamental error", "Must fix" );
    } // if
    
    mR_Child = new Node();
    mR_Child.mL_Child = node;
    return mR_Child;
  } // Add_NoDot_Child()
  
  public Node Add_Dot_Child( Node node ) throws Throwable {
    if ( mToken.mType != Symbol.sDOT ) {
      throw new EvaluatingError( "Foundamental error", "Must fix" );
    } // if
    
    mR_Child = node;
    return this;
  } // Add_Dot_Child()
  
} // class Node