package PL109_10627238;

import java.util.Vector;

public class BindingTB {
  public Vector<Binding> mBindings;
  public BindingTB mPreTable;
  
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
      
      Node bonsNode = bindingTarget;
      while ( bonsNode != null ) {
        
        if ( InnerFunction.Is_List( bonsNode.mL_Child ).mToken.mType == Symbol.sT ) {
          // list or quoted atom
          throw new ListError( "DEFINE format", null );
        } // if
        else if ( bonsNode.mL_Child.mToken.mType != Symbol.sSYMBOL ) {
          // if atom check it is not a primitive
          throw new ListError( "DEFINE format", null );
        } // else
        else {
          // acceptable argument symbol
          // check if reuse of symbol
          
          for ( int i = 0 ; i < params.size() ; i++ ) {
            if ( params.elementAt( i ).Get_Symbol().equals( bonsNode.mL_Child.Get_Symbol() ) ) {
              throw new ListError( "DEFINE format", null );
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
      Node functioArgsNode = bindingTarget.mR_Child;
      
      Binding target = get( targetSymbolString );
      if ( target == null ) {
        // insert at current binding table
        mBindings.add( new Binding( targetSymbolString, functioArgsNode, bindingSexp ) );
      } // if
      else {
        // update binding
        target.Set( functioArgsNode, bindingSexp );
      } // else
      
      return targetSymbolString;
    } // if
    else {
      // process atom or pair
      
      if ( InnerFunction.Is_Atom( bindingTarget ).mToken.mType == Symbol.sT ) {
        // a non quote atom
        
        if ( bindingTarget.mToken.mType != Symbol.sSYMBOL ) {
          // if atom check it is not a primitive
          throw new ListError( "DEFINE format", null );
        } // if
        
        targetSymbolString = bindingTarget.mToken.mContent;
        Binding target = get( targetSymbolString );
        
        // Set a atom symbol binding need to evaluate the bindingSexp
        Node bindingValue = OurSchemVM.Get_Instance().Evaluate( bindingSexp );
        
        if ( target == null ) {
          // insert at current binding table
          mBindings.add( new Binding( targetSymbolString, bindingValue ) );
        } // if
        else {
          // update binding
          target.Set( bindingValue );
        } // else
        
        return targetSymbolString;
        
      } // if
      else {
        // a non pure list
        
        throw new ListError( "DEFINE format", null );
      } // else
      
    } // else
    
  } // Set()
  
  public Binding Get( String symbol ) throws Throwable {
    
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
      //
      preTb = preTb.mPreTable;
      
    } // while
    
    return null;
  } // Get()
  
  public void Set_innerBinding( String bindingTarget, boolean isFunc, Node bind ) {
    mBindings.add( new Binding( bindingTarget, isFunc, bind ) );
  } // Set_innerBinding()
  
  // inner get
  private Binding get( String symbol ) {
    
    for ( int i = 0 ; i < mBindings.size() ; i++ ) {
      if ( mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
        return mBindings.elementAt( i );
      } // if
      
    } // for
    
    BindingTB preTb = mPreTable;
    
    while ( mPreTable != null ) {
      for ( int i = 0 ; i < preTb.mBindings.size() ; i++ ) {
        if ( preTb.mBindings.elementAt( i ).mSymbol.equals( symbol ) ) {
          return preTb.mBindings.elementAt( i );
        } // if
        
      } // for
      //
      preTb = preTb.mPreTable;
      
    } // while
    
    return null;
  } // Get()
} // class BindingTB

class Binding {
  public String mSymbol;
  public int mMemoryIndex;
  public boolean mIs_primitive;
  
