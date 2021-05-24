package PL109_10627238;

import java.util.Vector;

public class BindingTB {
  public Vector<Binding> mBindings;
  public BindingTB mPreTable;
  
  public BindingTB() {
    mBindings = new Vector<Binding>();
    mPreTable = null;
  } // BindingTB()
  
  public BindingTB( BindingTB preTb ) {
    mBindings = new Vector<Binding>();
    mPreTable = preTb;
  } // BindingTB()
  
  public String Set( Node bindingTarget, Node bindingSexp ) throws Throwable {
    String targetSymbolString = "";
    
    // check if function or symbol bind
    // list include the quoted atom
    if ( InnerFunction.Is_List( bindingTarget ).mToken.mType == Symbol.sT ) {
      // function binding
      
      // check if quote atom as function args in a list
      // because a list is not symbol as primitive
      Vector<Node> params = new Vector<Node>();
      
      Node bonsNode = bindingTarget.mR_Child;
      while ( bonsNode != null ) {
        
        if ( InnerFunction.Is_List( bonsNode.mL_Child ).mToken.mType == Symbol.sT ) {
          // list or quoted atom
          throw new MainSexpError( "DEFINE format" );
        } // if
        else if ( bonsNode.mL_Child.mToken.mType != Symbol.sSYMBOL ) {
          // if atom check it is not a primitive
          throw new MainSexpError( "DEFINE format" );
        } // else if
        else {
          // acceptable argument symbol
          // check if reuse of symbol
          
          for ( int i = 0 ; i < params.size() ; i++ ) {
            if ( params.elementAt( i ).Get_Symbol().equals( bonsNode.mL_Child.Get_Symbol() ) ) {
              throw new MainSexpError( "DEFINE format" );
            } // if
            
          } // for
          
          params.add( bonsNode.Get_L_C() );
        } // else
        
        bonsNode = bonsNode.mR_Child;
      } // while
      
      // TODO check all function call
      // // any unbound symbol
      // // // except the symbol in the argument list
      // // any non pure list
      // // any function call with correct argument count
      
      // start define process
      
      targetSymbolString = bindingTarget.mL_Child.mToken.mContent;
      Vector<Node> functionBody = OurSchemVM.ParseParemeter( "", 0, bindingSexp, true );
      
      // process project 3 special case
      // // define function binding with special lambda syntax
      BindingTB scope = OurSchemVM.Get_Instance().mScope_Global;
      Node func = OurSchemVM.Get_Instance().Evaluate( InnerFunction.Car( functionBody.elementAt( 0 ) ),
          scope );
      
      if ( functionBody.size() == 1 && func.Get_Symbol().equals( "(lambda)" ) ) {
        Binding target = I_get( targetSymbolString );
        if ( target == null ) {
          // insert at current binding table
          mBindings.add( new Binding( targetSymbolString, func, false ) );
        } // if
        else {
          // update binding
          target.Set( func );
        } // else
      } // if
      else {
        Binding target = I_get( targetSymbolString );
        if ( target == null ) {
          // insert at current binding table
          mBindings.add( new Binding( targetSymbolString, params, functionBody ) );
        } // if
        else {
          // update binding
          target.Set( params, functionBody );
        } // else
      } // else
      
      return targetSymbolString;
    } // if
    else {
      // process atom or pair
      
      if ( InnerFunction.Is_Atom( bindingTarget ).mToken.mType == Symbol.sT ) {
        // a non quote atom
        
        if ( bindingTarget.mToken.mType != Symbol.sSYMBOL ) {
          // if atom check it is not a primitive
          throw new MainSexpError( "DEFINE format" );
        } // if
        
        try {
          // if define a symbol
          // the define format allow only one argument on bindingSexp
          OurSchemVM.ParseParemeter( "", 1, bindingSexp, false );
        } // try
        catch ( Throwable e ) {
          throw new MainSexpError( "DEFINE format" );
        } // catch
        
        targetSymbolString = bindingTarget.mToken.mContent;
        Binding target = I_get( targetSymbolString );
        
        // Set a atom symbol binding need to evaluate the bindingSexp
        BindingTB scope = OurSchemVM.Get_Instance().mScope_Global;
        
        Node bindingValue;
        
        // System.out.println( "evaluate symbol define : " );
        // Interpreter.NewPrinter( bindingSexp.mL_Child );
        
        bindingValue = OurSchemVM.Get_Instance().Evaluate( bindingSexp.mL_Child, scope );
        
        // System.out.println( "evaluate result : " );
        // Interpreter.NewPrinter( bindingValue );
        
        if ( target == null ) {
          // insert at current binding table
          mBindings.add( new Binding( targetSymbolString, bindingValue, false ) );
        } // if
        else {
          // update binding
          target.Set( bindingValue );
        } // else
        
        return targetSymbolString;
        
      } // if
      else {
        // a non pure list
        
        throw new MainSexpError( "DEFINE format" );
      } // else
      
    } // else
    
  } // Set()
  
