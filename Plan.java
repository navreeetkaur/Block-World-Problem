import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Plan {
    List<Object> Start = new ArrayList<>();
    List<Object> Goal= new ArrayList<>();
    List<Object> Curr= new ArrayList<>();
    Stack<Object> goalStack;
    List<Object> plan = new ArrayList<>();

    public boolean contain(List<Object> l,Object o){
         boolean b=false;
         Iterator<Object> it = l.iterator();
         while (it.hasNext()){
            Object ob=it.next();
            if(ob.getClass().equals(Pred1.class)){
                if (o.getClass().equals(Pred1.class)){
                    Pred1 pdash=(Pred1) o;
                    Pred1 phash=(Pred1) ob;
                    if (pdash.name.equals(phash.name) && pdash.arg==phash.arg){
                        b=true;
                    }

                }
            }
             else if(ob.getClass().equals(Pred2.class)){
                if (o.getClass().equals(Pred2.class)){
                    Pred2 pdash=(Pred2) o;
                    Pred2 phash=(Pred2) ob;
                    if (pdash.name.equals(phash.name) && pdash.arg1==phash.arg1 && pdash.arg2==phash.arg2){
                        b=true;
                    }

                }
            }


         }
         return(b);

    }
    
    public Plan(List<Object> S, List<Object> G){        
        goalStack = new Stack<>();
        Start = S;
        Goal = G;
        Curr = Start;
        make_goalStack();
        //create goal stack
    }
    
    /*Operator2 US = new Operator2("US", 'X', 'Y');
    Operator2 S = new Operator2("S", 'X', 'Y');
    Operator1 PU = new Operator1("PU", 'X');
    Operator1 PD = new Operator1("PD", 'X');*/
    
    private void make_goalStack(){
       // System.out.println("yeah");
        List<Object> tsubg = new ArrayList<>();
        List<Object> g = new ArrayList<>();
        for(int i =0;i<Goal.size();i++){
            if(contain(Start,Goal.get(i))){
                tsubg.add(Goal.get(i));
            }
            else{
                g.add(Goal.get(i));
            }
        }
        goalStack.push(Goal);
        for(int i=0;i<g.size();i++){
            goalStack.push(g.get(i));
           // System.out.println(g.get(i).getClass());
        }                
    }
    
    
    public void func_plan(){
        
        while(goalStack.isEmpty()==false){
            //pop - check object type - get class
            Object obj = goalStack.pop();    
           // System.out.println("Popped object is: " + obj.getClass());    
            //if object is pred
            
            if(obj.getClass().equals(Pred1.class) || obj.getClass().equals(Pred2.class) ){
            //check if pred holds or not - that is if curr state contains the curr pred
                //if true - pop it
                if(contain(Curr,obj)){ 
                   // System.out.println("fo"); 
                    //already popped
                    continue;
                }
                else{
                    //if not - check from which operator it might have resulted --- make a seperate for this as well
                    Object op = operator_for_pred(obj);
                    //add that operator to the goal stack along with its precons
                    push_op(op);  
                }
            }    
            
            
            //if object is operator 
            else if(obj.getClass().equals(Operator1.class) || obj.getClass().equals(Operator2.class) ){
                //add to plan and change curr state
                
                //add list to curr state
                op_add_list(obj);
                //del list from curr state          
                op_del_list(obj);

                plan.add(obj);
            }

            //write the case for  obj being the list of all goals
           
           else if(obj.getClass().equals(ArrayList.class)){
            List<Object> list = (ArrayList<Object>) obj;
            Iterator<Object> it = list.iterator();
            while (it.hasNext()){
                Object o = it.next();
                if(!contain(Curr,o)){
                    goalStack.push(o);
                }
            }

           // System.out.println("lel");


           } 
        }
        
        
    } 
    
        
    //push an operator in the goal stack - after pushing op, push its precons as well
    private void push_op(Object obj){
        //push the operator
        goalStack.push(obj);    
        //push its pre-conditions
        if(obj.getClass().equals(Operator2.class)){
            Operator2 op2 = (Operator2)obj;
          //  System.out.println("Operator pushed is" + op2.name);
            // if op = US
            if(op2.name.equals("US")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new Pred2("ON",op2.arg1,op2.arg2));
                precon_list.add(new Pred1("CL",op2.arg1));
                precon_list.add(new Pred1("AE",'T'));
                goalStack.push(precon_list);
                goalStack.push(new Pred2("ON",op2.arg1,op2.arg2));
                goalStack.push(new Pred1("CL",op2.arg1));
                goalStack.push(new Pred1("AE",'T'));                 
            }
            // if op = S
            else if(op2.name.equals("S")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new Pred1("CL",op2.arg2));
                precon_list.add(new Pred1("HOLD",op2.arg1));
                goalStack.push(precon_list);
                goalStack.push(new Pred1("CL",op2.arg2));
                goalStack.push(new Pred1("HOLD",op2.arg1));                
            }
        }
        
        else if(obj.getClass().equals(Operator1.class)){
            Operator1 op1 = (Operator1)obj;
           // System.out.println("Operator pushed is"+ op1.name);
            // if op = PU
            if(op1.name.equals("PU")){
                List<Object> precon_list = new ArrayList<>();
                precon_list.add(new Pred1("ONT",op1.arg));
                precon_list.add(new Pred1("AE",'T'));
                precon_list.add(new Pred1("CL",op1.arg));
                goalStack.push(precon_list);
                goalStack.push(new Pred1("ONT",op1.arg));
                goalStack.push(new Pred1("AE",'T'));
                goalStack.push(new Pred1("CL",op1.arg));                  
            }
            // if op = PD
            else if(op1.name.equals("PD")){
                goalStack.push(new Pred1("HOLD",op1.arg));                              
            }
        }
    }


    //operator add list to curr state
    private void op_add_list(Object obj){
        

        if(obj.getClass().equals(Operator2.class)){
            Operator2 operator = (Operator2)obj;
            //System.out.println(operator.name);
            if(operator.name.equals("US")){
                Curr.add(new Pred1("HOLD",operator.arg1));
                Curr.add(new Pred1("CL",operator.arg2));                
            }
            else if(operator.name.equals("S")){
                Curr.add(new Pred1("AE",'T'));
                Curr.add(new Pred2("ON",operator.arg1,operator.arg2));
            }
            
        }
        else if(obj.getClass().equals(Operator1.class)){
            Operator1 operator = (Operator1)obj;
           // System.out.println(operator.name);
            if(operator.name.equals("PU")){
                Curr.add(new Pred1("HOLD",operator.arg));
            }
            else if(operator.name.equals("PD")){
                Curr.add(new Pred1("AE",'T'));
                Curr.add(new Pred1("ONT",operator.arg));
            }            
        }
        
    }

    
    //operator del list from curr state
    private void op_del_list(Object obj){
        
        if(obj.getClass().equals(Operator2.class)){
            
            Operator2 operator = (Operator2)obj;
           // System.out.println(operator.name);
            if(operator.name.equals("US")){
                Iterator<Object> it = Curr.iterator();
                
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 p = (Pred1)o;
                        if(p.name.equals("AE")){
                            it.remove();
                        }
                    }
                    else if(o.getClass().equals(Pred2.class)){
                        Pred2 p = (Pred2)o;
                        if(p.name.equals("ON") && p.arg1 == operator.arg1 && p.arg2==operator.arg2){
                           // Curr.remove(o);
                            it.remove();
                        }
                    }
                }
            }
            
            else if(operator.name.equals("S")){
                Iterator<Object> it = Curr.iterator();
                
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 p = (Pred1)o;
                        if(p.name.equals("CL") && p.arg==operator.arg2){
                            //Curr.remove(o);
                            it.remove();
                        }
                        if(p.name.equals("HOLD") && p.arg==operator.arg1){
                            //Curr.remove(o);
                            it.remove();
                        }
                    }                    
                }                
            }
            
        }
        
                
        else if(obj.getClass().equals(Operator1.class)){
            
            Operator1 operator = (Operator1)obj;
         //   System.out.println(operator.name);
            
            if(operator.name.equals("PU")){
                Iterator<Object> it = Curr.iterator();
                
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 p = (Pred1)o;
                        if(p.name.equals("ONT") && p.arg==operator.arg){
                            //Curr.remove(o);
                            it.remove();
                        }
                        if(p.name.equals("AE")){
                            //Curr.remove(o);
                            it.remove();
                        }                        
                    }                    
                }    
            }
            
            else if(operator.name.equals("PD")){
                Iterator<Object> it = Curr.iterator();
                
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 p = (Pred1)o;
                        if(p.name.equals("HOLD") && p.arg==operator.arg){
                            //Curr.remove(o);
                            it.remove();
                        }                        
                    }                    
                }                
            }  
            
        }
        
    }
    
    
    //operator whose application resulted in the given predicate
    private Object operator_for_pred(Object pred){
        Object opfinal = new Object();
        
        if(pred.getClass().equals(Pred1.class)){
            Pred1 p = (Pred1)pred;
  
            //ae
            if(p.name.equals("AE")){
                char x = 0;
                Iterator<Object> it = Curr.iterator();
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 p1 = (Pred1)o;
                        if(p1.name.equals("HOLD")){
                            x = p1.arg;
                        }
                    }
                }                
                Operator1 op = new Operator1("PD", x);
                opfinal = op;
            }           
            
            
            //hold
            else if(p.name.equals("HOLD")){
                char x = 0;
                Iterator<Object> it = Curr.iterator();
                Iterator<Object> it2 = Curr.iterator();
                
                //if ont and cl then pu
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred1.class)){
                        Pred1 o2 = (Pred1)o;
                        if(o2.name.equals("ONT") && o2.arg == p.arg){
                            //check if CL is also true
                            while(it2.hasNext()){
                                Object o_ = it2.next();
                                if(o_.getClass().equals(Pred1.class)){
                                    Pred1 o2_ = (Pred1)o_;
                                    if(o2_.name.equals("CL") && o2_.arg == p.arg){
                                        Operator1 op1 = new Operator1("PU",p.arg);
                                            opfinal = op1;
                                    }
                                }                                
                            }
                        }
                    }                    
                }
                
                //if on something then US
                Iterator<Object> i = Curr.iterator();
                while(i.hasNext()){
                    Object o = i.next();
                    if(o.getClass().equals(Pred2.class)){
                        Pred2 o2 = (Pred2)o;
                        if(o2.name.equals("ON") && o2.arg1 == p.arg){
                            x = o2.arg2;
                            Operator2 op2 = new Operator2("US",p.arg, x); 
                            opfinal = op2;
                        }
                    }
                }                  
            }
            
            
            //cl
            else if(p.name.equals("CL")){
                char x = 0;
                //find x
                Iterator<Object> it = Curr.iterator();
                while(it.hasNext()){
                    Object o = it.next();
                    if(o.getClass().equals(Pred2.class)){
                        Pred2 o2 = (Pred2)o;
                        if(o2.name.equals("ON") && o2.arg2 == p.arg){
                            x = o2.arg1;
                        }
                    }
                }                
                Operator2 op = new Operator2("US",x,p.arg);
                opfinal = op;
            }
            
            //ont
            else if(p.name.equals("ONT")){
                Operator1 op = new Operator1("PD",p.arg); 
                opfinal = op;
            }
        }
        
        else if (pred.getClass().equals(Pred2.class)){
            //on
            Pred2 p = (Pred2)pred;
            Operator2 op = new Operator2("S",p.arg1,p.arg2);
            opfinal = op;
        }       
        
        return opfinal;
    }
    
      
}