  public Binding( String symbol, boolean isFunc, Node bind ) {
    
    if ( bind == null ) {
      mSymbol = symbol;
      mIs_primitive = true;
      
      if ( isFunc ) {
        mMemoryIndex = Memory.Get_Instance().Add( symbol, null, null );
      } // if
      else {
        // create binding
        mMemoryIndex = Memory.Get_Instance().Add( symbol, bind );
      } // else
      
    } // if
    else {
      mSymbol = symbol;
      mIs_primitive = true;
      
      if ( isFunc ) {
        mMemoryIndex = Memory.Get_Instance().Add( symbol, null, null );
      } // if
      else {
        // create binding
        mMemoryIndex = Memory.Get_Instance().Add( symbol, bind );
      } // else
      
    } // else
    
  } // Binding()
  
  public Binding( String symbol, Node Sexp ) {
    mSymbol = symbol;
    mIs_primitive = false;
    
    int if_defined = Memory.Get_Instance().MemoryIndex( Sexp );
    if ( if_defined > -1 ) {
      mMemoryIndex = if_defined;
    } // if
    else {
      // create binding
      mMemoryIndex = Memory.Get_Instance().Add( symbol, Sexp );
    } // else
    
  } // Binding()
  
  public Binding( String symbol, Node functionArgs, Node Sexp ) {
    mSymbol = symbol;
    mIs_primitive = false;
    
    // create binding
    mMemoryIndex = Memory.Get_Instance().Add( symbol, functionArgs, Sexp );
  } // Binding()
  
  public void Set( Node functionArgs, Node Sexp ) throws PrimitiveRedefineError {
    if ( mIs_primitive ) {
      throw new PrimitiveRedefineError();
    } // if
    else {
      mMemoryIndex = Memory.Get_Instance().Add( "", functionArgs, Sexp );
    } // else
    
  } // Set()
  
  public void Set( Node Sexp ) throws PrimitiveRedefineError {
    
    if ( mIs_primitive ) {
      throw new PrimitiveRedefineError();
    } // if
    else {
      // create binding
      mMemoryIndex = Memory.Get_Instance().Add( "", Sexp );
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
  
  public int Add( String symbol, boolean isFunc, Node bind ) {
    MemoryItem item = new MemoryItem( symbol, isFunc, bind );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
    
  } // MemoryItem()
  
  public int Add( String symbol, Node Sexp ) {
    MemoryItem item = new MemoryItem( symbol, Sexp );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
  } // Binding()
  
  public int Add( String symbol, Node functionArgs, Node Sexp ) {
    MemoryItem item = new MemoryItem( symbol, functionArgs, Sexp );
    mMemoryItems.add( item );
    return mMemoryItems.indexOf( item );
  } // MemoryItem()
  
  public MemoryItem Get( int memory_Addr ) {
    return mMemoryItems.elementAt( memory_Addr );
  } // Get()
  
  public int MemoryIndex( Node item ) {
    return mMemoryItems.indexOf( item );
  } // MemoryIndex()
  
} // class Memory

class MemoryItem extends Node {
  
  public String mSymbol;
  public Node mFuncArgs;
  public Node mFuncBodyNode;
  public Node mSexp;
  public boolean mIs_function;
  public boolean mIs_primitive;
  
  public MemoryItem( String symbol, boolean isFunc, Node bind ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mSymbol = symbol;
    mIs_primitive = true;
    mIs_function = isFunc;
    
    if ( isFunc ) {
      mSexp = new Node( new Token( symbol, Symbol.sPROCEDUREL ) );
    } // if
    else {
      mSexp = bind;
    } // else
    
  } // MemoryItem()
  
  public MemoryItem( String symbol, Node Sexp ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mSexp = Sexp;
    mSymbol = symbol;
    mIs_function = false;
    mIs_primitive = false;
  } // Binding()
  
  public MemoryItem( String symbol, Node functionArgs, Node Sexp ) {
    // a binding is a dot node
    super( new Token( symbol, Symbol.sBINDING ) );
    
    mFuncBodyNode = Sexp;
    mSexp = new Node( new Token( symbol, Symbol.sPROCEDUREL ) );
    mSymbol = symbol;
    mIs_function = true;
    mIs_primitive = false;
  } // MemoryItem()
  
  public Node Get() {
    
    return mSexp;
    
  } // Get()
  
} // class MemoryItem