  public String Set_local( Node bindingTarget, Node bindingSexp, boolean isFunctionargs ) throws Throwable {
    String targetSymbolString = "";
    
    // check if function or symbol bind
    // list include the quoted atom
    if ( InnerFunction.Is_List( bindingTarget ).mToken.mType == Symbol.sT ) {
      // function binding
      throw new EvaluatingError( "warning", "define can not have list as argument" );
      
      // check if quote atom as function args in a list
      // because a list is not symbol as primitive
      
      // Vector<Node> params = new Vector<Node>();
      //
      // Node bonsNode = bindingTarget;
      // while ( bonsNode != null ) {
      //
      // if ( InnerFunction.Is_List( bonsNode.mL_Child ).mToken.mType ==
      // Symbol.sT ) {
      // // list or quoted atom
      // throw new MainSexpError( "DEFINE format" );
      // } // if
      // else if ( bonsNode.mL_Child.mToken.mType != Symbol.sSYMBOL ) {
      // // if atom check it is not a primitive
      // throw new MainSexpError( "DEFINE format" );
      // } // else if
      // else {
      // // acceptable argument symbol
      // // check if reuse of symbol
      //
      // for ( int i = 0 ; i < params.size() ; i++ ) {
      // if ( params.elementAt( i ).Get_Symbol().equals(
      // bonsNode.mL_Child.Get_Symbol() ) ) {
      // throw new MainSexpError( "DEFINE format" );
      // } // if
      //
      // } // for
      //
      // params.add( bonsNode.Get_L_C() );
      // } // else
      //
      // bonsNode = bonsNode.mR_Child;
      // } // while
      
      // TODO check all function call
      // // any unbound symbol
      // // // except the symbol in the argument list
      // // any non pure list
      // // any function call with correct argument count
      
      // start define process
      
      // targetSymbolString = bindingTarget.mL_Child.mToken.mContent;
      // Node functioArgsNode = bindingTarget.mR_Child;
      // Vector<Node> argumentList = OurSchemVM.ParseParemeter( "", 0,
      // functioArgsNode, true );
      // Vector<Node> functionBody = OurSchemVM.ParseParemeter( "", 0,
      // bindingSexp, true );
      //
      // Binding target = I_get_local( targetSymbolString );
      // if ( target == null ) {
      // // insert at current binding table
      // mBindings.add( new Binding( targetSymbolString, argumentList,
      // functionBody ) );
      // } // if
      // else {
      // // update binding
      // target.Set( argumentList, functionBody );
      // } // else
      //
      // return targetSymbolString;
    } // if
    else {
      // process atom or pair
      
      if ( InnerFunction.Is_Atom( bindingTarget ).mToken.mType == Symbol.sT ) {
        // a non quote atom
        
        if ( bindingTarget.mToken.mType != Symbol.sSYMBOL ) {
          // if atom check it is not a primitive
          throw new MainSexpError( "DEFINE format" );
        } // if
        
        targetSymbolString = bindingTarget.mToken.mContent;
        Binding target = I_get_local( targetSymbolString );
        
        // Set a atom symbol binding need to evaluate the bindingSexp
        Node bindingValue = bindingSexp;
        // System.out.println( "push in : " + targetSymbolString );
        // Interpreter.NewPrinter( bindingValue );
        // System.out.println( "end in : " + targetSymbolString );
        
        if ( target == null ) {
          // insert at current binding table
          mBindings.add( new Binding( targetSymbolString, bindingValue, isFunctionargs ) );
        } // if
        else {
          // update binding
          target.Set( bindingValue );
        } // else
        
        return targetSymbolString;
        
      } // if
      else {
        // a non pure list
        
        throw new MainSexpError( "DEFINE format" );
      } // else
      
    } // else
    
  } // Set_local()
  
