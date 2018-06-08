Course: COL333(Autumn'17): Artificial Intelligence

Assignment 2: Block World Problem

Creators: 2015TT10917, Navreet Kaur  and   2015CS10207, Aditya Sahdev

- Each block is uniquely identified by its single character name. No two different blocks can have the same name.
- State of the system can be defined by a set of predicates. Start and Goal states must be mentioned by the user initially. The predicates mean the folling:
	(1) ON(A, B): A is on B
	(2) CL(A): Top of A is clear
	(3) ONT(A): A is on table
	(4) HOLD(A): Robot is holding A
	(5) AE: Robot's arm is empty


Files:

	1) main.java:
	Run this to input the starting and goal state of the Block World. 
	Outputs a plan to reach the Goal state.

	2) Plan.java:
	This class develops planner(Start, Goal, Plan) - a plan to reach the Goal state from the State state using the Goal Stack technique for planning. It also contains routines for

	3) Operator1.java and Operator2.java:
	Types of Operators which can be applied on the current state of the system to change its state. Operator1 type can be applied on only one block whearas Operator2 type may be applied on two blocks.
	Routines of these operators are mentioned in Plan.java

	4) Pred1.java and Pred2.java:
	Predicates that indicate the current state of the system.
