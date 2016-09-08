package ru.stqa.pft.sandbox;

class MyFirstProgram {
	
	public static void main(String args[])
	{

      Square s=new Square(5);
      Rectangle r=new Rectangle(4,5);


    System.out.println("Площадь квадрата со стороной "+s.l+" равна "+ s.area());
    System.out.println("Площадь прямоугольника со сторонами "+r.a+" и "+r.b+" равна "+ r.area());
	}

}