  public Binding Get( String symbol ) throws Throwable {
    // System.out.println( "target : " + symbol );
    
    for ( int i = 0 ; i < mBindings.size() ; i++ ) {
      // System.out.println( "cur : " + mBindings.elementAt( i ).mSymbol );
      
      if ( mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
        return mBindings.elementAt( i );
      } // if
      
    } // for
    
    // if ( mPreTable != null ) {
    //
    // BindingTB preTb = mPreTable;
    //
    // while ( preTb != null ) {
    // for ( int i = 0 ; i < preTb.mBindings.size() ; i++ ) {
    // if ( preTb.mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
    //
    // if ( !preTb.mBindings.elementAt( i ).mIs_FunctionArgument ) {
    // return preTb.mBindings.elementAt( i );
    // } // if
    //
    // } // if
    //
    // } // for
    // //
    // preTb = preTb.mPreTable;
    //
    // } // while
    // } // if
    
    return null;
  } // Get()
  
  public void Set_innerBinding( String bindingTarget, boolean isFunc ) {
    mBindings.add( new Binding( bindingTarget, isFunc ) );
  } // Set_innerBinding()
  
  // inner get
  private Binding I_get( String symbol ) {
    
    for ( int i = 0 ; i < mBindings.size() ; i++ ) {
      if ( mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
        return mBindings.elementAt( i );
      } // if
      
    } // for
    
    BindingTB preTb = mPreTable;
    
    while ( preTb != null ) {
      for ( int i = 0 ; i < preTb.mBindings.size() ; i++ ) {
        if ( preTb.mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
          return preTb.mBindings.elementAt( i );
        } // if
        
      } // for
      
      preTb = preTb.mPreTable;
      
    } // while
    
    return null;
  } // I_get()
  
  // inner get
  private Binding I_get_local( String symbol ) {
    
    for ( int i = 0 ; i < mBindings.size() ; i++ ) {
      if ( mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
        return mBindings.elementAt( i );
      } // if
      
    } // for
    
    return null;
  } // I_get_local()
  
} // class BindingTB

class Binding {
  public String mSymbol;
  public int mMemoryIndex;
  public boolean mIs_primitive;
  // a function argument cannot be access from other scope
  public boolean mIs_FunctionArgument;
  
  public Binding( String symbol, boolean isFunc ) {
    // define a primitive
    
    mSymbol = symbol;
    mIs_primitive = true;
    
    if ( isFunc ) {
      mMemoryIndex = Memory.Get_Instance().Add( symbol, isFunc );
    } // if
    else {
      // create binding
      mMemoryIndex = Memory.Get_Instance().Add( symbol, isFunc );
    } // else
    
    // special process on primitive
    mIs_FunctionArgument = false;
  } // Binding()
  
  public Binding( String symbol, Node Sexp, boolean fuctionArg ) {
    mSymbol = symbol;
    mIs_primitive = false;
    mIs_FunctionArgument = fuctionArg;
    
    int if_defined = Memory.Get_Instance().MemoryIndex( Sexp );
    if ( if_defined > -1 ) {
      //
      MemoryItem tmp = Memory.Get_Instance().Get( if_defined );
      if ( tmp.mIs_function && tmp.Get_Symbol().equals( "(lambda)" ) ) {
        // if function is not directly define by user
        // OurScheme will remove the mark of System define presentation
        mMemoryIndex = Memory.Get_Instance().Add( ")(lambda", tmp.mFuncArgs, tmp.mFuncBodyNode );
      } // if
      else {
        mMemoryIndex = if_defined;
      } // else
      
    } // if
    else {
      // create binding
      mMemoryIndex = Memory.Get_Instance().Add( symbol, Sexp );
    } // else
    
  } // Binding()
  
  public Binding( String symbol, Vector<Node> functionArgs, Vector<Node> Sexp ) {
    mSymbol = symbol;
    mIs_primitive = false;
    
    // create binding
    mMemoryIndex = Memory.Get_Instance().Add( symbol, functionArgs, Sexp );
    
    // function cannot be FunctionArgument
    mIs_FunctionArgument = false;
  } // Binding()
  
  public void Set( Vector<Node> functionArgs, Vector<Node> Sexp ) throws PrimitiveRedefineError {
    if ( mIs_primitive ) {
      throw new PrimitiveRedefineError();
    } // if
    else {
      mMemoryIndex = Memory.Get_Instance().Add( mSymbol, functionArgs, Sexp );
    } // else
    
  } // Set()
  
