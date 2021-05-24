package PL109_10627238;

import java.util.Vector;

public class Function {
  public static Node Generate_True() {
    Node tmpNode = new Node( new Token( "#t", Symbol.sT ) );
    
    return tmpNode;
  } // Generate_True()
  
  public static Node Generate_False() {
    Node tmpNode = new Node( new Token( "nil", Symbol.sNIL ) );
    
    return tmpNode;
  } // Generate_False()
  
  public static Node BeginContinue() {
    Node tmpNode = new Node( new Token( "))((", Symbol.sBEGINECONTIMUE ) );
    
    return tmpNode;
  } // BeginContinue()
} // class Function

class InnerFunction {
  
  public static Node Cons( Node left, Node right ) throws Throwable {
    
    Node dotNode = new Node();
    Node boneNode = dotNode.Add_LeftChild( left );
    boneNode = boneNode.Add_Dot_Child( right );
    return dotNode;
    
  } // Cons()
  
  public static Node List( Vector<Node> params ) throws Throwable {
    
    if ( params.size() == 0 ) {
      return new Node( new Token( "()", Symbol.sNIL ) );
    } // if
    else {
      Node dotNode = new Node();
      Node boneNode = dotNode;
      
      for ( int i = 0 ; i < params.size() ; i++ ) {
        
        if ( i == 0 ) {
          boneNode = boneNode.Add_LeftChild( params.elementAt( i ) );
        } // if
        else {
          boneNode = boneNode.Add_NoDot_Child( params.elementAt( i ) );
        } // else
        
      } // for
      
      return dotNode;
    } // else
    
  } // List()
  
  public static Node Car( Node Sexp ) throws Throwable {
    if ( Is_Pair( Sexp ).Is_T() ) {
      return Sexp.Get().Get_L_C();
    } // if
    else {
      OurSchemVM.Get_Instance().Set_FailedList( Sexp );
      throw new OperationError( "car" );
    } // else
    
  } // Car()
  
  public static Node Cdr( Node Sexp ) throws Throwable {
    if ( Is_Pair( Sexp ).Is_T() ) {
      if ( Sexp.Get().Get_R_C() == null ) {
        return new Node( new Token( "()", Symbol.sNIL ) );
      } // if
      else {
        return Sexp.Get().Get_R_C();
      } // else
      
    } // if
    else {
      OurSchemVM.Get_Instance().Set_FailedList( Sexp );
      throw new OperationError( "cdr" );
    } // else
    
  } // Cdr()
  
