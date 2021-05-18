package PL109_10627238;

import java.util.Vector;

public class OurSchemVM {
  private static OurSchemVM sOurSchemVM_S;
  private Node mNowExcutingSexpNode;
  private Node mFailedList;
  public CallStack mCallStack;
  public Memory mMemory;
  
  public OurSchemVM() {
    
    // register inner Binding
    mCallStack = new CallStack();
    
    //
    mMemory = new Memory();
  } // OurSchemVM()
  
  public void Initial() {
    sOurSchemVM_S = new OurSchemVM();
  } // Initial()
  
  public static OurSchemVM Get_Instance() {
    if ( sOurSchemVM_S == null ) {
      sOurSchemVM_S = new OurSchemVM();
      return sOurSchemVM_S;
    } // if
    else {
      return sOurSchemVM_S;
    } // else
    
  } // Get_Instance()
  
  public boolean Is_CallStackTopLevel() {
    return mCallStack.Is_TopLevel();
  } // Is_CallStackTopLevel()
  
  public Node Get_FailedList() {
    return mFailedList;
  } // Get_FailedList()
  
  public void Set_FailedList( Node Sexp ) {
    mFailedList = Sexp;
  } // Set_FailedList()
  
  public Node Get_FailedMainSexp() {
    return mNowExcutingSexpNode;
  } // Get_FailedMainSexp()
  
  public Node Apply( Node Sexp ) throws Throwable {
    mNowExcutingSexpNode = Sexp;
    return Evaluate( Sexp );
  } // Apply()
  
  public Node Evaluate( Node Sexp ) throws Throwable {
    // if atom
    if ( InnerFunction.Is_Atom( Sexp ).mToken.mType == Symbol.sT ) {
      
      if ( Sexp.mToken.mType == Symbol.sSYMBOL ) {
        // if symbol
        
        Binding binding = mCallStack.Get_Binding( Sexp.mToken.mContent );
        if ( binding == null ) {
          throw new EvaluatingError( "unbound symbol", Sexp.mToken.mContent );
        } // if
        else {
          return binding.Get();
        } // else
      } // if
      else {
        // if pure atom
        // // include MemoriItem and primitive
        
        return Sexp;
      } // else
      
    } // if
    else {
      // if not atom
      // check if pure list
      if ( InnerFunction.Is_List( Sexp ).mToken.mType == Symbol.sT ) {
        return CallFunction( Sexp );
      } // if
      else {
        // TODO if non-list throw original List or Current List
        mFailedList = Sexp;
        throw new ListError( "non-list" );
        // throw new ListError( "non-list", "", mNowExcutingSexpNode );
      } // else
      
    } // else
    
  } // Evaluate()
  