  public void Set( Node Sexp ) throws PrimitiveRedefineError {
    
    if ( mIs_primitive ) {
      throw new PrimitiveRedefineError();
    } // if
    else {
      // create new memoryItem
      // or
      // point to an exit memoryItem
      int if_defined = Memory.Get_Instance().MemoryIndex( Sexp );
      if ( if_defined > -1 ) {
        MemoryItem tmp = Memory.Get_Instance().Get( if_defined );
        if ( tmp.mIs_function && tmp.Get_Symbol().equals( "(lambda)" ) ) {
          // if function is not directly define by user
          // OurScheme will remove the mark of System define presentation
          mMemoryIndex = Memory.Get_Instance().Add( ")(lambda", tmp.mFuncArgs, tmp.mFuncBodyNode );
        } // if
        else {
          mMemoryIndex = if_defined;
        } // else
        
      } // if
      else {
        mMemoryIndex = Memory.Get_Instance().Add( "", Sexp );
      } // else
      
    } // else
    
  } // Set()
  
  public Node Get() {
    return Memory.Get_Instance().Get( mMemoryIndex );
    
  } // Get()
  
} // class Binding

class Memory {
  private static Memory sSingleTone_Memory;
  
  private Vector<MemoryItem> mMemoryItems;
  
  public Memory() {
    mMemoryItems = new Vector<MemoryItem>();
  } // Memory()
  
  public static Memory Get_Instance() {
    if ( sSingleTone_Memory == null ) {
      sSingleTone_Memory = new Memory();
      return sSingleTone_Memory;
    } // if
    else {
      return sSingleTone_Memory;
    } // else
    
  } // Get_Instance()
  
  public void Init() {
    sSingleTone_Memory = new Memory();
  } // Init()
  
  public int Add( String symbol, boolean isFunc ) {
    MemoryItem item = new MemoryItem( symbol, isFunc );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
    
  } // Add()
  
  public int Add( String symbol, Node Sexp ) {
    MemoryItem item = new MemoryItem( symbol, Sexp );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
  } // Add()
  
  public int Add( String symbol, Vector<Node> functionArgs, Vector<Node> Sexp ) {
    MemoryItem item = new MemoryItem( symbol, functionArgs, Sexp );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
  } // Add()
  
  public Node Add( MemoryItem item ) {
    mMemoryItems.add( item );
    return item;
  } // Add()
  
  public MemoryItem Get( int memory_Addr ) {
    if ( memory_Addr < 0 || memory_Addr >= mMemoryItems.size() ) {
      return null;
    } // if
    else {
      return mMemoryItems.elementAt( memory_Addr );
    } // else
    
  } // Get()
  
  public int MemoryIndex( Node item ) {
    for ( int i = 0 ; i < mMemoryItems.size() ; i++ ) {
      if ( mMemoryItems.elementAt( i ).Get() == item.Get() ) {
        return i;
      } // if
    } // for
    
    return -1;
  } // MemoryIndex()
  
} // class Memory

class MemoryItem extends Node {
  
  public String mSymbol;
  public Vector<Node> mFuncArgs;
  public Vector<Node> mFuncBodyNode;
  public Node mSexp;
  public boolean mIs_function;
  public boolean mIs_primitive;
  
  public MemoryItem( String symbol, boolean isFunc ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mSymbol = symbol;
    mIs_primitive = true;
    mIs_function = isFunc;
    
    if ( isFunc ) {
      mSexp = new Node( new Token( symbol, Symbol.sPROCEDUREL ) );
    } // if
    
  } // MemoryItem()
  
  public MemoryItem( String symbol, Node Sexp ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mSexp = Sexp;
    mSymbol = symbol;
    mIs_function = false;
    mIs_primitive = false;
  } // MemoryItem()
  
  public MemoryItem( String symbol, Vector<Node> functionArgs, Vector<Node> Sexp ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mFuncBodyNode = Sexp;
    mFuncArgs = functionArgs;
    mSexp = new Node( new Token( symbol, Symbol.sPROCEDUREL ) );
    mSymbol = symbol;
    mIs_function = true;
    mIs_primitive = false;
  } // MemoryItem()
  
  public Node Get() {
    
    return mSexp;
    
  } // Get()
  
} // class MemoryItem
