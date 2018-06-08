import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class main {
    
    public static void input(List<Object> X){
        int n=0;
        while(n!=6){
            System.out.println("Enter number for predicate");
            Scanner s = new Scanner(System.in);
            n = s.nextInt();
            switch (n) {
                case 1:
                    {
                        //ON
                        Scanner a1 = new Scanner(System.in);
                        char arg1 = a1.next().charAt(0);
                        Scanner a2 = new Scanner(System.in);
                        char arg2 = a2.next().charAt(0);
                        Pred2 ON = new Pred2("ON", arg1, arg2);
                        X.add(ON);
                        break;
                    }
                case 2:
                    {
                        //CL
                        Scanner a1 = new Scanner(System.in);
                        char arg1 = a1.next().charAt(0);
                        Pred1 CL = new Pred1("CL", arg1);
                        X.add(CL);
                        break;
                    }
                case 3:
                    {
                        //ONT
                        Scanner a1 = new Scanner(System.in);
                        char arg1 = a1.next().charAt(0);
                        Pred1 ONT = new Pred1("ONT", arg1);
                        X.add(ONT);
                        break;
                    }
                case 4:
                    {
                        //HOLD
                        Scanner a1 = new Scanner(System.in);
                        char arg1 = a1.next().charAt(0);
                        Pred1 HOLD = new Pred1("HOLD", arg1);
                        X.add(HOLD);
                        break;
                    }
                case 5:
                    //AE
                    Pred1 AE = new Pred1("AE", 'T');
                    X.add(AE);
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        List<Object> S = new ArrayList<>();
        List<Object> G = new ArrayList<>();
        //START State
        System.out.println("1. ON");
        System.out.println("2. CL");
        System.out.println("3. ONT");
        System.out.println("4. HOLD");
        System.out.println("5. AE");
        System.out.println();
        System.out.println("Send START State");
        input(S);
        //GOAL State
        System.out.println("Send GOAL State");
        input(G); 
        
        /*for(int i=0; i<S.size();i++){
            Object obj = S.get(i);
            System.out.println(obj.getClass());
        }*/
        Plan P = new Plan(S, G);
        //Pred1 p = new Pred1("ONT",'b');
        //Object o  = P.operator_for_pred(p);
        //System.out.println(((Operator1)o).name + ((Operator1)o).arg); 


        P.func_plan();
        List<Object> plan = P.plan;
        Iterator<Object> it = plan.iterator();
        
        while(it.hasNext()){
            Object obj = it.next();
            if(obj.getClass().equals(Operator1.class)){
                Operator1 o1 = (Operator1)obj;
                System.out.println(o1.name + "(" + o1.arg + ")");
                
            }
            else if(obj.getClass().equals(Operator2.class)){
                Operator2 o2 = (Operator2)obj;
                System.out.println(o2.name + "(" + o2.arg1 + "," + o2.arg2 + ")");
                
            }
        }
        
    }
    
}
