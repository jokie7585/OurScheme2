package PL109_10627238;

import java.util.Vector;

public class OurSchemVM {
  private static OurSchemVM sOurSchemVM_S;
  // to meet the project4 feature
  // now mNowExcutingSexpNode is a stack
  private Vector<Node> mNowExcutingSexpNode;
  private Node mFailedList;
  private Node mVerbose;
  // it represent the control on a function
  // or Os
  // // when mScope is the first BindingTale
  public BindingTB mScope_Global;
  public Vector<BindingTB> mScope_Cur;
  public CallStack mCallStack;
  
  public OurSchemVM() {
    mVerbose = Function.Generate_True();
    mNowExcutingSexpNode = new Vector<Node>();
    
    Memory.Get_Instance().Init();
    // register inner Binding
    mCallStack = new CallStack();
    mScope_Global = mCallStack.Top();
    mScope_Cur = new Vector<BindingTB>();
    CurScope_Push( mScope_Global );
    
  } // OurSchemVM()
  
  public void Initial() {
    sOurSchemVM_S = new OurSchemVM();
  } // Initial()
  
  public void Exception_process() {
    // call stack exception process
    mCallStack.Exception_Process();
    // VM exception process
    // init
    mNowExcutingSexpNode = new Vector<Node>();
    // init
    mScope_Cur = new Vector<BindingTB>();
    CurScope_Push( mScope_Global );
  } // Exception_process()
  
  public static OurSchemVM Get_Instance() {
    if ( sOurSchemVM_S == null ) {
      sOurSchemVM_S = new OurSchemVM();
      return sOurSchemVM_S;
    } // if
    else {
      return sOurSchemVM_S;
    } // else
    
  } // Get_Instance()
  