  public Node CallFunction( Node Sexp ) throws Throwable {
    Node returnNode = null;
    Node functionBind = Evaluate( Sexp.mL_Child );
    
    String funcnName = functionBind.Get_Symbol();
    Node functionArgsSexp = Sexp.mR_Child;
    
    // first check primitive internal function
    
    if ( funcnName.equals( "atom?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "atom?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Atom( arg1 );
      mCallStack.Pop();
    } // if
    else if ( funcnName.equals( "pair?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "pair?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Pair( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "list?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "list?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_List( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "null?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "null?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Null( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "integer?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "integer?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Integer( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "real?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "real?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Real( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "number?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "number?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_Number( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_String( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "boolean?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "boolean?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is__Boolean( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "symbol?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "symbol?", 1, functionArgsSexp, false );
      Node arg1 = Evaluate( paremeters.elementAt( 0 ) );
      returnNode = InnerFunction.Is_symbol( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "+" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "+", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Add( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "-" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "-", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Sub( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "*" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "*", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Mul( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "/" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "/", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Div( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "not" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "not", 1, functionArgsSexp, false );
      Node evaluatedNode = Evaluate( paremeters.elementAt( 0 ) );
      
      returnNode = InnerFunction.Not( evaluatedNode );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "and" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "and", 2, functionArgsSexp, true );
      returnNode = Evaluate( paremeters.elementAt( 0 ) );
      
      for ( int i = 1 ; i < paremeters.size() ; i++ ) {
        
        if ( InnerFunction.And_Is_next( returnNode ) ) {
          returnNode = Evaluate( paremeters.elementAt( i ) );
        } // if
        else {
          // end for loop
          i = paremeters.size();
        } // else
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "or" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "or", 2, functionArgsSexp, true );
      
      returnNode = Evaluate( paremeters.elementAt( 0 ) );
      
      for ( int i = 1 ; i < paremeters.size() ; i++ ) {
        
        if ( InnerFunction.Or_Is_next( returnNode ) ) {
          returnNode = Evaluate( paremeters.elementAt( i ) );
        } // if
        else {
          // end for loop
          i = paremeters.size();
        } // else
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cons" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "cons", 2, functionArgsSexp, false );
      Node leftNode = Evaluate( paremeters.elementAt( 0 ) );
      Node rightNode = Evaluate( paremeters.elementAt( 1 ) );
      
      returnNode = InnerFunction.Cons( leftNode, rightNode );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "list" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "list", 0, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.List( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "quote" ) ) {
      // call by pass functions
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "quote", 1, functionArgsSexp, false );
      returnNode = InnerFunction.ByPass( paremeters.elementAt( 0 ) );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "define" ) ) {
      // no need to push a new call stack
      
      if ( mCallStack.Is_TopLevel() ) {
        try {
          Vector<Node> paremeters = ParseParemeter( "define", 2, functionArgsSexp, false );
          
          Node bindingValueNode = paremeters.elementAt( 1 );
          
          String bindTargetString = mCallStack.Set_Binding( paremeters.elementAt( 0 ), bindingValueNode );
          
          returnNode = Node.Generate_String( bindTargetString + " defined" );
        } // try
        catch ( ListError e ) {
          throw new MainSexpError( "DEFINE format" );
          
        } // catch
        catch ( PrimitiveRedefineError e ) {
          throw new MainSexpError( "DEFINE format" );
        } // catch
        
      } // if
      else {
        throw new EvaluatingError( "level of DEFINE", "" );
      } // else
      
    } // else if
    else if ( funcnName.equals( "car" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "car", 1, functionArgsSexp, false );
      Node param1 = Evaluate( paremeters.elementAt( 0 ) ).Get();
      Node result = InnerFunction.Car( param1 );
      
      if ( result == null ) {
        throw new EvaluatingError( "Foundamental error", "Must fix" );
      } // if
      else {
        returnNode = result;
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cdr" ) ) {
      // if rChild of first dot is null
      // // return a nil atom as result
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "cdr", 1, functionArgsSexp, false );
      Node param1 = Evaluate( paremeters.elementAt( 0 ) ).Get();
      Node result = InnerFunction.Cdr( param1 );
      
      if ( result == null ) {
        returnNode = Function.Generate_False();
      } // if
      else {
        returnNode = result;
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( ">" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( ">", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Larger( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( ">=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( ">=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Larger_equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "<" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "<", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Smaller( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "<=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "<=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Smaller_equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.Equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string-append" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string-append", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.String_Append( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string>?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string>?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.String_Larger( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string<?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string<?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.String_Smaller( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string=?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string=?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        evaluatedPram.add( Evaluate( paremeters.elementAt( i ) ) );
        
      } // for
      
      returnNode = InnerFunction.String_Equal( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "eqv?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "eqv?", 2, functionArgsSexp, false );
      Node param1 = Evaluate( paremeters.elementAt( 0 ) );
      Node param2 = Evaluate( paremeters.elementAt( 1 ) );
      
      if ( Eqv( param1, param2 ) ) {
        returnNode = Function.Generate_True();
      } // if
      else {
        returnNode = Function.Generate_False();
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "equal?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "equal?", 2, functionArgsSexp, false );
      Node param1 = Evaluate( paremeters.elementAt( 0 ) );
      Node param2 = Evaluate( paremeters.elementAt( 1 ) );
      
      if ( Equal( param1, param2 ) ) {
        returnNode = Function.Generate_True();
      } // if
      else {
        returnNode = Function.Generate_False();
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "if" ) ) {
      mCallStack.Push();
      Vector<Node> parems = ParseParemeter( "if", 2, functionArgsSexp, true );
      
      if ( parems.size() > 3 ) {
        throw new EvaluatingError( "incorrect number of arguments", "if" );
      } // if
      else {
        Node condition = Evaluate( parems.elementAt( 0 ) );
        
        if ( InnerFunction.And_Is_next( condition ) ) {
          returnNode = Evaluate( parems.elementAt( 1 ) );
        } // if
        else {
          if ( parems.size() == 3 ) {
            returnNode = Evaluate( parems.elementAt( 2 ) );
          } // if
          else {
            returnNode = Node.Generate_Empty();
          } // else
          
        } // else
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cond" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "cond", 1, functionArgsSexp, true );
      
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        if ( InnerFunction.Is_List( paremeters.elementAt( i ) ).Is_T() ) {
          Vector<Node> executSequence;
          // get the executing sequence
          try {
            executSequence = ParseParemeter( "", 2, paremeters.elementAt( i ), true );
          } // try
          catch ( Exception e ) {
            throw new MainSexpError( "COND format" );
          } // catch
          
          if ( i < paremeters.size() - 1 ) {
            // if true
            if ( InnerFunction.And_Is_next( Evaluate( executSequence.elementAt( 0 ) ) ) ) {
              for ( int j = 1 ; j < executSequence.size() ; j++ ) {
                returnNode = Evaluate( executSequence.elementAt( j ) );
              } // for
              
              // skip all
              i = paremeters.size();
            } // if
            else {
              // do nothing
            } // else
          } // if
          else {
            // check if keyword
            // // else
            if ( executSequence.elementAt( 0 ).Get_Symbol().compareTo( "else" ) == 0 ) {
              for ( int j = 1 ; j < executSequence.size() ; j++ ) {
                returnNode = Evaluate( executSequence.elementAt( j ) );
              } // for
            } // if
            else if ( InnerFunction.And_Is_next( Evaluate( executSequence.elementAt( 0 ) ) ) ) {
              for ( int j = 1 ; j < executSequence.size() ; j++ ) {
                returnNode = Evaluate( executSequence.elementAt( j ) );
              } // for
            } // else if
            else {
              if ( returnNode == null ) {
                throw new MainSexpError( "no return value" );
              } // if
            } // else
          } // else
          
        } // if
        else {
          throw new MainSexpError( "COND format" );
        } // else
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "begin" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "begin", 1, functionArgsSexp, true );
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        returnNode = Evaluate( paremeters.elementAt( i ) );
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "clean-environment" ) ) {
      // no need to push a new call stack
      
      if ( mCallStack.Is_TopLevel() ) {
        mCallStack.Init();
        returnNode = Node.Generate_String( "environment-cleaned" );
      } // if
      else {
        throw new EvaluatingError( "level of CLEAN-ENVIRONMENT", "" );
      } // else
    } // else if
    else if ( funcnName.equals( "exit" ) ) {
      // no need to push a new call stack
      
      if ( mCallStack.Is_TopLevel() ) {
        // check no argument
        ParseParemeter( "exit", 0, functionArgsSexp, false );
        InnerFunction.Exit();
        
        returnNode = Node.Generate_Empty();
      } // if
      else {
        throw new EvaluatingError( "level of EXIT", "" );
      } // else
      
    } // else if
    else {
      // call custom binding function
      
      if ( functionBind.Get().mToken.mType == Symbol.sPROCEDUREL ) {
        //
        mCallStack.Push();
        
        mCallStack.Pop();
      } // if
      else {
        mFailedList = Sexp;
        throw new ListError( "attempt to apply non-function" );
      } // else
      
    } // else
    
    if ( returnNode == null ) {
      throw new EvaluatingError( "Warning", "not process all evaluate exception or try to evaluate null" );
    } // if
    else {
      return returnNode;
    } // else
    
  } // CallFunction()
  
  private Node CustomFunctionCall( String functionName, Node Sexp ) {
    // push a call stack object
    
    // allocate function arguments as local variable
    
    return null;
  } // CustomFunctionCall()
  
  private Vector<Node> ParseParemeter( String fcName, int amount, Node PTree, boolean l ) throws Throwable {
    Vector<Node> paremeters = new Vector<Node>();
    
    while ( PTree != null ) {
      paremeters.add( PTree.mL_Child );
      // go down the bones
      PTree = PTree.mR_Child;
    } // while
    
    // check amount of arguments
    if ( l ) {
      if ( paremeters.size() >= amount ) {
        return paremeters;
      } // if
      else {
        throw new EvaluatingError( "incorrect number of arguments", fcName );
      } // else
      
    } // if
    else {
      if ( paremeters.size() != amount ) {
        throw new EvaluatingError( "incorrect number of arguments", fcName );
      } // if
      else {
        return paremeters;
      } // else
      
    } // else
    
  } // ParseParemeter()
  
  private boolean Eqv( Node param1, Node param2 ) throws Throwable {
    
    if ( param1.mToken.mType == Symbol.sBINDING && param1.mToken.mType == Symbol.sBINDING ) {
      
      if ( param1.Get().Is_Dot() || param2.Get().Is_Dot() || param1.Get().mToken.mType == Symbol.sPROCEDUREL
          || param2.Get().mToken.mType == Symbol.sPROCEDUREL ) {
        if ( param1.Get() == param2.Get() ) {
          return true;
        } // if
        else {
          return false;
        } // else
        
      } // if
      else {
        return Eqv( param1.Get(), param2.Get() );
      } // else
      
    } // if
    else {
      
      param1 = param1.Get();
      param2 = param2.Get();
      
      if ( InnerFunction.Is_Atom( param1 ).Is_T() && InnerFunction.Is_Atom( param2 ).Is_T() ) {
        
        if ( param1.mToken.mType == param2.mToken.mType ) {
          
          if ( param1.mToken.mType == Symbol.sBINDING ) {
            throw new EvaluatingError( "Warning", "un-dereference binding" );
          } // if
          else if ( InnerFunction.Is_Number( param1 ).Is_T() ) {
            if ( InnerFunction.Is_Integer( param1 ).Is_T() ) {
              int op1 = Integer.parseInt( param1.Get_Symbol() );
              int op2 = Integer.parseInt( param2.Get_Symbol() );
              
              if ( op1 == op2 ) {
                return true;
              } // if
              else {
                return false;
              } // else
            } // if
            else {
              
              float op1 = Float.parseFloat( param1.Get_Symbol() );
              float op2 = Float.parseFloat( param2.Get_Symbol() );
              
              if ( op1 == op2 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // else
            
          } // else if
          else if ( InnerFunction.Is_String( param1 ).Is_T() ) {
            
            if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
              return true;
            } // if
            else {
              return false;
            } // else
            
          } // else if
          else if ( InnerFunction.Is_symbol( param1 ).Is_T() ) {
            
            if ( param1.mToken.mType == Symbol.sSYMBOL ) {
              throw new EvaluatingError( "Warning", "unDereference symbol!" );
            } // if
            else {
              if ( param1.Get_Symbol().compareTo( param1.Get_Symbol() ) == 0 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // else
            
          } // else if
          else {
            throw new EvaluatingError( "Warning", "should not has the case" );
          } // else
          
        } // if
        else {
          return false;
        } // else
        
      } // if
      else {
        return false;
      } // else
      
    } // else
    
  } // Eqv()
  
  private boolean Equal( Node param1, Node param2 ) throws Throwable {
    
    if ( param1.mToken.mType == Symbol.sBINDING && param1.mToken.mType == Symbol.sBINDING ) {
      if ( param1.Get().mToken.mType == Symbol.sPROCEDUREL
          || param2.Get().mToken.mType == Symbol.sPROCEDUREL ) {
        if ( param1.Get() == param2.Get() ) {
          return true;
        } // if
        else {
          return false;
        } // else
        
      } // if
      else {
        return Equal( param1.Get(), param2.Get() );
      } // else
      
    } // if
    else {
      // not tow binding
      // // get the Sexp to compare
      
      param1 = param1.Get();
      param2 = param2.Get();
      
      if ( InnerFunction.Is_Atom( param1 ).Is_T() && InnerFunction.Is_Atom( param2 ).Is_T() ) {
        
        if ( param1.mToken.mType == param2.mToken.mType ) {
          
          if ( param1.mToken.mType == Symbol.sBINDING ) {
            throw new EvaluatingError( "Warning", "un-dereference binding" );
          } // if
          else if ( InnerFunction.Is_Number( param1 ).Is_T() ) {
            if ( InnerFunction.Is_Integer( param1 ).Is_T() ) {
              int op1 = Integer.parseInt( param1.Get_Symbol() );
              int op2 = Integer.parseInt( param2.Get_Symbol() );
              
              if ( op1 == op2 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // if
            else {
              
              float op1 = Float.parseFloat( param1.Get_Symbol() );
              float op2 = Float.parseFloat( param2.Get_Symbol() );
              
              if ( op1 == op2 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // else
            
          } // else if
          else if ( InnerFunction.Is_String( param1 ).Is_T() ) {
            
            if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
              return true;
            } // if
            else {
              return false;
            } // else
            
          } // else if
          else if ( InnerFunction.Is_symbol( param1 ).Is_T() ) {
            
            if ( param1.mToken.mType == Symbol.sSYMBOL ) {
              throw new EvaluatingError( "Warning", "unDereference symbol! param1: " + param1.Get_Symbol() );
            } // if
            else if ( param2.mToken.mType == Symbol.sSYMBOL ) {
              throw new EvaluatingError( "Warning", "unDereference symbol! param2: " + param2.Get_Symbol() );
            } // else if
            else {
              if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // else
            
          } // else if
          else {
            throw new EvaluatingError( "Warning", "should not has the case" );
          } // else
          
        } // if
        else {
          return false;
        } // else
        
      } // if
      else {
        // both not binding but
        // // there are one of param not a atom
        // // or both them are list
        if ( param1.Is_Dot() && param2.Is_Dot() ) {
          // if they are all dot
          // // fist process the first node of list
          if ( !param1.Is_Nil() && !param2.Is_Nil() ) {
            // System.out.println( "mL_Child hihi" );
            if ( Equal( param1.mL_Child, param2.mL_Child ) ) {
              // if left tree ok
              // // process the rest of node through the bone
              // System.out.println( "mL_Child equal" );
              if ( param1.mR_Child != null && param2.mR_Child != null ) {
                // System.out.println( "mR_Child hihi" );
                if ( Equal( param1.mR_Child, param2.mR_Child ) ) {
                  //
                  // System.out.println( "mR_Child equal" );
                  return true;
                } // if
                else {
                  return false;
                } // else
                
              } // if
              else {
                // check dot pair result
                if ( param1.mR_Child == null && param2.mR_Child == null ) {
                  return true;
                } // if
                else if ( param1.mR_Child == null && param2.mR_Child.Is_Nil() ) {
                  return true;
                } // else if
                else if ( param2.mR_Child == null && param1.mR_Child.Is_Nil() ) {
                  return true;
                } // else if
                else {
                  return false;
                } // else
                
              } // else
              
            } // if
            else {
              
              return false;
            } // else
            
          } // if
          else {
            throw new EvaluatingError( "Warning", "should not has the case" );
          } // else
          
        } // if
        else {
          return false;
        } // else
        
      } // else
    } // else
    
  } // Equal()
  
} // class OurSchemVM

class CallStack {
  private Vector<BindingTB> mStack;
  
  public CallStack() {
    Init();
  } // CallStack()
  
  public void Init() {
    mStack = new Vector<BindingTB>();
    mStack.add( new BindingTB() );
    
    // load inner symbol
    // Set_Binding_inner( "t", false, Function.Generate_True() );
    
    // load inner function
    Set_Binding_inner( "cons", true );
    Set_Binding_inner( "list", true );
    Set_Binding_inner( "quote", true );
    Set_Binding_inner( "'", true );
    Set_Binding_inner( "car", true );
    Set_Binding_inner( "cdr", true );
    Set_Binding_inner( "define", true );
    Set_Binding_inner( "atom?", true );
    Set_Binding_inner( "pair?", true );
    Set_Binding_inner( "list?", true );
    Set_Binding_inner( "null?", true );
    Set_Binding_inner( "integer?", true );
    Set_Binding_inner( "real?", true );
    Set_Binding_inner( "number?", true );
    Set_Binding_inner( "string?", true );
    Set_Binding_inner( "boolean?", true );
    Set_Binding_inner( "symbol?", true );
    Set_Binding_inner( "+", true );
    Set_Binding_inner( "-", true );
    Set_Binding_inner( "*", true );
    Set_Binding_inner( "/", true );
    Set_Binding_inner( "not", true );
    Set_Binding_inner( "and", true );
    Set_Binding_inner( "or", true );
    Set_Binding_inner( ">", true );
    Set_Binding_inner( ">=", true );
    Set_Binding_inner( "<", true );
    Set_Binding_inner( "<=", true );
    Set_Binding_inner( "=", true );
    Set_Binding_inner( "string-append", true );
    Set_Binding_inner( "string>?", true );
    Set_Binding_inner( "string<?", true );
    Set_Binding_inner( "string=?", true );
    Set_Binding_inner( "eqv?", true );
    Set_Binding_inner( "equal?", true );
    Set_Binding_inner( "if", true );
    Set_Binding_inner( "cond", true );
    Set_Binding_inner( "begine", true );
    Set_Binding_inner( "clean-environment", true );
  } // Init()
  
  public void Exception_Process() {
    while ( mStack.size() > 1 ) {
      mStack.removeElementAt( mStack.size() - 1 );
    } // while
    
  } // Exception_Process()
  
  public void Push() {
    // push a stack
    mStack.add( new BindingTB( mStack.elementAt( mStack.size() - 1 ) ) );
    
  } // Push()
  
  public void Pop() {
    // pop a stack
    mStack.removeElementAt( mStack.size() - 1 );
    
  } // Pop()
  
  public boolean Is_TopLevel() {
    if ( mStack.size() > 1 ) {
      return false;
    } // if
    
    return true;
  } // Is_TopLevel()
  
  public Binding Get_Binding( String symbol ) throws Throwable {
    BindingTB tmp = mStack.elementAt( mStack.size() - 1 );
    return tmp.Get( symbol );
  } // Get_Binding()
  
  public String Set_Binding( Node bindingTarget, Node Sexp ) throws Throwable {
    BindingTB tmp = mStack.elementAt( mStack.size() - 1 );
    return tmp.Set( bindingTarget, Sexp );
  } // Set_Binding()
  
  private void Set_Binding_inner( String bindingTarget, boolean isFunc ) {
    BindingTB tmp = mStack.elementAt( mStack.size() - 1 );
    tmp.Set_innerBinding( bindingTarget, isFunc );
  } // Set_Binding_inner()
  
} // class CallStack