  public static Node Is_Atom( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Is_Pair( Sexp ).mToken.mType == Symbol.sT ) {
      return Function.Generate_False();
    } // if
    else {
      return Function.Generate_True();
    } // else
  } // Is_Atom()
  
  public static Node Is_List( Node Sexp ) throws Throwable {
    Sexp = Sexp.Get();
    // Interpreter.NewPrinter( Sexp );
    // check
    if ( Sexp.mToken.mType == Symbol.sDOT ) {
      
      // check last mR_Child is nil or null
      while ( Sexp.mR_Child != null ) {
        Sexp = Sexp.mR_Child;
      } // while
      
      if ( Sexp.Is_Dot() || Sexp.Is_Nil() || Sexp.mToken.mContent.equals( "()" ) ) {
        // if
        return Function.Generate_True();
      } // if
      
    } // if
    else {
      
      if ( Sexp.mToken.mContent.equals( "()" ) ) {
        return Function.Generate_True();
      } // if
      
      return Function.Generate_False();
      
    } // else
    
    return Function.Generate_False();
  } // Is_List()
  
  public static Node Is_Pair( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sDOT ) {
      return Function.Generate_True();
    } // if
    else {
      return Function.Generate_False();
      
    } // else
    
  } // Is_Pair()
  
  public static Node Is_Null( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp == null ) {
      return Function.Generate_True();
    } // if
    else {
      if ( Sexp.mToken.mType == Symbol.sNIL ) {
        return Function.Generate_True();
      } // if
    } // else
    
    return Function.Generate_False();
  } // Is_Null()
  
  public static Node Is_Integer( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sINT ) {
      return Function.Generate_True();
    } // if
    else {
      return Function.Generate_False();
    } // else
    
  } // Is_Integer()
  
  public static Node Is_Real( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sINT || Sexp.mToken.mType == Symbol.sFLOAT ) {
      return Function.Generate_True();
    } // if
    else {
      return Function.Generate_False();
    } // else
  } // Is_Real()
  
  public static Node Is_Number( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sINT || Sexp.mToken.mType == Symbol.sFLOAT ) {
      return Function.Generate_True();
    } // if
    else {
      return Function.Generate_False();
    } // else
  } // Is_Number()
  
  public static Node Is_String( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sSTRING ) {
      return Function.Generate_True();
    } // if
    else {
      return Function.Generate_False();
    } // else
  } // Is_String()
  
  public static Node Is__Boolean( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp == null ) {
      return Function.Generate_True();
    } // if
    else {
      if ( Sexp.mToken.mType == Symbol.sNIL || Sexp.mToken.mType == Symbol.sT ) {
        return Function.Generate_True();
      } // if
    } // else
    
    return Function.Generate_False();
  } // Is__Boolean()
  
  public static Node Is_symbol( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Sexp.mToken.mType == Symbol.sSYMBOL || Sexp.mToken.mType == Symbol.sSYMBOL_LEXICAL ) {
      return Function.Generate_True();
    } // if
    
    return Function.Generate_False();
  } // Is_symbol()
  
  public static Node Add( Vector<Node> params ) throws Throwable {
    Node resltNode = params.elementAt( 0 );
    for ( int i = 1 ; i < params.size() ; i++ ) {
      resltNode = Add( resltNode, params.elementAt( i ) );
    } // for
    
    return resltNode;
  } // Add()
  
  private static Node Add( Node op1, Node op2 ) throws Throwable {
    
    // check type
    if ( Is_Number( op1.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op1.Get() );
      throw new OperationError( "+" );
    } // if
    else if ( Is_Number( op2.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op2.Get() );
      throw new OperationError( "+" );
    } // else if
    
    // declare
    if ( op1.Get().mToken.mType == Symbol.sFLOAT ) {
      float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
      float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
      float result = n_op1 + n_op2;
      return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
    } // if
    else {
      if ( op2.Get().mToken.mType == Symbol.sFLOAT ) {
        float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
        float result = n_op1 + n_op2;
        return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
      } // if
      else {
        int n_op1 = Integer.parseInt( op1.Get().Get_Symbol() );
        int n_op2 = Integer.parseInt( op2.Get().Get_Symbol() );
        int result = n_op1 + n_op2;
        return new Node( new Token( Integer.toString( result ), Symbol.sINT ) );
      } // else
    } // else
    
  } // Add()
  
  public static Node Sub( Vector<Node> params ) throws Throwable {
    Node resltNode = params.elementAt( 0 );
    for ( int i = 1 ; i < params.size() ; i++ ) {
      resltNode = Sub( resltNode, params.elementAt( i ) );
    } // for
    
    return resltNode;
  } // Sub()
  
  private static Node Sub( Node op1, Node op2 ) throws Throwable {
    
    // check type
    if ( Is_Number( op1.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op1.Get() );
      throw new OperationError( "-" );
    } // if
    else if ( Is_Number( op2.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op2.Get() );
      throw new OperationError( "-" );
    } // else if
    
    // declare
    if ( op1.Get().mToken.mType == Symbol.sFLOAT ) {
      float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
      float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
      float result = n_op1 - n_op2;
      return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
    } // if
    else {
      if ( op2.Get().mToken.mType == Symbol.sFLOAT ) {
        float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
        float result = n_op1 - n_op2;
        return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
      } // if
      else {
        int n_op1 = Integer.parseInt( op1.Get().Get_Symbol() );
        int n_op2 = Integer.parseInt( op2.Get().Get_Symbol() );
        int result = n_op1 - n_op2;
        return new Node( new Token( Integer.toString( result ), Symbol.sINT ) );
      } // else
    } // else
    
  } // Sub()
  
  public static Node Mul( Vector<Node> params ) throws Throwable {
    Node resltNode = params.elementAt( 0 );
    for ( int i = 1 ; i < params.size() ; i++ ) {
      resltNode = Mul( resltNode, params.elementAt( i ) );
    } // for
    
    return resltNode;
  } // Mul()
  
  private static Node Mul( Node op1, Node op2 ) throws Throwable {
    
    // check type
    if ( Is_Number( op1.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op1.Get() );
      throw new OperationError( "*" );
    } // if
    else if ( Is_Number( op2.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op2.Get() );
      throw new OperationError( "*" );
    } // else if
    
    // declare
    if ( op1.Get().mToken.mType == Symbol.sFLOAT ) {
      float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
      float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
      float result = n_op1 * n_op2;
      return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
    } // if
    else {
      if ( op2.Get().mToken.mType == Symbol.sFLOAT ) {
        float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
        float result = n_op1 * n_op2;
        return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
      } // if
      else {
        int n_op1 = Integer.parseInt( op1.Get().Get_Symbol() );
        int n_op2 = Integer.parseInt( op2.Get().Get_Symbol() );
        int result = n_op1 * n_op2;
        return new Node( new Token( Integer.toString( result ), Symbol.sINT ) );
      } // else
    } // else
    
  } // Mul()
  
  public static Node Div( Vector<Node> params ) throws Throwable {
    Node resltNode = params.elementAt( 0 );
    for ( int i = 1 ; i < params.size() ; i++ ) {
      resltNode = Div( resltNode, params.elementAt( i ) );
    } // for
    
    return resltNode;
  } // Div()
  
  private static Node Div( Node op1, Node op2 ) throws Throwable {
    
    // check type
    if ( Is_Number( op1.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op1.Get() );
      throw new OperationError( "/" );
    } // if
    else if ( Is_Number( op2.Get() ).Is_Nil() ) {
      OurSchemVM.Get_Instance().Set_FailedList( op2.Get() );
      throw new OperationError( "/" );
    } // else if
    
    // declare
    if ( op1.Get().mToken.mType == Symbol.sFLOAT ) {
      float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
      float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
      if ( n_op2 == 0 ) {
        throw new EvaluatingError( "division by zero", "/" );
      } // if
      
      float result = n_op1 / n_op2;
      return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
    } // if
    else {
      if ( op2.Get().mToken.mType == Symbol.sFLOAT ) {
        float n_op1 = Float.parseFloat( op1.Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( op2.Get().Get_Symbol() );
        if ( n_op2 == 0 ) {
          throw new EvaluatingError( "division by zero", "/" );
        } // if
        
        float result = n_op1 / n_op2;
        return new Node( new Token( Float.toString( result ), Symbol.sFLOAT ) );
      } // if
      else {
        int n_op1 = Integer.parseInt( op1.Get().Get_Symbol() );
        int n_op2 = Integer.parseInt( op2.Get().Get_Symbol() );
        if ( n_op2 == 0 ) {
          throw new EvaluatingError( "division by zero", "/" );
        } // if
        
        int result = n_op1 / n_op2;
        return new Node( new Token( Integer.toString( result ), Symbol.sINT ) );
      } // else
    } // else
    
  } // Div()
  
  public static Node Not( Node Sexp ) {
    Sexp = Sexp.Get();
    
    if ( Is__Boolean( Sexp ).Is_T() ) {
      if ( Sexp.Is_T() ) {
        return Function.Generate_False();
      } // if
      else {
        return Function.Generate_True();
      } // else
    } // if
    else {
      return Function.Generate_False();
    } // else
    
  } // Not()
  
  public static boolean And_Is_next( Node Sexp ) {
    Sexp = Sexp.Get();
    if ( !Sexp.Is_Nil() ) {
      return true;
    } // if
    
    return false;
  } // And_Is_next()
  
  public static boolean Or_Is_next( Node Sexp ) {
    Sexp = Sexp.Get();
    if ( Sexp.Is_Nil() ) {
      return true;
    } // if
    
    return false;
  } // Or_Is_next()
  
  public static Node Larger( Vector<Node> params ) throws Throwable {
    
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_Number( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( ">" );
      } // if
      else {
        
        if ( Is_Number( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( ">" );
        } // if
        
        float n_op1 = Float.parseFloat( params.elementAt( i - 1 ).Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( params.elementAt( i ).Get().Get_Symbol() );
        
        if ( n_op1 <= n_op2 ) {
          return Function.Generate_False();
          
        } // if
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // Larger()
  
  public static Node Larger_equal( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_Number( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( ">=" );
      } // if
      else {
        
        if ( Is_Number( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( ">=" );
        } // if
        
        float n_op1 = Float.parseFloat( params.elementAt( i - 1 ).Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( params.elementAt( i ).Get().Get_Symbol() );
        
        if ( n_op1 < n_op2 ) {
          return Function.Generate_False();
          
        } // if
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // Larger_equal()
  
  public static Node Smaller( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_Number( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "<" );
      } // if
      else {
        
        if ( Is_Number( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "<" );
        } // if
        
        float n_op1 = Float.parseFloat( params.elementAt( i - 1 ).Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( params.elementAt( i ).Get().Get_Symbol() );
        
        if ( n_op1 >= n_op2 ) {
          return Function.Generate_False();
          
        } // if
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // Smaller()
  
  public static Node Smaller_equal( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_Number( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "<=" );
      } // if
      else {
        
        if ( Is_Number( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "<=" );
        } // if
        
        float n_op1 = Float.parseFloat( params.elementAt( i - 1 ).Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( params.elementAt( i ).Get().Get_Symbol() );
        
        if ( n_op1 > n_op2 ) {
          return Function.Generate_False();
          
        } // if
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // Smaller_equal()
  
  public static Node Equal( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_Number( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "=" );
      } // if
      else {
        
        if ( Is_Number( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "=" );
        } // if
        
        float n_op1 = Float.parseFloat( params.elementAt( i - 1 ).Get().Get_Symbol() );
        float n_op2 = Float.parseFloat( params.elementAt( i ).Get().Get_Symbol() );
        
        if ( n_op1 != n_op2 ) {
          return Function.Generate_False();
          
        } // if
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // Equal()
  
  public static Node String_Equal( Vector<Node> params ) throws Throwable {
    
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_String( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "string=?" );
      } // if
      else {
        
        if ( Is_String( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "string=?" );
        } // if
        
        String n_op1 = params.elementAt( i - 1 ).Get().Get_Symbol();
        String n_op2 = params.elementAt( i ).Get().Get_Symbol();
        
        if ( n_op1.compareTo( n_op2 ) != 0 ) {
          return Function.Generate_False();
          
        } // if
        
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // String_Equal()
  
  public static Node String_Larger( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_String( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "string>?" );
      } // if
      else {
        
        if ( Is_String( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "string>?" );
        } // if
        
        String n_op1 = params.elementAt( i - 1 ).Get().Get_Symbol();
        String n_op2 = params.elementAt( i ).Get().Get_Symbol();
        
        if ( n_op1.compareTo( n_op2 ) <= 0 ) {
          return Function.Generate_False();
          
        } // if
        
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // String_Larger()
  
  public static Node String_Smaller( Vector<Node> params ) throws Throwable {
    for ( int i = 1 ; i < params.size() ; i++ ) {
      
      // check type
      if ( Is_String( params.elementAt( i ).Get() ).Is_Nil() ) {
        OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
        throw new OperationError( "string<?" );
      } // if
      else {
        
        if ( Is_String( params.elementAt( i - 1 ).Get() ).Is_Nil() ) {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i - 1 ).Get() );
          throw new OperationError( "string<?" );
        } // if
        
        String n_op1 = params.elementAt( i - 1 ).Get().Get_Symbol();
        String n_op2 = params.elementAt( i ).Get().Get_Symbol();
        
        if ( n_op1.compareTo( n_op2 ) >= 0 ) {
          return Function.Generate_False();
          
        } // if
        
      } // else
    } // for
    
    return Function.Generate_True();
    
  } // String_Smaller()
  
  public static Node String_Append( Vector<Node> params ) throws Throwable {
    StringBuffer result = new StringBuffer();
    
    if ( params.elementAt( 0 ).Get().mToken.mType == Symbol.sSTRING ) {
      
      result.append( params.elementAt( 0 ).Get().Get_Symbol() );
      
      for ( int i = 1 ; i < params.size() ; i++ ) {
        if ( params.elementAt( i ).Get().mToken.mType == Symbol.sSTRING ) {
          // delete close quote
          result.deleteCharAt( result.length() - 1 );
          // append a string without open quote
          String tmpString = params.elementAt( i ).Get().Get_Symbol();
          result.append( tmpString.substring( 1, tmpString.length() ) );
        } // if
        else {
          OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( i ).Get() );
          throw new OperationError( "String-Append" );
        } // else
      } // for
      
      return new Node( new Token( result.toString(), Symbol.sSTRING ) );
    } // if
    else {
      OurSchemVM.Get_Instance().Set_FailedList( params.elementAt( 0 ).Get() );
      throw new OperationError( "String-Append" );
    } // else
    
  } // String_Append()
  
  public static Node ByPass( Node Sexp ) throws Throwable {
    // TODO bypass need to let all node be primitive
    
    // change a symbol to a string
    if ( Is_Atom( Sexp ).mToken.mType == Symbol.sT ) {
      if ( Sexp.mToken.mType == Symbol.sSYMBOL ) {
        Sexp.mToken.mType = Symbol.sSYMBOL_LEXICAL;
        Sexp.mToken.mContent = "))" + Sexp.mToken.mContent;
      } // if
    } // if
    else {
      Sequentialize( Sexp );
    } // else
    
    return Sexp;
  } // ByPass()
  
  public static void Exit() throws Throwable {
    throw new FinishProgramException();
  } // Exit()
  
  private static void Sequentialize( Node Sexp ) {
    // change all symbol to lexical value
    // TODO if String and lexical value is not difference
    
    if ( Sexp != null ) {
      
      if ( Sexp.Is_Dot() ) {
        
        Sequentialize( Sexp.mL_Child );
        Sequentialize( Sexp.mR_Child );
        
      } // if
      else {
        if ( Sexp.mToken.mType == Symbol.sSYMBOL ) {
          Sexp.mToken.mType = Symbol.sSYMBOL_LEXICAL;
          Sexp.mToken.mContent = "))" + Sexp.mToken.mContent;
        } // if
      } // else
      
    } // if
    
  } // Sequentialize()
} // class InnerFunction