  public boolean Is_Verbose() {
    if ( mVerbose.Is_T() ) {
      return true;
    } // if
    else {
      return true;
    } // else
    
  } // Is_Verbose()
  
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
    return ExecutingSexpNode_Top();
  } // Get_FailedMainSexp()
  
  public Node ExecutingSexpNode_Top() {
    return mNowExcutingSexpNode.elementAt( mNowExcutingSexpNode.size() - 1 );
  } // ExecutingSexpNode_Top()
  
  public void ExecutingSexpNode_Push( Node sexpNode ) {
    mNowExcutingSexpNode.add( sexpNode );
  } // ExecutingSexpNode_Push()
  
  public void ExecutingSexpNode_Pop() {
    mNowExcutingSexpNode.remove( mNowExcutingSexpNode.size() - 1 );
  } // ExecutingSexpNode_Pop()
  
  public boolean Is_TopLevel( Node sexp ) throws Throwable {
    return Equal( sexp, ExecutingSexpNode_Top() );
  } // Is_TopLevel()
  
  public void CurScope_Push( BindingTB tb ) {
    mScope_Cur.add( tb );
  } // CurScope_Push()
  
  public void CurScope_Pop() {
    mScope_Cur.remove( mScope_Cur.size() - 1 );
  } // CurScope_Pop()
  
  public BindingTB CurScope_Top() {
    return mScope_Cur.elementAt( mScope_Cur.size() - 1 );
  } // CurScope_Top()
  
  public Node Apply( Node Sexp ) throws Throwable {
    try {
      ExecutingSexpNode_Push( Sexp );
      Node retunNode = Evaluate( Sexp, CurScope_Top() );
      ExecutingSexpNode_Pop();
      
      if ( mNowExcutingSexpNode.size() == 0 ) {
        Interpreter.NewPrinter( retunNode );
        // now printer not print line change at end
        System.out.println( "" );
      } // if
      
      return retunNode;
    } // try
    catch ( NoNeedReturnException e ) {
      // it actually a return but only generate by
      // // define
      // // clean environment
      // // quit is the same but it will shout down the program
      
      // // it null is okay because the three function never be nested call
      ExecutingSexpNode_Pop();
      return null;
    } // catch
    
  } // Apply()
  
  public Node Evaluate( Node Sexp, BindingTB scope ) throws Throwable {
    Sexp = Sexp.Get();
    
    // if atom
    if ( InnerFunction.Is_Atom( Sexp ).mToken.mType == Symbol.sT ) {
      
      if ( Sexp.mToken.mType == Symbol.sSYMBOL ) {
        // if symbol
        
        Binding binding = null;
        
        if ( Sexp.mScope.size() > 0 ) {
          
          for ( int i = Sexp.mScope.size() - 1 ; i >= 0 ; i-- ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope :" );
            // mCallStack.ListLayer( Sexp.mScope.elementAt( i ) );
            
            if ( binding == null ) {
              // System.out.println( "call Get_Binding" );
              binding = mCallStack.Get_Binding( Sexp.mToken.mContent, Sexp.mScope.elementAt( i ) );
            } // if
            else {
              
              i = -1;
            } // else
            
          } // for
          
          if ( binding == null ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope (carry):" );
            binding = mCallStack.Get_Binding( Sexp.mToken.mContent, scope );
          } // if
          
          if ( binding == null ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope (global):" );
            binding = mCallStack.Get_Binding( Sexp.mToken.mContent, mScope_Global );
          } // if
          
        } // if
        else {
          if ( binding == null ) {
            binding = mCallStack.Get_Binding( Sexp.mToken.mContent, scope );
          } // if
          
          if ( binding == null ) {
            binding = mCallStack.Get_Binding( Sexp.mToken.mContent, mScope_Global );
          } // if
          
        } // else
        
        if ( binding == null ) {
          throw new EvaluatingError( "unbound symbol", Sexp.mToken.mContent );
        } // if
        else {
          // System.out.println( "dereference : " + Sexp.mToken.mContent );
          // System.out.println( "get binding : " + binding.Get().Get_Symbol() +
          // " : " );
          // Interpreter.NewPrinter( binding.Get() );
          // System.out.println( "geted : " );
          // Interpreter.NewPrinter( binding.Get() );
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
        // if reserved words
        return CallFunction( Sexp, scope );
      } // if
      else {
        // TODO if non-list throw original List or Current List
        mFailedList = Sexp;
        throw new ListError( "non-list" );
        // throw new ListError( "non-list", "", mNowExcutingSexpNode );
      } // else
      
    } // else
    
  } // Evaluate()
  
  public Node CallFunction( Node Sexp, BindingTB scope ) throws Throwable {
    Node returnNode = null;
    Sexp = Sexp.Get();
    Node functionBind;
    
    try {
      functionBind = Evaluate( Sexp.mL_Child.Get(), scope ).Get();
    } // try
    catch ( NoReturnValue e ) {
      mFailedList = Sexp.mL_Child;
      throw new EvaluateNonBinding();
    } // catch
    
    if ( functionBind == null ) {
      mFailedList = Sexp.mL_Child;
      throw new EvaluateNonBinding();
    } // if
    
    String funcnName = functionBind.Get_Symbol();
    Node functionArgsSexp = Sexp.mR_Child;
    
    // first check primitive internal function
    
    if ( funcnName.equals( "atom?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "atom?", 1, functionArgsSexp, false );
      Node arg1;
      
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Atom( arg1 );
      mCallStack.Pop();
    } // if
    else if ( funcnName.equals( "pair?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "pair?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Pair( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "list?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "list?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_List( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "null?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "null?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Null( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "integer?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "integer?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Integer( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "real?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "real?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Real( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "number?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "number?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_Number( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_String( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "boolean?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "boolean?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is__Boolean( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "symbol?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "symbol?", 1, functionArgsSexp, false );
      Node arg1;
      try {
        arg1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        mFailedList = paremeters.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Is_symbol( arg1 );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "+" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "+", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          mCallStack.Pop();
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          throw new OperationError( "+" );
        } // if
      } // for
      
      returnNode = InnerFunction.Add( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "-" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "-", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          mCallStack.Pop();
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          throw new OperationError( "-" );
        } // if
      } // for
      
      returnNode = InnerFunction.Sub( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "*" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "*", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "*" );
        } // if
      } // for
      
      returnNode = InnerFunction.Mul( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "/" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "/", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "/" );
        } // if
      } // for
      
      returnNode = InnerFunction.Div( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "not" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "not", 1, functionArgsSexp, false );
      Node evaluatedNode = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      
      returnNode = InnerFunction.Not( evaluatedNode );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "and" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "and", 2, functionArgsSexp, true );
      
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        
        try {
          returnNode = Evaluate( paremeters.elementAt( i ).Get(), scope );
          
          if ( InnerFunction.And_Is_next( returnNode ) ) {
            // do nothing
          } // if
          else {
            // end for loop
            i = paremeters.size();
          } // else
          
        } // try
        catch ( NoReturnValue e ) {
          mFailedList = paremeters.elementAt( i );
          mCallStack.Pop();
          throw new UnboundCondition();
        } // catch
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "or" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "or", 2, functionArgsSexp, true );
      
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        
        try {
          returnNode = Evaluate( paremeters.elementAt( i ).Get(), scope );
          
          if ( InnerFunction.Or_Is_next( returnNode ) ) {
            // do nothing
          } // if
          else {
            // end for loop
            i = paremeters.size();
          } // else
          
        } // try
        catch ( NoReturnValue e ) {
          mFailedList = paremeters.elementAt( i );
          mCallStack.Pop();
          throw new UnboundCondition();
        } // catch
        catch ( UnboundTestCondition e ) {
          mFailedList = paremeters.elementAt( i );
          mCallStack.Pop();
          throw new UnboundCondition();
        } // catch
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cons" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "cons", 2, functionArgsSexp, false );
      Node leftNode;
      Node rightNode;
      
      // System.out.println( "cons : left eval" );
      // Interpreter.NewPrinter( paremeters.elementAt( 0 ) );
      
      try {
        leftNode = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
        // System.out.println( "\ncons : left after eval" );
        // Interpreter.NewPrinter( leftNode );
        // System.out.println();
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 0 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      //
      // System.out.println( "\n end cons : left eval" );
      //
      // System.out.println( "cons : right eval" );
      // Interpreter.NewPrinter( paremeters.elementAt( 1 ) );
      
      try {
        rightNode = Evaluate( paremeters.elementAt( 1 ).Get(), scope );
        // System.out.println( "\ncons : right after eval" );
        // Interpreter.NewPrinter( rightNode );
        // System.out.println();
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 1 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      // System.out.println( "\n end cons : right eval" );
      
      returnNode = InnerFunction.Cons( leftNode, rightNode );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "list" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "list", 0, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
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
      
      if ( Is_TopLevel( Sexp ) ) {
        mCallStack.Push();
        
        try {
          Vector<Node> paremeters = new Vector<Node>();
          Vector<Node> excutinglist = new Vector<Node>();
          try {
            paremeters = ParseParemeter( "define", 2, functionArgsSexp, true );
          } // try
          catch ( EvaluatingError e ) {
            
            throw new MainSexpError( "DEFINE format" );
          } // catch
          
          for ( int i = 1 ; i < paremeters.size() ; i++ ) {
            excutinglist.add( paremeters.elementAt( i ) );
            
          } // for
          
          Node bindingValueNode = InnerFunction.List( excutinglist );
          
          String bindTargetString = mCallStack.Set_Binding( paremeters.elementAt( 0 ), bindingValueNode );
          
          if ( mVerbose.Is_T() ) {
            System.out.println( bindTargetString + " defined" );
          } // if
          else {
            // do nothing
          } // else
          
          mCallStack.Pop();
          
          throw new NoNeedReturnException();
        } // try
        catch ( MainSexpError e ) {
          mCallStack.Pop();
          throw new MainSexpError( "DEFINE format" );
          
        } // catch
        catch ( PrimitiveRedefineError e ) {
          mCallStack.Pop();
          throw new MainSexpError( "DEFINE format" );
        } // catch
        catch ( IncorrectArgNumError e ) {
          mCallStack.Pop();
          throw new MainSexpError( "DEFINE format" );
        } // catch
        
      } // if
      else {
        mCallStack.Pop();
        throw new EvaluatingError( "level of DEFINE", "" );
      } // else
      
    } // else if
    else if ( funcnName.equals( "set!" ) ) {
      // no need to push a new call stack
      
      mCallStack.Push();
      
      try {
        Vector<Node> paremeters = new Vector<Node>();
        try {
          paremeters = ParseParemeter( "set!", 2, functionArgsSexp, false );
        } // try
        catch ( IncorrectArgNumError e ) {
          mFailedList = Sexp;
          throw new FormatError( "SET! format" );
        } // catch
        
        if ( !InnerFunction.Is_symbol( paremeters.elementAt( 0 ) ).Is_T() ) {
          mFailedList = Sexp;
          throw new FormatError( "SET!  format" );
        } // if
        
        Node valueNode = Evaluate( paremeters.elementAt( 1 ).Get(), scope );
        // first node should be symbol
        Node symbol = paremeters.elementAt( 0 );
        Binding binding = null;
        
        // System.out.println( "find bind from symbol : " +
        // symbol.mToken.mContent );
        
        if ( symbol.mScope.size() > 0 ) {
          
          for ( int i = symbol.mScope.size() - 1 ; i >= 0 ; i-- ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope :" );
            // mCallStack.ListLayer( Sexp.mScope.elementAt( i ) );
            
            if ( binding == null ) {
              // System.out.println( "find in scope : " );
              // mCallStack.ListLayer( symbol.mScope.elementAt( i ) );
              // System.out.println( "call Get_Binding" );
              binding = mCallStack.Get_Binding( symbol.mToken.mContent, symbol.mScope.elementAt( i ) );
            } // if
            else {
              
              i = -1;
            } // else
            
          } // for
          
          if ( binding == null ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope (carry):" );
            binding = mCallStack.Get_Binding( symbol.mToken.mContent, scope );
          } // if
          
          if ( binding == null ) {
            // System.out.println( "find symbol : " + Sexp.mToken.mContent + "
            // in scope (global):" );
            binding = mCallStack.Get_Binding( symbol.mToken.mContent, mScope_Global );
          } // if
          
        } // if
        
        if ( binding == null ) {
          // System.out.println( "set new binding" );
          mCallStack.Set_Binding_local( symbol, valueNode, false, mScope_Global );
          returnNode = Evaluate( symbol, scope );
        } // if
        else {
          // System.out.println( "set exit binding" );
          binding.Set( valueNode );
          returnNode = binding.Get();
        } // else
        
      } // try
      catch ( MainSexpError e ) {
        mCallStack.Pop();
        mFailedList = Sexp;
        throw new FormatError( "SET!  format" );
        
      } // catch
      catch ( PrimitiveRedefineError e ) {
        mFailedList = Sexp;
        mCallStack.Pop();
        throw new FormatError( "SET!  format" );
      } // catch
      catch ( IncorrectArgNumError e ) {
        mFailedList = Sexp;
        mCallStack.Pop();
        throw new FormatError( "SET!  format" );
      } // catch
      
      mCallStack.Pop();
      
    } // else if
    else if ( funcnName.equals( "car" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "car", 1, functionArgsSexp, false );
      Node param1;
      try {
        param1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope ).Get();
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 0 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Car( param1 );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cdr" ) ) {
      // if rChild of first dot is null
      // // return a nil atom as result
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "cdr", 1, functionArgsSexp, false );
      Node param1;
      try {
        param1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope ).Get();
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 0 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      returnNode = InnerFunction.Cdr( param1 );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( ">" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( ">", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( ">" );
        } // if
      } // for
      
      returnNode = InnerFunction.Larger( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( ">=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( ">=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( ">=" );
        } // if
      } // for
      
      returnNode = InnerFunction.Larger_equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "<" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "<", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "<" );
        } // if
      } // for
      
      returnNode = InnerFunction.Smaller( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "<=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "<=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "<=" );
        } // if
      } // for
      
      returnNode = InnerFunction.Smaller_equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "=" ) ) {
      mCallStack.Push();
      
      Vector<Node> paremeters = ParseParemeter( "=", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_Number( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "=" );
        } // if
      } // for
      
      returnNode = InnerFunction.Equal( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string-append" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string-append", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_String( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "string-append" );
        } // if
      } // for
      
      returnNode = InnerFunction.String_Append( evaluatedPram );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string>?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string>?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_String( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "string>?" );
        } // if
      } // for
      
      returnNode = InnerFunction.String_Larger( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string<?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string<?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_String( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "string<?" );
        } // if
      } // for
      
      returnNode = InnerFunction.String_Smaller( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "string=?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "string=?", 2, functionArgsSexp, true );
      Vector<Node> evaluatedPram = new Vector<Node>();
      try {
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // pre set as mFailedList
          mFailedList = paremeters.elementAt( i );
          evaluatedPram.add( Evaluate( paremeters.elementAt( i ).Get(), scope ) );
          
        } // for
      } // try
      catch ( NoReturnValue e ) {
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      for ( int i = 0 ; i < evaluatedPram.size() ; i++ ) {
        if ( InnerFunction.Is_String( evaluatedPram.elementAt( i ).Get() ).Is_Nil() ) {
          Set_FailedList( evaluatedPram.elementAt( i ).Get() );
          mCallStack.Pop();
          throw new OperationError( "string=?" );
        } // if
      } // for
      
      returnNode = InnerFunction.String_Equal( evaluatedPram );
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "eqv?" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters = ParseParemeter( "eqv?", 2, functionArgsSexp, false );
      Node param1;
      Node param2;
      
      try {
        param1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 0 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      try {
        param2 = Evaluate( paremeters.elementAt( 1 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 1 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
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
      Node param1;
      Node param2;
      
      try {
        param1 = Evaluate( paremeters.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 0 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
      try {
        param2 = Evaluate( paremeters.elementAt( 1 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = paremeters.elementAt( 1 );
        mCallStack.Pop();
        throw new NoReturnParame();
      } // catch
      
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
        mCallStack.Pop();
        throw new EvaluatingError( "incorrect number of arguments", "if" );
      } // if
      else {
        
        Node condition;
        
        try {
          condition = Evaluate( parems.elementAt( 0 ).Get(), scope );
        } // try
        catch ( NoReturnValue e ) {
          mFailedList = parems.elementAt( 0 );
          mCallStack.Pop();
          throw new UnboundTestCondition();
        } // catch
        
        if ( InnerFunction.And_Is_next( condition ) ) {
          returnNode = Evaluate( parems.elementAt( 1 ).Get(), scope );
        } // if
        else {
          if ( parems.size() == 3 ) {
            returnNode = Evaluate( parems.elementAt( 2 ).Get(), scope );
          } // if
          else {
            mFailedList = Sexp;
            mCallStack.Pop();
            throw new NoReturnValue();
          } // else
          
        } // else
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "cond" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters;
      
      try {
        
        // System.out.println( "cond functionArgsSexp: " );
        // Interpreter.NewPrinter( functionArgsSexp );
        // System.out.println( "\n" );
        
        paremeters = ParseParemeter( "cond", 1, functionArgsSexp, true );
        
        for ( int i = 0 ; i < paremeters.size() ; i++ ) {
          // System.out.println( "in cond process el: " );
          // Interpreter.NewPrinter( paremeters.elementAt( i ) );
          // System.out.println( "\n" );
          if ( InnerFunction.Is_List( paremeters.elementAt( i ).Get() ).Is_Nil() ) {
            mFailedList = Sexp;
            
            throw new ListError( "COND format" );
          } // if
          else {
            // test all list has at least two symbol
            // System.out.println( "slice cond params : " );
            // Interpreter.NewPrinter( paremeters.elementAt( i ) );
            Vector<Node> executSequence = ParseParemeter( "", 2, paremeters.elementAt( i ), true );
          } // else
        } // for
        
      } // try
      catch ( IncorrectArgNumError e ) {
        mFailedList = Sexp;
        mCallStack.Pop();
        throw new ListError( "COND format" );
      } // catch
      
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        Vector<Node> executSequence;
        
        // get the executing sequence
        try {
          executSequence = ParseParemeter( "", 2, paremeters.elementAt( i ), true );
        } // try
        catch ( IncorrectArgNumError e ) {
          mFailedList = Sexp;
          mCallStack.Pop();
          throw new ListError( "COND format" );
        } // catch
        
        // execute all
        if ( i != paremeters.size() - 1 ) {
          // if true
          Node condition;
          // System.out.println( "condition Sexp: " );
          // Interpreter.NewPrinter( executSequence.elementAt( 0 ) );
          
          // first is condition
          condition = Evaluate( executSequence.elementAt( 0 ).Get(), scope );
          
          if ( InnerFunction.And_Is_next( condition ) ) {
            // System.out.println( "in" );
            for ( int j = 1 ; j < executSequence.size() ; j++ ) {
              try {
                returnNode = Evaluate( executSequence.elementAt( j ).Get(), scope );
                
              } // try
              catch ( NoReturnValue e ) {
                if ( j < executSequence.size() - 1 ) {
                  returnNode = null;
                } // if
                else {
                  mFailedList = executSequence.elementAt( j );
                  mCallStack.Pop();
                  throw new NoReturnValue();
                } // else
                
              } // catch
              
            } // for
            
            // skip all
            i = paremeters.size();
          } // if
          else {
            // do nothing
          } // else
        } // if
        else {
          // System.out.println( "in cond : check else : " );
          // Interpreter.NewPrinter( executSequence.elementAt( 0 ) );
          // System.out.println( "\nend in cond else check" );
          // last one
          // check if keyword
          // // else
          
          if ( executSequence.elementAt( 0 ).Get().Get_Symbol().compareTo( "else" ) == 0 ) {
            for ( int j = 1 ; j < executSequence.size() ; j++ ) {
              
              try {
                returnNode = Evaluate( executSequence.elementAt( j ).Get(), scope );
              } // try
              catch ( NoReturnValue e ) {
                if ( j < executSequence.size() - 1 ) {
                  returnNode = null;
                } // if
                else {
                  mFailedList = executSequence.elementAt( j );
                  mCallStack.Pop();
                  throw new NoReturnValue();
                } // else
                
              } // catch
              
            } // for
            
          } // if
          else {
            Node condition;
            
            try {
              condition = Evaluate( executSequence.elementAt( 0 ).Get(), scope );
            } // try
            catch ( NoReturnValue e ) {
              mFailedList = executSequence.elementAt( 0 );
              mCallStack.Pop();
              throw new UnboundTestCondition();
            } // catch
            
            if ( InnerFunction.And_Is_next( condition ) ) {
              for ( int j = 1 ; j < executSequence.size() ; j++ ) {
                try {
                  returnNode = Evaluate( executSequence.elementAt( j ).Get(), scope );
                } // try
                catch ( NoReturnValue e ) {
                  if ( j < executSequence.size() - 1 ) {
                    returnNode = null;
                  } // if
                  else {
                    mFailedList = executSequence.elementAt( j );
                    mCallStack.Pop();
                    throw new NoReturnValue();
                  } // else
                  
                } // catch
                
              } // for
              
            } // if
            else {
              
              if ( returnNode == null ) {
                mFailedList = Sexp;
                mCallStack.Pop();
                throw new NoReturnValue();
              } // if
              
            } // else
            
          } // else
          
        } // else
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "begin" ) ) {
      mCallStack.Push();
      // System.out.println( "begin Expression :" );
      // Interpreter.NewPrinter( functionArgsSexp );
      
      Vector<Node> paremeters = ParseParemeter( "begin", 1, functionArgsSexp, true );
      for ( int i = 0 ; i < paremeters.size() ; i++ ) {
        
        try {
          // System.out.println( "in begin run :" );
          // Interpreter.NewPrinter( paremeters.elementAt( i ).Get() );
          returnNode = Evaluate( paremeters.elementAt( i ).Get(), scope );
          // System.out.println( "" );
          // System.out.println( " result : " );
          // Interpreter.NewPrinter( returnNode );
          // System.out.println( "\nin begin end :" );
          
        } // try
        catch ( NoReturnValue e ) {
          if ( i == paremeters.size() - 1 ) {
            mFailedList = paremeters.elementAt( i );
            throw new NoReturnValue();
          } // if
          else {
            returnNode = null;
          } // else
          
        } // catch
        
      } // for
      
      // System.out.println( "okok return " );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "clean-environment" ) ) {
      // no need to push a new call stack
      
      if ( Is_TopLevel( Sexp ) ) {
        ParseParemeter( "clean-environment", 0, functionArgsSexp, false );
        Initial();
        if ( mVerbose.Is_T() ) {
          System.out.println( "environment cleaned" );
        } // if
        else {
          // do nothing
        } // else
        
        mCallStack.Pop();
        
        throw new NoNeedReturnException();
      } // if
      else {
        mCallStack.Pop();
        throw new EvaluatingError( "level of CLEAN-ENVIRONMENT", "" );
      } // else
    } // else if
    else if ( funcnName.equals( "exit" ) ) {
      // no need to push a new call stack
      
      if ( Is_TopLevel( Sexp ) ) {
        // check no argument
        ParseParemeter( "exit", 0, functionArgsSexp, false );
        InnerFunction.Exit();
        
        returnNode = Node.Generate_Empty();
      } // if
      else {
        mCallStack.Pop();
        throw new EvaluatingError( "level of EXIT", "" );
      } // else
      
    } // else if
    else if ( funcnName.equals( "lambda" ) ) {
      mCallStack.Push();
      Vector<Node> paremeters;
      Vector<Node> argumentList = new Vector<Node>();
      Vector<Node> functionBody = new Vector<Node>();
      
      try {
        paremeters = ParseParemeter( "LAMBDA", 2, functionArgsSexp, true );
        // check the parameter
        if ( InnerFunction.Is_List( paremeters.elementAt( 0 ) ).Is_T() ) {
          
          // do set argument list
          // cut parse parameters
          argumentList = ParseParemeter( "", 0, paremeters.elementAt( 0 ), true );
          for ( int i = 0 ; i < argumentList.size() ; i++ ) {
            if ( InnerFunction.Is_symbol( argumentList.elementAt( i ) ).Is_T() ) {
              // do nothing
            } // if
            else {
              throw new FormatError( "LAMBDA format" );
            } // else
          } // for
          
          // cut executable Sexp
          for ( int i = 1 ; i < paremeters.size() ; i++ ) {
            functionBody.add( paremeters.elementAt( i ) );
          } // for
          
        } // if
        else {
          throw new FormatError( "LAMBDA format" );
        } // else
      } // try
      catch ( Throwable e ) {
        mFailedList = Sexp;
        mCallStack.Pop();
        throw new FormatError( "LAMBDA format" );
      } // catch
      
      // a function define by system internal workflow
      // funcName is Enclosed in parentheses to separate it from the general
      // function
      returnNode = Memory.Get_Instance().Add( new MemoryItem( "(lambda)", argumentList, functionBody ) );
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "let" ) ) {
      // System.out.println( "let befoe load : " );
      // System.out.println( " carry scope: " );
      // mCallStack.ListLayer( scope );
      // System.out.println( " top stack scope: " );
      // mCallStack.ListLayer( mCallStack.Top() );
      // System.out.println( " before top stack scope: " );
      // mCallStack.ListLayer( mCallStack.Top().mPreTable );
      BindingTB cur = mCallStack.Push();
      Vector<Node> parameter;
      Vector<Node> executableSexp;
      Vector<Node> argSymbol = new Vector<Node>();
      Vector<Node> argValue = new Vector<Node>();
      
      try {
        parameter = ParseParemeter( "let", 0, functionArgsSexp, true );
        executableSexp = new Vector<Node>();
        
        if ( parameter.size() >= 2 && InnerFunction.Is_List( parameter.elementAt( 0 ) ).Is_T() ) {
          // System.out.println( "let have more than2 args" );
          Vector<Node> argumentPair = ParseParemeter( "", 0, parameter.elementAt( 0 ), true );
          
          // check all argument pair
          for ( int i = 0 ; i < argumentPair.size() ; i++ ) {
            // System.out.println( "start parse local" );
            if ( InnerFunction.Is_List( argumentPair.elementAt( i ) ).Is_T() ) {
              // System.out.println( "is list" );
              
              Vector<Node> tmpPair = ParseParemeter( "", 0, argumentPair.elementAt( i ), true );
              
              if ( tmpPair.size() != 2 ) {
                // System.out.println( "is 2 element list" );
                throw new FormatError( "LET format" );
              } // if
              
              if ( InnerFunction.Is_symbol( tmpPair.elementAt( 0 ) ).Is_T() ) {
                // System.out.println( "first el is symbol : " +
                // tmpPair.elementAt( 0 ).Get_Symbol() );
                argSymbol.add( tmpPair.elementAt( 0 ) );
                // TODO let Not evaluate argValue
                
                argValue.add( tmpPair.elementAt( 1 ).Get() );
                // System.out.println( "finish" );
              } // if
              else {
                
                throw new FormatError( "LET format" );
              } // else
              
            } // if
            else {
              
              throw new FormatError( "LET format" );
            } // else
            
          } // for
          
          // System.out.println( "run" );
          for ( int i = 1 ; i < parameter.size() ; i++ ) {
            executableSexp.add( parameter.elementAt( i ) );
          } // for
          
        } // if
        else {
          
          throw new FormatError( "LET format" );
        } // else
        
      } // try
      catch ( FormatError e ) {
        mFailedList = Sexp;
        mCallStack.Pop();
        throw new ListError( "LET format" );
      } // catch
      
      // load local variable
      try {
        // TODO not sure PAL project3 d type error
        // // if Let or Set in user define function
        // // // to top
        // // // or need be catch by user define function
        for ( int i = 0 ; i < argSymbol.size() ; i++ ) {
          mFailedList = argValue.elementAt( i );
          Node argNode = argSymbol.elementAt( i );
          Node valNode = Evaluate( argValue.elementAt( i ).Get(), scope );
          mCallStack.Set_Binding_local( argNode, valNode.Get(), false, cur );
        } // for
      } // try
      catch ( NoReturnValue e ) {
        throw new DefineOrLetANoReturn();
      } // catch
      
      // System.out.println( "let success load all : " );
      // mCallStack.ListLayer( cur );
      
      for ( int i = 0 ; i < executableSexp.size() ; i++ ) {
        try {
          Scope_Bind( executableSexp.elementAt( i ), cur );
          returnNode = Evaluate( executableSexp.elementAt( i ).Get(), cur );
          Scope_DeBind( executableSexp.elementAt( i ) );
        } // try
        catch ( NoReturnValue e ) {
          if ( i == executableSexp.size() - 1 ) {
            Scope_DeBind( executableSexp.elementAt( i ).Get() );
            mFailedList = executableSexp.elementAt( i );
            throw new NoReturnValue();
          } // if
          else {
            Scope_DeBind( executableSexp.elementAt( i ).Get() );
          } // else
        } // catch
        
      } // for
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "verbose?" ) ) {
      mCallStack.Push();
      // verbose
      Vector<Node> parameter = ParseParemeter( "verbose?", 0, functionArgsSexp, false );
      returnNode = mVerbose;
      mCallStack.Pop();
      
    } // else if
    else if ( funcnName.equals( "verbose" ) ) {
      mCallStack.Push();
      
      // System.out.println( "In verbose in scope find : " +
      // functionArgsSexp.Get_Symbol() );
      //
      // mCallStack.ListLayer( scope );
      
      Vector<Node> parameter = ParseParemeter( "verbose", 1, functionArgsSexp, false );
      Node valueNode = Evaluate( parameter.elementAt( 0 ).Get(), scope ).Get();
      
      // System.out.println( " dereference : " + functionArgsSexp.Get_Symbol()
      // );
      // Interpreter.NewPrinter( valueNode );
      // System.out.println();
      
      if ( InnerFunction.Is__Boolean( valueNode ).Is_T() ) {
        
        if ( valueNode.Is_T() ) {
          // System.out.println( "set verbose to : true" );
          mVerbose = Function.Generate_True();
        } // if
        else {
          // System.out.println( "set verbose to : false" );
          mVerbose = Function.Generate_False();
        } // else
        
        returnNode = mVerbose;
        mCallStack.Pop();
        
      } // if
      else {
        mCallStack.Pop();
        throw new OperationError( "verbose" );
      } // else
      
    } // else if
    else if ( funcnName.equals( "create-error-object" ) ) {
      mCallStack.Push();
      // verbose
      Vector<Node> parameter = ParseParemeter( "create-error-object", 1, functionArgsSexp, false );
      
      Node msgNode;
      
      try {
        msgNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      if ( InnerFunction.Is_String( msgNode ).Is_T() ) {
        returnNode = Node.Generate_Error( msgNode.Get_Symbol() );
      } // if
      
      mCallStack.Pop();
      
    } // else if
    else if ( funcnName.equals( "error-object?" ) ) {
      mCallStack.Push();
      // verbose
      Vector<Node> parameter = ParseParemeter( "error-object?", 1, functionArgsSexp, false );
      
      Node msgNode;
      
      System.out.println( "in error-object? detect : " + parameter.elementAt( 0 ).Get().Get_Symbol() );
      mCallStack.ListLayer( scope );
      System.out.println( "\nend print scope in error-object?" );
      try {
        msgNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      if ( msgNode.Get().mToken.mType == Symbol.sERROR ) {
        returnNode = Function.Generate_True();
      } // if
      else {
        returnNode = Function.Generate_False();
      } // else
      
      mCallStack.Pop();
      
    } // else if
    else if ( funcnName.equals( "read" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "read", 0, functionArgsSexp, false );
      
      try {
        MyScanner.Get_Instance().FinishReset();
        returnNode = Interpreter.ReadSexp();
      } // try
      catch ( EOFEncounterError e ) {
        // returnNode = Node
        // .Generate_Error( "\"ERROR : END-OF-FILE encountered when there should
        // be more input\"" );
        
        throw e;
      } // catch
      catch ( UnexpectedError e ) {
        StringBuffer tmpBuffer = new StringBuffer();
        Token errorToken = MyScanner.Get_Instance().PreToken();
        
        tmpBuffer.append( "\"ERROR (unexpected character) : " );
        tmpBuffer.append( "Line " );
        tmpBuffer.append( MyScanner.Get_Instance().CurLine() );
        tmpBuffer.append( " Column " );
        tmpBuffer.append( MyScanner.Get_Instance().PreTokenCol() );
        tmpBuffer.append( " '" );
        tmpBuffer.append( errorToken.mContent.charAt( 0 ) );
        tmpBuffer.append( "'\"" );
        // System.out.println( "in do : " + tmpBuffer.toString() );
        
        returnNode = Node.Generate_Error( tmpBuffer.toString() );
        // Interpreter.NewPrinter( returnNode );
        
        MyScanner.Get_Instance().ErrorReset();
        mCallStack.Pop();
      } // catch
      
    } // else if
    else if ( funcnName.equals( "write" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "write", 1, functionArgsSexp, false );
      Node msgNode;
      
      try {
        msgNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      Interpreter.NewPrinter( msgNode );
      returnNode = msgNode;
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "display-string" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "display-string", 1, functionArgsSexp, false );
      Node value;
      
      try {
        value = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      if ( InnerFunction.Is_String( value ).Is_T() || value.mToken.mType == Symbol.sERROR ) {
        String str = Interpreter.Evaluate( value.Get().mToken );
        System.out.print( str.substring( 1, str.length() - 1 ) );
        returnNode = value;
      } // if
      else {
        mFailedList = value;
        throw new OperationError( "display-string" );
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "newline" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "newline", 0, functionArgsSexp, false );
      Node msgNode;
      
      System.out.println( "" );
      returnNode = Function.Generate_False();
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "symbol->string" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "symbol->string", 1, functionArgsSexp, false );
      Node msgNode;
      
      try {
        msgNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      if ( InnerFunction.Is_symbol( msgNode ).Is_T() ) {
        returnNode = Node.Generate_String( "\"" + Interpreter.Evaluate( msgNode.Get().mToken ) + "\"" );
      } // if
      else {
        mFailedList = msgNode;
        throw new OperationError( "symbol->string" );
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "number->string" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "number->string", 1, functionArgsSexp, false );
      Node msgNode;
      
      try {
        msgNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      if ( InnerFunction.Is_Number( msgNode ).Is_T() ) {
        returnNode = Node.Generate_String( "\"" + Interpreter.Evaluate( msgNode.Get().mToken ) + "\"" );
      } // if
      else {
        mFailedList = msgNode;
        throw new OperationError( "number->string" );
      } // else
      
      mCallStack.Pop();
    } // else if
    else if ( funcnName.equals( "eval" ) ) {
      mCallStack.Push();
      Vector<Node> parameter = ParseParemeter( "eval", 1, functionArgsSexp, false );
      
      Node sexpNode;
      try {
        sexpNode = Evaluate( parameter.elementAt( 0 ).Get(), scope );
      } // try
      catch ( NoReturnValue e ) {
        mFailedList = parameter.elementAt( 0 );
        throw new NoReturnParame();
      } // catch
      
      // System.out.println( "\nexecute in eval : " );
      // Interpreter.NewPrinter( sexpNode );
      
      CurScope_Push( scope );
      returnNode = Apply( sexpNode );
      CurScope_Pop();
      
      // System.out.println( "end in eval : " );
      mCallStack.Pop();
    } // else if
    else {
      // call custom binding function
      // System.out.println( "user-define : " + functionBind.Get_Symbol() );
      
      if ( functionBind.Get().mToken.mType == Symbol.sPROCEDUREL ) {
        
        BindingTB cur = mCallStack.Push( scope );
        
        // System.out.println( "run function : " +
        // functionBind.Get().Get_Symbol() );
        
        Vector<Node> argSymbol = new Vector<Node>();
        Vector<Node> argValue = new Vector<Node>();
        
        // get function memoryItem
        int addr = Memory.Get_Instance().MemoryIndex( functionBind.Get() );
        MemoryItem fnc = Memory.Get_Instance().Get( addr );
        if ( fnc == null ) {
          mCallStack.Pop();
          throw new EvaluatingError( "Warning", "Memory error" );
        } // if
        
        int paramNum = fnc.mFuncArgs.size();
        // parse argument
        Vector<Node> arguments = ParseParemeter( fnc.Get_Symbol(), paramNum, functionArgsSexp, false );
        // set local variable
        try {
          for ( int i = 0 ; i < paramNum ; i++ ) {
            // pre set as mFailedList
            mFailedList = arguments.elementAt( i );
            // do
            argSymbol.add( fnc.mFuncArgs.elementAt( i ) );
            
            // System.out.println( "cut: " + argSymbol.elementAt( i
            // ).Get_Symbol() );
            
            argValue.add( Evaluate( arguments.elementAt( i ).Get(), scope ) );
            
            // Interpreter.NewPrinter( argValue.elementAt( i ) );
            
          } // for
          
        } // try
        catch ( NoReturnValue e ) {
          mCallStack.Pop();
          throw new NoReturnParame();
        } // catch
        
        // load in local variable
        for ( int i = 0 ; i < paramNum ; i++ ) {
          mCallStack.Set_Binding_local( argSymbol.elementAt( i ), argValue.elementAt( i ), true, cur );
        } // for
        
        // System.out.println( "load all var in scope : " );
        // mCallStack.ListLayer( cur );
        
        // flattened the function body to avoid it works like
        // nested call
        for ( int i = 0 ; i < fnc.mFuncBodyNode.size() ; i++ ) {
          
          try {
            // System.out.println( "run bady :" );
            // Interpreter.NewPrinter( fnc.mFuncBodyNode.elementAt( i ) );
            // System.out.println( "in scope :" );
            // mCallStack.ListLayer( cur );
            Scope_Bind( fnc.mFuncBodyNode.elementAt( i ), cur );
            returnNode = Evaluate( fnc.mFuncBodyNode.elementAt( i ), cur );
            Scope_DeBind( fnc.mFuncBodyNode.elementAt( i ) );
          } // try
          catch ( NoReturnValue e ) {
            if ( i == fnc.mFuncBodyNode.size() - 1 ) {
              Scope_DeBind( fnc.mFuncBodyNode.elementAt( i ) );
              mFailedList = Sexp;
              mCallStack.Pop();
              throw new NoReturnValue();
            } // if
            else {
              Scope_DeBind( fnc.mFuncBodyNode.elementAt( i ) );
            } // else
            
          } // catch
          
        } // for
        
        // // process begin return
        // if ( returnNode.mToken.mType == Symbol.sBEGINECONTIMUE ) {
        // returnNode = null;
        // } // if
        
        mCallStack.Pop();
        // System.out.println( "function return : " +
        // functionBind.Get().Get_Symbol() );
        // mCallStack.ListLayer( mCallStack.Top() );
      } // if
      else {
        mFailedList = Sexp;
        Set_FailedList( functionBind.Get() );
        throw new ApplyNonFunction();
      } // else
      
    } // else
    
    if ( returnNode == null ) {
      mFailedList = Sexp;
      throw new NoReturnValue();
    } // if
    else {
      return returnNode;
    } // else
    
  } // CallFunction()
  
  private void Scope_Bind( Node Sexp, BindingTB Scope ) {
    if ( Sexp != null ) {
      
      if ( Sexp.Is_Dot() ) {
        
        Scope_Bind( Sexp.mL_Child, Scope );
        Scope_Bind( Sexp.mR_Child, Scope );
        
      } // if
      else {
        Sexp.Push_Scope( Scope );
        
      } // else
      
    } // if
  } // Scope_Bind()
  
  private void Scope_DeBind( Node Sexp ) {
    if ( Sexp != null ) {
      
      if ( Sexp.Is_Dot() ) {
        
        Scope_DeBind( Sexp.mL_Child );
        Scope_DeBind( Sexp.mR_Child );
        
      } // if
      else {
        Sexp.Pop_Scope();
        
      } // else
      
    } // if
  } // Scope_DeBind()
  
  public static Vector<Node> ParseParemeter( String fcName, int amount, Node PTree, boolean l )
      throws Throwable {
    Vector<Node> paremeters = new Vector<Node>();
    
    while ( PTree != null ) {
      PTree = PTree.Get();
      
      if ( PTree.Is_Dot() ) {
        if ( PTree.mL_Child != null ) {
          paremeters.add( PTree.mL_Child );
          // System.out.println( " not nil, parse a arg : " );
          // Interpreter.NewPrinter( PTree.mL_Child );
          // System.out.println();
        } // if
        
      } // if
      
      // go down the bones
      PTree = PTree.mR_Child;
    } // while
    
    // check amount of arguments
    if ( l ) {
      
      if ( paremeters.size() >= amount ) {
        
        return paremeters;
      } // if
      else {
        if ( fcName.equals( "(lambda)" ) ) {
          throw new IncorrectArgNumError( "lambda expression" );
        } // if
        else if ( fcName.equals( ")(lambda" ) ) {
          throw new IncorrectArgNumError( "lambda" );
        } // if
        else {
          throw new IncorrectArgNumError( fcName );
        } // else
        
      } // else
      
    } // if
    else {
      if ( paremeters.size() != amount ) {
        if ( fcName.equals( "(lambda)" ) ) {
          throw new IncorrectArgNumError( "lambda" );
        } // if
        else if ( fcName.equals( ")(lambda" ) ) {
          throw new IncorrectArgNumError( "lambda" );
        } // if
        else {
          throw new IncorrectArgNumError( fcName );
        } // else
      } // if
      else {
        return paremeters;
      } // else
      
    } // else
    
  } // ParseParemeter()
  
  private boolean Eqv( Node param1, Node param2 ) throws Throwable {
    
    if ( param1.mToken.mType == Symbol.sBINDING && param1.mToken.mType == Symbol.sBINDING ) {
      
      if ( param1.Get().Is_Dot() && param2.Get().Is_Dot() ) {
        if ( param1.Get() == param2.Get() ) {
          return true;
        } // if
        else {
          return false;
        } // else
        
      } // if
      else if ( ( param1.Get().mToken.mType == Symbol.sPROCEDUREL )
          && ( param2.Get().mToken.mType == Symbol.sPROCEDUREL ) ) {
        if ( param1.Get() == param2.Get() ) {
          return true;
        } // if
        else {
          return false;
        } // else
      } // else if
      else if ( ( param1.Get().mToken.mType == Symbol.sSTRING )
          && ( param2.Get().mToken.mType == Symbol.sSTRING ) ) {
        if ( param1.Get() == param2.Get() ) {
          return true;
        } // if
        else {
          return false;
        } // else
      } // else if
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
            
            return false;
            
          } // else if
          else if ( InnerFunction.Is_symbol( param1 ).Is_T() ) {
            
            if ( param1.mToken.mType == Symbol.sSYMBOL ) {
              // throw new EvaluatingError( "Warning", "unDereference symbol!"
              // );
              if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
                return true;
              } // if
              else {
                return false;
              } // else
            } // if
            else {
              if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
                return true;
              } // if
              else {
                return false;
              } // else
              
            } // else
            
          } // else if
          else if ( InnerFunction.Is__Boolean( param1 ).Is_T() ) {
            return true;
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
      
      if ( ( param1.Get().mToken.mType == Symbol.sPROCEDUREL )
          && ( param2.Get().mToken.mType == Symbol.sPROCEDUREL ) ) {
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
            
            // if ( param1.mToken.mType == Symbol.sSYMBOL ) {
            // throw new EvaluatingError( "Warning", "unDereference symbol!
            // param1: " + param1.Get_Symbol() );
            // } // if
            // else if ( param2.mToken.mType == Symbol.sSYMBOL ) {
            // throw new EvaluatingError( "Warning", "unDereference symbol!
            // param2: " + param2.Get_Symbol() );
            // } // else if
            // else {
            if ( param1.Get_Symbol().compareTo( param2.Get_Symbol() ) == 0 ) {
              return true;
            } // if
            else {
              return false;
            } // else
            
            // } // else
            
          } // else if
          else if ( InnerFunction.Is__Boolean( param1 ).Is_T() ) {
            return true;
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
    Set_Binding_inner( "begin", true );
    Set_Binding_inner( "clean-environment", true );
    Set_Binding_inner( "exit", true );
    // project3
    Set_Binding_inner( "lambda", true );
    Set_Binding_inner( "let", true );
    Set_Binding_inner( "verbose?", true );
    Set_Binding_inner( "verbose", true );
    // project4
    
    Set_Binding_inner( "create-error-object", true );
    Set_Binding_inner( "error-object?", true );
    Set_Binding_inner( "read", true );
    Set_Binding_inner( "write", true );
    Set_Binding_inner( "display-string", true );
    Set_Binding_inner( "newline", true );
    Set_Binding_inner( "symbol->string", true );
    Set_Binding_inner( "number->string", true );
    Set_Binding_inner( "eval", true );
    Set_Binding_inner( "set!", true );
  } // Init()
  
  public void Exception_Process() {
    while ( mStack.size() > 1 ) {
      mStack.removeElementAt( mStack.size() - 1 );
    } // while
    
  } // Exception_Process()
  
  public BindingTB Push() {
    // push a stack
    mStack.add( new BindingTB( mStack.elementAt( mStack.size() - 1 ) ) );
    return Top();
  } // Push()
  
  public BindingTB Push( BindingTB parant ) {
    // push a stack
    mStack.add( new BindingTB( parant ) );
    return Top();
  } // Push()
  
  public void Pop() {
    // pop a stack
    
    // if ( mStack.size() > 1 ) {
    // mStack.removeElementAt( mStack.size() - 1 );
    // } // if
    
    // TODO callStack UnSafe Mode
    mStack.removeElementAt( mStack.size() - 1 );
    
  } // Pop()
  
  public BindingTB Top() {
    return mStack.elementAt( mStack.size() - 1 );
  } // Top()
  
  public boolean Is_TopLevel() {
    if ( mStack.size() > 1 ) {
      return false;
    } // if
    
    return true;
  } // Is_TopLevel()
  
  public void ListLayer( BindingTB bt ) throws Throwable {
    if ( bt == null ) {
      System.out.println( "gloadbal no pretable" );
    } // if
    else {
      Vector<Binding> tmp = bt.mBindings;
      System.out.println( "\nList Binding IsArgument" );
      System.out.println( "symbol  value" );
      for ( int i = 0 ; i < tmp.size() ; i++ ) {
        System.out.println( tmp.elementAt( i ).mSymbol + "\t" + tmp.elementAt( i ).Get() + "\t"
            + tmp.elementAt( i ).mIs_FunctionArgument );
        Interpreter.NewPrinter( tmp.elementAt( i ).Get().Get() );
        System.out.println( "" );
      } // for
      
      System.out.println( "\n" );
    } // else
    
  } // ListLayer()
  
  public Binding Get_Binding( String symbol, BindingTB scope ) throws Throwable {
    
    if ( scope != null ) {
      
      return scope.Get( symbol );
    } // if
    else {
      
      return null;
    } // else
    
  } // Get_Binding()
  
  public String Set_Binding_local( Node bindingTg, Node Sexp, boolean i, BindingTB b ) throws Throwable {
    
    return b.Set_local( bindingTg, Sexp, i );
  } // Set_Binding_local()
  
  public String Set_Binding( Node bindingTarget, Node Sexp ) throws Throwable {
    BindingTB tmp = mStack.elementAt( 0 );
    return tmp.Set( bindingTarget, Sexp );
  } // Set_Binding()
  
  private void Set_Binding_inner( String bindingTarget, boolean isFunc ) {
    BindingTB tmp = mStack.elementAt( 0 );
    tmp.Set_innerBinding( bindingTarget, isFunc );
  } // Set_Binding_inner()
  
} // class CallStack
