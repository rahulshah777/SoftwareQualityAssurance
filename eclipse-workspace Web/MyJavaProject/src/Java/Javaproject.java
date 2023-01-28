package Java;

public class Javaproject {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		
		int x=10;
		
	System.out.println(x++);//Post-increment
	System.out.println(++x);//Pre-increment 
	System.out.println(x--);//Post-Decrement
	System.out.println(--x);//Pre-Decrement
	/*These are the examples of a java program using unary operators.
	 */
	
	int a=10;
	int b=5;
	
	System.out.println(a+b);
	System.out.println(a-b);
	System.out.println(a*b);
	System.out.println(a/b);
	System.out.println(a%b);
/*These are the examples of airthmetic operators.
 */
	int c=10;
	int d=15;
	
	System.out.println(c<<2);
	System.out.println(c<<3);
	System.out.println(c<<4);
	
	System.out.println(d>>2);
	System.out.println(d>>3);
	System.out.println(d>>4);
	/*These are the examples of shift operators shifting binary bits for n number of times on either right or left side.
	 */
	
	int e=10;
	int f=15;
	int g=20;
	
	System.out.println(e<f&e<g);
	System.out.println(f<e&f<g);
	/*This is the example of rational operator. 
	 */	 
	 System.out.println(e<f&&e++<g);
	 System.out.println(e);
	 System.out.println(e<f&++e<g);
	 System.out.println(e);
	 /*This is the example of bitwise operator & Logical repository.
	  */
	 
	 int h=5;
	 int i=10;
	 int j=20;
	 int min=h<j?h:j;         //This is the example of ternary operators.
                                 	 
	 System.out.println(min);
	 
	 int k=5;
	 int l=15;
	 int m=20;
	 int max=k>l?k:l;
	 int max1=l>m?l:m;
	 int max2=k>m?k:m;
	 System.out.println(max);
	 System.out.println(max1);
	 System.out.println(max2);
	 
	 
	
	 
      	
	}

}
